package com.example.monos.domain;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t.ueta
 * 在庫のDomainクラス
 */
@Data
@NoArgsConstructor
public class Inventory {
	private Integer inventoryId;
	private Integer companyId;
	private Integer productId;
	private Integer warehouseId;
	private int onHandQty;
    private int resevedQty;
    private Integer version;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deteledAt;
}
