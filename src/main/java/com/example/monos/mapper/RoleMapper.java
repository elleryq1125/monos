package com.example.monos.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.domain.Role;

@Mapper
public interface RoleMapper {
    List<Role> findAll();
}
