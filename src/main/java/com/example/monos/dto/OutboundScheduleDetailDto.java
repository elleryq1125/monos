package com.example.monos.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * <p>出庫予定詳細情報を表すDTO。</p>
 * <p>各倉庫の在庫情報はJavascriptイベントで取得する。</p>
 * @author t.ueta
 */
@Data
public class OutboundScheduleDetailDto {
    private Integer outboundScheduleId;
    private String productCode;
    private String productName;
    private Integer productId;
    private String warehouseCode;
    private String warehouseName;
    private Integer totalResultQty;
    private Integer scheduleQty;
    private LocalDate scheduleDate;
    private Integer status;
    private Integer version;
}
