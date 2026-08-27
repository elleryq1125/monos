package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.Inventory;
import com.example.monos.dto.InventoryListDto;
import com.example.monos.dto.InventoryDetailDto;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.dto.AvaliableInventoryDto;

@Mapper
public interface InventoryMapper {
	List<InventoryListDto> selectList(InventorySearchCondition condition);
	InventoryDetailDto selectDetailById(@Param("inventoryId") int inventoryId, @Param("companyId") int companyId);
	Inventory selectById(@Param("inventoryId") Integer inventoryId, @Param("companyId") Integer companyId);
	Inventory selectByIdAndVersion(@Param("inventoryId") Integer inventoryId, @Param("version") Integer version);
	Inventory selectByProductIdAndWarehouseId(@Param("companyId") Integer companyId, @Param("productId") Integer productId, @Param("warehouseId") Integer warehouseId);
	List<AvaliableInventoryDto> selectAvailableInventories(@Param("companyId") int companyId, @Param("productId") int productId, @Param("outboundScheduleId") Integer outboundScheduleId);
	Integer insert(Inventory inventory);
	int updateOnHandQty(@Param("inventory") Inventory inventory, @Param("inboundQty") int inboundQty);
	int updateStockSettings(Inventory inventory);
	int updateVersion(@Param("inventory") Inventory inventory);
}
