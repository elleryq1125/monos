package com.example.monos.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import com.example.monos.common.Const;

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

    /**
     * 出庫予定数量と合計出庫実績数量を比較し、ステータスを決定する。
     * @param totalResultQty 合計出庫実績数量（今回出庫分含む）
     */
    public void determineStatus(int totalResultQty) {
        if (totalResultQty == 0) {
            this.status = Const.OutboundStatus.MINSHUKO;
        } else if (totalResultQty < this.scheduleQty) {
            this.status = Const.OutboundStatus.SHUKOCHU;
        } else if (totalResultQty == this.scheduleQty) {
            this.status = Const.OutboundStatus.SHUKOZUMI;
        }
    }
}
