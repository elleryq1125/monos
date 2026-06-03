package com.example.monos.dto;

import lombok.Data;

@Data
/**
 * <p>在庫一覧画面で表示する在庫情報を表すDTO。</p>
 * @author t.ueta
 */
public class InventoryListDto {
	private int inventoryId;
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private int onHandQty;
	private int resevedQty;
}
