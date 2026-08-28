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

import com.example.monos.domain.Product;
import com.example.monos.dto.ProductSearchCondition;
import com.example.monos.exception.BusinessException;
import com.example.monos.exception.FatalBusinessException;
import com.example.monos.mapper.ProductMapper;
import com.example.monos.service.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	@InjectMocks
	private ProductServiceImpl productService;
	
	@Mock
	private ProductMapper productMapper;
	
	@Mock
	private MessageSource messageSource;


	@Test
	void search_Mapperが呼ばれて商品情報が返る() {
		// Arrange
		var condition = new ProductSearchCondition();
		var products = List.of(new Product());
		
		when(productMapper.selectList(condition)).thenReturn(products);
		
		// Act
		List<Product> result = productService.search(condition);
		
		// Assert
		assertEquals(result, products);
		verify(productMapper).selectList(condition);
	}
	
	@Nested
	class FindById{
		int productId = 123;
		int companyId = 456;
		
		@Test
		void 商品情報が存在する場合_Optionalに値が入る() {
			// Arrange
			var product = new Product();
			
			when(productMapper.selectById(productId, companyId)).thenReturn(product);
		
			// Act
			Optional<Product> result = productService.findById(productId, companyId);
			
			// Assert
			assertEquals(result.get(), product);
		}
		
		@Test
		void 商品情報が存在しない場合_Optionalが空になる() {
			//Arrange
			when(productMapper.selectById(productId, companyId)).thenReturn(null);
			
			// Act
			Optional<Product> result = productService.findById(productId, companyId);
			
			// Assert
			assertTrue(result.isEmpty());
		}
	}
	
	@Nested
	class Save{
		Product product;
		
		@BeforeEach
		void setup(){
			product = new Product();
			product.setProductId(null);
			product.setCompanyId(1);
			product.setProductCode("TEST-001");
		}
		
		@Test
		void 商品登録_正常(){
			// Arrange
			when(productMapper.existsByProductCode(product.getProductCode(), product.getCompanyId()))
				.thenReturn(false);
			
			when(messageSource.getMessage("registComplete", null, Locale.JAPAN))
				.thenReturn("登録成功");
			
			// Act
			String result = productService.save(product);
			
			// assert
			verify(productMapper).insert(product);
			assertEquals(result, "登録成功");
		}
		
		@Test
		void 商品登録_商品コード重複() {
			// Arrange
			when(productMapper.existsByProductCode(product.getProductCode(), product.getCompanyId()))
				.thenReturn(true);
			
			when(messageSource.getMessage("existsProductCode", null, Locale.JAPAN))
				.thenReturn("商品コード重複");
			
			// Act
			// Assert
			BusinessException ex = assertThrows(
					BusinessException.class, () -> productService.save(product));
		
			verify(productMapper, never()).insert(product);
			assertEquals(ex.getErrors().get("productCode"), "商品コード重複");
		}
		
		@Test
		void 商品更新_正常() {
			// Arrange
			product.setProductId(123);
			
			when(productMapper.update(product)).thenReturn(1);
			
			when(messageSource.getMessage("updateComplete", null, Locale.JAPAN))
				.thenReturn("更新成功");
			
			// Act
			String result = productService.save(product);
			
			// Assert
			verify(productMapper).update(product);
			assertEquals(result, "更新成功");
		}
		
		@Test
		void 商品更新_更新0件() {
			// Arrange
			product.setProductId(123);
			
			when(productMapper.update(product)).thenReturn(0);
			
			when(messageSource.getMessage("updateFaild", null, Locale.JAPAN))
				.thenReturn("更新失敗");
			
			// Act
			// Assert
			FatalBusinessException ex = assertThrows(
					FatalBusinessException.class, () -> productService.save(product));
			
			verify(productMapper).update(product);
			assertEquals(ex.getMessage(), "更新失敗");
		}
	}
}
