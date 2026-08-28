package com.example.monos.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class OutboundResultSearchForm {
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate resultDateFrom;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate resultDateTo;
}