package com.example.monos.dto;

import lombok.Data;

@Data
/**
 * <p>出庫予定検索の検索条件を表すDTO。</p>
 * @author t.ueta
 */
public class OutboundScheduleSearchCondition {
	private int companyId;
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private Integer status;
}
