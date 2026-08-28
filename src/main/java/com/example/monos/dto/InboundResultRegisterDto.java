package com.example.monos.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * @author t.ueta
 * 入庫実績登録画面のリクエストデータ
 */
@Data
public class InboundResultRegisterDto {
	private Integer inboundScheduleId;
	
	private Integer companyId;
	
	private Integer inboundScheduleVersion;
	
	private int resultQty;
	
	private LocalDate resultDate;
}
