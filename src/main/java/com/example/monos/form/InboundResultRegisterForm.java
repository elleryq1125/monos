package com.example.monos.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author t.ueta
 * 入庫実績登録画面のリクエストデータ
 */
@Data
public class InboundResultRegisterForm {
	private Integer inboundScheduleId;
	
	private Integer inboundScheduleVersion;
	
	private int totalResultQty;
	
	@NotNull(message = "{NotNull.inboundResultInputForm}")
	@Min(value = 1, message = "{Min.inboundResultInputForm}")
	private Integer resultQty;
	
	@NotNull(message = "{NotNull.inboundResultInputForm}")
	private LocalDate resultDate;
}
