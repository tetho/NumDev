package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	@InjectMocks
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
	public void findById_shouldReturnTeacher_whenTeacherExists() {
		// Arrange
		when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

		// Act
		Teacher result = teacherService.findById(1L);

		// Assert
		assertNotNull(result);
		assertEquals(teacher, result);
	}
	
	@Test
	public void findById_shouldReturnNull_whenTeacherDoesntExist() {
		// Arrange
		when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

		// Act
		Teacher result = teacherService.findById(1L);

		// Assert
		assertNull(result);
	}
	
	@Test
	public void findAll_shouldReturnListOfAllTeachers() {
		// Arrange
		List<Teacher> teachers = new ArrayList<Teacher>();
		teachers.add(teacher);
		Teacher teacher2 = new Teacher();
		teacher2.setId(2L);
		teacher2.setFirstName("Hélène");
		teacher2.setLastName("THIERCELIN");
		teachers.add(teacher2);
		when(teacherRepository.findAll()).thenReturn(teachers);

		// Act
		List<Teacher> result = teacherService.findAll();

		// Assert
		assertEquals(2, result.size());
		verify(teacherRepository).findAll();
	}
}
