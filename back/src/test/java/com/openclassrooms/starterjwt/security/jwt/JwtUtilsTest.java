package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${oc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySecretKey");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 60000); // 1 minute
    }

    @Test
    public void generateJwtToken_shouldReturnToken() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .username("yoga@studio.com")
                .build();
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    public void validateJwtToken_shouldReturnFalse_whenTokenIsInvalid() {
    	// Arrange
    	String token = "invalidToken";
    	
        // Act & Assert
        assertFalse(jwtUtils.validateJwtToken(token));
    }
}
