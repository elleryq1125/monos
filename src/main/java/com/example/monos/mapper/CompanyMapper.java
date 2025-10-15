package com.example.monos.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.domain.Company;

@Mapper
public interface CompanyMapper {
    int insert(Company company);
}
