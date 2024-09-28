package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    public void doFilterInternal_shouldSetAuthentication_whenTokenIsValid() throws Exception {
        // Arrange
        String jwt = "validJwtToken";
        String username = "yoga@studio.com";
        UserDetails userDetails = UserDetailsImpl.builder()
            .username(username)
            .password("password")
            .build();
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(jwt)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
    @Test
    public void doFilterInternal_shouldNotSetAuthentication_whenTokenIsInvalid() throws Exception {
        // Arrange
        String jwt = "invalidJwtToken";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtUtils.validateJwtToken(jwt)).thenReturn(false); // Token validation fails

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtUtils).validateJwtToken(jwt);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
    
    @Test
    void doFilterInternal_shouldNotSetAuthentication_whenTokenIsNull() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        when(request.getHeader("Authorization")).thenReturn(null); // No token present

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validateJwtToken(anyString());
    }

}
