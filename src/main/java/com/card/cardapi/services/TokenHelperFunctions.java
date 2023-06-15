package com.card.cardapi.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.card.cardapi.entities.User;
import com.card.cardapi.repositories.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class TokenHelperFunctions {
    @Value("${jwt.secret}")
    public  String base64SecretBytes;

    @Autowired
    private UserRepo userRepo;

    @Value("${jwt.expiry.time}")
    private Long jwtExpiresIn;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public Map<String, Object> validateToken(String token, String username) {

        Map<String, Object> response = new HashMap<>();
        if(token != null){
            if(token.startsWith("Bearer ")){
                token = token.substring(7);
            }
        }

        // Users user = (Users) userDetails;

        Optional<User> optional = Optional.ofNullable(userRepo.findByUsername(username));
        if(!optional.isPresent()) {
            response.put("valid", false);
            response.put("user", null);
            return response;
        }

        User user = optional.get();
        final String phoneNo = getUsernameFromToken(token);

        log.info("*******PHONE_NUMBER_TOKEN2: " + phoneNo);

        final Date created = getIssuedAtDateFromToken(token);
        response.put ("valid", phoneNo != null && phoneNo.equals(user.getUsername()));
        response.put("user", user);
        return response;
    }

    public String getUsernameFromToken(String token) {

        if(token != null){
            if(token.startsWith("Bearer ")){
                token = token.substring(7);
            }
        }

        String phoneNo;
        try {
            final Claims claims = getAllClaimsFromToken(token);
            phoneNo = claims.getSubject();

            log.info("*****TOKEN AUDIENCE: " + claims.getAudience());
            log.info("*****TOKEN SUBJECT: " + claims.getSubject());
            log.info("*****TOKEN CREATION: " + claims.getIssuedAt());
            log.info("*****TOKEN EXPIRATION: " + claims.getExpiration());
            log.info("*****TOKEN ISSUER: " + claims.getIssuer());

        } catch (Exception e) {
            phoneNo = null;
        }
        return phoneNo;
    }
    public String refreshToken(Authentication authentication, HttpServletRequest request){
        org.springframework.security.core.userdetails.User user=(org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        Algorithm algorithm= Algorithm.HMAC256(base64SecretBytes);
        String refreshToken=JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30* 60*100))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        return refreshToken;
    }


    public String generateToken(String username, HttpServletRequest request) {
        String audience = generateAudience();
        try {
            return Jwts.builder()
                    .setIssuer(request.getRequestURL().toString())
                    .setSubject(username)
                    .setAudience(audience)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() +jwtExpiresIn ))
                    .signWith( SIGNATURE_ALGORITHM, base64SecretBytes )
                    .compact();

        } catch (Exception e) {
            log.error("An error has occurred: ",e);
        }

        return null;
    }


    private String generateAudience() {
        String audience = "API";
        return audience;
    }
    public boolean isTokenExpired(String token) {
        boolean tokenExpired = true;

        if(token != null){
            long secondsInMilli = 1000 * 60;

            if(getIssuedAtDateFromToken(token) != null ){
                long expiryTimeSeconds = getExpirationDateFromToken(token).getTime()/ secondsInMilli;

                //long issuedTimeSeconds = getIssuedAtDateFromToken(token).getTime()/ secondsInMilli;

                //long expiryTimeSeconds = issuedTimeSeconds + Long.valueOf(expiresIn);

                Date date = new Date(System.currentTimeMillis());
                log.info("Token Expiry Time: "+getExpirationDateFromToken(token).toString()
                        +"\nCurrent Time: "+getExpirationDateFromToken(token).toString());

                long currentTimeSeconds =   date.getTime()/secondsInMilli;

                if(currentTimeSeconds < expiryTimeSeconds)
                    tokenExpired = false;
            }
        }

        return tokenExpired;
    }



    public Date getExpirationDateFromToken(String token) {

        if(token != null){
            if(token.startsWith("Bearer ")){
                token = token.substring(7);
            }
        }

        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public Date getIssuedAtDateFromToken(String token) {

        if(token != null){
            if(token.startsWith("Bearer ")){
                token = token.substring(7);
            }
        }

        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }


    private Claims getAllClaimsFromToken(String token) {
        Claims claims;

        try {
            claims = Jwts.parser()
                    // .setSigningKey(keyProvider.getKey())
                    .setSigningKey(base64SecretBytes)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            log.error("=== error getting claims from token {} ",e.getMessage());
            claims = null;
        }
        return claims;
    }

    public boolean isTokenValid(String token){
        try {
            Claims claims;
            claims = Jwts.parser()
                    .setSigningKey(base64SecretBytes)
                    .parseClaimsJws(token)
                    .getBody();

            if(claims == null){
                return false;
            }
            else {
                return true;
            }
        } catch (Exception e) {
            log.error("An error has occurred: ",e);
            return false;
        }
    }

}
