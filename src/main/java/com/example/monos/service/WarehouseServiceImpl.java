package com.example.monos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.monos.domain.Warehouse;
import com.example.monos.dto.WarehouseSearchCondition;
import com.example.monos.exception.BusinessException;
import com.example.monos.exception.FatalBusinessException;
import com.example.monos.mapper.WarehouseMapper;

@Service
public class WarehouseServiceImpl implements WarehouseService {
	private final WarehouseMapper warehouseMapper;
	private final MessageSource messageSource;
	
	public WarehouseServiceImpl(WarehouseMapper warehouseMapper, MessageSource messageSource) {
		this.warehouseMapper = warehouseMapper;
		this.messageSource = messageSource;
	}

	/**
	 * 検索条件に合致する倉庫情報を検索する。
	 * @param condition 倉庫一覧画面の検索条件DTO
	 * @return 倉庫情報のリスト
	 */
	@Override
	public List<Warehouse> search(WarehouseSearchCondition condition) {
		return warehouseMapper.selectList(condition);
	}

	/**
	 * 特定のIDを持つ倉庫情報を検索する。
	 * @param warehouseId 倉庫ID
	 * @param companyId 会社ID
	 * 
	 * @return 倉庫情報
	 */
	@Override
	public Optional<Warehouse> findById(int warehousetId, int companyId) {
		return Optional.ofNullable(warehouseMapper.selectById(warehousetId, companyId));
	}
	
	@Override
	public List<Warehouse> findActiveByCompanyId(int companyId) {
		var condition = new WarehouseSearchCondition();
		condition.setCompanyId(companyId);
		condition.setActive(true);
		
		return warehouseMapper.selectList(condition);
	}

	/**
	 * 倉庫情報を登録・更新する。
	 * @param warehouse 倉庫情報
	 */
	@Override
	@Transactional
	public String save(Warehouse warehouse) {
		String resultMessage = "";
		
		// 倉庫IDがnullであれば登録、それ以外は更新
		if(warehouse.getWarehouseId() == null) {
			
			// 倉庫コードの重複チェック
			if (warehouseMapper.existsByWarehouseCode(warehouse.getWarehouseCode(), warehouse.getCompanyId())) {
				var errors = new HashMap<String, String>();
				errors.put("warehouseCode", messageSource.getMessage("existsWarehouseCode", null, Locale.JAPAN));
				throw new BusinessException(errors);
			}
					
			// 倉庫情報の登録
			warehouseMapper.insert(warehouse);
			resultMessage = messageSource.getMessage("registComplete", null, Locale.JAPAN);
			
		} else {
			
			// 倉庫情報の更新
			int count = warehouseMapper.update(warehouse);
			
			if (count == 0) {
				throw new FatalBusinessException(messageSource.getMessage("updateFaild", null, Locale.JAPAN));
			} else {
				resultMessage = messageSource.getMessage("updateComplete", null, Locale.JAPAN);
			}
		}
		
		return resultMessage;
	}

}
