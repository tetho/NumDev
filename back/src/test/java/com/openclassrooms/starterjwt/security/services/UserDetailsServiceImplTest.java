package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        // Arrange
        String username = "notfound@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act
        Executable executable = () -> userDetailsService.loadUserByUsername(username); 
        
        // Assert
        assertThrows(UsernameNotFoundException.class, executable);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setFirstName("First");
        user.setLastName("Last");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("First", userDetails.getFirstName());
        assertEquals("Last", userDetails.getLastName());
    }
}
