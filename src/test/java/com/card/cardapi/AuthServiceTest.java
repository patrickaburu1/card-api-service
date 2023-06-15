package com.card.cardapi;



import com.card.cardapi.dtos.LoginRequestDto;
import com.card.cardapi.entities.User;
import com.card.cardapi.repositories.RoleRepo;
import com.card.cardapi.repositories.UserRepo;
import com.card.cardapi.services.AuthService;
import com.card.cardapi.services.TokenHelperFunctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenHelperFunctions tokenHelper;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_SuccessfulAuthentication() {
        // Mock the userRepo.findByUsername() method
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        when(userRepo.findByUsername(any())).thenReturn(user);

        // Mock the authenticationManager.authenticate() method
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock the tokenHelper.generateToken() method
        String mockToken = "mockToken";
        when(tokenHelper.generateToken(anyString(), any(HttpServletRequest.class))).thenReturn(mockToken);

        // Create a LoginDto object
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("testuser@example.com");
        loginDto.setPassword("password");

        // Create a mock HttpServletRequest
        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);

        // Call the login() method
        Map<String, Object> response = authService.login(loginDto, mockHttpServletRequest);

        // Verify the userRepo.findByUsername() method is called once
        verify(userRepo, times(1)).findByUsername(any());

        // Verify the authenticationManager.authenticate() method is called once
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Verify the tokenHelper.generateToken() method is called once
        verify(tokenHelper, times(1)).generateToken(anyString(), any(HttpServletRequest.class));

        // Verify the response contains the expected values
        assertEquals("success", response.get("status"));
        assertEquals(mockToken, response.get("accessToken"));
        assertEquals("Authentication successful.", response.get("message"));
        assertEquals(user, response.get("user"));
    }

    @Test
    public void testLogin_InvalidCredentials() {
        // Mock the userRepo.findByUsername() method to return null (user not found)
        when(userRepo.findByUsername(any())).thenReturn(null);

        // Create a LoginDto object
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("testuser@example.com");
        loginDto.setPassword("password");

        // Create a mock HttpServletRequest
        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);

        // Call the login() method
        Map<String, Object> response = authService.login(loginDto, mockHttpServletRequest);

        // Verify the userRepo.findByUsername() method is called once
        verify(userRepo, times(1)).findByUsername(any());

        // Verify that authenticationManager.authenticate() method is not called
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Verify that tokenHelper.generateToken() method is not called
        verify(tokenHelper, never()).generateToken(anyString(), any(HttpServletRequest.class));

        // Verify the response contains the expected values
        assertEquals("01", response.get("status"));
        assertEquals("NOT_REGISTERED", response.get("statusMessage"));
        assertEquals("Sorry your not registered.", response.get("message"));
    }

    @Test
    public void testLogin_BadCredentialsException() {
        // Mock the userRepo.findByUsername() method to return a user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        when(userRepo.findByUsername(any())).thenReturn(user);

        // Mock the authenticationManager.authenticate() method to throw a BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        // Create a LoginDto object
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("testuser@example.com");
        loginDto.setPassword("invalidPassword");

        // Create a mock HttpServletRequest
        HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);

        // Call the login() method
        Map<String, Object> response = authService.login(loginDto, mockHttpServletRequest);

        // Verify the userRepo.findByUsername() method is called once
        verify(userRepo, times(1)).findByUsername(any());

        // Verify the authenticationManager.authenticate() method is called once
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Verify that tokenHelper.generateToken() method is not called
        verify(tokenHelper, never()).generateToken(anyString(), any(HttpServletRequest.class));

        // Verify the response contains the expected values
        assertEquals("error", response.get("status"));
        assertEquals("Sorry invalid credentials", response.get("message"));
        assertEquals("ERR_MESSAGE_WRONG_PIN", response.get("statusMessage"));
    }

}