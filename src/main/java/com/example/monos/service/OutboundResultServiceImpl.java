package com.example.monos.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.monos.common.Const;
import com.example.monos.domain.Inventory;
import com.example.monos.domain.OutboundResult;
import com.example.monos.domain.OutboundSchedule;
import com.example.monos.dto.OutboundResultRegisterDto;
import com.example.monos.dto.OutboundResultListDto;
import com.example.monos.dto.OutboundResultSearchCondition;
import com.example.monos.exception.BusinessException;
import com.example.monos.exception.FatalBusinessException;
import com.example.monos.form.OutboundResultService;
import com.example.monos.mapper.InventoryMapper;
import com.example.monos.mapper.OutboundResultMapper;
import com.example.monos.mapper.OutboundScheduleMapper;

@Service
public class OutboundResultServiceImpl implements OutboundResultService {
    private MessageSource messageSource;
    private OutboundScheduleMapper outboundScheduleMapper;
    private OutboundResultMapper outboundResultMapper;
    private InventoryMapper inventoryMapper;

    public OutboundResultServiceImpl(MessageSource messageSource, OutboundScheduleMapper outboundScheduleMapper, OutboundResultMapper outboundResultMapper, InventoryMapper inventoryMapper) {
        this.messageSource = messageSource;
        this.outboundScheduleMapper = outboundScheduleMapper;
        this.outboundResultMapper = outboundResultMapper;
        this.inventoryMapper = inventoryMapper;
    }

    /**
     * 出庫実績を登録し、登録完了メッセージを返す。
     *
     * @param registerDto 出庫実績の登録リクエストデータ
     * @return 登録完了メッセージ
     * @throws com.example.monos.exception.BusinessException 出庫数量が出庫予定数量を超えるなど、入力内容に不備がある場合
     * @throws com.example.monos.exception.FatalBusinessException 出庫予定情報が存在しない、ステータスが不正、または更新・登録処理に失敗した場合
     */
    @Override
	public List<OutboundResultListDto> search(OutboundResultSearchCondition condition) {
		return outboundResultMapper.selectList(condition);
	}

	@Override
    @Transactional
    public String register(OutboundResultRegisterDto registerDto) {
        // 現在の出庫予定を取得
        OutboundSchedule schedule = outboundScheduleMapper.selectByIdAndVersion(
            registerDto.getOutboundScheduleId(), registerDto.getCompanyId(), registerDto.getVersion());
        
        // 合計出庫実績数量を取得
        int totalResultQty = outboundResultMapper.selectTotalResultQty(
            registerDto.getOutboundScheduleId(), registerDto.getCompanyId());
        
        // 合計出庫実績数量＋今回出庫数量
		int registerTotalResultQty = totalResultQty + registerDto.getResultQty();
		
		// 業務チェック
		validateRegisterBusinessRules(schedule);
		
		// フィールドチェック
		validateRegisterFields(schedule, registerTotalResultQty);

        // 出庫予定のステータスを更新
        updateOutboundScheduleStatus(schedule, registerTotalResultQty);

        // 出庫実績を登録
        insertOutboundResult(registerDto, schedule);

        // 出庫済の時点で在庫数量を更新
        if (schedule.getStatus() == Const.OutboundStatus.SHUKOZUMI) {
            Inventory inventory = inventoryMapper.selectById(schedule.getInventoryId(), schedule.getCompanyId());
            updateInventoryOnHandQty(inventory, schedule.getScheduleQty());
        }    
        
        return messageSource.getMessage("registCompleteTarget", new Object[] {"出庫実績"}, Locale.JAPAN);
    }


