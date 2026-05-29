package com.example.monos.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.InboundResult;

@Mapper
public interface InboundResultMapper {
	int selectTotalResultQty(@Param("inboundScheduleId") Integer inboundScheduleId, @Param("companyId") Integer companyId);
	Integer insert(InboundResult inboundResult);
}
