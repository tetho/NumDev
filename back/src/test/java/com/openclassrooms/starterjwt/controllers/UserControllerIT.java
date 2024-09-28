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
class UserControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void findById_shouldReturnOk_whenUserExists() throws Exception {
		// Act & Assert
		mockMvc.perform(get("/api/user/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("yoga@studio.com"));
	}

	@Test
	public void findById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
		// Act & Assert
		mockMvc.perform(get("/api/user/999")).andExpect(status().isNotFound());
	}

	@Test
	public void findById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
		// Act & Assert
		mockMvc.perform(get("/api/user/invalid_id")).andExpect(status().isBadRequest());
	}

	@Test
	public void delete_shouldDeleteUser_whenAuthorized() throws Exception {
		mockMvc.perform(delete("/api/user/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void delete_shouldReturnUnauthorized_whenUserIsNotOwner() throws Exception {
		mockMvc.perform(delete("/api/user/{id}", 1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
}
