package com.example.monos.dto;

import lombok.Data;

@Data
public class InboundScheduleSearchCondition {
	private int companyId;
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private Integer status;
}
