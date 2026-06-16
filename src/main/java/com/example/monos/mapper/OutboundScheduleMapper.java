package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.OutboundSchedule;
import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;

@Mapper
public interface OutboundScheduleMapper {
	List<OutboundScheduleListDto> selectList(OutboundScheduleSearchCondition condition);
	OutboundSchedule selectById(@Param("outboundScheduleId") Integer outboundScheduleId, @Param("companyId") Integer companyId);
	Integer insert(OutboundSchedule outboundSchedule);
	int selectTotalScheduleQtyByInventoryId(@Param("companyId") Integer companyId, @Param("inventoryId") Integer inventoryId);
}
