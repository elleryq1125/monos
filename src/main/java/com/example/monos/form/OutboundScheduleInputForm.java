package com.example.monos.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author t.ueta
 * 出庫予定登録・更新画面のリクエストデータ
 */
@Data
public class OutboundScheduleInputForm {
	private Integer outboundScheduleId;
	
	private Integer outboundScheduleVersion;
	
	@NotNull(message = "{NotNull.outboundScheduleInputForm}")
	private Integer inventoryId;
	
	private Integer inventoryVersion;
	
	@NotNull(message = "{NotNull.outboundScheduleInputForm}")
	@Min(value = 1, message = "{Min.outboundScheduleInputForm}")
	private Integer scheduleQty;
	
	@NotNull(message = "{NotNull.outboundScheduleInputForm}")
	private LocalDate scheduleDate;
}
