package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    public void setUp() {
        session = new Session();
        session.setId(1L);
        sessionDto = new SessionDto();
    }

    @Test
    public void findById_shouldReturnSession_whenSessionExists() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(session);
    }

    @Test
    public void findById_shouldReturnNotFound_whenSessionDoesNotExist() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = sessionController.findById("1");

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        verify(sessionService).getById(1L);
    }

    @Test
    public void findAll_shouldReturnListOfSessions() {
        // Arrange
        List<Session> sessions = Collections.singletonList(session);
        List<SessionDto> sessionDtos = Collections.singletonList(sessionDto);

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Act
        ResponseEntity<?> response = sessionController.findAll();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessions);
    }

    @Test
    public void create_shouldCreateSession() {
        // Arrange
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).create(session);
        verify(sessionMapper).toDto(session);
    }

    @Test
    public void update_shouldUpdateSession() {
        // Arrange
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act
        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).update(1L, session);
    }

    @Test
    public void delete_shouldDeleteSession() {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(session);

        // Act
        ResponseEntity<?> response = sessionController.save("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).delete(1L);
    }

    @Test
    public void participate_shouldAddUserToSession() {
        // Act
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).participate(1L, 1L);
    }

    @Test
    public void noLongerParticipate_shouldRemoveUserFromSession() {
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).noLongerParticipate(1L, 1L);
    }
}
