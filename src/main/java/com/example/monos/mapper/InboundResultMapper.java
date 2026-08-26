package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.InboundResult;
import com.example.monos.dto.InboundResultListDto;
import com.example.monos.dto.InboundResultSearchCondition;

@Mapper
public interface InboundResultMapper {
	List<InboundResultListDto> selectList(InboundResultSearchCondition condition);
	int selectTotalResultQty(@Param("inboundScheduleId") Integer inboundScheduleId, @Param("companyId") Integer companyId);
	Integer insert(InboundResult inboundResult);
}
