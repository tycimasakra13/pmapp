package com.project.controllers;

import com.project.model.User;
import com.project.model.Zadanie;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("")
public class ZadanieController {
    private final ZadanieService zadanieService;
 
    
    @Autowired
    public ZadanieController(ZadanieService zadanieService) {
        this.zadanieService = zadanieService;
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
    
    @GetMapping("/task")
    public String getPaginatedTasks(@ModelAttribute("loggedUser") User lUser, 
            @RequestParam(value="pageNumber") Integer pageNumber,
            @RequestParam(value="pageSize") Integer pageSize,
            Model model, Pageable pageable, User user) {
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        Page<Zadanie> totalTasks = zadanieService.getPaginatedTasks(pageNumber, pageSize);
        Integer totalPages = totalTasks.getTotalPages();

        String userRole = lUser.getRole();
        String userName = lUser.getUserName();
        model.addAttribute("formData", new Zadanie());
        model.addAttribute("tasks",totalTasks);
        model.addAttribute("mode","taskListViewPaginated");
        model.addAttribute("userRole", userRole);
        model.addAttribute("userName", userName);
        model.addAttribute("totalPages", totalPages);
   
        return "task.html";
    }
    
    @GetMapping("/addTask")
    public String getAddTaskForm(Model model) {
        model.addAttribute("saveData", new Zadanie());
        model.addAttribute("mode","taskAdd");
        return "task.html";
    }
    
    @GetMapping("/editTask")
    public String getEditTaskForm(@RequestParam(value="taskId") Integer taskId, Model model) {
        Zadanie selectedTask = zadanieService.getZadanieById(taskId).get();
     
        model.addAttribute("updateData", selectedTask);
        model.addAttribute("mode","taskEdit");
 
        return "task.html";
    }
    
    @PostMapping("/searchTask")
    public String searchTask(@Valid @ModelAttribute("formData") Zadanie formData,
                               Model model, Pageable pageable) {

        System.out.println("response " + formData.getNazwa());
        Page<Zadanie> totalTasks = zadanieService.getZadaniaProjektu(formData.getProjekt().getProjektId(), pageable);
        Integer totalPages = totalTasks.getTotalPages();
        
        model.addAttribute("formData", new Zadanie());
        model.addAttribute("tasks",totalTasks);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("mode","taskListViewPaginated");
        
        return "task.html";
    }
    
    @PostMapping("/updateTask")
    public String updateTask(@Valid @ModelAttribute Zadanie updateData, Model model, Pageable pageable) {
       
        String statusCode = updateZadanie(updateData, updateData.getZadanieId()).getStatusCode().toString();
                                          
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "task.html";
    }
    
    @GetMapping("/deleteTask")
    public String deleteTask(@RequestParam(value="taskId") Integer taskId, Model model) {
        String statusCode = deleteZadanie(taskId).getStatusCode().toString();
       
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "task.html";
    }
    
    @PostMapping("/addTask")
    public String addTask(@Valid @ModelAttribute Zadanie saveData, Model model, Pageable pageable) {
        
        String statusCode = createZadanie(saveData).getStatusCode().toString();
   
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "task.html";
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
