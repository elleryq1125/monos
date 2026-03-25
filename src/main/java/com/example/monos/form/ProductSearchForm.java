package com.example.monos.form;

import lombok.Data;

/**
 * @author t.Ueta
 * 商品一覧画面 検索欄 リクエストデータ
 */
@Data
public class ProductSearchForm {
    
    private String productCode;
    private String name;
    private boolean active = true;
}
