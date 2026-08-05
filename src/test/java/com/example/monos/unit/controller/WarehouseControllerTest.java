package com.example.monos.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.monos.controller.WarehouseController;
import com.example.monos.domain.Warehouse;
import com.example.monos.service.WarehouseService;

@WebMvcTest(WarehouseController.class)
@AutoConfigureMockMvc
public class WarehouseControllerTest extends AbstractControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	WarehouseService warehouseService; 

	@Test
	void showWarehouses_倉庫一覧が表示_検索結果がモデルに設定される() throws Exception{
		// Arrange
		var w1 = new Warehouse();
		w1.setWarehouseCode("TEST-001");
		var w2 = new Warehouse();
		w2.setWarehouseCode("TEST-002");
		
		List<Warehouse> mockWarehouses = List.of(w1,w2);
		
		when(warehouseService.search(any())).thenReturn(mockWarehouses);
		
		// Act
		mockMvc.perform(get("/warehouses")
				.param("warehouseCode", "TEST")
				.param("name", "テスト倉庫")
				.param("active", "true")
				.with(testUser()))
		.andExpect(status().isOk())
		.andExpect(view().name("warehouses/warehouses"))
		.andExpect(model().attributeExists("warehouses"))
		.andExpect(model().attribute("warehouses", mockWarehouses));
		
		verify(warehouseService).search(any());
	}
}
