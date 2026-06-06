package com.example.monos.dto;

import java.util.Date;

import lombok.Data;

/**
 * <p>出庫予定一覧画面に表示する出庫予定の情報を保持するDTO。</p>
 * @author t.ueta
 */
@Data
public class OutboundScheduleListDto {
	private int outboundScheduleId;
	private Integer status;
	private String statusName;
	private String productCode;
	private String productName;
	private String warehouseCode;
	private String warehouseName;
	private int scheduleQty;
	private Date scheduleDate;
	private int totalResultQty;
}
