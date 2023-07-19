package com.project.controllers;

import com.project.model.User;
import com.project.repositories.UserRepository;
import com.project.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @GetMapping("/index") 
    public String index(Model model) {
        return "index"; 
    }
    
    @GetMapping("/login") 
    public String login(HttpServletRequest request, HttpSession session, Model model) { 
        session.setAttribute(
            "error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION")
        );
        model.addAttribute("mode", "login");
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
                        statusMsg = "Incorrect password";
                    }
                }
            } else {
                        statusMsg = "Login error";
            }
        }else {
            statusMsg = "Incorrect username";
        }
        
        model.addAttribute("msg", statusMsg);
        model.addAttribute("loginError", "true");
        return "index.html";
    }
    
    
    
    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        
        model.addAttribute("saveData", user);
        model.addAttribute("mode","register");
        return "login";
    }
    
    @PostMapping("/register")
    public String register(Model model, @Valid @ModelAttribute User saveData) {
        Boolean msgError = false;
        String msg = "";
        String mode = "login";
        String returnTemplate = "login";
        Boolean userExists = checkIfUserExist(saveData.getUsername());
        Boolean passwordMatch = checkIfPasswordMatch(saveData.getPassword(), saveData.getConfirmPassword());

        if (saveData.getUsername().isBlank()) {
            msg = "Username is required";
            msgError = true;
            mode = "register";
        } else if (saveData.getPassword().isBlank()) {
            msg = "Passowrd is required";
            msgError = true;
            mode = "register";
        }else if ( userExists ) {
            msg = "Username already exists.";
            msgError = true;
            mode = "register";
        } else if ( !passwordMatch ) {
            msg = "Password not match";
            msgError = true;
            mode = "register";
        } else {
            createUser(saveData).getStatusCode().toString();
            returnTemplate = "redirect:/project?pageNumber=1&pageSize=5";
        }
        
        if ( msgError ) {
          model.addAttribute("saveData", saveData);  
        }
        
        model.addAttribute("msgError", msgError);
        model.addAttribute("msg", msg);
        model.addAttribute("mode",mode);
        return returnTemplate;
    }
    
    ResponseEntity<Void> createUser(@Valid @RequestBody User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setConfirmPassword(null);
        user.setRole("USER");
        User createdUser = userService.insert(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{zadanieId}").buildAndExpand(createdUser.getUserID()).toUri();
        return ResponseEntity.created(location).build();
    }
    
    public boolean checkIfUserExist(String username) {
        Boolean userExists = !userRepository.findByUsername(username).isEmpty();
        return userExists;
    }
    
    public boolean checkIfPasswordMatch(String password, String confirmPassword) {
        Boolean passwordMatch = false;
        if(password.equalsIgnoreCase(confirmPassword)) {
            passwordMatch = true;
        }

        return passwordMatch;
    }

}