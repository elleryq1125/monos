package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.monos.domain.OutboundResult;
import com.example.monos.dto.OutboundResultListDto;
import com.example.monos.dto.OutboundResultSearchCondition;

/**
 * @author t.ueta
 * outbound_resultsのMapperインターフェース
 */
@Mapper
public interface OutboundResultMapper {
	List<OutboundResultListDto> selectList(OutboundResultSearchCondition condition);
	int selectTotalResultQty(@Param("outboundScheduleId") Integer outboundScheduleId, @Param("companyId") Integer companyId);
	Integer insert(OutboundResult outboundResult);
}

