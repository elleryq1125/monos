package com.example.monos.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 倉庫追加・更新画面のリクエストデータ
 * @author t.ueta
 */
@Data
public class WarehouseInputForm {
	private Integer warehouseId;
	
	@NotBlank(message = "{NotBlank.warehouseInputForm}")
	@Pattern(regexp = "^[A-Z0-9-]+$", message = "{Pattern.warehouseInputForm.warehouseCode}")
	@Size(max = 10, message = "{Size.warehouseInputForm}")
	private String warehouseCode;
	
	@Size(max = 40, message = "{Size.warehouseInputForm}")
	private String name;
	
	private boolean active;
}
