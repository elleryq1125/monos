package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.domain.InboundSchedule;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;

@Mapper
public interface InboundScheduleMapper {
	List<InboundScheduleListDto> selectList(InboundScheduleSearchCondition condition);
	void insert(InboundSchedule inboundSchedule);
}
