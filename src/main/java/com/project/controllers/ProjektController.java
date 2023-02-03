package com.project.controllers;

import com.project.model.File;
import com.project.model.Projekt;
import com.project.model.User;
import com.project.services.ProjektService;
import com.project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("")
public class ProjektController {
    private final ProjektService projektService;
    private String userName;
    private String userRole;

    @Autowired
    public ProjektController(ProjektService projektService) {
        this.projektService = projektService;
    }
    
    @Autowired
    private UserService userService;
    
    
    private Integer setPageNumber(Integer pageNumber) {
        if(pageNumber == null || pageNumber == 0) {
            pageNumber = 1;
        }
        
        return pageNumber;
    }
    
    private Integer setPageSize(Integer pageSize) {
        if(pageSize == null || pageSize == 0){
            pageSize = 2;
        }
        
        return pageSize;
    }
    
    @GetMapping("/project")
    public String getPaginatedProjects( 
            @RequestParam(value="pageNumber") Integer pageNumber,
            @RequestParam(value="pageSize") Integer pageSize,
            Model model, Pageable pageable, User user,
            Authentication authentication) {
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        Page<Projekt> totalProjects = projektService.getPaginatedProjects(pageNumber, pageSize);
        Integer totalPages = totalProjects.getTotalPages();
        
        userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new Projekt());
        model.addAttribute("projects",totalProjects);
        model.addAttribute("userRole",userRole);
        model.addAttribute("mode","projectListViewPaginated");
        model.addAttribute("totalPages", totalPages);
   
        return "projekt.html";
    }
    
    @PostMapping("/project")
    public String searchProject(@Valid @ModelAttribute("formData") Projekt formData,
                               Model model, Pageable pageable, Authentication authentication) {

        Integer pageNumber = 1;
        Integer pageSize = 2;
        Page<Projekt> totalProjects = projektService.searchByNazwa(formData.getNazwa(), pageNumber, pageSize);
        Integer totalPages = totalProjects.getTotalPages();
        
        userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new Projekt());
        model.addAttribute("projects",totalProjects);
        model.addAttribute("userRole",userRole);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("mode","projectListViewPaginated");
        
        return "projekt.html";
        
    }
    
    //add new project
    @GetMapping("/addProject")
    public String getAddProjectForm(Model model) {
        model.addAttribute("saveData", new Projekt());
        model.addAttribute("mode","projectAdd");
        return "projekt.html";
    }
    
    @PostMapping("/addProject")
    public String addProject(@Valid @ModelAttribute Projekt saveData, Model model, Pageable pageable) {
        
        String statusCode = createProjekt(saveData).getStatusCode().toString();
   
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/project?pageNumber=1&pageSize=2";
    }
     
    ResponseEntity<Void> createProjekt(Projekt projekt) {
        Projekt createdProjekt = projektService.insert(projekt);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{projektId}").buildAndExpand(createdProjekt.getProjektId()).toUri();
        return ResponseEntity.created(location).build();
    }
    //end of add new project
    
    //edit project
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
    
    @PostMapping("/updateProject")
    public String updateProject(@Valid @ModelAttribute Projekt updateData, Model model, Pageable pageable) {
        String statusCode = updateProjekt(updateData, updateData.getProjektId()).getStatusCode().toString();
       
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/project?pageNumber=1&pageSize=2";
    }
   
    public ResponseEntity<Void> updateProjekt(@Valid @RequestBody Projekt projekt,
                                              @PathVariable Integer projektId) {
        return projektService.getProjektById(projektId)
                .map(p -> {
                    projektService.updateProjekt(projektId, projekt);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    //end of udpate project
    
    //delete project
    @GetMapping("/deleteProject")
    public String deleteProject(@RequestParam(value="projectID") Integer projektId, Model model) {        
        String statusCode = deleteProjekt(projektId).getStatusCode().toString();
        
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/project?pageNumber=1&pageSize=2";
    }
    
    public ResponseEntity<Void> deleteProjekt(@PathVariable Integer projektId) {
        return projektService.getProjektById(projektId).map(p -> {
            projektService.deleteProjekt(projektId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    //end of delete project
}

