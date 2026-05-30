package com.example.monos.dto;

import lombok.Data;

@Data
public class InventorySearchCondition {
	private int companyId;
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private boolean stockOut;
}
