package com.example.monos.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.monos.domain.User;
import com.example.monos.dto.UserInfo;

@Mapper
public interface UserMapper {
    List<UserInfo> findByCompanyId(int companyId);
    Optional<UserInfo> findByUserId(int userId);
	Optional<UserInfo> findByEmail(String email);
	void insert(User user);
	void update(User user);
	void delete(int userId);
}
