package com.example.monos.unit.controller;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.example.monos.common.Const;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.UserInfo;

public abstract class AbstractControllerTest {

    protected RequestPostProcessor testUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setCompanyId(111);
        userInfo.setRoleCode(Const.ROLE_ADMIN);
        return SecurityMockMvcRequestPostProcessors.user(
                new UserDetailsImpl(userInfo)
        );
    }
}
