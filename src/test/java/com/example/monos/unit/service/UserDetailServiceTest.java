package com.example.monos.unit.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.monos.common.Const;
import com.example.monos.dto.UserInfo;
import com.example.monos.mapper.UserMapper;
import com.example.monos.service.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;
    
    @Mock
    UserMapper userMapper;
    
    @Nested
    class loadUserByUsernameTests{
        String EMAIL = "test@gmail.com";
        
        @Test
        @DisplayName("メールアドレスに紐づくユーザー情報が存在する場合、UserDetailsを返すことを確認")
        void testReturn_UserDetails() {
            // Arrange
            var userInfo = new UserInfo();
            userInfo.setUserId(123);
            userInfo.setEmail(EMAIL);
            userInfo.setRoleCode(Const.ROLE_ADMIN);
            when(userMapper.findByEmail(EMAIL)).thenReturn(Optional.of(userInfo));
            
            // Act
            UserDetails result = userDetailsService.loadUserByUsername(EMAIL);
            
            // Assert
            assertNotNull(result);
        }
        
        @Test
        @DisplayName("メールアドレスに紐づくユーザー情報が存在しない場合、例外がThrowされることを確認")
        void testReturn_UsernameNotFoundException() {
            // Arrange
            when(userMapper.findByEmail(EMAIL)).thenReturn(Optional.empty());
            
            // Assert
            assertThrows(UsernameNotFoundException.class, () ->{
                // Act
                userDetailsService.loadUserByUsername(EMAIL);
            });
        }
    }
}
