package com.example.monos.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.monos.domain.Company;
import com.example.monos.mapper.CompanyMapper;


@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompanyMapperTest {
    private final CompanyMapper companyMapper;
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    CompanyMapperTest(CompanyMapper companyMapper, JdbcTemplate jdbcTemplate){
        this.companyMapper = companyMapper;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Test
    @DisplayName("【正常系】INSERTの確認")
    void testInsert() {
        // Arrange
        var company = new Company();
        company.setName("テスト株式会社");
        
        // Act
        int count = companyMapper.insert(company);
        
        // Assert
        Map<String, Object> result = jdbcTemplate.queryForMap(
            "SELECT * FROM companies WHERE company_id = ? ",
            1
        );

        assertThat(result.get("name")).isEqualTo("テスト株式会社");
    }
}
