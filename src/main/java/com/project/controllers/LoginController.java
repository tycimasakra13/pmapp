package com.project.controllers;

import com.project.model.User;
import com.project.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    
    //Login form
    @GetMapping("/login")
    public String getLoginForm(Model model) {
        model.addAttribute("loginData", new User());
        return "index.html";
    }
    
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginData") User loginForm,
                        Model model, ProjektController projContr, Pageable pageable, 
                        Authentication authentication, HttpServletRequest request,
                        final RedirectAttributes redirectAttributes) {
        String statusMsg = "";
        
        String userName = loginForm.getUserName();
        String password = loginForm.getPassword();
        
        if(userName != null){
            Optional<User> tmpUser = userService.getUserByName(userName);
            if(!tmpUser.isEmpty()) {
                String tmpUserName = tmpUser.get().getUserName();
                if(tmpUserName != null) {
                    String userPassword = tmpUser.get().getPassword();
                    if(userPassword == null ? password == null : userPassword.equals(password)) {
                        redirectAttributes.addFlashAttribute("loggedUser", tmpUser.get());
                        return "redirect:/project?pageNumber=1&pageSize=2";
                    } else {
                        System.out.println("Incorrect password");
                        statusMsg = "Incorrect password";
                    }
                }
            } else {
                System.out.println("Login error");
                        statusMsg = "Login error";
            }
        }else {
            System.out.println("Incorrect username");
            statusMsg = "Incorrect username";
        }
        
        model.addAttribute("msg", statusMsg);
        model.addAttribute("loginError", "true");
        return "index.html";
    }
}
