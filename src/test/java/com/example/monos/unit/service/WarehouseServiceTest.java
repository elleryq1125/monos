package com.example.monos.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.example.monos.domain.Warehouse;
import com.example.monos.dto.WarehouseSearchCondition;
import com.example.monos.exception.BusinessException;
import com.example.monos.exception.FatalBusinessException;
import com.example.monos.mapper.WarehouseMapper;
import com.example.monos.service.WarehouseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {
	@InjectMocks
	private WarehouseServiceImpl warehouseService;
	
	@Mock
	private WarehouseMapper warehouseMapper;
	
	@Mock
	private MessageSource messageSource;


	@Test
	void search_Mapperが呼ばれて倉庫情報が返る() {
		// Arrange
		var condition = new WarehouseSearchCondition();
		var warehouses = List.of(new Warehouse());
		
		when(warehouseMapper.selectList(condition)).thenReturn(warehouses);
		
		// Act
		List<Warehouse> result = warehouseService.search(condition);
		
		// Assert
		assertEquals(result, warehouses);
		verify(warehouseMapper).selectList(condition);
	}
	
	@Nested
	class FindById{
		int warehouseId = 123;
		int companyId = 456;
		
		@Test
		void 倉庫情報が存在する場合_Optionalに値が入る() {
			// Arrange
			var warehouse = new Warehouse();
			
			when(warehouseMapper.selectById(warehouseId, companyId)).thenReturn(warehouse);
		
			// Act
			Optional<Warehouse> result = warehouseService.findById(warehouseId, companyId);
			
			// Assert
			assertEquals(result.get(), warehouse);
		}
		
		@Test
		void 倉庫情報が存在しない場合_Optionalが空になる() {
			//Arrange
			when(warehouseMapper.selectById(warehouseId, companyId)).thenReturn(null);
			
			// Act
			Optional<Warehouse> result = warehouseService.findById(warehouseId, companyId);
			
			// Assert
			assertTrue(result.isEmpty());
		}
	}
	
	@Nested
	class Save{
		Warehouse warehouse;
		
		@BeforeEach
		void setup(){
			warehouse = new Warehouse();
			warehouse.setWarehouseId(null);
			warehouse.setCompanyId(1);
			warehouse.setWarehouseCode("TEST-001");
		}
		
		@Test
		void 倉庫登録_正常(){
			// Arrange
			when(warehouseMapper.existsByWarehouseCode(warehouse.getWarehouseCode(),warehouse.getCompanyId()))
				.thenReturn(false);
			
			when(messageSource.getMessage("registComplete", null, Locale.JAPAN))
				.thenReturn("登録成功");
			
			// Act
			String result = warehouseService.save(warehouse);
			
			// assert
			verify(warehouseMapper).insert(warehouse);
			assertEquals(result, "登録成功");
		}
		
		@Test
		void 倉庫登録_倉庫コード重複() {
			// Arrange
			when(warehouseMapper.existsByWarehouseCode(warehouse.getWarehouseCode(), warehouse.getCompanyId()))
				.thenReturn(true);
			
			when(messageSource.getMessage("existsWarehouseCode", null, Locale.JAPAN))
				.thenReturn("倉庫コード重複");
			
			// Act
			// Assert
			BusinessException ex = assertThrows(
					BusinessException.class, () -> warehouseService.save(warehouse));
		
			verify(warehouseMapper, never()).insert(warehouse);
			assertEquals(ex.getErrors().get("warehouseCode"), "倉庫コード重複");
		}
		
		@Test
		void 倉庫更新_正常() {
			// Arrange
			warehouse.setWarehouseId(123);
			
			when(warehouseMapper.update(warehouse)).thenReturn(1);
			
			when(messageSource.getMessage("updateComplete", null, Locale.JAPAN))
				.thenReturn("更新成功");
			
			// Act
			String result = warehouseService.save(warehouse);
			
			// Assert
			verify(warehouseMapper).update(warehouse);
			assertEquals(result, "更新成功");
		}
		
		@Test
		void 倉庫更新_更新0件() {
			// Arrange
			warehouse.setWarehouseId(123);
			
			when(warehouseMapper.update(warehouse)).thenReturn(0);
			
			when(messageSource.getMessage("updateFaild", null, Locale.JAPAN))
				.thenReturn("更新失敗");
			
			// Act
			// Assert
			FatalBusinessException ex = assertThrows(
					FatalBusinessException.class, () -> warehouseService.save(warehouse));
			
			verify(warehouseMapper).update(warehouse);
			assertEquals(ex.getMessage(), "更新失敗");
		}
	}
}
