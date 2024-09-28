package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp() {
    	user = new User();
        user.setId(1L);
        user.setEmail("yoga@studio.com");
        user.setFirstName("Firstname");
        user.setLastName("Name");
        user.setPassword("test!1234");
        user.setAdmin(false);
    }

    @Test
    public void findById_shouldReturnUser_whenUserExists() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        UserDto expectedUserDto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getLastName(),
                user.getFirstName(),
                user.isAdmin(),
                null,
                null,
                null
            );
        when(userMapper.toDto(user)).thenReturn(expectedUserDto);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void findById_shouldReturnNotFound_whenUserDoesNotExist() {
        // Arrange
        when(userService.findById(1L)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findById_shouldReturnBadRequest_whenIdIsInvalid() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid_id");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void delete_shouldDeleteUser_whenAuthorized() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(userService).delete(1L);
    }

    @Test
    public void delete_shouldReturnUnauthorized_whenUserIsNotOwner() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otheruser@test.com");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        verify(userService, never()).delete(anyLong());
    }

    @Test
    public void delete_shouldReturnBadRequest_whenIdIsInvalid() {
        // Act
        ResponseEntity<?> response = userController.save("invalid");

        // Assert
        assertEquals(400, response.getStatusCodeValue());
    }
}
