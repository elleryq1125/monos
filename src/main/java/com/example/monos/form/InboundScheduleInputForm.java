package com.example.monos.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author t.ueta
 * 入庫予定登録・更新画面のリクエストデータ
 */
@Data
public class InboundScheduleInputForm {
	private Integer inboundScheduleId;
	
	private Integer version;
	
	@NotNull(message = "{NotNull.inboundScheduleInputForm}")
	private Integer productId;
	
	@NotNull(message = "{NotNull.inboundScheduleInputForm}")
	private Integer warehouseId;
	
	@NotNull(message = "{NotNull.inboundScheduleInputForm}")
	@Min(value = 1, message = "{Min.inboundScheduleInputForm}")
	private Integer scheduleQty;
	
	@NotNull(message = "{NotNull.inboundScheduleInputForm}")
	private LocalDate scheduleDate;
}
