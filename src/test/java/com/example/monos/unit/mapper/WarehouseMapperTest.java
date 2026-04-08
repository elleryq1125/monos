package com.example.monos.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.monos.domain.Warehouse;
import com.example.monos.dto.WarehouseSearchCondition;
import com.example.monos.mapper.WarehouseMapper;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WarehouseMapperTest {
	private final WarehouseMapper warehouseMapper;
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    WarehouseMapperTest(WarehouseMapper warehouseMapper, JdbcTemplate jdbcTemplate){
        this.warehouseMapper = warehouseMapper;
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
    		createWarehouses();
    	}
    	
    	@Test
    	void マッピング確認() {
    		// Arrange
    		var condition = new WarehouseSearchCondition();
    		condition.setCompanyId(1);
    		condition.setWarehouseCode("TEST-001");
    		condition.setActive(true);
    		
    		// Act
    		List<Warehouse> result = warehouseMapper.selectList(condition);
    		
    		// Assert
    		assertThat(result)
    			.first()
    			.extracting(Warehouse::getWarehouseId, Warehouse::getCompanyId, Warehouse::getWarehouseCode, Warehouse::getName,Warehouse::isActive)
    			.containsExactly(1, 1, "TEST-001", "テスト倉庫1",  true);
    	}
    	
    	@Test
    	void 必須条件のみ() {
    		// Arrange
    		var condition = new WarehouseSearchCondition();
    		condition.setCompanyId(1);
    		condition.setActive(true);
    		
    		// Act
    		List<Warehouse> result = warehouseMapper.selectList(condition);
    		
    		// Assert
    		assertThat(result)
    			.extracting(Warehouse::getWarehouseCode)
    			.containsExactly("TEST-001", "TEST-999");
    	}
    	
    	@Test
    	void あいまい検索_倉庫コード() {
    		// Arrange
    		var condition = new WarehouseSearchCondition();
    		condition.setCompanyId(1);
    		condition.setWarehouseCode("999");
    		condition.setActive(true);
    		
    		// Act
    		List<Warehouse> result = warehouseMapper.selectList(condition);
    		
    		// Asstert
    		assertThat(result)
    			.extracting(Warehouse::getWarehouseCode)
    			.contains("TEST-999");
    	}
    	
    	@Test
    	void あいまい検索_倉庫名() {
    		// Arrange
    		var condition = new WarehouseSearchCondition();
    		condition.setCompanyId(1);
    		condition.setName("999");
    		condition.setActive(true);
    		
    		// Act
    		List<Warehouse> result = warehouseMapper.selectList(condition);
    		
    		// Asstert
    		assertThat(result)
    			.extracting(Warehouse::getWarehouseCode)
    			.contains("TEST-999");
    	}
    	
    	@Test
    	void 検索結果なし() {
    		// Arrange
    		var condition = new WarehouseSearchCondition();
    		condition.setCompanyId(3);
    		condition.setActive(true);
    		
    		// Act
    		List<Warehouse> result = warehouseMapper.selectList(condition);
    	
    		// Assert
    		assertThat(result).isEmpty();
    	}
    }
    
    @Nested
    class SelectById{
    	@BeforeEach()
    	void createTestData() {
    		createWarehouses();
    	}
    	
    	@Test
        void 検索結果あり() {	
        	// Act
        	Warehouse result = warehouseMapper.selectById(2, 1);
        	
        	// Assert
        	assertThat(result)
        		.extracting(Warehouse::getWarehouseId, Warehouse::getCompanyId, Warehouse::getWarehouseCode, Warehouse::getName, Warehouse::isActive)
        		.containsExactly(2, 1, "TEST-002", "テスト倉庫2", false);
        }
    	
    	@Test
    	void 検索結果なし() {
    		// Act
    		Warehouse result = warehouseMapper.selectById(2, 2);
        	
        	// Assert
    		assertThat(result).isNull();
    	}
    }
    
    @Nested
    class ExistisByWarehouseCode{
    	@BeforeEach()
    	void createTestData() {
    		createWarehouses();
    	}
    	
    	@Test
    	void 同じ倉庫コードが存在する() {
    		// Act
        	boolean result = warehouseMapper.existsByWarehouseCode("TEST-001", 1);
        	
        	// Assert
        	assertThat(result).isTrue();
    	}
    	
    	@Test
    	void 同じ倉庫コードが存在しない() {
    		// Act
        	boolean result = warehouseMapper.existsByWarehouseCode("TEST-888", 1);
        	
        	// Assert
        	assertThat(result).isFalse();
    	}
    }
    
    @Test
    void insert_倉庫情報が登録できていることを確認() {
    	// Arrange
    	var warehouse = new Warehouse();
    	warehouse.setCompanyId(2);
    	warehouse.setWarehouseCode("TEST-INS");
    	warehouse.setName("新倉庫");
    	warehouse.setActive(false);
    	
    	// Act
    	warehouseMapper.insert(warehouse);
    	Warehouse insWarehouse = warehouseMapper.selectById(warehouse.getWarehouseId(), 2);
    	
    	// Assert
    	assertThat(insWarehouse)
		.extracting(Warehouse::getCompanyId, Warehouse::getWarehouseCode, Warehouse::getName, Warehouse::isActive)
		.containsExactly(2, "TEST-INS", "新倉庫", false);
    }
    
    @Test
    void update_倉庫情報が更新できていることを確認() {
    	// Arrange
    	createWarehouses();
    	
    	var warehouse = new Warehouse();
    	warehouse.setWarehouseId(4);
    	warehouse.setCompanyId(2);
    	warehouse.setName("更新倉庫");
    	warehouse.setActive(false);
    	
    	// Act
    	int result = warehouseMapper.update(warehouse);
    	Warehouse updProduct = warehouseMapper.selectById(4, 2);
    	
    	// Assert
    	assertThat(result).isEqualTo(1);
    	
    	assertThat(updProduct)
    		.extracting(Warehouse::getName, Warehouse::isActive)
    		.containsExactly("更新倉庫", false);
    }
    
	/**
	 * warehousesのテストデータ作成
	 */
	void createWarehouses() {
		// products
    	jdbcTemplate.update("INSERT INTO warehouses(warehouse_id, company_id, warehouse_code, name, active) VALUES (1, 1, 'TEST-001', 'テスト倉庫1', true)");
    	jdbcTemplate.update("INSERT INTO warehouses(warehouse_id, company_id, warehouse_code, name, active) VALUES (2, 1, 'TEST-002', 'テスト倉庫2', false)");
    	jdbcTemplate.update("INSERT INTO warehouses(warehouse_id, company_id, warehouse_code, name, active) VALUES (3, 1, 'TEST-999', 'テスト倉庫999', true)");
    	jdbcTemplate.update("INSERT INTO warehouses(warehouse_id, company_id, warehouse_code, name, active) VALUES (4, 2, 'TEST-002', 'テスト倉庫2', true)");
	}
}
