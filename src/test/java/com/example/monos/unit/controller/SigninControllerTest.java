package com.example.monos.unit.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.monos.controller.SigninController;

@WebMvcTest(SigninController.class)
@AutoConfigureMockMvc
public class SigninControllerTest extends AbstractControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("【正常系】トップページが表示されることを確認")
    void testShowIndex_success() throws Exception {
        mockMvc.perform(get("/").with(testUser()))
                .andExpect(status().isOk())              
                .andExpect(view().name("index"));        
    }

    @Test
    @DisplayName("【正常系】サインイン画面が表示されることを確認")
    void testShowSignin_success() throws Exception {
        mockMvc.perform(get("/signin").with(testUser()))
                .andExpect(status().isOk())             
                .andExpect(view().name("signin"));       
    }
}
