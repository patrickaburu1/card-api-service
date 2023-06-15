package com.card.cardapi;

import com.card.cardapi.entities.Role;
import com.card.cardapi.entities.User;
import com.card.cardapi.repositories.RoleRepo;
import com.card.cardapi.repositories.UserRepo;
import com.card.cardapi.services.TokenHelperFunctions;
import com.card.cardapi.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenHelperFunctions tokenHelper;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Mock the userRepo.findByUsername() method to return a user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        // Mock the role names for the user
        Role role = new Role();
        role.setName("ROLE_USER");
        user.getRoles().add(role);

        // Call the loadUserByUsername() method
        UserDetails userDetails = userService.loadUserByUsername("testuser");

        // Verify the userRepo.findByUsername() method is called once
        verify(userRepo, times(1)).findByUsername("testuser");

        // Verify the returned UserDetails contains the expected values
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock the userRepo.findByUsername() method to return null
        when(userRepo.findByUsername("testuser")).thenReturn(null);

        // Call the loadUserByUsername() method and expect an exception
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("testuser"));

        // Verify the userRepo.findByUsername() method is called once
        verify(userRepo, times(1)).findByUsername("testuser");
    }

    @Test
    void testSaveUser() {
        // Create a user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        // Mock the passwordEncoder.encode() method
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Mock the userRepo.save() method to return the user
        when(userRepo.save(user)).thenReturn(user);

        // Call the saveUser() method
        User savedUser = userService.saveUser(user);

        // Verify the passwordEncoder.encode() method is called once with the user's password
        verify(passwordEncoder, times(1)).encode("password");

        // Verify the userRepo.save() method is called once with the user
        verify(userRepo, times(1)).save(user);

        // Verify the returned savedUser is the same as the original user

        // Verify that the user's password is set to the encoded password
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void testSaveRole() {
        // Create a role
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        // Mock the roleRepo.save() method to return the role
        when(roleRepo.save(role)).thenReturn(role);

        // Call the saveRole() method
        Role savedRole = userService.saveRole(role);

        // Verify the roleRepo.save() method is called once with the role
        verify(roleRepo, times(1)).save(role);

        // Verify the returned savedRole is the same as the original role
        assertSame(role, savedRole);
    }

    @Test
    void testAddRoleToUser() {
        // Create a user
        User user = new User();
        user.setUsername("testuser");

        // Create a role
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        // Mock the userRepo.findByUsername() method to return the user
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        // Mock the roleRepo.findByName() method to return the role
        when(roleRepo.findByName("ROLE_ADMIN")).thenReturn(role);

        // Call the addRoleToUser() method
        userService.addRoleToUser("testuser", "ROLE_ADMIN");

        // Verify the userRepo.findByUsername() method is called once with the username
        verify(userRepo, times(1)).findByUsername("testuser");

        // Verify the roleRepo.findByName() method is called once with the roleName
        verify(roleRepo, times(1)).findByName("ROLE_ADMIN");

        // Verify that the role is added to the user's roles
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    void testGetUser() {
        // Create a user
        User user = new User();
        user.setUsername("testuser");

        // Mock the userRepo.findByUsername() method to return the user
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        // Call the getUser() method
        User retrievedUser = userService.getUser("testuser");

        // Verify the userRepo.findByUsername() method is called once with the username
        verify(userRepo, times(1)).findByUsername("testuser");

        // Verify the returned retrievedUser is the same as the original user
        assertSame(user, retrievedUser);
    }

    @Test
    void testGetUsers() {
        // Create a list of users
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("user1");
        userList.add(user1);
        User user2 = new User();
        user2.setUsername("user2");
        userList.add(user2);

        // Mock the userRepo.findAll() method to return the list of users
        when(userRepo.findAll()).thenReturn(userList);

        // Call the getUsers() method
        List<User> retrievedUsers = userService.getUsers();

        // Verify the userRepo.findAll() method is called once
        verify(userRepo, times(1)).findAll();

        // Verify the returned retrievedUsers is the same as the original userList
        assertSame(userList, retrievedUsers);
    }

}