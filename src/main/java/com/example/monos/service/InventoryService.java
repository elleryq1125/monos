package com.example.monos.service;

import java.util.List;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.dto.WarehouseAvailabilityDto;

public interface InventoryService {
	List<InventoryListDto> search(InventorySearchCondition condition);
	List<WarehouseAvailabilityDto> getWarehouseAvailabilities(int companyId, int productId);
}
