package com.card.cardapi.controllers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.card.cardapi.dtos.LoginRequestDto;
import com.card.cardapi.services.AuthService;
import com.card.cardapi.services.UserService;
import com.card.cardapi.entities.Role;
import com.card.cardapi.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@Log4j2
public class UserController {

    final UserService userService;

    final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping(value = "/v1/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDto request, HttpServletRequest httpServletRequest) {
        Map<String, Object> response = authService.login(request,httpServletRequest);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(response);
            log.info("*************** api login request {} "+mapper.writeValueAsString(request));
            log.info("*************** api login response "+json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/token/refresh")
    public void refreshtoken(HttpServletRequest request, HttpServletResponse response ) throws IOException {


        String authorizationHeader=request.getHeader(AUTHORIZATION);

        if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){

            try {

                String refresh_token=authorizationHeader.substring("Bearer ".length());

                Algorithm algorithm= Algorithm.HMAC256("secret".getBytes());

                JWTVerifier verifier=JWT.require(algorithm).build();

                DecodedJWT decodedJWT=verifier.verify(refresh_token);

                String username=decodedJWT.getSubject();

                User user=userService.getUser(username);

                String access_token=JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10* 60*100))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);



                Map<String,String> tokens=new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),tokens );

            }catch (Exception ex){

                Map<String,String> error=new HashMap<>();
                error.put("" +
                        "error message for refresf", ex.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),error );

            }

        }else {
             throw new RuntimeException("Refresh token is missing");


        }




    }









}
