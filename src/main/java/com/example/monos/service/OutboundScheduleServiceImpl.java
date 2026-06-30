package com.example.monos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.objenesis.ObjenesisHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.monos.common.Const;
import com.example.monos.domain.Inventory;
import com.example.monos.domain.OutboundSchedule;
import com.example.monos.dto.OutboundScheduleDetailDto;
import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;
import com.example.monos.exception.BusinessException;
import com.example.monos.exception.FatalBusinessException;
import com.example.monos.form.OutboundScheduleSaveDto;
import com.example.monos.mapper.InventoryMapper;
import com.example.monos.mapper.OutboundScheduleMapper;
import com.example.monos.mapper.ProductMapper;

/**
 * 出庫予定に関する業務ロジックを提供するサービス実装。
 * <p>出庫予定の検索、取得、登録、更新、および関連する在庫のバージョン管理を行う。
 */
@Service
public class OutboundScheduleServiceImpl implements OutboundScheduleService {
    private final OutboundScheduleMapper outboundScheduleMapper;
    private final InventoryMapper inventoryMapper;
    private final ProductMapper productMapper;
    private final MessageSource messageSource;

    /**
     * コンストラクタ。
     *
     * @param outboundScheduleMapper 出庫予定用マッパー
     * @param inventoryMapper 在庫用マッパー
     * @param productMapper 商品用マッパー
     * @param messageSource メッセージソース（国際化用）
     */
    public OutboundScheduleServiceImpl(OutboundScheduleMapper outboundScheduleMapper, InventoryMapper inventoryMapper, ProductMapper productMapper, MessageSource messageSource) {
        this.outboundScheduleMapper = outboundScheduleMapper;
        this.inventoryMapper = inventoryMapper;
        this.productMapper = productMapper;
        this.messageSource = messageSource;
    }

    /**
     * <p>出庫予定を検索する。</p>
      * @param condition 検索条件
      * @return 出庫予定のリスト
     */
    @Override
    public List<OutboundScheduleListDto> search(OutboundScheduleSearchCondition condition) {
        return outboundScheduleMapper.selectList(condition);
    }

    /**
     * 指定した出庫予定IDおよび会社IDに対応する出庫予定の詳細を取得する。
     *
     * @param outboundScheduleId 出庫予定ID
     * @param companyId 会社ID
     * @return 出庫予定が存在する場合は `OutboundScheduleDetailDto` を格納した `Optional`、存在しない場合は空の `Optional`
     */
    @Override
    public Optional<OutboundScheduleDetailDto> findById(int outboundScheduleId, int companyId) {

        var schedule = outboundScheduleMapper.selectById(outboundScheduleId, companyId);
        if (schedule == null) {
            return Optional.empty();
        }

        var inventory = inventoryMapper.selectById(schedule.getInventoryId(), companyId);
        if (inventory == null) {
            return Optional.empty();
        }

        var product = productMapper.selectById(inventory.getProductId(), companyId);
        if (product == null) {
            return Optional.empty();
        }

        var detailDto = new OutboundScheduleDetailDto();
        detailDto.setOutboundScheduleId(outboundScheduleId);
        detailDto.setProductCode(product.getProductCode());
        detailDto.setProductName(product.getName());
        detailDto.setProductId(product.getProductId());
        detailDto.setScheduleQty(schedule.getScheduleQty());
        detailDto.setScheduleDate(schedule.getScheduleDate());
        detailDto.setStatus(schedule.getStatus());
        detailDto.setVersion(schedule.getVersion());

        return Optional.ofNullable(detailDto);
    }

