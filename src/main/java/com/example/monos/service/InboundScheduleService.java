package com.example.monos.service;

import java.util.List;
import java.util.Optional;

import com.example.monos.domain.Product;
import com.example.monos.domain.Warehouse;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.dto.WarehouseSearchCondition;

public interface InboundScheduleService {
	List<InboundScheduleListDto> search(InboundScheduleSearchCondition condition);
}
