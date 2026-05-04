package com.example.monos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.monos.common.Const;
import com.example.monos.domain.InboundSchedule;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;
import com.example.monos.exception.BusinessException;
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
			inboundSchedule.setStatus(Const.INBOUND_STATUS_MINYUKO);
			inboundScheduleMapper.insert(inboundSchedule);
			resultMessage = messageSource.getMessage("registComplete", null, Locale.JAPAN);
			
		} else {
			
		}
		return resultMessage;
	}

}
