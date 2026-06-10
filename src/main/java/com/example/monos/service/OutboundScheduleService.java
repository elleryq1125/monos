package com.example.monos.service;

import java.util.List;

import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;
import com.example.monos.form.OutboundScheduleSaveDto;

public interface OutboundScheduleService {
	List<OutboundScheduleListDto> search(OutboundScheduleSearchCondition condition);
	String save(OutboundScheduleSaveDto saveDto);
}
