package com.example.monos.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

 /**
  * @author t.ueta
  * 出庫実績登録画面のリクエストデータ
  */
@Data
public class OutboundResultRegisterForm {
    private Integer outboundScheduleId;
	
	private Integer version;
	
	private int totalResultQty;
	
	@NotNull(message = "{NotNull.outboundResultInputForm}")
	@Min(value = 1, message = "{Min.outboundResultInputForm}")
	private Integer resultQty;
	
	@NotNull(message = "{NotNull.outboundResultInputForm}")
	private LocalDate resultDate;
}
