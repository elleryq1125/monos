package com.example.monos.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t.ueta
 * 入庫予定のDomainクラス
 */
@Data
@NoArgsConstructor
public class InboundSchedule {
	private Integer inboundScheduleId;
	private Integer companyId;
	private Integer productId;
    private Integer warehouseId;
    private Integer scheduleQty;
    private LocalDate scheduleDate;
    private Integer status;
    private Integer version;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deteledAt;
}
