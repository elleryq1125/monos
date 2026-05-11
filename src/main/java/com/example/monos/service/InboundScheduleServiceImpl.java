package com.example.monos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.monos.common.Const;
import com.example.monos.domain.InboundSchedule;
import com.example.monos.dto.InboundScheduleDetailDto;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;
import com.example.monos.exception.BusinessException;
import com.example.monos.exception.FatalBusinessException;
import com.example.monos.mapper.InboundScheduleMapper;
import com.example.monos.mapper.ProductMapper;
import com.example.monos.mapper.WarehouseMapper;

@Service
public class InboundScheduleServiceImpl implements InboundScheduleService {
	private final InboundScheduleMapper inboundScheduleMapper;
	private final ProductMapper productMapper;
	private final WarehouseMapper warehouseMapper;
	private final MessageSource messageSource;
	
	public InboundScheduleServiceImpl(InboundScheduleMapper inboundScheduleMapper,
															 ProductMapper productMapper,
															 WarehouseMapper warehouseMapper,
															 MessageSource messageSource){
		this.inboundScheduleMapper = inboundScheduleMapper;
		this.productMapper = productMapper;
		this.warehouseMapper = warehouseMapper;
		this.messageSource = messageSource;
	}

	/**
	 * 検索条件に合致する入庫予定情報を検索する。
	 * @param condition 入庫予定一覧画面の検索条件DTO
	 * @return 検索結果（入庫予定情報のリスト）
	 */
	@Override
	public List<InboundScheduleListDto> search(InboundScheduleSearchCondition condition) {
		return inboundScheduleMapper.selectList(condition);
	}

	/**
	 * 入庫予定情報を追加・更新する。
	 * @param 入庫予定情報
	 */
	@Override
	public String save(InboundSchedule inboundSchedule) {
		String resultMessage = "";
		
		// 入庫予定IDがnullであれば追加、それ以外は更新
		if (inboundSchedule.getInboundScheduleId() == null) {
			var errors = new HashMap<String, String>();
			
			// 商品の存在チェック
			if (!productMapper.existsByProductId(inboundSchedule.getProductId(), inboundSchedule.getCompanyId())) {
				errors.put("productId", messageSource.getMessage("selectNotExists", new String[] {"商品"}, Locale.JAPAN));
			}
			
			// 倉庫の存在チェック
			if (!warehouseMapper.existsByWarehouseId(inboundSchedule.getWarehouseId(), inboundSchedule.getCompanyId())) {
				errors.put("warehouseId", messageSource.getMessage("selectNotExists", new String[] {"倉庫"}, Locale.JAPAN));
			}
			
			if (!errors.isEmpty()) {
				throw new BusinessException(errors);
			}
			
			// 入庫予定情報の登録
			inboundSchedule.setStatus(Const.InboundStatus.MINYUKO);
			inboundScheduleMapper.insert(inboundSchedule);
			resultMessage = messageSource.getMessage("registComplete", null, Locale.JAPAN);
			
		} else {
			
			// 現在の入庫予定情報を取得
			InboundSchedule currentInboundSchedule = inboundScheduleMapper.selectByIdAndVersion(
					inboundSchedule.getInboundScheduleId(), inboundSchedule.getCompanyId(), inboundSchedule.getVersion());
			
			// 業務チェック
			validateUpdateBusinessRules(currentInboundSchedule);
			
			// フィールドチェック
			validateUpdateFields(currentInboundSchedule, inboundSchedule);
		}
		return resultMessage;
	}

	/**
	 * 入庫予定情報を取得する。
	 * @param inboundScheduleId 入庫予定ID
	 * @param companyId 企業ID
	 * @return 入庫予定詳細情報
	 */
	@Override
	public Optional<InboundScheduleDetailDto> findById(int inboundScheduleId, int companyId) {
		return Optional.ofNullable(inboundScheduleMapper.selectDetailById(inboundScheduleId, companyId));
	}

	/**
	 * 更新時の業務チェック
	 * @param current 現在の入庫予定情報
	 */
	private void validateUpdateBusinessRules(InboundSchedule current) {
		
		// nullの場合は排他エラー
		if (current == null) {
			throw new FatalBusinessException(messageSource.getMessage("ExclusiveError", new String[] {"入庫予定情報"}, Locale.JAPAN));
		}
		
		// 状態「入庫済」または「キャンセル」の場合はエラー
		if (current.getStatus() == Const.InboundStatus.NYUKOZUMI) {
			throw new FatalBusinessException(
					messageSource.getMessage(
							"inboundScheduleUpdStatusError", new String[] {Const.InboundStatus.NYUKOZUMI_LABEL}, Locale.JAPAN));
		}	
		if (current.getStatus() == Const.InboundStatus.CANCEL) {
			throw new FatalBusinessException(
					messageSource.getMessage(
							"inboundScheduleUpdStatusError", new String[] {Const.InboundStatus.CANCEL_LABEL}, Locale.JAPAN));
		}
	}
	
	/**
	 * 更新時のフィールドチェック
	 * @param current 現在の入庫予定情報
	 * @param input 入力した入庫予定情報
	 */
	private void validateUpdateFields(InboundSchedule current, InboundSchedule input) {
		var errors = new HashMap<String, String>();
		
		switch(current.getStatus()) {
		case Const.InboundStatus.NYUKOCHU:
			validateNyukochuUpdateFields(current, input, errors);
			break;	
		}
		
		if (!errors.isEmpty()) {
			throw new BusinessException(errors);
		}
	}
	
	
	/**
	 * 更新時のフィールドチェック
	 * 状態「入庫中」
	 * @param current 現在の入庫予定情報
	 * @param input 入力した入庫予定情報
	 * @param フィールドエラー格納変数
	 */
	private void validateNyukochuUpdateFields(InboundSchedule current, InboundSchedule input, HashMap<String,String> errors) {
		
		// 商品または倉庫が変更されている場合はエラー
		if (current.getProductId() != input.getProductId()) {
			errors.put("productId",
							 messageSource.getMessage(
									"inboundScheduleFieldUpdError", 
									new String[] {Const.InboundStatus.NYUKOCHU_LABEL, "商品"},
									Locale.JAPAN));
		}
		if (current.getWarehouseId() != input.getWarehouseId()) {
			errors.put("warehouseId",
							 messageSource.getMessage(
									"inboundScheduleFieldUpdError", 
									new String[] {Const.InboundStatus.NYUKOCHU_LABEL, "倉庫"},
									Locale.JAPAN));
		}
	}
	
}
