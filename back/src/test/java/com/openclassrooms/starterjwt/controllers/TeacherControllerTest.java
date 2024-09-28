package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        teacher = new Teacher();
        teacher.setId(1L);
        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
    }

    @Test
    public void findById_shouldReturnTeacher_whenTeacherExists() {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Act
        ResponseEntity<?> response = teacherController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
    }

    @Test
    public void findById_shouldReturnNotFound_whenTeacherDoesNotExist() {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = teacherController.findById("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findById_shouldReturnBadRequest_whenIdIsInvalid() {
        // Act
        ResponseEntity<?> response = teacherController.findById("invalid");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void findAll_shouldReturnListOfTeachers() {
        // Arrange
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(List.of(teacherDto));

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, ((List<?>) response.getBody()).size());
        verify(teacherService).findAll();
    }
}
