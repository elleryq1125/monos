package com.example.monos.service;

import java.util.List;
import java.util.Optional;

import com.example.monos.domain.Warehouse;
import com.example.monos.dto.WarehouseSearchCondition;

public interface WarehouseService {
	List<Warehouse> search(WarehouseSearchCondition condition);
	Optional<Warehouse> findById(int warehousetId, int companyId);
	List<Warehouse> findActiveByCompanyId(int companyId);
	String save(Warehouse product);
}
