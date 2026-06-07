package com.example.monos.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.example.monos.common.Const;
import com.example.monos.domain.TempUser;
import com.example.monos.mapper.TempUserMapper;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TempUserMapperTest {
    private final TempUserMapper tempUserMapper;
    
    @Autowired
    TempUserMapperTest(TempUserMapper tempUserMapper){
        this.tempUserMapper = tempUserMapper;
    }
    
    @Test
    @DisplayName("【正常系】INSERT及びUUIDでSELECTの確認")
    void testInsertAndFindByUuid() {
        // Arrange
        var tempUser = new TempUser();
        tempUser.setUuid(UUID.randomUUID().toString());
        tempUser.setName("テスト太郎");
        tempUser.setEmail("test@monos.co.jp");
        tempUser.setPassword("12345");
        tempUser.setRoleCode(Const.ROLE_ADMIN);
        tempUser.setCompanyId(123);
        tempUser.setCompanyName("テスト株式会社");
        
        // Act
        tempUserMapper.insert(tempUser);
        Optional<TempUser> result = tempUserMapper.findByUuid(tempUser.getUuid());
        
        // Assert
        assertThat(result).isNotEmpty();
        
        assertThat(result.get().getUuid()).isEqualTo(tempUser.getUuid());
        assertThat(result.get().getName()).isEqualTo(tempUser.getName());
        assertThat(result.get().getEmail()).isEqualTo(tempUser.getEmail());
        assertThat(result.get().getPassword()).isEqualTo(tempUser.getPassword());
        assertThat(result.get().getRoleCode()).isEqualTo(tempUser.getRoleCode());
        assertThat(result.get().getCompanyId()).isEqualTo(tempUser.getCompanyId());
        assertThat(result.get().getCompanyName()).isEqualTo(tempUser.getCompanyName());
    }
}
