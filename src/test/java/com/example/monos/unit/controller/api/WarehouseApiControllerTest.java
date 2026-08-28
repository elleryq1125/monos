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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.monos.controller.restcontroller.WarehouseApiController;
import com.example.monos.domain.Warehouse;
import com.example.monos.service.WarehouseService;
import com.example.monos.unit.controller.AbstractControllerTest;

@WebMvcTest(WarehouseApiController.class)
@AutoConfigureMockMvc
public class WarehouseApiControllerTest extends AbstractControllerTest {

	 	@Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private WarehouseService warehouseService;

	    @MockBean
	    private MessageSource messageSource;
	    
	    @Nested
	    class Get{
	    	
	    	@Test
		    void 倉庫情報が存在する場合_倉庫情報がJSONで返却される() throws Exception {
	    		// Arrange
		        Warehouse warehouse = new Warehouse();
		        warehouse.setWarehouseId(1);
		        warehouse.setWarehouseCode("TEST-001");
		        warehouse.setName("テスト倉庫");
		        warehouse.setActive(false);

		        when(warehouseService.findById(anyInt(), anyInt()))
		                .thenReturn(Optional.of(warehouse));

		        // Act
		        mockMvc.perform(get("/api/warehouses/1")
		                .with(testUser()))
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.success").value(true))
		            .andExpect(jsonPath("$.data.warehouseId").value(1))
		            .andExpect(jsonPath("$.data.warehouseCode").value("TEST-001"))
		            .andExpect(jsonPath("$.data.name").value("テスト倉庫"))
		            .andExpect(jsonPath("$.data.active").value(false));

		        verify(warehouseService).findById(1, testCompanyId);
		    }
	    
		    @Test
		    void 倉庫情報が存在しない場合_エラー内容がJSONで返却される() throws Exception {
		    	// Arrange
		        when(warehouseService.findById(anyInt(), anyInt()))
		                .thenReturn(Optional.empty());
		        
		        when(messageSource.getMessage(anyString(), any(), any()))
		        	.thenReturn("DataNotExists");
	
		        // Act
		        mockMvc.perform(get("/api/warehouses/1")
		                .with(testUser()))
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$.success").value(false))
		            .andExpect(jsonPath("$.message").value("DataNotExists"));
	
		        verify(warehouseService).findById(1, testCompanyId);
		    }
	    }
	    
	    @Nested
	    class Save{
	    	
	    	@Test
	    	void 倉庫情報の保存に成功した場合_成功がJSONで返却される() throws Exception {
	    		// Arrange
	    		when(warehouseService.save(any()))
	    			.thenReturn("Success");
	    		
	    		String json = """
	    				{
	    					"warehouseId" : 1,
	    					"warehouseCode" : "TEST-001",
	    					"name" : "テスト倉庫",
	    					"active" : true
	    				}
	    				""";
	    		
	    		// Act
	            mockMvc.perform(post("/api/warehouses/save")
	                    .contentType("application/json")
	                    .content(json)
	                    .with(testUser())
	            		.with(csrf()))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.success").value(true))
	                .andExpect(jsonPath("$.message").value("Success"));

	            // from→DTOへの変換でServiceに正しい値が渡っているか検証
	            ArgumentCaptor<Warehouse> captor = ArgumentCaptor.forClass(Warehouse.class);
	            verify(warehouseService).save(captor.capture());

	            Warehouse w = captor.getValue();

	            assertEquals(1, w.getWarehouseId());
	            assertEquals("TEST-001", w.getWarehouseCode());
	            assertEquals("テスト倉庫", w.getName());
	            assertEquals(true, w.isActive());
	            assertEquals(testCompanyId, w.getCompanyId());
	    	}
	    	
	    	@Test
	    	void バリデーションエラーの結果がJSONで返却される() throws Exception {
	    		// Arrange
	    		String json = """
	    				{
	    					"warehouseId" : 1,
	    					"warehouseCode" : "",
	    					"name" : "テスト倉庫",
	    					"active" : true
	    				}
	    				""";
	    		
	    		// Act
	            mockMvc.perform(post("/api/warehouses/save")
	                    .contentType("application/json")
	                    .content(json)
	                    .with(testUser())
	            		.with(csrf()))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.success").value(false));
	    	}
	    }
}