    /**
     * 出庫予定を保存する。
     * <p>引数の `saveDto` によって新規登録または更新を判定し、必要な検証を行った後、DB 操作を実行する。
     * 処理完了後に画面表示用の完了メッセージを返す。
     *
     * @param saveDto 出庫予定の保存情報
     * @return 処理完了メッセージ（国際化済み）
     * @throws BusinessException 入力検証でエラーが発生した場合
     * @throws FatalBusinessException 排他や更新失敗など致命的なエラーが発生した場合
     */
    @Override
    @Transactional
    public String save(OutboundScheduleSaveDto saveDto) {
        String resultMessage = "";

        // 現在の在庫情報を取得
        Inventory inventory = inventoryMapper.selectByIdAndVersion(saveDto.getInventoryId(), saveDto.getInventoryVersion());

        if (saveDto.getOutboundScheduleId() == null) {
            
            validateInsertBusinessRules(inventory);

            validateInsertFields(saveDto, inventory);

            insertOutboundSchedule(saveDto);
            
            updateInventoryVersion(inventory);
            
            resultMessage = messageSource.getMessage("registCompleteTarget", new String[] {"出庫予定"}, LocaleContextHolder.getLocale());

        } else {
            // 出庫予定情報を取得
            var schedule = outboundScheduleMapper.selectByIdAndVersion(
                    saveDto.getOutboundScheduleId(), saveDto.getCompanyId(), saveDto.getOutboundScheduleVersion());

            validateUpdateBusinessRules(schedule, inventory);

            validateUpdateFields(saveDto, schedule, inventory);

            updateOutboundSchedule(saveDto, schedule);

            updateInventoryVersion(inventory);
            
            resultMessage = messageSource.getMessage("updateCompleteTarget", new String[] {"出庫予定"}, LocaleContextHolder.getLocale());

        }

        return resultMessage;
    }

    /**
     * <p>出庫予定の登録に関するビジネスルールを検証する。</p>
     * @param inventory 出庫予定に関連する在庫情報
     */
    private void validateInsertBusinessRules(Inventory inventory) {
        validateInventoryExists(inventory);
    }

