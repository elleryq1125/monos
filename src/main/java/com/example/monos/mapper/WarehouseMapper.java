package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.Warehouse;
import com.example.monos.dto.WarehouseSearchCondition;

@Mapper
public interface WarehouseMapper {
	List<Warehouse> selectList(WarehouseSearchCondition condition);
	Warehouse selectById(@Param("warehouseId") int productId, @Param("companyId") int companyId);
	boolean existsByWarehouseCode(@Param("warehouseCode") String warehouseCode, @Param("companyId") int companyId);
	void insert(Warehouse warehouse);
	int update(Warehouse warehouse);
}
