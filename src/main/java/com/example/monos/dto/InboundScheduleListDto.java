package com.example.monos.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InboundScheduleListDto {
	private int inboundScheduleId;
	private String status;
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private int scheduleQty;
	private Date scheduleDate;
}
