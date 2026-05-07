package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.InboundSchedule;
import com.example.monos.dto.InboundScheduleDetailDto;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;

@Mapper
public interface InboundScheduleMapper {
	List<InboundScheduleListDto> selectList(InboundScheduleSearchCondition condition);
	InboundScheduleDetailDto selectDetailById(@Param("inboundScheduleId") int inboundScheduleId, @Param("companyId") int companyId);
	void insert(InboundSchedule inboundSchedule);
}
