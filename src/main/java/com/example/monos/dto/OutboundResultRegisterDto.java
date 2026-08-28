package com.example.monos.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * @author t.ueta
 * 出庫実績の登録リクエストデータ
 */
@Data
public class OutboundResultRegisterDto {
    private Integer outboundScheduleId;
	
	private Integer companyId;
	
	private Integer version;
	
	private int resultQty;
	
	private LocalDate resultDate;
}
