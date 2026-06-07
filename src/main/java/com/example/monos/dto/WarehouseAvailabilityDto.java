package com.example.monos.dto;

import lombok.Data;

/**
 * <p>倉庫の在庫状況を表すDTO。</p>
 * @author t.ueta
 */
@Data
public class WarehouseAvailabilityDto {
    private int warehouseId;
    private String warehouseCode;
    private String warehouseName;
    private int availableQty;
}
