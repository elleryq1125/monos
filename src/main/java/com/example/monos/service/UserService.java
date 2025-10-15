package com.example.monos.service;

import java.util.List;
import java.util.Optional;

import com.example.monos.domain.User;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ResultMessage;
import com.example.monos.dto.UserInfo;

public interface UserService {
    List<UserInfo> getUserInfoInCompany(int signinCompanyId);
    Optional<UserInfo> getUserInfo(int userId, int signinUserCompanyId);
    ResultMessage registUser(String uuid);
    ResultMessage updateUser(User updateUser, int signinUserCompanyId);
    ResultMessage deleteUser(int deleteUserId, UserDetailsImpl signinUser);
}
