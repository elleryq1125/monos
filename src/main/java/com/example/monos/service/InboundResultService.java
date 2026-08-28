package com.example.monos.service;

import java.util.List;

import com.example.monos.dto.InboundResultListDto;
import com.example.monos.dto.InboundResultRegisterDto;
import com.example.monos.dto.InboundResultSearchCondition;

public interface InboundResultService {
	List<InboundResultListDto> search(InboundResultSearchCondition condition);
	String register(InboundResultRegisterDto registerDto);
}
