package com.project.controllers;

import com.project.config.SecurityConfig;
import com.project.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    
//    //Login form
//    @GetMapping("/login")
//    public String getLoginForm(Model model) {
//        model.addAttribute("loginData", new User());
//        return "index.html";
//    }
    
//    @PostMapping("/login")
//    public String login(@Valid @ModelAttribute("loginData") User loginForm,
//                        Model model, ProjektController projContr, Pageable pageable, 
//                        Authentication authentication, HttpServletRequest request) {
//        String msg = "";
//        SecurityConfig userConf = new SecurityConfig();
//        
//        PasswordEncoder passwordEncoder = userConf.passwordEncoder();
//        String userName = loginForm.getUserName();
//        String password = loginForm.getPassword();
//        if(userName != null){
//            UserDetails details = userConf.userDetailsService(passwordEncoder)
//                                    .loadUserByUsername(userName);
//            
//            if(details != null) {
//                String userPassword = details.getPassword();
//                if(userPassword == null ? password == null : userPassword.equals(password)) {
//                    return projContr.getPaginatedProjects(1, 2, model, pageable, authentication, request);
//                } else {
//                    System.out.println("wrong password");
//                    msg = "Wrong password";
//                }
//            }
//        } else {
//            System.out.println("wrong login");
//            msg = "Wrong login";
//        }
//        
//        model.addAttribute("msg", msg);
//        model.addAttribute("loginError", "true");
//        return "index.html";
//    }
}
