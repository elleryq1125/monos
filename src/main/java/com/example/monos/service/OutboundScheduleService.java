package com.example.monos.service;

import java.util.List;

import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;

public interface OutboundScheduleService {
	List<OutboundScheduleListDto> search(OutboundScheduleSearchCondition condition);
}
