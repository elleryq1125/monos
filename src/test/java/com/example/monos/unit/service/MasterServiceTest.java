package com.example.monos.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.monos.domain.Role;
import com.example.monos.mapper.RoleMapper;
import com.example.monos.service.MasterServiceImpl;

@ExtendWith(MockitoExtension.class)
public class MasterServiceTest {

    @Mock
    private RoleMapper roleMapper;
    
    @InjectMocks
    private MasterServiceImpl masterService;

    @Test
    @DisplayName("【正常系】ロールを全件取得する")
    void testGetAllRoles() {
        var currentTime = new Timestamp(System.currentTimeMillis());
        var mockRole1 = new Role("ROLE_ADMIN", "管理者", currentTime, currentTime);
        var mockRole2 = new Role("ROLE_GENERAL", "一般", currentTime, currentTime);
        List<Role> mockRoles = Arrays.asList(mockRole1, mockRole2);
        
        when(roleMapper.findAll()).thenReturn(mockRoles);
        
        // 検証メソッド実行
        List<Role> result = masterService.getAllRoles();
        
        // サイズ検証
        assertThat(result).hasSize(2);
        
        // フィールド値検証
        assertThat(result)
            .extracting(Role::getRoleCode, Role::getName, Role::getCreatedAt, Role::getUpdatedAt)
            .containsExactly(
                tuple("ROLE_ADMIN", "管理者", currentTime, currentTime),
                tuple("ROLE_GENERAL", "一般", currentTime, currentTime)
            );
    }
}
