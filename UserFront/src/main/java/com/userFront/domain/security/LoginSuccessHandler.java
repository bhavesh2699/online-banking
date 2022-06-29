package com.userFront.domain.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.userFront.domain.User;
 
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
 
    	User userDetails = (User) authentication.getPrincipal();
         
        String redirectURL = request.getContextPath();
         
        if (userDetails.hasRole("ROLE_ADMIN")) {
            redirectURL = "admin";
        } else if (userDetails.hasRole("ROLE_USER")) {
            redirectURL = "userFront";
        }
         
        response.sendRedirect(redirectURL);
         
    }
 
}