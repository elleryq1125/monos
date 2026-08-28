package com.example.monos.dto;

import lombok.Data;

@Data
public class WarehouseSearchCondition {
	int companyId;
	String warehouseCode;
	String name;
	boolean active;
}
