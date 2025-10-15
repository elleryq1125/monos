package com.example.monos.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.domain.TempUser;

@Mapper
public interface TempUserMapper {
    Optional<TempUser> findByUuid(String uuid);
	void insert(TempUser tempUser);
}
