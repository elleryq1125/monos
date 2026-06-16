package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.dto.AvaliableInventoryDto;
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
     * 指定された商品IDと会社IDの在庫状況を取得する。
     * @param companyId  会社ID
     * @param productId  商品ID
     * @param outboundScheudleId 出庫予定ID（引き当て可能数加算用）
     * @return 在庫状況のリスト {@link AvaliableInventoryDto}
     * @see InventoryMapper#selectAvailableInventories(int, int)
     */
    @Override
    public List<AvaliableInventoryDto> getAvailableInventories(int companyId, int productId, Integer outboundScheduleId) {
        return inventoryMapper.selectAvailableInventories(companyId, productId, outboundScheduleId);
    }
}
