package com.example.monos.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.monos.common.Const;
import com.example.monos.controller.UsersController;
import com.example.monos.dto.ResultMessage;
import com.example.monos.dto.UserInfo;
import com.example.monos.mapper.UserMapper;
import com.example.monos.service.MasterService;
import com.example.monos.service.TempUserService;
import com.example.monos.service.UserService;

@WebMvcTest(UsersController.class)
@AutoConfigureMockMvc
public class UsersControllerTest extends AbstractControllerTest {
    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    TempUserService tempUserService;
    
    @MockBean
    UserService userService;
    
    @MockBean
    MasterService masterService;
    
    @MockBean
    UserMapper userMapper;
    
    @Nested
    class ShowUsersTests {
        
        @Test
        @DisplayName("【正常系】ユーザー一覧画面が表示されることを確認")
        void testShowUsers() throws Exception {
            // Arrange
            var user1 = new UserInfo(1, "test1@gmail.com", "1******", "テスト１","ADMIN","管理者", 1234, "株式会社テスト", null, null);
            var user2 = new UserInfo(2, "test2@gmail.com", "2******", "テスト２","GENERAL","一般", 1234, "株式会社テスト", null, null);
            List<UserInfo> companyUsers = Arrays.asList(user1, user2);
            when(userService.getUserInfoInCompany(anyInt())).thenReturn(companyUsers);
            
            // Act
            mockMvc.perform(get("/users")
                    .with(testUser())
            )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("users/users"));
        }
    }
    
    @Nested
    class ShowUserAddFormTests {
        @Test
        @DisplayName("【正常系】ユーザー追加画面が表示されることを確認")
        void testShowUserAddForm() throws Exception {
            // Act
            mockMvc.perform(get("/users/add")
                    .with(testUser())
            )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("users/user-add"));
        }
    }
    
    @Nested
    class UserAddTests {
        
        @Test
        @DisplayName("【異常系】入力エラー発生時、ユーザー情報が仮登録されずにユーザー追加画面が表示されることを確認")
        void testValidationError_showUserAdd() throws Exception {
            // Act
            mockMvc.perform(post("/users/add")
                    .param("name","")
                    .param("email","")
                    .param("password","")
                    .with(testUser())
                    .with(csrf())
            )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("users/user-add"))
            .andExpect(model().attributeHasFieldErrors("userAddForm","name", "email", "password"));
            
            verify(tempUserService, never()).register(any());
        }
        
        @Test
        @DisplayName("【正常系】入力エラーがない場合、ユーザー情報が仮登録されてユーザー一覧画面にリダイレクトされることを確認")
        void testTempUserRegist_redirectUsers() throws Exception {
            // Arrange
            var resultMessage = new ResultMessage(Const.MESSAGE_TYPE_SUCCESS, "OK");
            when(tempUserService.register(any())).thenReturn(resultMessage);
            
            // Act
            mockMvc.perform(post("/users/add")
                    .param("name","テスト　太郎")
                    .param("email","test@gmail.com")
                    .param("password","1234567890")
                    .param("passwordConfirm", "1234567890")
                    .with(testUser())
                    .with(csrf())
            )
            // Assert
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attribute("resultMessageType", resultMessage.getType()))
            .andExpect(flash().attribute("resultMessage", resultMessage.getMessage()));
        }
    }
    
    @Nested
    class ShowUserUpdateFormTests {
        
        @Test
        @DisplayName("【異常系】サインインユーザーの会社に存在しないユーザーIDがパスに指定された場合、ユーザー一覧画面にリダイレクトすることを確認")
        void testNotExistsUserId_redirectUsers() throws Exception{
            // Arrange
            int userId = 12345;
            when(userService.getUserInfo(eq(userId), anyInt())).thenReturn(Optional.empty());
            
            // Act
            mockMvc.perform(get("/users//update/{userId}", userId)
                    .with(testUser())
            )
            // Assert
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users"));
        }
        
        @Test
        @DisplayName("【正常系】サインインユーザーの会社に存在するユーザーIDがパスに指定された場合、ユーザー更新画面が表示されることを確認")
        void testExistsUserId_showUserUpdate() throws Exception{
            // Arrange
            int userId = 12345;
            var userInfo = new UserInfo(userId, "test@gmail.com", "1234567890", "テスト　太郎", Const.ROLE_ADMIN, "管理者", 111, "株式会社テスト", null, null);
            when(userService.getUserInfo(eq(userId), anyInt())).thenReturn(Optional.of(userInfo));
            
            // Act
            mockMvc.perform(get("/users//update/{userId}", userId)
                    .with(testUser())
            )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("users/user-update"))
            .andExpect(model().attribute("userId", userInfo.getUserId()))
            .andExpect(model().attributeExists("userUpdateForm"));
        }
    }
    
    @Nested
    class UserUpdateTests {
        
        @Test
        @DisplayName("【異常系】入力エラー発生時、ユーザー情報が更新されずにユーザー更新画面が表示されることを確認")
        void testValidationError_showUserUpdate() throws Exception {
            // Act
            mockMvc.perform(post("/users/update/{userId}", 12345)
                    .param("name", "")
                    .with(testUser())
                    .with(csrf())
            )
            // Assert
            .andExpect(status().isOk())
            .andExpect(view().name("users/user-update"))
            .andExpect(model().attributeHasFieldErrors("userUpdateForm", "name"));
            
            verify(userService, never()).updateUser(any(), anyInt());
        }
        
        @Test
        @DisplayName("【正常系】入力エラーがない場合、ユーザー情報が更新されてユーザー一覧画面にリダイレクトすることを確認")
        void testUpdateUser_redirectUsers() throws Exception {
            // Arrange
            var resultMessage = new ResultMessage(Const.MESSAGE_TYPE_SUCCESS, "OK");
            when(userService.updateUser(any(), anyInt())).thenReturn(resultMessage);
            
            // Act
            mockMvc.perform(post("/users/update/{userId}", 12345)
                    .param("name", "テスト　太郎")
                    .param("roleCode", Const.ROLE_ADMIN)
                    .with(testUser())
                    .with(csrf())
            )
            // Assert
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attribute("resultMessageType", resultMessage.getType()))
            .andExpect(flash().attribute("resultMessage",resultMessage.getMessage()));
        }
    }
    
    @Nested
    class UserDeleteTests {
        
        @Test
        @DisplayName("【正常系】ユーザー情報が削除されて、ユーザー一覧画面にリダイレクトすることを確認")
        void testDeleteUser_redirectUsers() throws Exception {
            // Arrange
            int userId = 12345;
            var resultMessage = new ResultMessage(Const.MESSAGE_TYPE_SUCCESS, "OK");
            when(userService.deleteUser(eq(userId), any())).thenReturn(resultMessage);
            
            // Act
            mockMvc.perform(post("/users/delete/{userId}", userId)
                    .with(testUser())
                    .with(csrf())
            )
            // Assert
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/users"))
            .andExpect(flash().attribute("resultMessageType", resultMessage.getType()))
            .andExpect(flash().attribute("resultMessage",resultMessage.getMessage()));
        }
    }
}
