package com.project.controllers;

import com.project.model.Zadanie;
import com.project.services.ProjektService;
import com.project.services.StudentService;
import com.project.services.UserService;
import com.project.services.ZadanieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("")
public class ZadanieController {
    private final ZadanieService zadanieService;
    private String userRole;
  
    @Autowired
    public ZadanieController(ZadanieService zadanieService) {
        this.zadanieService = zadanieService;
    }
    
    @Autowired
    UserService userService;
    
    @Autowired
    ProjektService projektService;
    
    @Autowired
    StudentService studentService;
    
    private Integer setPageNumber(Integer pageNumber) {
        if(pageNumber == null || pageNumber == 0) {
            pageNumber = 1;
        }
        
        return pageNumber;
    }
    
    private Integer setPageSize(Integer pageSize) {
        if(pageSize == null || pageSize == 0){
            pageSize = 5;
        }
        
        return pageSize;
    }
    
    private Map<Integer, String> projectsForSelect() { 
        Map<Integer, String> projectsLists = new HashMap();
        projektService.getProjectsList().forEach((projekt) -> {
            projekt.getProjektId();
            projectsLists.put(projekt.getProjektId(),projekt.getNazwa());
        });
        
        return projectsLists;
    }
    
    private Map<Integer, String> studentsForSelect() {
        Map<Integer, String> studentsLists = new HashMap<>();
        studentService.getStudentsList().forEach((student) -> {
            studentsLists.put(student.getId(), student.getImie() + " " + student.getNazwisko());
        });
        
        return studentsLists;
    }

    @GetMapping("/task")
    public String getPaginatedTasks(
            @RequestParam(required = false, value="pageNumber") Integer pageNumber,
            @RequestParam(required = false, value="pageSize") Integer pageSize,
            @RequestParam(required = false, value="studentId") Integer studentId,
            @RequestParam(required = false, value="projectID") Integer projectID,
            Model model, Authentication authentication) {
        
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        
        Page<Zadanie> tasksList = null;
        
        if(studentId != null) {
            tasksList = zadanieService.getZadaniaStudenta(studentId, pageNumber, pageSize);
        } else if(projectID != null) { 
            tasksList = zadanieService.getZadaniaProjektu(projectID, pageNumber, pageSize);
        } else {
            tasksList = zadanieService.getPaginatedTasks(pageNumber, pageSize);
        }
        
        Integer totalPages = tasksList.getTotalPages();
        
        userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new Zadanie());
        model.addAttribute("tasks",tasksList);
        model.addAttribute("mode","taskListViewPaginated");
        model.addAttribute("userRole", userRole);
        model.addAttribute("totalPages", totalPages);
   
        return "task.html";
    }
    
    @PostMapping("/task")
    public String searchTask(@Valid @ModelAttribute("formData") Zadanie formData,
                               Model model, Authentication authentication) {
        
        Integer pageNumber = setPageNumber(0);
        Integer pageSize = setPageSize(0);
        
        Page<Zadanie> totalTasks = zadanieService.searchByNazwa(formData.getNazwa(), pageNumber, pageSize);
        Integer totalPages = totalTasks.getTotalPages();
        
        userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new Zadanie());
        model.addAttribute("tasks",totalTasks);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("userRole", userRole);
        model.addAttribute("mode","taskListViewPaginated");
        
        return "task.html";
    }
    
    @GetMapping("/addTask")
    public String getAddTaskForm(Model model, Pageable pageable) {
        Zadanie task = new Zadanie();
        
        model.addAttribute("saveData", task);
        model.addAttribute("projects", projectsForSelect());
        model.addAttribute("students", studentsForSelect());
        model.addAttribute("mode","taskAdd");
        return "task.html";
    }
    
    @GetMapping("/editTask")
    public String getEditTaskForm(@RequestParam(value="taskId") Integer taskId, Model model, Pageable pageable) {
        Zadanie selectedTask = zadanieService.getZadanieById(taskId).get();
        
        model.addAttribute("updateData", selectedTask);
        model.addAttribute("projects", projectsForSelect());
        model.addAttribute("students", studentsForSelect());
        model.addAttribute("mode","taskEdit");
 
        return "task.html";
    }
    
    
    @PostMapping("/updateTask")
    public String updateTask(@Valid @ModelAttribute Zadanie updateData, 
            Model model) {
        String returnValue = "task.html";
        Integer taskId = updateData.getZadanieId();
        HttpStatusCode statusCode;
        try {
            statusCode = updateZadanie(updateData, taskId).getStatusCode();
            if(statusCode.is2xxSuccessful()){
                returnValue = "redirect:/task?pageNumber=1&pageSize=5";
            } 
        } catch(Exception e) {
            model.addAttribute("formUrl","/editTask?taskId="+taskId);
            model.addAttribute("msg", e.getLocalizedMessage());
            model.addAttribute("msgError", true);
        }
       
        return returnValue;
    }
    
    @GetMapping("/deleteTask")
    public String deleteTask(@RequestParam(value="taskId") Integer taskId, Model model) {
        String statusCode = deleteZadanie(taskId).getStatusCode().toString();
       
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/task?pageNumber=1&pageSize=5";
    }
    
    @PostMapping("/addTask")
    public String addTask(@Valid @ModelAttribute Zadanie saveData, Model model) {
        String returnValue = "task.html";
        
        try {
            createZadanie(saveData).getStatusCode().toString();
            returnValue = "redirect:/task?pageNumber=1&pageSize=5";
        } catch(Exception e) {
            model.addAttribute("formUrl","/addTask");
            model.addAttribute("msg", e.getLocalizedMessage());
            model.addAttribute("msgError", true);
        }
   
        return returnValue;
    }
    
    ResponseEntity<Void> createZadanie(@Valid @RequestBody Zadanie zadanie) {
        Zadanie createdZadanie = zadanieService.insert(zadanie);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{zadanieId}").buildAndExpand(createdZadanie.getZadanieId()).toUri();
        return ResponseEntity.created(location).build();
    }


    public ResponseEntity<Void> updateZadanie(@Valid @RequestBody Zadanie zadanie,
                                              @PathVariable Integer zadanieId) {
        return zadanieService.getZadanieById(zadanieId)
                .map(p -> {
                    zadanieService.updateZadanie(zadanieId, zadanie);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteZadanie(@PathVariable Integer zadanieId) {
        return zadanieService.getZadanieById(zadanieId).map(p -> {
            zadanieService.deleteZadanie(zadanieId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
