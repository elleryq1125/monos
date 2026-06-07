package com.example.monos.service;

import java.util.List;
import java.util.Optional;

import com.example.monos.domain.InboundSchedule;
import com.example.monos.dto.InboundScheduleDetailDto;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;

public interface InboundScheduleService {
	List<InboundScheduleListDto> search(InboundScheduleSearchCondition condition);
	Optional<InboundScheduleDetailDto> findById(int inboundScheduleId, int companyId);
	String save(InboundSchedule inboundSchedule);
}
