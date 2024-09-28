package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@Transactional
public class SessionServiceIT {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private UserRepository userRepository;

	private static Session session;
	private static User user;

	@BeforeEach
	public void setUp() {
		session = new Session();
		session.setId(1L);
        session.setName("Session 1");
        session.setDescription("Description de la session");
        session.setDate(new Date());
        session.setUsers(new ArrayList<User>());
		sessionRepository.save(session);

		user = new User();
		user.setId(1L);
        user.setEmail("yoga@studio.com");
        user.setLastName("Name");
        user.setFirstName("Firstname");
        user.setPassword("test!1234");
        user.setAdmin(false);
		userRepository.save(user);
	}

	@Test
	public void create_shouldSaveSession() {
		// Act
		Session result = sessionService.create(session);

		// Assert
		assertNotNull(result.getId());
	}

	@Test
	public void delete_shouldDeleteSession() {
		// Act
		sessionService.delete(session.getId());

		// Assert
		Optional<Session> deletedSession = sessionRepository.findById(session.getId());
		assertFalse(deletedSession.isPresent(), "La session devrait être supprimée");
	}

	@Test
	public void findAll_shouldReturnAllSessions() {
		// Act
		List<Session> result = sessionService.findAll();

		// Assert
		assertFalse(result.isEmpty());
		assertEquals(2, result.size());
	}

	@Test
	public void getById_shouldReturnSession_whenSessionExists() {
		// Act
		Session result = sessionService.getById(session.getId());

		// Assert
		assertEquals(session.getName(), result.getName());
	}

	@Test
	public void getById_shouldReturnNull_whenSessionDoesNotExist() {
		// Act
		Session result = sessionService.getById(999L);

		// Assert
		assertNull(result);
	}

	@Test
	public void update_shouldUpdateSession() {
		// Arrange
		session.setName("Session modifiée");

		// Act
		Session updatedSession = sessionService.update(session.getId(), session);

		// Assert
		assertEquals("Session modifiée", updatedSession.getName());
	}

	@Test
	public void participate_shouldAddUserToSession() {
		// Act
		sessionService.participate(session.getId(), user.getId());

		// Assert
		Session updatedSession = sessionService.getById(session.getId());
		assertTrue(updatedSession.getUsers().contains(user));
	}

	@Test
	public void noLongerParticipate_shouldRemoveUserFromSession() {
		// Arrange
		sessionService.participate(session.getId(), user.getId());

		// Act
		sessionService.noLongerParticipate(session.getId(), user.getId());

		// Assert
		Session updatedSession = sessionService.getById(session.getId());
		assertFalse(updatedSession.getUsers().contains(user));
	}

	@AfterEach
	public void reset() {
		sessionRepository.deleteAll();
		userRepository.deleteAll();
	}
}
