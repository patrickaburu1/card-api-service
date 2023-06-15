package com.card.cardapi.security.filter;


import com.card.cardapi.security.TokenBasedAuthentication;
import com.card.cardapi.services.TokenHelperFunctions;
import com.card.cardapi.services.UserServiceImpl;
import com.card.cardapi.utils.ResponseModel;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
public class RequestAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Log logger = LogFactory.getLog(this.getClass());
    private final TokenHelperFunctions tokenHelper;
    private final UserServiceImpl userDetailsService;
    public RequestAuthenticationFilter(TokenHelperFunctions tokenHelper, UserServiceImpl userDetailsService) {
        this.tokenHelper = tokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authToken = httpRequest.getHeader("Authorization");
        if (authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
             if (this.tokenHelper.isTokenExpired(authToken)) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                logger.info("*********** token received as expired " + authToken);
                response.getOutputStream().println(new Gson().toJson(new ResponseModel("error", "003", "Token expired")));
                MDC.clear();
            }
             else if (!this.tokenHelper.isTokenValid(authToken)) {
                 logger.info("===========Invalid Token: " + authToken);
                 ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                 // ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
                 response.setContentType("application/json");
                 response.getOutputStream().println(new Gson().toJson(new ResponseModel("error", "Invalid request")));
                 MDC.clear();
             }
             else {
                ((HttpServletResponse) response).setStatus(200);
                String username = this.tokenHelper.getUsernameFromToken(authToken);
                logger.info("*********TOKEN VALID: " + username);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    Map<String, Object> ret = this.tokenHelper.validateToken(authToken, userDetails.getUsername());
                    if ((Boolean) ret.get("valid")) {
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        chain.doFilter(request, response);
                        MDC.clear();
                    } else {
                        logger.info("===========Invalid Token Claims: " + authToken);
                        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.getOutputStream().println(new Gson().toJson(new ResponseModel("error", "Invalid request")));
                        MDC.clear();
                    }
                } else {
                    logger.info("===========username not null, Security context: " + username);
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getOutputStream().println(new Gson().toJson(new ResponseModel("error", "Invalid request")));
                    MDC.clear();
                }
            }
        } else {
            chain.doFilter(request, response);
            MDC.clear();
        }
    }
}
