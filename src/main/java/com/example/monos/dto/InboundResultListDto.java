package com.example.monos.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InboundResultListDto {
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private int resultQty;
	private Date resultDate;
}