package com.example.monos.service;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.monos.common.Const;
import com.example.monos.domain.InboundResult;
import com.example.monos.domain.InboundSchedule;
import com.example.monos.domain.Inventory;
import com.example.monos.dto.InboundResultRegisterDto;
import com.example.monos.exception.BusinessException;
import com.example.monos.exception.FatalBusinessException;
import com.example.monos.mapper.InboundResultMapper;
import com.example.monos.mapper.InboundScheduleMapper;
import com.example.monos.mapper.InventoryMapper;

@Service
public class InboundResultServiceImpl implements InboundResultService {
	private InboundScheduleMapper inboundScheduleMapper;
	private InboundResultMapper inboundResultMapper;
	private InventoryMapper inventoryMapper;
	private MessageSource messageSource;
	
	
	public InboundResultServiceImpl(InboundScheduleMapper inboundScheduleMapper,
														 InboundResultMapper inboundResultMapper,
														 InventoryMapper inventoryMapper,
														 MessageSource messageSource) {
		this.inboundScheduleMapper = inboundScheduleMapper;
		this.inboundResultMapper = inboundResultMapper;
		this.inventoryMapper = inventoryMapper;
		this.messageSource = messageSource;
	}
	
	/**
	 * <p>入庫実績を登録する。</p>
	 * <p>入庫実績数量に合わせて入庫予定のステータスを更新する。</p>
	 * <p>既に在庫が存在すれば在庫数を更新、なければ在庫を新たに作成する。</p>
	 */
	@Override
	@Transactional
	public void register(InboundResultRegisterDto registerDto) {
		
		// 入庫予定を取得
		InboundSchedule schedule = inboundScheduleMapper.selectByIdAndVersion(registerDto.getInboundScheduleId(),
																																	registerDto.getCompanyId(),
																																	registerDto.getInboundScheduleVersion());
		// 合計入庫実績数量を取得
		int totalResultQty = inboundResultMapper.selectTotalResultQty(registerDto.getInboundScheduleId(), registerDto.getCompanyId());
		
		// 合計入庫済数量＋今回入庫数量
		int registerTotalResultQty = totalResultQty + registerDto.getResultQty();
		
		// 業務チェック
		validateRegisterBusinessRules(schedule);
		
		// フィールドチェック
		validateRegisterFields(schedule, registerTotalResultQty);
		
		// 入庫予定のステータス更新
		updateInboundScheduleStatus(schedule, registerTotalResultQty);
		
		// 入庫実績登録
		insertInboundResult(registerDto, schedule);
		
		// 在庫取得 
		Inventory inventory = inventoryMapper.selectByProductIdAndWarehouseId(registerDto.getCompanyId(), schedule.getProductId(), schedule.getWarehouseId());
		
		// 在庫が存在しなければ作成、存在すれば更新
		insertOrUpdateInventory(inventory, registerDto, schedule);
	}

	/**
	 * 入庫実績登録の業務チェック
	 * <p>エラーが発生した場合はFatalException</p>
	 * @param schedule 入庫予定
	 */
	private void validateRegisterBusinessRules(InboundSchedule schedule) {
		if (schedule == null) {
			throw new FatalBusinessException(
					messageSource.getMessage("ExclusiveError", new String[] {"入庫予定情報"}, Locale.JAPAN));
		}
		
		// 入庫予定の状態「キャンセル」はエラー
		if (schedule.getStatus() == Const.InboundStatus.NYUKOZUMI) {
			throw new FatalBusinessException(
					messageSource.getMessage("inboundScheduleUpdStatusError", new String[] {Const.InboundStatus.NYUKOZUMI_LABEL}, Locale.JAPAN));
		}
		
		// 入庫予定の状態「キャンセル」はエラー
		if (schedule.getStatus() == Const.InboundStatus.CANCEL) {
			throw new FatalBusinessException(
					messageSource.getMessage("inboundScheduleUpdStatusError", new String[] {Const.InboundStatus.CANCEL_LABEL}, Locale.JAPAN));
		}
	}
	
	/**
	 * 入庫実績登録のフィールドチェック
	 * <p>エラーが発生した場合はBusinessException</p>
	 * @param schedule 入庫予定
	 * @param registerTotalResultQty 合計入庫実績数量（今回入庫分含む）
	 */
	private void validateRegisterFields(InboundSchedule schedule, int registerTotalResultQty) {
		var errors = new HashMap<String, String>();
		
		// 入庫予定数量を超える場合はエラー
		if (schedule.getScheduleQty() < registerTotalResultQty) {
			errors.put("resultQty", messageSource.getMessage("totalInboundResultQtyOver", null, Locale.JAPAN));
		}
		
		if (!errors.isEmpty()) {
			throw new BusinessException(errors);
		}
	}
	
	
	/**
	 * 入庫予定のステータスを更新する。
	 * <p>更新に失敗した場合はFatalException</p>
	 * @param schedule 入庫予定
	 * @param registerTotalResultQty 合計入庫実績数量（今回入庫分含む）
	 */
	private void updateInboundScheduleStatus(InboundSchedule schedule, int registerTotalResultQty) {
		schedule.determineStatus(registerTotalResultQty);
		
		int count = inboundScheduleMapper.updateStatus(schedule);
		
		if(count == 0) {
			throw new FatalBusinessException(messageSource.getMessage("updateFaildTarget", new String[] {"入庫予定"}, Locale.JAPAN));
		}
	}
	
	/**
	 * 入庫実績を登録する。
	 * <p>登録に失敗した場合はFatalException</p>
	 * @param registerDto 入庫実績登録のリクエストデータ
	 * @param schedule 入庫予定
	 */
	private void insertInboundResult(InboundResultRegisterDto registerDto, InboundSchedule schedule) {
		InboundResult result = new InboundResult();
		result.setCompanyId(registerDto.getCompanyId());
		result.setInboundScheduleId(registerDto.getInboundScheduleId());
		result.setResultQty(registerDto.getResultQty());
		result.setResultDate(registerDto.getResultDate());
		
		Integer id = inboundResultMapper.insert(result);

		if (id == null) {
			throw new FatalBusinessException(messageSource.getMessage("registFaildTarget", new String[] {"入庫実績"}, Locale.JAPAN));
		}
	}
	
	/**
	 * 在庫が存在すれば在庫数を加算、存在しなければ新たに作成する。
	 * <p>更新・登録に失敗した場合はFatalException</p>
	 * @param inventory 在庫
	 * @param registerDto 入庫実績リクエストデータ
	 * @param schedule 入庫予定
	 */
	private void insertOrUpdateInventory(Inventory inventory, InboundResultRegisterDto registerDto, InboundSchedule schedule) {
		if (inventory == null) {
			inventory = new Inventory();
			inventory.setCompanyId(registerDto.getCompanyId());
			inventory.setProductId(schedule.getProductId());
			inventory.setWarehouseId(schedule.getWarehouseId());
			inventory.setOnHandQty(registerDto.getResultQty());
			
			Integer id = inventoryMapper.insert(inventory);
			
			if (id == null) {
				throw new FatalBusinessException(messageSource.getMessage("registFaildTarget", new String[] {"在庫"}, Locale.JAPAN));
			}
		} else {
			int count = inventoryMapper.updateOnHandQty(inventory, registerDto.getResultQty());
			
			if(count == 0) {
				throw new FatalBusinessException(messageSource.getMessage("updateFaildTarget", new String[] {"在庫"}, Locale.JAPAN));
			}
		}
	}
}
