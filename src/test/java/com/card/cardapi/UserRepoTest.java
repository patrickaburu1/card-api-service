package com.card.cardapi;


import com.card.cardapi.entities.User;
import com.card.cardapi.repositories.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserRepoTest {


    @Autowired
    private UserRepo userRepo;



    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testFindByUsername() {
        // Create a test user
        User user = new User();
        user.setName("John Doe");
        user.setUsername("johndoe");
        user.setPassword("password");

        // Save the user to the database
        userRepo.save(user);

        // Call the findByUsername method
        User result = userRepo.findByUsername("johndoe");

        // Assert the expected result
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("johndoe", result.getUsername());
        assertEquals("password", result.getPassword());
    }
}