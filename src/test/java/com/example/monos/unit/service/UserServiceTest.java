package com.example.monos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.example.monos.common.Const;
import com.example.monos.domain.TempUser;
import com.example.monos.domain.User;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ResultMessage;
import com.example.monos.dto.UserInfo;
import com.example.monos.mapper.CompanyMapper;
import com.example.monos.mapper.TempUserMapper;
import com.example.monos.mapper.UserMapper;
import com.example.monos.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Mock
    private UserMapper userMapper;
    @Mock
    private TempUserMapper tempUserMapper;
    @Mock
    private CompanyMapper companyMappper;
    @Mock
    private MessageSource messageSource;
    
    @Nested
    class GetUserInfoInCompanyTests{
        @Test
        @DisplayName("【正常系】会社IDに紐づくユーザー情報が存在する場合、List<UserInfo>を取得することを確認")
        void testReturnUserInfo() {
            // Arrange
            int signinUserCompanyId = 12345;
            var mockUser1 = new UserInfo(1, "test1@gmail.com", "1******", "テスト１","ROLE_ADMIN","管理者", signinUserCompanyId, "株式会社テスト", null, null);
            var mockUser2 = new UserInfo(2, "test2@gmail.com", "2******", "テスト２","ROLE_GENERAL","一般", signinUserCompanyId, "株式会社テスト", null, null);
            List<UserInfo> mockUsers = Arrays.asList(mockUser1, mockUser2);
            when(userMapper.findByCompanyId(signinUserCompanyId)).thenReturn(mockUsers);

            // Act
            List<UserInfo> result = userService.getUserInfoInCompany(signinUserCompanyId);
        
            // Assert
            assertThat(result).hasSize(2);
            assertEquals(result, mockUsers);
        }
        
        @Test
        @DisplayName("【正常系】会社IDに紐づくユーザー情報が存在しない場合、Emptyを取得することを確認")
        void testReturnEmpty() {
            // Arrange
            int signinUserCompanyId = 12345;
            when(userMapper.findByCompanyId(signinUserCompanyId)).thenReturn(Collections.emptyList());

            // Act
            List<UserInfo> result = userService.getUserInfoInCompany(signinUserCompanyId);
        
            // Assert
            assertTrue(result.isEmpty());
        }
    }
    
    @Nested
    class GetUserInfoTests{
        
        @Test
        @DisplayName("【正常系】会社IDが一致する場合、Optional<UserInfo>が返されることを確認")
        void testMatchCompanyId() {
            // Arrange
            var userId = 1;
            var signinCompanyId = 12345;
            // 会社IDが一致するユーザー情報
            var mockUser = new UserInfo(userId, "test1@gmail.com", "1******", "テスト１","ROLE_ADMIN","管理者", signinCompanyId, "株式会社テスト", null, null);
            when(userMapper.findByUserId(userId)).thenReturn(Optional.of(mockUser));
            
            // Act
            Optional<UserInfo> result = userService.getUserInfo(userId, signinCompanyId);
            
            // Assert
            assertTrue(result.isPresent());
            assertEquals(result.get(), mockUser);
        }
        
        @Test
        @DisplayName("【正常系】会社IDが一致しない場合、Optional.empty()が返されることを確認")
        void testNonMatchCompanyId() {
            // Arrange
            var userId = 1;
            var signinCompanyId = 12345;
            // 会社IDが一致しないユーザー情報
            var mockUser = new UserInfo(userId, "test1@gmail.com", "1******", "テスト１","ROLE_ADMIN","管理者", 99999, "株式会社テスト", null, null);
            when(userMapper.findByUserId(userId)).thenReturn(Optional.of(mockUser));
            
            // Act
            Optional<UserInfo> result = userService.getUserInfo(userId, signinCompanyId);
            
            // Assert
            assertFalse(result.isPresent());
        }
        
        @Test
        @DisplayName("【正常系】ユーザー情報が存在しない場合、Optional.empty()が返されることを確認")
        void testUserInfoNotFound() {
            // Arrange
            var userId = 1;
            var signinCompanyId = 12345;
            when(userMapper.findByUserId(userId)).thenReturn(Optional.empty());
            
            // Act
            Optional<UserInfo> result = userService.getUserInfo(userId, signinCompanyId);
            
            // Assert
            assertFalse(result.isPresent());
        }
    }
    
    @Nested
    class RegistUserTests{
        static final String UUID = "12345";
        static final String EMAIL = "test@gmail.com";
          
        @Test
        @DisplayName("【異常系】UUIDに紐づく仮登録情報が存在しない場合、エラーメッセージを返すことを確認")
        void testTempRegistInfoNotExists_returnError(){
            // Arrange
            when(tempUserMapper.findByUuid(UUID)).thenReturn(Optional.empty());
            when(messageSource.getMessage(eq("urlExpired"), any(), eq(Locale.JAPAN))).thenReturn("Url Expired");
        
            // Act
            ResultMessage result = userService.registUser(UUID);
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_ERROR);
            assertEquals(result.getMessage(), "Url Expired");
            verify(tempUserMapper).findByUuid(UUID);
            verify(userMapper, never()).findByEmail(any());
        }
        
        @Test
        @DisplayName("【異常系】仮登録メールアドレスが本登録済の場合、エラーメッセージを返すことを確認")
        void testEmailExists_returnError(){
            // Arrange
            var tempUser = new TempUser();
            tempUser.setEmail(EMAIL);
            when(tempUserMapper.findByUuid(UUID)).thenReturn(Optional.of(tempUser));
            when(userMapper.findByEmail(EMAIL)).thenReturn(Optional.of(new UserInfo()));
            when(messageSource.getMessage(eq("emailExists"), any(), eq(Locale.JAPAN))).thenReturn("Email Exists");
        
            // Act
            ResultMessage result = userService.registUser(UUID);
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_ERROR);
            assertEquals(result.getMessage(), "Email Exists");
            verify(userMapper).findByEmail(EMAIL);
            verify(companyMappper, never()).insert(any());
            verify(userMapper, never()).insert(any());
        }
        
        @Test
        @DisplayName("【正常系】会社IDが0の場合、会社とユーザー情報を追加し、成功メッセージを返すことを確認")
        void testNewCompany_registCompanyAndUserInfo() {
            // Arrange
            var tempUser = new TempUser();
            tempUser.setEmail(EMAIL);
            tempUser.setCompanyId(0);
            when(tempUserMapper.findByUuid(UUID)).thenReturn(Optional.of(tempUser));
            when(userMapper.findByEmail(EMAIL)).thenReturn(Optional.empty());
            when(messageSource.getMessage(eq("userRegistComplete"), any(), eq(Locale.JAPAN))).thenReturn("User Regist Complete");
            
            // Act
            ResultMessage result = userService.registUser(UUID);
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_SUCCESS);
            assertEquals(result.getMessage(), "User Regist Complete");
            verify(companyMappper).insert(any());
            verify(userMapper).insert(any());
        }
        
        @Test
        @DisplayName("【正常系】会社IDが0以外の場合、ユーザー情報を追加し、成功メッセージを返すことを確認")
        void testExistsCompany_registUserInfo() {
            // Arrange
            var tempUser = new TempUser();
            tempUser.setEmail(EMAIL);
            tempUser.setCompanyId(123);
            when(tempUserMapper.findByUuid(UUID)).thenReturn(Optional.of(tempUser));
            when(userMapper.findByEmail(EMAIL)).thenReturn(Optional.empty());
            when(messageSource.getMessage(eq("userRegistComplete"), any(), eq(Locale.JAPAN))).thenReturn("User Regist Complete");
            
            // Act
            ResultMessage result = userService.registUser(UUID);
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_SUCCESS);
            assertEquals(result.getMessage(), "User Regist Complete");
            verify(companyMappper, never()).insert(any());
            verify(userMapper).insert(any());
        }
    }
    
    @Nested
    class UpdateUserTests {
        static final int USER_ID = 123;
        static final int SIGNIN_COMPANY_ID = 222;
        
        @Test
        @DisplayName("【異常系】更新対象のユーザー情報が存在しない場合、エラーメッセージが返されることを確認")
        void testUserInfoNotExists_returnError() {
            // Arrange
            var updateUser = new User();
            updateUser.setUserId(USER_ID);
            when(userMapper.findByUserId(USER_ID)).thenReturn(Optional.empty());
            when(messageSource.getMessage(eq("userInfoNotExists"), any(), eq(Locale.JAPAN))).thenReturn("UserInfo Not Exists");
       
            // Act
            ResultMessage result = userService.updateUser(updateUser, SIGNIN_COMPANY_ID);
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_ERROR);
            assertEquals(result.getMessage(), "UserInfo Not Exists");
            verify(userMapper).findByUserId(USER_ID);
            verify(userMapper, never()).update(any());
        }
        
        @Test
        @DisplayName("【異常系】更新対象のユーザー情報とサインインユーザの会社IDが相違する場合、エラーメッセージを返すことを確認")
        void testCompanyIdDiff_returnError(){
            // Arrange
            var updateUser = new User();
            updateUser.setUserId(USER_ID);
            // 会社IDが相違するユーザー情報
            var updUserInfo = new UserInfo();   
            updUserInfo.setCompanyId(333);
            when(userMapper.findByUserId(USER_ID)).thenReturn(Optional.of(updUserInfo));
            when(messageSource.getMessage(eq("updateFaild"), any(), eq(Locale.JAPAN))).thenReturn("Update Faild");
       
            // Act
            ResultMessage result = userService.updateUser(updateUser, SIGNIN_COMPANY_ID);
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_ERROR);
            assertEquals(result.getMessage(), "Update Faild");
            verify(userMapper, never()).update(any());
        }
        
        @Test
        @DisplayName("【正常系】エラー条件に該当しない場合、ユーザ情報が更新されることを確認")
        void testUpdateUser(){
            // Arrange
            var updateUser = new User();
            updateUser.setUserId(USER_ID);
            var updUserInfo = new UserInfo();
            updUserInfo.setCompanyId(SIGNIN_COMPANY_ID);
            when(userMapper.findByUserId(USER_ID)).thenReturn(Optional.of(updUserInfo));
            when(messageSource.getMessage(eq("updateComplete"), any(), eq(Locale.JAPAN))).thenReturn("Update Complete");
            
            // Act
            ResultMessage result = userService.updateUser(updateUser, SIGNIN_COMPANY_ID);
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_SUCCESS);
            assertEquals(result.getMessage(), "Update Complete");
            verify(userMapper).update(updateUser);
        }
    }
    
    @Nested
    class DeleteUserTests {
        static final int DELETE_USER_ID = 123;
        static final int SIGNIN_USER_ID = 456;
        static final int SIGNIN_COMPANY_ID = 111;
        
        // サインインユーザー情報を取得
        UserDetailsImpl getSigninUser() {
            var userInfo = new UserInfo();
            userInfo.setUserId(SIGNIN_USER_ID);
            userInfo.setCompanyId(SIGNIN_COMPANY_ID);
            userInfo.setRoleCode(Const.ROLE_ADMIN);
            return new UserDetailsImpl(userInfo);
        }
        
        @Test
        @DisplayName("【異常系】削除対象のユーザー情報が存在しない場合、エラーメッセージが返されることを確認")
        void testUserInfoNotExists_returnError() {
            // Arrange
            when(userMapper.findByUserId(DELETE_USER_ID)).thenReturn(Optional.empty());
            when(messageSource.getMessage(eq("userInfoNotExists"), any(), eq(Locale.JAPAN))).thenReturn("UserInfo Not Exists");
       
            // Act
            ResultMessage result = userService.deleteUser(DELETE_USER_ID, getSigninUser());
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_ERROR);
            assertEquals(result.getMessage(), "UserInfo Not Exists");
            verify(userMapper).findByUserId(DELETE_USER_ID);
            verify(userMapper, never()).delete(anyInt());
        }
        
        @Test
        @DisplayName("【異常系】削除対象のユーザー情報とサインインユーザの会社IDが相違する場合、エラーメッセージを返すことを確認")
        void testCompanyIdDiff_returnError(){
            // Arrange
            // 会社IDが相違するユーザー情報
            var delUserInfo = new UserInfo();   
            delUserInfo.setCompanyId(333);
            when(userMapper.findByUserId(DELETE_USER_ID)).thenReturn(Optional.of(delUserInfo));
            when(messageSource.getMessage(eq("deleteFaild"), any(), eq(Locale.JAPAN))).thenReturn("Delete Faild");
       
            // Act
            ResultMessage result = userService.deleteUser(DELETE_USER_ID, getSigninUser());
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_ERROR);
            assertEquals(result.getMessage(), "Delete Faild");
            verify(userMapper, never()).delete(anyInt());
        }
        
        @Test
        @DisplayName("【異常系】削除対象のユーザーがサインインユーザー自身の場合、エラーメッセージを返すことを確認")
        void testDeleteUserAndSigninUserAtSame_returnError(){
            // Arrange
            // サインインユーザー自身
            var delUserInfo = new UserInfo();
            delUserInfo.setUserId(SIGNIN_USER_ID);
            delUserInfo.setCompanyId(SIGNIN_COMPANY_ID);
            when(userMapper.findByUserId(SIGNIN_USER_ID)).thenReturn(Optional.of(delUserInfo));
            when(messageSource.getMessage(eq("isSigninUser"), any(), eq(Locale.JAPAN))).thenReturn("is Signin User");
       
            // Act
            ResultMessage result = userService.deleteUser(SIGNIN_USER_ID, getSigninUser());
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_ERROR);
            assertEquals(result.getMessage(), "is Signin User");
            verify(userMapper, never()).delete(anyInt());
        }
        
        @Test
        @DisplayName("【正常系】エラー条件に該当しない場合、ユーザ情報が削除されることを確認")
        void testUpdateUser(){
            // Arrange
            var delUserInfo = new UserInfo();
            delUserInfo.setUserId(DELETE_USER_ID);
            delUserInfo.setCompanyId(SIGNIN_COMPANY_ID);
            when(userMapper.findByUserId(DELETE_USER_ID)).thenReturn(Optional.of(delUserInfo));
            when(messageSource.getMessage(eq("deleteComplete"), any(), eq(Locale.JAPAN))).thenReturn("Delete Complete");
            
            // Act
            ResultMessage result = userService.deleteUser(DELETE_USER_ID, getSigninUser());
            
            // Assert
            assertEquals(result.getType(), Const.MESSAGE_TYPE_SUCCESS);
            assertEquals(result.getMessage(), "Delete Complete");
            verify(userMapper).delete(DELETE_USER_ID);
        }
    }
}
