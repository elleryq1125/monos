package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.dto.AvaliableInventoryDto;
import com.example.monos.dto.InventoryDetailDto;
import com.example.monos.domain.Inventory;
import com.example.monos.mapper.InventoryMapper;
import com.example.monos.exception.FatalBusinessException;
import org.springframework.context.MessageSource;
import java.util.Locale;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryMapper inventoryMapper;
    private final MessageSource messageSource;

    public InventoryServiceImpl(InventoryMapper inventoryMapper, MessageSource messageSource) {
        this.inventoryMapper = inventoryMapper;
        this.messageSource = messageSource;
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

    @Override
    public InventoryDetailDto findById(int inventoryId, int companyId) {
        return inventoryMapper.selectDetailById(inventoryId, companyId);
    }

    @Override
    public void update(Inventory inventory) {
        if (inventoryMapper.updateStockSettings(inventory) == 0) {
            throw new FatalBusinessException(messageSource.getMessage("ExclusiveError", new String[] {"在庫情報"}, Locale.JAPAN));
        }
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
