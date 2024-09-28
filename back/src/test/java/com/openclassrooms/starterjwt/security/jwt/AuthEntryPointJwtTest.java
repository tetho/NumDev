package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;

public class AuthEntryPointJwtTest {

    private final AuthEntryPointJwt authEntryPointJwt = new AuthEntryPointJwt();

    @Test
    void commence_shouldReturnUnauthorizedResponse_whenAuthenticationFails() throws Exception {
        // Arrange
        AuthenticationException authException = new AuthenticationServiceException("Unauthorized");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        authEntryPointJwt.commence(request, response, authException);

        // Assert
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        String jsonResponse = response.getContentAsString();
        assertTrue(jsonResponse.contains("Unauthorized"));
    }
}
