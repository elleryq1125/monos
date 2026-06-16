package com.example.monos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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

@Service
public class OutboundScheduleServiceImpl implements OutboundScheduleService {
    private final OutboundScheduleMapper outboundScheduleMapper;
    private final InventoryMapper inventoryMapper;
    private final ProductMapper productMapper;
    private final MessageSource messageSource;

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

        }

        return resultMessage;
    }

    /**
     * <p>出庫予定の登録に関するビジネスルールを検証する。</p>
     * @param inventory 出庫予定に関連する在庫情報
     */
    private void validateInsertBusinessRules(Inventory inventory) {

        // 在庫が存在しない場合はエラー
        if (inventory == null) {
            throw new FatalBusinessException(
                    messageSource.getMessage("ExclusiveError", new String[]{"在庫"}, LocaleContextHolder.getLocale())
            );
        }
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
}