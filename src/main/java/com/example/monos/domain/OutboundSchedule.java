package com.example.monos.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.Data;

/**
 * <p>出庫予定を表すドメインクラス。</p>
 */
@Data
public class OutboundSchedule {
    private Integer outboundScheduleId;
    private Integer companyId;
    private Integer inventoryId;
    private Integer scheduleQty;
    private LocalDate scheduleDate;
    private Integer status;
    private Integer version;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
}
