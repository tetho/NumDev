package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserDetailsImplTest {

    @Test
    public void getAdmin_shouldReturnTrue_whenUserIsAdmin() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .admin(true)
                .build();

        // Act
        Boolean isAdmin = userDetails.getAdmin();

        // Assert
        assertTrue(isAdmin);
    }

    @Test
    public void getAdmin_shouldReturnFalse_whenUserIsNotAdmin() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .admin(false)
                .build();

        // Act
        Boolean isAdmin = userDetails.getAdmin();

        // Assert
        assertFalse(isAdmin);
    }
    
    @Test
    public void getPassword_shouldReturnPassword() {
        // Arrange
        String password = "password";
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .password(password)
                .build();

        // Act
        String retrievedPassword = userDetails.getPassword();

        // Assert
        assertEquals(password, retrievedPassword);
    }

    @Test
    public void isAccountNonExpired_shouldReturnTrue() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act
        boolean isNonExpired = userDetails.isAccountNonExpired();

        // Assert
        assertTrue(isNonExpired);
    }

    @Test
    public void isAccountNonLocked_shouldReturnTrue() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act
        boolean isNonLocked = userDetails.isAccountNonLocked();

        // Assert
        assertTrue(isNonLocked);
    }

    @Test
    public void isCredentialsNonExpired_shouldReturnTrue() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act
        boolean areCredentialsNonExpired = userDetails.isCredentialsNonExpired();

        // Assert
        assertTrue(areCredentialsNonExpired);
    }

    @Test
    public void isEnabled_shouldReturnTrue() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Act
        boolean isEnabled = userDetails.isEnabled();

        // Assert
        assertTrue(isEnabled);
    }
    
    @Test
    public void equals_shouldReturnTrue_whenSameObject() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("yoga@studio.com")
                .firstName("Firstname")
                .lastName("Lastname")
                .admin(false)
                .password("password")
                .build();

        // Act
        boolean result = userDetails.equals(userDetails);

        // Assert
        assertTrue(result);
    }
    
    @Test
    public void equals_shouldReturnTrue_whenIdsAreEqual() {
        // Arrange
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .build();
        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .build();

        // Act
        boolean result = user1.equals(user2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void equals_shouldReturnFalse_whenIdsAreDifferent() {
        // Arrange
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .build();
        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L)
                .build();

        // Act
        boolean result = user1.equals(user2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void equals_shouldReturnFalse_whenComparingToNull() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("yoga@studio.com")
                .firstName("Firstname")
                .lastName("Lastname")
                .admin(false)
                .password("password")
                .build();

        // Act
        boolean result = userDetails.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void equals_shouldReturnFalse_whenComparingToDifferentClass() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("yoga@studio.com")
                .firstName("Firstname")
                .lastName("Lastname")
                .admin(false)
                .password("password")
                .build();
        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L)
                .username("test@test.com")
                .firstName("Utilisateur")
                .lastName("Test")
                .admin(true)
                .password("motDePasse")
                .build();

        // Act
        boolean result = userDetails.equals(user2);

        // Assert
        assertFalse(result);
    }
}
