package com.example.monos.unit.controller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.monos.controller.restcontroller.ProductApiController;
import com.example.monos.domain.Product;
import com.example.monos.service.ProductService;
import com.example.monos.unit.controller.AbstractControllerTest;

@WebMvcTest(ProductApiController.class)
@AutoConfigureMockMvc
public class ProductApiControllerTest extends AbstractControllerTest {

	 	@Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private ProductService productService;

	    @MockBean
	    private MessageSource messageSource;
	    
	    @Nested
	    class Get{
	    	
	    	@Test
		    void 商品情報が存在する場合_商品情報がJSONで返却される() throws Exception {
	    		// Arrange
		        Product product = new Product();
		        product.setProductId(1);
		        product.setProductCode("TEST-001");
		        product.setName("テスト商品");
		        product.setUnit("個");
		        product.setActive(false);

		        when(productService.findById(anyInt(), anyInt()))
		                .thenReturn(Optional.of(product));

		        // Act
		        mockMvc.perform(get("/api/products/1")
		                .with(testUser()))
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.success").value(true))
		            .andExpect(jsonPath("$.data.productId").value(1))
		            .andExpect(jsonPath("$.data.productCode").value("TEST-001"))
		            .andExpect(jsonPath("$.data.name").value("テスト商品"))
		            .andExpect(jsonPath("$.data.unit").value("個"))
		            .andExpect(jsonPath("$.data.active").value(false));

		        verify(productService).findById(1, testCompanyId);
		    }
	    
		    @Test
		    void 商品情報が存在しない場合_エラー内容がJSONで返却される() throws Exception {
		    	// Arrange
		        when(productService.findById(anyInt(), anyInt()))
		                .thenReturn(Optional.empty());
		        
		        when(messageSource.getMessage(anyString(), any(), any()))
		        	.thenReturn("DataNotExists");
	
		        // Act
		        mockMvc.perform(get("/api/products/1")
		                .with(testUser()))
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.success").value(false))
		            .andExpect(jsonPath("$.message").value("DataNotExists"));
	
		        verify(productService).findById(1, testCompanyId);
		    }
	    }
	    
	    @Nested
	    class Save{
	    	
	    	@Test
	    	void 商品情報の保存に成功した場合_成功がJSONで返却される() throws Exception {
	    		// Arrange
	    		when(productService.save(any()))
	    			.thenReturn("Success");
	    		
	    		String json = """
	    				{
	    					"productId" : 1,
	    					"productCode" : "TEST-001",
	    					"name" : "テスト商品",
	    					"unit" : "個",
	    					"active" : true
	    				}
	    				""";
	    		
	    		// Act
	            mockMvc.perform(post("/api/products/save")
	                    .contentType("application/json")
	                    .content(json)
	                    .with(testUser())
	            		.with(csrf()))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.success").value(true))
	                .andExpect(jsonPath("$.message").value("Success"));

	            // from→DTOへの変換でServiceに正しい値が渡っているか検証
	            ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
	            verify(productService).save(captor.capture());

	            Product p = captor.getValue();

	            assertEquals(1, p.getProductId());
	            assertEquals("TEST-001", p.getProductCode());
	            assertEquals("テスト商品", p.getName());
	            assertEquals("個", p.getUnit());
	            assertEquals(true, p.isActive());
	            assertEquals(testCompanyId, p.getCompanyId());
	    	}
	    	
	    	@Test
	    	void バリデーションエラーの結果がJSONで返却される() throws Exception {
	    		// Arrange
	    		String json = """
	    				{
	    					"productId" : 1,
	    					"productCode" : "",
	    					"name" : "テスト商品",
	    					"unit" : "個",
	    					"active" : true
	    				}
	    				""";
	    		
	    		// Act
	            mockMvc.perform(post("/api/products/save")
	                    .contentType("application/json")
	                    .content(json)
	                    .with(testUser())
	            		.with(csrf()))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.success").value(false));
	    	}
	    }
}
