package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authenticateUser_shouldReturnJwt_whenCredentialsAreCorrect() throws Exception {
        String loginRequestJson = "{\"email\": \"user@example.com\", \"password\": \"password\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void authenticateUser_shouldReturnUnauthorized_whenCredentialsAreIncorrect() throws Exception {
        String loginRequestJson = "{\"email\": \"wrong@example.com\", \"password\": \"wrongpassword\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void registerUser_shouldReturnBadRequest_whenEmailIsAlreadyTaken() throws Exception {
        String signupRequestJson = "{\"email\": \"user@example.com\", \"password\": \"password\", \"firstName\": \"John\", \"lastName\": \"Doe\"}";

        // Simulate an existing user with the same email
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerUser_shouldRegisterNewUser_whenDataIsValid() throws Exception {
        String signupRequestJson = "{\"email\": \"newuser@example.com\", \"password\": \"password\", \"firstName\": \"Jane\", \"lastName\": \"Smith\"}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }
}
