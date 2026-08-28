package com.example.monos.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
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

import com.example.monos.controller.ProductController;
import com.example.monos.domain.Product;
import com.example.monos.service.ProductService;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest extends AbstractControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	ProductService productService; 

	@Test
	void showProducts_商品一覧が表示_検索結果がモデルに設定される() throws Exception{
		// Arrange
		var p1 = new Product();
		p1.setProductCode("TEST-001");
		var p2 = new Product();
		p2.setProductCode("TEST-002");
		
		List<Product> mockProducts = List.of(p1,p2);
		
		when(productService.search(any())).thenReturn(mockProducts);
		
		// Act
		mockMvc.perform(get("/products")
				.param("productCode", "TEST")
				.param("name", "テスト商品")
				.param("active", "true")
				.with(testUser()))
		.andExpect(status().isOk())
		.andExpect(view().name("products/products"))
		.andExpect(model().attributeExists("products"))
		.andExpect(model().attribute("products", mockProducts));
		
		verify(productService, times(1)).search(any());
	}
}
