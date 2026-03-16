package com.example.monos.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 商品追加・更新画面のリクエストデータ
 * @author t.ueta
 */
@Data
public class ProductInputForm {
	private Integer productId;
	
	@NotBlank(message = "{NotBlank.productInputForm.productCode}")
	@Pattern(regexp = "^[A-Z0-9-]+$", message = "{Pattern.productInputForm.productCode}")
	@Size(max = 10, message = "{Size.productInputForm.productCode}")
	private String productCode;
	
	@Size(max = 40, message = "{Size.productInputForm.name}")
	private String name;
	
	@Size(max = 10, message = "{Size.productInputForm.unit}")
	private String unit;
}
