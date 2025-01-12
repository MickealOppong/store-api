package com.store.test_api;

import com.store.api.controller.AuthController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final String END_POINT="/auth";

    @Autowired
    private MockMvc  mockMvc;

    @MockBean
    private AuthController authController;

    @Test
    void check_welcome_message() throws Exception {
        Mockito.when(authController.welcome()).thenReturn("Welcome");
      mockMvc.perform(get(END_POINT)).andExpect(status().isOk());
    }
}