package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@Transactional
class UserServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
        user = userRepository.save(user);    	
    }

    @Test
    public void findById_shouldReturnUser_whenUserExists() {
        // Arrange
    	Long userId = 1L;
    	
        // Act
        User result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void findById_shouldReturnNull_whenUserDoesNotExist() {
    	// Arrange
    	Long userId = 999L;
    	
    	// Act
        User result = userService.findById(userId);

        // Assert
        assertNull(result);
    }

    @Test
    public void delete_shouldDeleteUser_whenUserExists() {
        // Arrange
    	Long userId = 1L;
    	
        // Act
        userService.delete(userId);

        // Assert
        assertNull(userRepository.findById(user.getId()).orElse(null));
    }
    
    @AfterEach
	public void reset() {
		userRepository.deleteAll();
	}
}
