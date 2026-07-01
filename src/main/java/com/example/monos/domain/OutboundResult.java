package com.example.monos.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.Data;

/**
 * @author t.ueta
 * outbound_resultsのドメインクラス
 */
@Data
public class OutboundResult {
    private Integer outboundResultId;
    private Integer companyId;
    private Integer outboundScheduleId;
    private int resultQty;
    private LocalDate resultDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deteledAt;
}
