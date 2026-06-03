package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.Inventory;
import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventorySearchCondition;

@Mapper
public interface InventoryMapper {
	List<InventoryListDto> selectList(InventorySearchCondition condition);
	Inventory selectByProductIdAndWarehouseId(@Param("companyId") Integer companyId, @Param("productId") Integer productId, @Param("warehouseId") Integer warehouseId);
	Integer insert(Inventory inventory);
	int updateOnHandQty(@Param("inventory") Inventory inventory, @Param("inboundQty") int inboundQty);
}
