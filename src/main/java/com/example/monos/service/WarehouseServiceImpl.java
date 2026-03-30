package com.example.monos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.example.monos.domain.Warehouse;
import com.example.monos.dto.WarehouseSearchCondition;
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

	@Override
	public Optional<Warehouse> findById(int warehousetId, int companyId) {
		// TODO 自動生成されたメソッド・スタブ
		return Optional.empty();
	}

	@Override
	public String save(Warehouse product) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
