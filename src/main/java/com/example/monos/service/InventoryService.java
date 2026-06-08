package com.example.monos.service;

import java.util.List;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.dto.AvaliableInventoryDto;

public interface InventoryService {
	List<InventoryListDto> search(InventorySearchCondition condition);
	List<AvaliableInventoryDto> getAvailableInventories(int companyId, int productId);
}
