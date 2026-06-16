package com.example.monos.service;

import java.util.List;
import java.util.Optional;

import com.example.monos.dto.OutboundScheduleDetailDto;
import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;
import com.example.monos.form.OutboundScheduleSaveDto;

public interface OutboundScheduleService {
	List<OutboundScheduleListDto> search(OutboundScheduleSearchCondition condition);
	Optional<OutboundScheduleDetailDto> findById(int outboundScheduleId, int companyId);
	String save(OutboundScheduleSaveDto saveDto);
}
