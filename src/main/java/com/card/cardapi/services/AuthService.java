package com.card.cardapi.services;

import com.card.cardapi.dtos.LoginRequestDto;
import com.card.cardapi.entities.User;
import com.card.cardapi.repositories.RoleRepo;
import com.card.cardapi.repositories.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;

@Component
@Log4j2
public class AuthService
{
    UserRepo userRepo;
    RoleRepo roleRepo;
    PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenHelperFunctions tokenHelper;

    public AuthService(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenHelperFunctions tokenHelper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenHelper = tokenHelper;
    }

    public Map<String, Object> login(LoginRequestDto request, HttpServletRequest httpServletRequest) {
        Map<String, Object> response = new HashMap<>();

        String ip = httpServletRequest.getHeader("X-Forwarded-For") == null ? httpServletRequest.getRemoteAddr() : httpServletRequest.getHeader("X-Forwarded-For");

        User user = userRepo.findByUsername(request.getEmail());

        if (null == user) {
            response.put("status", "01");
            response.put("statusMessage", "NOT_REGISTERED");
            response.put("message", "Sorry your not registered.");
            return response;
        }

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    request.getPassword()
            );

            //  usernamePasswordAuthenticationToken.setDetails(loginIpAddress);

            final Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            // Inject into security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String phoneNumber = null;

            if (authentication.getPrincipal() instanceof User)
                phoneNumber = ((User) authentication.getPrincipal()).getUsername();
            else
                phoneNumber = (String) authentication.getName();

            log.info("*******USERNAME_TOKEN: " + phoneNumber);

            //String jwToken = tokenHelper.generateToken(authentication,httpServletRequest);
            String jwToken = tokenHelper.generateToken(request.getEmail(),httpServletRequest);

            response.put("status", "success");
            response.put("accessToken", jwToken);
            // response.put("expiry", expiresIn * 1000);
            response.put("message", "Authentication successful.");
            response.put("user", user);

        } catch (BadCredentialsException e) {
            // error.append("Sorry! Your account is inactive. Contact administrator.  ");
            response.put("message", "Sorry invalid credentials");
            response.put("statusMessage", "ERR_MESSAGE_WRONG_PIN");
            response.put("status", "error");
        }
        return response;
    }
}
