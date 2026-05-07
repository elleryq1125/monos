package com.example.monos.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InboundScheduleDetailDto {
	private Integer inboundScheduleId;
	private String productCode;
	private String productName;
	private Integer warehouseId;
	private int scheduleQty;
	private LocalDate scheduleDate;
	private Integer status;
	private Integer version;
}
