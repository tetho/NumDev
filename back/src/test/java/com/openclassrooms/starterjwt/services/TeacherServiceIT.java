package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
@Transactional
public class TeacherServiceIT {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private TeacherRepository teacherRepository;

	private Teacher teacher;

	@BeforeEach
	public void setUp() {
		teacher = new Teacher();
		teacher.setId(1L);
		teacher.setFirstName("Margot");
		teacher.setLastName("DELAHAYE");
		teacherRepository.save(teacher);
	}

	@Test
	public void findById_shouldReturnTeacher_whenTeacherExists() {
		// Arrange
		Long teacherId = 1L;

		// Act
		Teacher result = teacherService.findById(teacherId);

		// Assert
		assertNotNull(result);
		assertEquals(teacher, result);
	}

	@Test
	public void findById_shouldReturnNull_whenTeacherDoesNotExist() {
		// Arrange
		Long teacherId = 999L;

		// Act
		Teacher result = teacherService.findById(teacherId);

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
		teacher2 = teacherRepository.save(teacher2);
		teachers.add(teacher2);
		

		// Act
		List<Teacher> result = teacherService.findAll();

		// Assert
		assertEquals(2, result.size());
		verify(teacherRepository).findAll();
	}
	
	@AfterEach
	public void reset() {
		teacherRepository.deleteAll();
	}
}