    /**
     * 出庫実績登録時の業務ルールを検証する。
     *
     * @param schedule 登録対象の出庫予定情報
     * @throws com.example.monos.exception.FatalBusinessException 出庫予定情報が存在しない、または出庫済み・キャンセル済みの状態である場合
     */
	private void validateRegisterBusinessRules(OutboundSchedule schedule) {
        // 出庫予定の存在＆排他チェック
		if (schedule == null) {
			throw new FatalBusinessException(
					messageSource.getMessage("ExclusiveError", new String[] {"出庫予定情報"}, Locale.JAPAN));
		}
		
		// 出庫予定の状態チェック
        // 出庫済、キャンセルはエラー
		if (schedule.getStatus() == Const.OutboundStatus.SHUKOZUMI) {
			throw new FatalBusinessException(
					messageSource.getMessage("outboundScheduleUpdStatusError", new String[] {Const.OutboundStatus.SHUKOZUMI_LABEL}, Locale.JAPAN));
		} else if (schedule.getStatus() == Const.OutboundStatus.CANCEL) {
			throw new FatalBusinessException(
					messageSource.getMessage("outboundScheduleUpdStatusError", new String[] {Const.OutboundStatus.CANCEL_LABEL}, Locale.JAPAN));
		}
	}

    /**
	 * 出庫実績登録時の入力項目に対して、数量制約を検証する。
	 *
	 * @param schedule 登録対象の出庫予定情報
	 * @param registerTotalResultQty 今回の出庫実績を含めた合計出庫実績数量
	 * @throws com.example.monos.exception.BusinessException 出庫予定数量を超える出庫実績が登録される場合
	 */
	private void validateRegisterFields(OutboundSchedule schedule, int registerTotalResultQty) {
		var errors = new HashMap<String, String>();
		
		// 出庫予定数量を超える場合はエラー
		if (schedule.getScheduleQty() < registerTotalResultQty) {
			errors.put("resultQty", messageSource.getMessage("totalOutboundResultQtyOver", null, Locale.JAPAN));
		}
		
		if (!errors.isEmpty()) {
			throw new BusinessException(errors);
		}
	}

    /**
	 * 出庫予定のステータスを、登録結果に応じて更新する。
	 *
	 * @param schedule 更新対象の出庫予定情報
	 * @param registerTotalResultQty 今回の出庫実績を含めた合計出庫実績数量
	 * @throws com.example.monos.exception.FatalBusinessException 出庫予定のステータス更新に失敗した場合
	 */
	private void updateOutboundScheduleStatus(OutboundSchedule schedule, int registerTotalResultQty) {
		schedule.determineStatus(registerTotalResultQty);
		
		int count = outboundScheduleMapper.updateStatus(schedule);
		
		if(count == 0) {
			throw new FatalBusinessException(messageSource.getMessage("updateFaildTarget", new String[] {"入庫予定"}, Locale.JAPAN));
		}
	}

    /**
	 * 出庫実績を登録する。
	 *
	 * @param registerDto 出庫実績の登録リクエストデータ
	 * @param schedule 登録対象の出庫予定情報
	 * @throws com.example.monos.exception.FatalBusinessException 出庫実績の登録に失敗した場合
	 */
	private void insertOutboundResult(OutboundResultRegisterDto registerDto, OutboundSchedule schedule) {
		OutboundResult result = new OutboundResult();
        result.setOutboundScheduleId(registerDto.getOutboundScheduleId());
		result.setCompanyId(registerDto.getCompanyId());
		result.setResultQty(registerDto.getResultQty());
		result.setResultDate(registerDto.getResultDate());
		
		Integer id = outboundResultMapper.insert(result);

		if (id == null) {
			throw new FatalBusinessException(messageSource.getMessage("registFaildTarget", new String[] {"出庫実績"}, Locale.JAPAN));
		}
	}

    /**
     * 出庫実績登録時に在庫情報の在庫数を更新する。
     *
     * @param inventory 更新対象の在庫情報
     * @param outboundQty 出庫数量
     * @throws com.example.monos.exception.FatalBusinessException 在庫数の更新に失敗した場合
     */
    private void updateInventoryOnHandQty(Inventory inventory, int outboundQty) {
        int count = inventoryMapper.updateOnHandQty(inventory, -outboundQty);
        
        if (count == 0) {
            throw new FatalBusinessException(messageSource.getMessage("updateFaildTarget", new String[] {"在庫情報"}, Locale.JAPAN));
        }
    }
}
