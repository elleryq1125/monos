package com.example.monos.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t.ueta
 * 商品のDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int productId;
    private int companyId;
    private String productCode;
    private String name;
    private String unit;
    private boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deteledAt;
}
