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
	InboundSchedule selectByIdAndVersion(@Param("inboundScheduleId") int inboundScheduleId, @Param("companyId") int companyId, @Param("version") int version);
	InboundScheduleDetailDto selectDetailById(@Param("inboundScheduleId") int inboundScheduleId, @Param("companyId") int companyId);
	void insert(InboundSchedule inboundSchedule);
	int update(InboundSchedule inboundSchedule);
	int updateStatus(InboundSchedule inboundSchedule);
}
