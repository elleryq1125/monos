package com.example.monos.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author t.ueta
 * 入庫実績のDomainクラス
 */
@Data
@NoArgsConstructor
public class InboundResult {
	private Integer inboundResultId;
	private Integer companyId;
	private Integer inboundScheduleId;
    private int resultQty;
    private LocalDate resultDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deteledAt;
}
