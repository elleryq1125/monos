package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;
import com.example.monos.mapper.InboundScheduleMapper;

@Service
public class InboundScheduleServiceImpl implements InboundScheduleService {
	InboundScheduleMapper inboundScheduleMapper;
	
	public InboundScheduleServiceImpl(InboundScheduleMapper inboundScheduleMapper){
		this.inboundScheduleMapper = inboundScheduleMapper;
	}

	@Override
	public List<InboundScheduleListDto> search(InboundScheduleSearchCondition condition) {
		return inboundScheduleMapper.selectList(condition);
	}

}
