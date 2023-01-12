package com.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    //Login form
    @RequestMapping("/login")
    public String login() {
        return "login.html";
    }
    
    //Error during login
    @RequestMapping("/login_error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }
}
