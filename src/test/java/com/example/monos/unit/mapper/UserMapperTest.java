package com.example.monos.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.monos.common.Const;
import com.example.monos.domain.User;
import com.example.monos.dto.UserInfo;
import com.example.monos.mapper.UserMapper;


@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserMapperTest {
    private final UserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    UserMapperTest(UserMapper userMapper, JdbcTemplate jdbcTemplate){
        this.userMapper = userMapper;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Test
    @DisplayName("【正常系】会社IDをキー条件としたSELECTの確認")
    void testFindByCompanyId() {
        // Arrange
        createTestData();
        
        // Act
        var key = 1001;
        List<UserInfo> result = userMapper.findByCompanyId(key);
        
        // Assert
        assertThat(result).hasSize(2);
        
        // 1件目のみ内容を確認
        var res1 = result.get(0);
        assertThat(res1.getUserId()).isEqualTo(1);
        assertThat(res1.getEmail()).isEqualTo("test1@test.com");
        assertThat(res1.getPassword()).isEqualTo("123");
        assertThat(res1.getName()).isEqualTo("テスト１");
        assertThat(res1.getCompanyId()).isEqualTo(key);
    }
    
    @Test
    @DisplayName("【正常系】ユーザーIDをキー条件としたSELECTの確認")
    void testFindByUserId() {
        // Arrange
        createTestData();
        
        // Act
        var key = 2;
        Optional<UserInfo> resultOp = userMapper.findByUserId(key);
        
        // Assert
        assertThat(resultOp).isNotEmpty();
        
        var result = resultOp.get();
        assertThat(result.getUserId()).isEqualTo(key);
        assertThat(result.getEmail()).isEqualTo("test2@test.com");
        assertThat(result.getPassword()).isEqualTo("123");
        assertThat(result.getName()).isEqualTo("テスト２");
        assertThat(result.getCompanyId()).isEqualTo(1001);
    }
    
    @Test
    @DisplayName("【正常系】メールアドレスをキー条件としたSELECTの確認")
    void testFindByEmail() {
        // Arrange
        createTestData();
        
        // Act
        var key = "test3@test.com";
        Optional<UserInfo> resultOp = userMapper.findByEmail(key);
        
        // Assert
        assertThat(resultOp).isNotEmpty();
        
        var result = resultOp.get();
        assertThat(result.getUserId()).isEqualTo(3);
        assertThat(result.getEmail()).isEqualTo(key);
        assertThat(result.getPassword()).isEqualTo("123");
        assertThat(result.getName()).isEqualTo("テスト３");
        assertThat(result.getCompanyId()).isEqualTo(1002);
    }
    
    @Test
    @DisplayName("【正常系】INSERTの確認")
    void testInsert() {
        // Arrange
        var user = new User();
        user.setEmail("test@monos.co.jp");
        user.setPassword("12345");
        user.setName("テスト　太郎");
        user.setRoleCode(Const.ROLE_ADMIN);
        user.setCompanyId(123);
        
        // 参照制約回避
        jdbcTemplate.update("INSERT INTO companies(company_id) VALUES (?)",user.getCompanyId());
        
        // Act
        userMapper.insert(user);
        
        // Assert
        Map<String, Object> result = jdbcTemplate.queryForMap(
            "SELECT email, password, name, role_code, company_id FROM users WHERE user_id = ? ",
            user.getUserId()
        );
        
        assertThat(result.get("email")).isEqualTo(user.getEmail());
        assertThat(result.get("password")).isEqualTo(user.getPassword());
        assertThat(result.get("name")).isEqualTo(user.getName());
        assertThat(result.get("role_code")).isEqualTo(user.getRoleCode());
        assertThat(result.get("company_id")).isEqualTo(user.getCompanyId());
    }
    
    @Test
    @DisplayName("【正常系】UPDATEの確認")
    void testUpdate() {
        // Arrange
        createTestData();
        
        var user = new User();
        user.setUserId(2);
        user.setName("更新テスト２");
        user.setRoleCode(Const.ROLE_ADMIN);
        
        // Act
        userMapper.update(user);
        
        // Assert
        Map<String, Object> result = jdbcTemplate.queryForMap(
            "SELECT name, role_code FROM users WHERE user_id = ? ",
            user.getUserId()
        );
        
        assertThat(result.get("name")).isEqualTo(user.getName());
        assertThat(result.get("role_code")).isEqualTo(user.getRoleCode());
    }
    
    @Test
    @DisplayName("【正常系】DELETEの確認")
    void testDELETE() {
        // Arrange
        createTestData();
        
        // Act
        var key = 2;
        userMapper.delete(key);
        
        // Assert
        Map<String, Object> result = jdbcTemplate.queryForMap(
            "SELECT count(1) AS count FROM users WHERE user_id = ? ",
            key
        );
        
        assertThat(result.get("count")).isEqualTo(0L);
    }
    
    /**
     * テストデータを作成
     */
    void createTestData() {
        // companies
        jdbcTemplate.update("INSERT INTO companies(company_id, name) VALUES (?, ?)", 1001, "テスト株式会社");
        jdbcTemplate.update("INSERT INTO companies(company_id, name) VALUES (?, ?)", 1002, "テスト商事");
        
        // users
        jdbcTemplate.update("INSERT INTO users(user_id, email, password, name, role_code, company_id) VALUES(1, 'test1@test.com', 123, 'テスト１', 'ADMIN', 1001)");
        jdbcTemplate.update("INSERT INTO users(user_id, email, password, name, role_code, company_id) VALUES(2, 'test2@test.com', 123, 'テスト２', 'GENERAL', 1001)");
        jdbcTemplate.update("INSERT INTO users(user_id, email, password, name, role_code, company_id) VALUES(3, 'test3@test.com', 123, 'テスト３', 'ADMIN', 1002)");
    }
}
