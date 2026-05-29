package com.example.monos.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import com.example.monos.common.Const;

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

    
    /**
     * 入庫予定数量と合計入庫実績数量を比較し、ステータスを決定する。
     * @param registerTotalResultQty 合計入庫実績数量（今回入庫分含む）
     */
    public void determineStatus(int registerTotalResultQty) {
    	if (this.scheduleQty == registerTotalResultQty) {
    		this.status = Const.InboundStatus.NYUKOZUMI;
    	} else {
    		this.status = Const.InboundStatus.NYUKOCHU;
    	}
    }
}