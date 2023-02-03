package com.project.bootstrap;
import org.springframework.security.authentication.AuthenticationProvider; 
import org.springframework.security.core.Authentication; 
import org.springframework.stereotype.Component;

@Component 
public class AuthProvider implements AuthenticationProvider {   
   @Override 
   public Authentication authenticate(Authentication authentication) {
      return authentication;
   } 
  
   @Override public boolean supports(Class<?> authentication) { 
      return true; 
   }
}