package com.example.monos.form;

import lombok.Data;

/**
 * @author t.Ueta
 * 出庫予定一覧画面 検索欄 リクエストデータ
 */
@Data
public class OutboundScheduleSearchForm {
    private String productCode;
    private String productName;
    private String warehouseCode;
    private String warehouseName;
    private String name;
    private Integer statusValue;
}
