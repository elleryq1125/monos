package com.example.monos.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InboundResultSearchCondition {
	private int companyId;
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private LocalDate resultDateFrom;
	private LocalDate resultDateTo;
}