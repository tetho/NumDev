package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;
    private Authentication authentication;
    private UserDetailsImpl userDetails;
    private User user;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword("password");

        signupRequest = new SignupRequest();
        signupRequest.setEmail("user@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password");

        user = new User("user@example.com", "Doe", "John", "encodedPassword", false);
        userDetails =UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .admin(false)
                .password("encodedPassword")
                .build();
        authentication = mock(Authentication.class);
    }

    @Test
    void authenticateUser_shouldReturnJwt_whenLoginIsSuccessful() {
        // Arrange
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals("jwt-token", jwtResponse.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenEmailIsAlreadyTaken() {
        // Arrange
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_shouldSaveNewUser_whenRegistrationIsSuccessful() {
        // Arrange
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository).save(any(User.class));
    }
}
