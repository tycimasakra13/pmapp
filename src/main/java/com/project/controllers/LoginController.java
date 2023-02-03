package com.project.controllers;

import com.project.model.User;
import com.project.services.SecurityUserDetailsService;
import com.project.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private final UserService userService;
    
    @Autowired 
    private SecurityUserDetailsService userDetailsManager; 
    @Autowired
    private PasswordEncoder passwordEncoder; 

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/index") 
    public String index(Model model) {
        return "index"; 
    }
    
    @GetMapping("/login") 
    public String login(HttpServletRequest request, HttpSession session) { 
        session.setAttribute(
            "error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION")
        ); 
        return "login"; 
    } 
   
    private String getErrorMessage(HttpServletRequest request, String key) {
      Exception exception = (Exception) request.getSession().getAttribute(key); 
      String error = ""; 
      if (exception instanceof BadCredentialsException) { 
         error = "Invalid username and password!"; 
      } else if (exception instanceof LockedException) { 
         error = exception.getMessage(); 
      } else { 
         error = "Invalid username and password!"; 
      } 
      return error;
   }
//    //Login form
//    @GetMapping("/login")
//    public String getLoginForm(Model model) {
//        model.addAttribute("loginData", new User());
//        return "login.html";
//    }
    
    @PostMapping("/logining")
    public String login(@Valid @ModelAttribute("loginData") User loginForm,
                        Model model, ProjektController projContr, Pageable pageable, 
                        Authentication authentication, HttpServletRequest request,
                        final RedirectAttributes redirectAttributes) {
        String statusMsg = "";
        
        String userName = loginForm.getUsername();
        String password = loginForm.getPassword();
        
        if(userName != null){
            Optional<User> tmpUser = userService.getUserByName(userName);
            if(!tmpUser.isEmpty()) {
                String tmpUserName = tmpUser.get().getUsername();
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



