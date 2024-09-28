package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Margot");
        teacher.setLastName("DELAHAYE");
    }

    @Test
    public void findById_shouldReturnTeacher_whenTeacherExists() throws Exception {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(teacher);

        // Act and Assert
        mockMvc.perform(get("/api/teacher/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(teacher.getLastName()));
    }

    @Test
    public void findById_shouldReturnNotFound_whenTeacherDoesNotExist() throws Exception {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(null);

        // Act and Assert
        mockMvc.perform(get("/api/teacher/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        // Act and Assert
        mockMvc.perform(get("/api/teacher/{id}", "invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAll_shouldReturnListOfTeachers() throws Exception {
        // Arrange
        when(teacherService.findAll()).thenReturn(Collections.singletonList(teacher));

        // Act and Assert
        mockMvc.perform(get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(teacher.getLastName()));
    }
}
