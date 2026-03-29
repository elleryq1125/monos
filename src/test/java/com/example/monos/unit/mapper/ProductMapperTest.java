package com.example.monos.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.mapper.ProductMapper;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductMapperTest {
	private final ProductMapper productMapper;
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    ProductMapperTest(ProductMapper productMapper, JdbcTemplate jdbcTemplate){
        this.productMapper = productMapper;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @BeforeEach
    void setup() {
    	// 共通テストデータ投入
    	// companies（参照制約回避）
    	jdbcTemplate.update("INSERT INTO companies(company_id, name) VALUES (?, ?)", 1, "テスト株式会社");
        jdbcTemplate.update("INSERT INTO companies(company_id, name) VALUES (?, ?)", 2, "テスト商事");	
    }
    
    @Nested
    class SelectList{
    	@BeforeEach()
    	void createTestData() {
    		createProducts();
    	}
    	
    	@Test
    	void マッピング確認() {
    		// Arrange
    		var condition = new ProductSearchCondition();
    		condition.setCompanyId(1);
    		condition.setProductCode("TEST-001");
    		condition.setActive(true);
    		
    		// Act
    		List<Product> result = productMapper.selectList(condition);
    		
    		// Assert
    		assertThat(result)
    			.first()
    			.extracting(Product::getProductId, Product::getCompanyId, Product::getProductCode, Product::getName, Product::getUnit, Product::isActive)
    			.containsExactly(1, 1, "TEST-001", "テスト商品1", "個", true);
    	}
    	
    	@Test
    	void 必須条件のみ() {
    		// Arrange
    		var condition = new ProductSearchCondition();
    		condition.setCompanyId(1);
    		condition.setActive(true);
    		
    		// Act
    		List<Product> result = productMapper.selectList(condition);
    		
    		// Assert
    		assertThat(result)
    			.extracting(Product::getProductCode)
    			.containsExactly("TEST-001", "TEST-999");
    	}
    	
    	@Test
    	void あいまい検索_商品コード() {
    		// Arrange
    		var condition = new ProductSearchCondition();
    		condition.setCompanyId(1);
    		condition.setProductCode("999");
    		condition.setActive(true);
    		
    		// Act
    		List<Product> result = productMapper.selectList(condition);
    		
    		// Asstert
    		assertThat(result)
    			.extracting(Product::getProductCode)
    			.contains("TEST-999");
    	}
    	
    	@Test
    	void あいまい検索_商品名() {
    		// Arrange
    		var condition = new ProductSearchCondition();
    		condition.setCompanyId(1);
    		condition.setName("999");
    		condition.setActive(true);
    		
    		// Act
    		List<Product> result = productMapper.selectList(condition);
    		
    		// Asstert
    		assertThat(result)
    			.extracting(Product::getProductCode)
    			.contains("TEST-999");
    	}
    	
    	@Test
    	void 検索結果なし() {
    		// Arrange
    		var condition = new ProductSearchCondition();
    		condition.setCompanyId(3);
    		condition.setActive(true);
    		
    		// Act
    		List<Product> result = productMapper.selectList(condition);
    	
    		// Assert
    		assertThat(result).isEmpty();
    	}
    }
    
    @Nested
    class SelectById{
    	@BeforeEach()
    	void createTestData() {
    		createProducts();
    	}
    	
    	@Test
        void 検索結果あり() {	
        	// Act
        	Product result = productMapper.selectById(2, 1);
        	
        	// Assert
        	assertThat(result)
        		.extracting(Product::getProductId, Product::getCompanyId, Product::getProductCode, Product::getName, Product::getUnit, Product::isActive)
        		.containsExactly(2, 1, "TEST-002", "テスト商品2", "束", false);
        }
    	
    	@Test
    	void 検索結果なし() {
    		// Act
        	Product result = productMapper.selectById(2, 2);
        	
        	// Assert
    		assertThat(result).isNull();
    	}
    }
    
    @Nested
    class ExistisByProductCode{
    	@BeforeEach()
    	void createTestData() {
    		createProducts();
    	}
    	
    	@Test
    	void 同じ商品コードが存在する() {
    		// Act
        	boolean result = productMapper.existsByProductCode("TEST-001", 1);
        	
        	// Assert
        	assertThat(result).isTrue();
    	}
    	
    	@Test
    	void 同じ商品コードが存在しない() {
    		// Act
        	boolean result = productMapper.existsByProductCode("TEST-888", 1);
        	
        	// Assert
        	assertThat(result).isFalse();
    	}
    }
    
    @Test
    void insert_商品情報が登録できていることを確認() {
    	// Arrange
    	var product = new Product();
    	product.setCompanyId(2);
    	product.setProductCode("TEST-INS");
    	product.setName("新商品");
    	product.setUnit("台");
    	product.setActive(false);
    	
    	// Act
    	productMapper.insert(product);
    	Product insProduct = productMapper.selectById(product.getProductId(), 2);
    	
    	// Assert
    	assertThat(insProduct)
		.extracting(Product::getCompanyId, Product::getProductCode, Product::getName, Product::getUnit, Product::isActive)
		.containsExactly(2, "TEST-INS", "新商品", "台", false);
    }
    
    @Test
    void update_商品情報が更新できていることを確認() {
    	// Arrange
    	createProducts();
    	
    	var product = new Product();
    	product.setProductId(4);
    	product.setCompanyId(2);
    	product.setName("更新商品");
    	product.setUnit("台");
    	product.setActive(false);
    	
    	// Act
    	int result = productMapper.update(product);
    	Product updProduct = productMapper.selectById(4, 2);
    	
    	// Assert
    	assertThat(result).isEqualTo(1);
    	
    	assertThat(updProduct)
    		.extracting(Product::getName, Product::getUnit, Product::isActive)
    		.containsExactly("更新商品", "台", false);
    }
    
	/**
	 * productsのテストデータ作成
	 */
	void createProducts() {
		// products
    	jdbcTemplate.update("INSERT INTO products(product_id, company_id, product_code, name, unit, active) VALUES (1, 1, 'TEST-001', 'テスト商品1', '個', true)");
    	jdbcTemplate.update("INSERT INTO products(product_id, company_id, product_code, name, unit, active) VALUES (2, 1, 'TEST-002', 'テスト商品2', '束', false)");
    	jdbcTemplate.update("INSERT INTO products(product_id, company_id, product_code, name, unit, active) VALUES (3, 1, 'TEST-999', 'テスト商品999', '品', true)");
    	jdbcTemplate.update("INSERT INTO products(product_id, company_id, product_code, name, unit, active) VALUES (4, 2, 'TEST-002', 'テスト商品2', '束', true)");
	}
}
