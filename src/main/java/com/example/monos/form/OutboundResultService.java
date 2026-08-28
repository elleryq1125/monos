package com.example.monos.form;

import java.util.List;

import com.example.monos.dto.OutboundResultListDto;
import com.example.monos.dto.OutboundResultRegisterDto;
import com.example.monos.dto.OutboundResultSearchCondition;

public interface OutboundResultService {
	List<OutboundResultListDto> search(OutboundResultSearchCondition condition);
	String register(OutboundResultRegisterDto registerDto);
}
