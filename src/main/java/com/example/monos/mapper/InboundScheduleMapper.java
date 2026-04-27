package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.Product;
import com.example.monos.domain.Warehouse;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;
import com.example.monos.dto.WarehouseSearchCondition;

@Mapper
public interface InboundScheduleMapper {
	List<InboundScheduleListDto> selectList(InboundScheduleSearchCondition condition);
}
