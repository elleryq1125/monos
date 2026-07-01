package com.example.monos.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.OutboundResult;

/**
 * @author t.ueta
 * outbound_resultsのMapperインターフェース
 */
@Mapper
public interface OutboundResultMapper {
	int selectTotalResultQty(@Param("outboundScheduleId") Integer outboundScheduleId, @Param("companyId") Integer companyId);
	Integer insert(OutboundResult outboundResult);
}

