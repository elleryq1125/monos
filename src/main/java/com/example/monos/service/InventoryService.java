package com.example.monos.service;

import java.util.List;

import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;

public interface InventoryService {
	List<InventoryListDto> search(InventorySearchCondition condition);
}
