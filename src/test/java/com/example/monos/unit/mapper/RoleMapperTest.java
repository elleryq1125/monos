package com.example.monos.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.example.monos.common.Const;
import com.example.monos.domain.Role;
import com.example.monos.mapper.RoleMapper;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleMapperTest {
    private final RoleMapper roleMapper;
    
    @Autowired
    RoleMapperTest(RoleMapper roleMapper){
        this.roleMapper = roleMapper;
    }
    
    @Test
    @DisplayName("【正常系】全件取得の確認")
    void testFindAll() {
        // Act
        List<Role> result = roleMapper.findAll();
        
        // Assert
        // flywayで投入したデータを検証
        assertThat(result).hasSize(3);
        
        assertThat(result)
            .extracting(Role::getRoleCode, Role::getName)
            .contains(
                tuple(Const.ROLE_ADMIN,"管理者"),
                tuple(Const.ROLE_GENERAL, "一般"),
                tuple(Const.ROLE_REFERENCE, "参照")
            );
    }
}
