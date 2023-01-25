package com.project.controllers;

import com.project.model.Projekt;
import com.project.model.User;
import com.project.services.ProjektService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("")
public class ProjektController {
    private final ProjektService projektService;

    @Autowired
    public ProjektController(ProjektService projektService) {
        this.projektService = projektService;
    }
    
    private Integer setPageNumber(Integer pageNumber) {
        if(pageNumber == null || pageNumber == 0) {
            pageNumber = 1;
        }
        
        return pageNumber;
    }
    
    private Integer setPageSize(Integer pageSize) {
        if(pageSize == null || pageSize == 0){
            pageSize = 10;
        }
        
        return pageSize;
    }
    
    @GetMapping("/project")
    public String getPaginatedProjects(@ModelAttribute("loggedUser") User lUser, 
            @RequestParam(value="pageNumber") Integer pageNumber,
            @RequestParam(value="pageSize") Integer pageSize,
            Model model, Pageable pageable, User user) {
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        Page<Projekt> totalProjects = projektService.getPaginatedProjects(pageNumber, pageSize);
        Integer totalPages = totalProjects.getTotalPages();

        String userRole = lUser.getRole();
        String userName = lUser.getUserName();
        model.addAttribute("formData", new Projekt());
        model.addAttribute("projects",totalProjects);
        model.addAttribute("mode","projectListViewPaginated");
        model.addAttribute("userRole", userRole);
        model.addAttribute("userName", userName);
        model.addAttribute("totalPages", totalPages);
   
        return "projekt.html";
    }
    
    @GetMapping("/addProject")
    public String getAddProjectForm(Model model) {
        model.addAttribute("saveData", new Projekt());
        model.addAttribute("mode","projectAdd");
        return "projekt.html";
    }
    
    @GetMapping("/editProjekt")
    public String getEditProjektForm(@RequestParam(value="projectID") Integer projectID, Model model) {
        Projekt selectedProject = projektService.getProjektById(projectID).get();

        String nazwa = selectedProject.getNazwa();
        String opis = selectedProject.getOpis();
        
        model.addAttribute("updateData", selectedProject);
        model.addAttribute("mode","projectEdit");
        model.addAttribute("nazwa", nazwa);
        model.addAttribute("opis", opis);
 
        return "projekt.html";
    }
    
    @PostMapping("/searchProject")
    public String searchProject(@Valid @ModelAttribute("formData") Projekt formData,
                               Model model, Pageable pageable) {

        System.out.println("response " + formData.getNazwa());
        Page<Projekt> totalProjects = projektService.searchByNazwa(formData.getNazwa(), pageable);
        Integer totalPages = totalProjects.getTotalPages();
        
        model.addAttribute("formData", new Projekt());
        model.addAttribute("projects",totalProjects);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("mode","projectListViewPaginated");
        
        return "projekt.html";
        
    }
    
    @PostMapping("/updateProject")
    public String updateProject(@Valid @ModelAttribute Projekt updateData, Model model, Pageable pageable) {
        
        ResponseEntity<Void> updateProj = projektService.getProjektById(updateData.getProjektId())
                .map(p -> {
                    projektService.updateProjekt(updateData.getProjektId(), updateData);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
        String statusCode = updateProj.getStatusCode().toString();
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "projekt.html";
    }
    
    @GetMapping("/deleteProject")
    public String deleteProject(@RequestParam(value="projectID") Integer projektId, Model model) {
        ResponseEntity<Void> deleteProj = projektService.getProjektById(projektId).map(p -> {
            projektService.deleteProjekt(projektId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
        
        
        String statusCode = deleteProj.getStatusCode().toString();
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "projekt.html";
    }
    
    @PostMapping("/addProject")
    public String addProject(@Valid @ModelAttribute Projekt saveData, Model model, Pageable pageable) {
        
        String statusCode = createProjekt(saveData).getStatusCode().toString();
   
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "projekt.html";
    }
    
       
    ResponseEntity<Void> createProjekt(Projekt projekt) {
        Projekt createdProjekt = projektService.insert(projekt);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{projektId}").buildAndExpand(createdProjekt.getProjektId()).toUri();
        return ResponseEntity.created(location).build();
    }
}

//    private String currentUserName(Authentication authentication) {
//        return authentication.getName();
//    }
//    
//    private String currentUserRole(HttpServletRequest request) {
//        //SecurityConfig userConf = new SecurityConfig();
//        //PasswordEncoder passwordEncoder = userConf.passwordEncoder();
//        //UserDetails details = userConf.userDetailsService(passwordEncoder).loadUserByUsername(currentUserName(authentication));
//        
//        String userRole;
//        if (request.isUserInRole("ADMIN")) {
//           userRole = "Admin";
//        } else {
//           userRole = "User";
//        }
//        
//        return userRole;
//    }
