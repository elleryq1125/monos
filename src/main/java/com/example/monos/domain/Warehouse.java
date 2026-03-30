package com.example.monos.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t.ueta
 * 倉庫のDTO
 */
@Data
@NoArgsConstructor
public class Warehouse {
    private Integer warehouseId;
    private int companyId;
    private String warehouseCode;
    private String name;
    private boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deteledAt;
}
