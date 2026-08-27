package com.example.monos.service;

import java.util.List;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.dto.AvaliableInventoryDto;
import com.example.monos.dto.InventoryDetailDto;
import com.example.monos.domain.Inventory;

public interface InventoryService {
	List<InventoryListDto> search(InventorySearchCondition condition);
	InventoryDetailDto findById(int inventoryId, int companyId);
	void update(Inventory inventory);
	List<AvaliableInventoryDto> getAvailableInventories(int companyId, int productId, Integer outboundScheduleId);
}
