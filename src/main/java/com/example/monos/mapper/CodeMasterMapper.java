package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.domain.CodeMaster;

@Mapper
public interface CodeMasterMapper {
    List<CodeMaster> selectListByCodeType(String codeType);
}
