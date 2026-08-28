package com.example.monos.form;

import lombok.Data;

/**
 * @author t.Ueta
 * 倉庫一覧画面 検索欄 リクエストデータ
 */
@Data
public class WarehouseSearchForm {
    
    private String warehouseCode;
    private String name;
    private boolean active = true;
}
