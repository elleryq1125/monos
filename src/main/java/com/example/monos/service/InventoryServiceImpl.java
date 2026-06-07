package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.dto.WarehouseAvailabilityDto;
import com.example.monos.mapper.InventoryMapper;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }


    /**
     * <p>在庫情報を検索する。</p>
     * @param condition 検索条件 {@link InventorySearchCondition}
     * @return 在庫情報のリスト {@link InventoryListDto}
     * @see InventoryMapper#selectList(InventorySearchCondition)
     */
    @Override
    public List<InventoryListDto> search(InventorySearchCondition condition) {
        return inventoryMapper.selectList(condition);
    }

    /**
     * 指定された商品IDと会社IDの倉庫在庫状況を取得する。
     * @param productId  商品ID
     * @param companyId  会社ID
     * @return 倉庫在庫状況のリスト {@link WarehouseAvailabilityDto}
     * @see InventoryMapper#selectWarehouseAvailabilities(int, int)
     */
    @Override
    public List<WarehouseAvailabilityDto> getWarehouseAvailabilities(int productId, int companyId) {
        return inventoryMapper.selectWarehouseAvailabilities(productId, companyId);
    }
}
