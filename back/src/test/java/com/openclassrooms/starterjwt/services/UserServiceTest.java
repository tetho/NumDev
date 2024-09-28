package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("yoga@studio.com");
        user.setLastName("Name");
        user.setFirstName("Firstname");
        user.setPassword("test!1234");
        user.setAdmin(false);
    }

    @Test
    public void findById_shouldReturnUser_whenUserExists() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        User result = userService.findById(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void findById_shouldReturnNull_whenUserDoesNotExist() {
        // Arrange
    	Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.findById(userId);

        // Assert
        assertNull(result);
    }

    @Test
    public void delete_shouldDeleteUser_whenUserExists() {
        // Act
        userService.delete(user.getId());

        // Assert
        verify(userRepository).deleteById(user.getId());
    }
}
