package com.card.cardapi;


import com.card.cardapi.entities.Role;
import com.card.cardapi.entities.User;
import com.card.cardapi.repositories.RoleRepo;
import com.card.cardapi.repositories.UserRepo;
import com.card.cardapi.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    public UserRepo userRepository;

    @Mock
    public RoleRepo roleRepository;

    @InjectMocks
    public UserServiceImpl userService;



    @Test
    public void testSaveRole() {
        // Create a role object
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        // Mock the saveRole() method
        when(roleRepository.save(role)).thenReturn(role);

        // Call the saveRole() method
        Role savedRole = userService.saveRole(role);

        // Verify that the roleRepository.save() method is called once
        verify(roleRepository, times(1)).save(role);

        // Verify that the returned role object is the same as the savedRole
        assertEquals(role, savedRole);
    }

    @Test
    public void testAddRoleToUser() {
        // Create a user object
        User user = new User();
        user.setUsername("testuser");

        // Mock the getUser() method
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Call the addRoleToUser() method
        userService.addRoleToUser("testuser", "ROLE_ADMIN");

        // Verify that the userRepository.findByUsername() method is called once
        verify(userRepository, times(1)).findByUsername("testuser");

        // Verify that the role is added to the user
        assertEquals(1, user.getRoles().size());
        // assertEquals("ROLE_ADMIN", user.getRoles().getName());
    }

    @Test
    public void testGetUser() {
        // Create a user object
        User user = new User();
        user.setUsername("testuser");

        // Mock the getUser() method
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Call the getUser() method
        User retrievedUser = userService.getUser("testuser");

        // Verify that the userRepository.findByUsername() method is called once
        verify(userRepository, times(1)).findByUsername("testuser");

        // Verify that the returned user object is the same as the retrievedUser
        assertEquals(user, retrievedUser);
    }

}