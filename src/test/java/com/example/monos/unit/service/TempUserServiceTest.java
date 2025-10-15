package com.example.monos.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.monos.common.Const;
import com.example.monos.common.Mail;
import com.example.monos.domain.TempUser;
import com.example.monos.dto.ResultMessage;
import com.example.monos.mapper.TempUserMapper;
import com.example.monos.service.TempUserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TempUserServiceTest {
    @InjectMocks
    private TempUserServiceImpl tempUserService;

    @Mock
    TempUserMapper tempUserMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    Mail mail;
    @Mock
    MessageSource messageSource;
    
    @Test
    @DisplayName("【正常系】仮ユーザー情報を登録し、認証メールを送信後、成功メッセージが返されることを確認")
    void testRegister() {
        // Arrange
        var tempUser = new TempUser();
        tempUser.setPassword("12345");
        when(passwordEncoder.encode(anyString())).thenReturn("abcdefghijk");
        when(messageSource.getMessage(eq("tempUserRegistComplete"), any(), eq(Locale.JAPAN))).thenReturn("Temp User Regist Complete");
        
        // Act
        ResultMessage result = tempUserService.register(tempUser);
        
        // Assert
        assertEquals(result.getType(), Const.MESSAGE_TYPE_SUCCESS);
        assertEquals(result.getMessage(), "Temp User Regist Complete");
        verify(tempUserMapper).insert(tempUser);
        verify(mail).sendTempUserRegisteredMail(tempUser);
    }
}