    /**
     * <p>出庫予定の登録に関する入力フィールドを検証する。</p>
     * @param saveDto 出庫予定の保存情報
     * @param inventory 出庫予定に関連する在庫情報
     */
    private void validateInsertFields(OutboundScheduleSaveDto saveDto, Inventory inventory) {
        var errors = new HashMap<String, String>();

        int totalScheduleQty = outboundScheduleMapper.selectTotalScheduleQtyByInventoryId(
                saveDto.getCompanyId(), saveDto.getInventoryId());

        // 出庫予定数量が在庫数量を超過している場合はエラー
        if (saveDto.getScheduleQty() > (inventory.getOnHandQty() - totalScheduleQty)) {
            errors.put(
                "scheduleQty", 
                messageSource.getMessage("totalOutboundScheduleQtyOver", null, LocaleContextHolder.getLocale())
            );
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    /**
     * <p>出庫予定の更新に関するビジネスルールを検証する。</p>
     * @param inventory 出庫予定に関連する在庫情報
     * @param schedule 出庫予定
     */
    private void validateUpdateBusinessRules(OutboundSchedule schedule, Inventory inventory){
        // 出庫予定の存在チェック
        if (schedule == null) {
            throw new FatalBusinessException(
                messageSource.getMessage("ExclusiveError", new String[]{"出庫予定"}, LocaleContextHolder.getLocale())
            );
        }

        // ステータスチェック
        if (schedule.getStatus().equals(Const.OutboundStatus.SHUKOZUMI)){
            throw new FatalBusinessException(
                messageSource.getMessage(
                    "outboundScheduleUpdStatusError",
                    new String[]{Const.OutboundStatus.SHUKOZUMI_LABEL},
                    LocaleContextHolder.getLocale())
            );

        } else if(schedule.getStatus().equals(Const.OutboundStatus.CANCEL)){
            throw new FatalBusinessException(
                messageSource.getMessage(
                    "outboundScheduleUpdStatusError",
                    new String[]{Const.OutboundStatus.CANCEL_LABEL},
                    LocaleContextHolder.getLocale())
            );
        }
        
        // 在庫の存在チェック
        validateInventoryExists(inventory);
    }

    /**
     * <p>出庫予定の更新に関する入力フィールドを検証する。</p>
     * @param saveDto 出庫予定の保存情報
     * @param schedule 出庫予定情報
     * @param inventory 出庫予定に関する在庫情報
     */
    private void validateUpdateFields(OutboundScheduleSaveDto saveDto, OutboundSchedule schedule, Inventory inventory){
        var errors = new HashMap<String, String>();

        // 在庫の合計出庫予定数量を取得（更新する出庫予定の予定数量は減算）
        int totalScheduleQty = outboundScheduleMapper.selectTotalScheduleQtyByInventoryId(inventory.getCompanyId(), inventory.getInventoryId());
        totalScheduleQty -= schedule.getScheduleQty();

        // 出庫予定数量が在庫数量を超過している場合はエラー
        if (saveDto.getScheduleQty() > (inventory.getOnHandQty() - totalScheduleQty)) {
            errors.put(
                "scheduleQty", 
                messageSource.getMessage("totalOutboundScheduleQtyOver", null, LocaleContextHolder.getLocale())
            );
        }

        switch (schedule.getStatus()) {
            case Const.OutboundStatus.SHUKOCHU:
                validateShukochuUpdateFields(errors, schedule, saveDto);    
            break;
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

    /**
     * <p>出庫予定が出庫中の場合の更新に関するフィールド値を検証する。</p>
     * <p>errorsにフィールドエラーを格納して返却する<。</p>
     * @param errors   フィールドエラー
     * @param schedule 更新対象の出庫予定情報
     * @param saveDto  出庫予定の入力内容
     */
    private void validateShukochuUpdateFields(HashMap<String,String> errors, OutboundSchedule schedule, OutboundScheduleSaveDto saveDto){

        // 在庫または予定数量が変更されている場合はエラー
        if (schedule.getInventoryId() != saveDto.getInventoryId()){
            errors.put(
                "inventoryId",
                messageSource.getMessage(
                    "outboundScheduleFieldUpdError",
                    new String[]{Const.OutboundStatus.SHUKOCHU_LABEL, "在庫"},
                    LocaleContextHolder.getLocale())
            );

        } else if (schedule.getScheduleQty() != saveDto.getScheduleQty()){
            errors.put(
                "scheduleQty",
                messageSource.getMessage(
                    "outboundScheduleFieldUpdError",
                    new String[]{Const.OutboundStatus.SHUKOCHU_LABEL, "予定数量"},
                    LocaleContextHolder.getLocale())
            );
        }
    }

    /**
     * <p>出庫予定を登録する。</p>
     * @param saveDto 出庫予定の保存情報
     */
    private void insertOutboundSchedule(OutboundScheduleSaveDto saveDto) {

        var schedule = new OutboundSchedule();
        schedule.setCompanyId(saveDto.getCompanyId());
        schedule.setInventoryId(saveDto.getInventoryId());
        schedule.setScheduleQty(saveDto.getScheduleQty());
        schedule.setScheduleDate(saveDto.getScheduleDate());
        schedule.setStatus(Const.OutboundStatus.MINSHUKO);
        
        Integer id = outboundScheduleMapper.insert(schedule);

        // IDが取得できない場合は登録失敗とみなしエラー
        if (id == null) {
            throw new FatalBusinessException(
                messageSource.getMessage("registFaildTarget", new String[]{"出庫予定"}, LocaleContextHolder.getLocale())
            );
        }
    }

    /**
     * <p>出庫予定情報を更新する。</p>
     * @param saveDto   出庫予定の保存情報
     * @param schedule　出庫予定情報（更新対象）
     * @throws FatalBusinessException 更新に失敗した場合
     */
    private void updateOutboundSchedule(OutboundScheduleSaveDto saveDto, OutboundSchedule schedule){

        schedule.setInventoryId(saveDto.getInventoryId());
        schedule.setScheduleQty(saveDto.getScheduleQty());
        schedule.setScheduleDate(saveDto.getScheduleDate());

        int count = outboundScheduleMapper.update(schedule);

        if (count == 0){
            throw new FatalBusinessException(
                messageSource.getMessage("updateFaildTarget", new String[]{"出庫予定"} , LocaleContextHolder.getLocale())
            );
        }
    }

    /**
     * <p>在庫のバージョンを更新する。</p>
     * @param inventory 在庫情報
     */
    private void updateInventoryVersion(Inventory inventory) {
        int updatedRows = inventoryMapper.updateVersion(inventory);
        if (updatedRows == 0) {
            throw new FatalBusinessException(
                    messageSource.getMessage("updateFaildTarget", new String[]{"在庫"}, LocaleContextHolder.getLocale())
            );
        }
    }

    /**
     * <p>在庫が存在しない場合はエラー</p>
     * @param inventory 在庫情報
     */
    private void validateInventoryExists(Inventory inventory){
        // 在庫が存在しない場合はエラー
        if (inventory == null) {
            throw new FatalBusinessException(
                    messageSource.getMessage("ExclusiveError", new String[]{"在庫"}, LocaleContextHolder.getLocale())
            );
        }
    }
}