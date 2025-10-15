package com.example.monos.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.monos.common.Const;
import com.example.monos.controller.SignupController;
import com.example.monos.dto.ResultMessage;
import com.example.monos.mapper.UserMapper;
import com.example.monos.service.TempUserService;
import com.example.monos.service.UserService;

@WebMvcTest(SignupController.class)
@AutoConfigureMockMvc
public class SignupControllerTest extends AbstractControllerTest {

    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    TempUserService tempUserService;
    
    @MockBean
    UserService userService;
    
    @MockBean
    UserMapper userMapper;
    
    @Nested
    class showSignupTests {
        @Test
        @DisplayName("【正常系】サインアップ画面が表示されることを確認")
        void testShowSignup() throws Exception {
            // Act
            mockMvc.perform(get("/signup")
                    .with(testUser())
                    )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("signup/signup"));
        }
    }
    
    
    @Nested
    class tempUserRegistTests{
        
        @Test
        @DisplayName("【異常系】入力エラー発生時、ユーザー情報が仮登録されずにサインアップ画面が表示されることを確認")
        void testValidationError_showSignup() throws Exception {
            // Act
            mockMvc.perform(post("/signup")
                    .param("name", "")
                    .param("email", "")
                    .param("password", "")
                    .param("companyName", "")
                    .with(testUser())
                    .with(csrf())
            )
            // Asert    
            .andExpect(status().isOk())
            .andExpect(view().name("signup/signup"))
            .andExpect(model().attributeHasFieldErrors("signupForm", "name", "email", "password", "companyName"));
            
            verify(tempUserService, never()).register(any());
        }
        
        @Test
        @DisplayName("【正常系】入力エラーが無い場合、ユーザー情報が仮登録されて仮登録完了画面が表示されることを確認")
        void testTempUserRegist_redirectTempUserRegist() throws Exception {
            // Arrange
            var resultMessage = new ResultMessage(Const.MESSAGE_TYPE_SUCCESS, "OK");
            when(tempUserService.register(any())).thenReturn(resultMessage);
            
            // Act
            mockMvc.perform(post("/signup")
                    .param("name", "テスト　太郎")
                    .param("email", "test@gmail.com")
                    .param("password", "1234567890")
                    .param("passwordConfirm", "1234567890")
                    .param("companyName", "テスト株式会社")
                    .with(testUser())
                    .with(csrf())
            )
            // Assert
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/signup/tempuserregist"))
            .andExpect(flash().attribute("email", "test@gmail.com"))
            .andExpect(flash().attribute("resultMessage", resultMessage.getMessage()))
            .andDo(print());
        }
    }
    
    @Nested
    class showSignupTempUserRegistTests {
        @Test
        @DisplayName("【正常系】仮登録完了画面が表示されることを確認")
        void testShowSignupTempUserRegist() throws Exception {
            // Act
            mockMvc.perform(get("/signup/tempuserregist")
                    .with(testUser())
            )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("signup/signup-tempuser-regist"));
        }
    }
    
    @Nested
    class showSignupUserRegistTests {
        @Test
        @DisplayName("【正常系】本登録完了画面が表示されることを確認")
        void testShowSignupUserRegist() throws Exception{
            // Arrange
            var uuid = "abcd1234";
            var resultMessage = new ResultMessage(Const.MESSAGE_TYPE_SUCCESS, "OK");
            when(userService.registUser(uuid)).thenReturn(resultMessage);
            
            // Act
            mockMvc.perform(get("/signup/userregist")
                    .param("uuid", uuid)
                    .with(testUser())
            )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("signup/signup-user-regist"))
            .andExpect(model().attribute("resultMessage", resultMessage.getMessage()));
        }
    }
}
