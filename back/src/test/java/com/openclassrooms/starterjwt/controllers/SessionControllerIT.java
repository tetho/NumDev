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
class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findById_shouldReturnSession_whenSessionExists() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/api/session/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void findById_shouldReturnNotFound_whenSessionDoesNotExist() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/api/session/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAll_shouldReturnListOfSessions() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void create_shouldCreateSession() throws Exception {
        // Arrange
    	String newSessionJson = "{\"name\": \"New Session\", \"description\": \"This is a test session\"}";

        // Act and Assert
        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newSessionJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New Session"));
    }

    @Test
    public void update_shouldUpdateSession() throws Exception {
        // Arrange
    	String updatedSessionJson = "{\"name\": \"Updated Session\", \"description\": \"Updated description\"}";

        // Act and Assert
        mockMvc.perform(put("/api/session/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSessionJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Session"));
    }

    @Test
    public void delete_shouldDeleteSession() throws Exception {
        // Act and Assert
        mockMvc.perform(delete("/api/session/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void participate_shouldAddUserToSession() throws Exception {
        // Act and Assert
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void noLongerParticipate_shouldRemoveUserFromSession() throws Exception {
        // Act and Assert
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
