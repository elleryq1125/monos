package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;

@Mapper
public interface OutboundScheduleMapper {
	List<OutboundScheduleListDto> selectList(OutboundScheduleSearchCondition condition);
}
