package com.example.monos.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.InboundResult;
import com.example.monos.domain.Inventory;

@Mapper
public interface InventoryMapper {
	Inventory selectByProductIdAndWarehouseId(@Param("companyId") Integer companyId, @Param("productId") Integer productId, @Param("warehouseId") Integer warehouseId);
	Integer insert(Inventory inventory);
	int updateOnHandQty(@Param("inventory") Inventory inventory, @Param("inboundQty") int inboundQty);
}
