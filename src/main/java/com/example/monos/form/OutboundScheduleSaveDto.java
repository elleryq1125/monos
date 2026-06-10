package com.example.monos.form;

import java.time.LocalDate;

import lombok.Data;

/**
 * @author t.ueta
 * 出庫予定保存用のDTO
 */
@Data
public class OutboundScheduleSaveDto {
    private Integer companyId;
    
    private Integer outboundScheduleId;
    
    private Integer outboundScheduleVersion;
    
    private Integer inventoryId;
    
    private Integer inventoryVersion;
    
    private Integer scheduleQty;
    
    private LocalDate scheduleDate;
}
