package com.example.monos.dto;

import lombok.Data;

@Data
public class InventoryDetailDto {
    private int inventoryId;
    private String productCode;
    private String productName;
    private String warehouseCode;
    private String warehouseName;
    private int onHandQty;
    private int reorderPoint;
    private int appropriateStockQty;
    private int version;
}
