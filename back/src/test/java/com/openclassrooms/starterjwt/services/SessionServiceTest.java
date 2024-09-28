package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

	@Mock
	private SessionRepository sessionRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private SessionService sessionService;

	private Session session;
	private User user;

	@BeforeEach
	public void setUp() {
		session = new Session();
		session.setId(1L);
		session.setName("Session 1");
		session.setDescription("Description de la session");
		session.setDate(new Date());
		session.setUsers(new ArrayList<User>());

		user = new User();
		user.setId(1L);
		user.setEmail("yoga@studio.com");
		user.setLastName("Name");
		user.setFirstName("Firstname");
		user.setPassword("test!1234");
		user.setAdmin(false);
	}

	@Test
	public void create_shouldSaveSession() {
		// Arrange
		when(sessionRepository.save(session)).thenReturn(session);

		// Act
		Session result = sessionService.create(session);

		// Assert
		assertEquals(session, result);
		verify(sessionRepository).save(session);
	}

	@Test
	public void delete_shouldDeleteSession() {
		// Arrange
		Long sessionId = 1L;

		// Act
		sessionService.delete(sessionId);

		// Assert
		verify(sessionRepository).deleteById(sessionId);
	}

	@Test
	public void findAll_shouldReturnAllSessions() {
		// Arrange
		List<Session> sessions = new ArrayList<Session>();
		sessions.add(session);
		when(sessionRepository.findAll()).thenReturn(sessions);

		// Act
		List<Session> result = sessionService.findAll();

		// Assert
		assertEquals(1, result.size());
		assertEquals(session, result.get(0));
		verify(sessionRepository).findAll();
	}

	@Test
	public void getById_shouldReturnSession_whenSessionExists() {
		// Arrange
		Long sessionId = 1L;
		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

		// Act
		Session result = sessionService.getById(sessionId);

		// Assert
		assertEquals(session, result);
		verify(sessionRepository).findById(sessionId);
	}

	@Test
	public void getById_shouldReturnNull_whenSessionDoesNotExist() {
		// Arrange
		Long sessionId = 1L;
		when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

		// Act
		Session result = sessionService.getById(sessionId);

		// Assert
		assertNull(result);
		verify(sessionRepository).findById(sessionId);
	}

	@Test
	public void update_shouldUpdateSession() {
		// Arrange
		Long sessionId = 1L;
		when(sessionRepository.save(session)).thenReturn(session);

		// Act
		Session result = sessionService.update(sessionId, session);

		// Assert
		assertEquals(session, result);
		verify(sessionRepository).save(session);
	}

	@Test
	public void participate_shouldAddUserToSession_whenUserDoesNotAlreadyParticipate() {
		// Arrange
		Long sessionId = 1L;
		Long userId = 1L;
		when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		// Act
		sessionService.participate(sessionId, userId);

		// Assert
		assertTrue(session.getUsers().contains(user));
		verify(sessionRepository).save(session);
	}

	@Test
	void participate_shouldThrowBadRequestException_whenUserAlreadyParticipates() {
		// Arrange
		session.getUsers().add(user);
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		// Act
		Executable executable = () -> sessionService.participate(session.getId(), user.getId());

		// Assert
		assertThrows(BadRequestException.class, executable);
	}

	@Test
	void participate_shouldThrowNotFoundException_whenSessionDoesNotExist() {
		// Arrange
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

		// Act
		Executable executable = () -> sessionService.participate(session.getId(), user.getId());

		// Assert
		assertThrows(NotFoundException.class, executable);
	}

	@Test
	void participate_shouldThrowNotFoundException_whenUserDoesNotExist() {
		// Arrange
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

		// Act
		Executable executable = () -> sessionService.participate(session.getId(), user.getId());

		// Assert
		assertThrows(NotFoundException.class, executable);
	}

	@Test
	public void noLongerParticipate_shouldRemoveUserFromSession_whenUserAlreadyParticipates() {
		// Arrange
		Long sessionId = 1L;
		Long userId = 1L;
		session.getUsers().add(user);
		when(sessionRepository.findById(user.getId())).thenReturn(Optional.of(session));

		// Act
		sessionService.noLongerParticipate(sessionId, userId);

		// Assert
		assertFalse(session.getUsers().contains(user));
		verify(sessionRepository).save(session);
	}

	@Test
	void noLongerParticipate_shouldThrowNotFoundException_whenSessionDoesNotExist() {
		// Arrange
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

		// Act
		Executable executable = () -> sessionService.noLongerParticipate(session.getId(), user.getId());

		// Assert
		assertThrows(NotFoundException.class, executable);
	}

	@Test
	void noLongerParticipate_shouldThrowBadRequestException_whenUserDoesNotParticipate() {
		// Arrange
		when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

		// Act
		Executable executable = () -> sessionService.noLongerParticipate(session.getId(), user.getId());

		// Assert
		assertThrows(BadRequestException.class, executable);
	}
}
