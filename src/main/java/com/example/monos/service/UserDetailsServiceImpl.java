package com.example.monos.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.UserInfo;
import com.example.monos.mapper.UserMapper;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {
    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfo> userOp = userMapper.findByEmail(email);
        return userOp.map(user -> new UserDetailsImpl(user))
                .orElseThrow(() -> new UsernameNotFoundException("not found"));
    }
}
