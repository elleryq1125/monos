package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.mapper.InventoryMapper;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryMapper inventoryMapper) {
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    /**
     * <p>在庫情報を検索する。</p>
     * @param condition 検索条件 {@link InventorySearchCondition}
     * @return 在庫情報のリスト {@link InventoryListDto}
     * @see InventoryMapper#selectList(InventorySearchCondition)
     */
    public List<InventoryListDto> search(InventorySearchCondition condition) {
        return inventoryMapper.selectList(condition);
    }
}
