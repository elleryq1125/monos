package com.example.monos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;
import com.example.monos.mapper.OutboundScheduleMapper;

@Service
public class OutboundScheduleServiceImpl implements OutboundScheduleService {
    private final OutboundScheduleMapper outboundScheduleMapper;

    public OutboundScheduleServiceImpl(OutboundScheduleMapper outboundScheduleMapper) {
        this.outboundScheduleMapper = outboundScheduleMapper;
    }

    /**
     * <p>出庫予定を検索する。</p>
      * @param condition 検索条件
      * @return 出庫予定のリスト
     */
    @Override
    public List<OutboundScheduleListDto> search(OutboundScheduleSearchCondition condition) {
        return outboundScheduleMapper.selectList(condition);
    }

}
