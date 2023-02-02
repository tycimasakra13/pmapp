package com.project.controllers;

import com.project.model.Projekt;
import com.project.model.Student;
import com.project.model.User;
import com.project.model.Zadanie;
import com.project.services.ProjektService;
import com.project.services.StudentService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("")
public class ZadanieController {
    private final ZadanieService zadanieService;
    private final ProjektService projektService;
    private final StudentService studentService;
  
    @Autowired
    public ZadanieController(ZadanieService zadanieService, 
            ProjektService projektService,
            StudentService studentService) {
        this.zadanieService = zadanieService;
        this.projektService = projektService;
        this.studentService = studentService;
    }
    
    private Integer setPageNumber(Integer pageNumber) {
        if(pageNumber == null || pageNumber == 0) {
            pageNumber = 1;
        }
        
        return pageNumber;
    }
    
    private Integer setPageSize(Integer pageSize) {
        if(pageSize == null || pageSize == 0){
            pageSize = 3;
        }
        
        return pageSize;
    }
    
    private Map<Integer, String> projectsForSelect(Pageable pageable) { 
        Map<Integer, String> projectsLists = new HashMap<>();
        Page<Projekt> proj = projektService.getProjekts(pageable);
        proj.forEach((pr) -> {
            projectsLists.put(pr.getProjektId(),pr.getNazwa());
        });
        
        return projectsLists;
    }
    
    private Map<Integer, String> studentsForSelect(Pageable pageable) {
        
        Map<Integer, String> studentsLists = new HashMap<>();
        Page<Student> students = studentService.getStudents(pageable);
        students.forEach((student) -> {
            studentsLists.put(student.getStudentId(), student.getImie() + " " + student.getNazwisko());
        });
        
        return studentsLists;
    }

    @GetMapping("/task")
    public String getPaginatedTasks(@ModelAttribute("loggedUser") User lUser, 
            @RequestParam(required = false, value="pageNumber") Integer pageNumber,
            @RequestParam(required = false, value="pageSize") Integer pageSize,
            @RequestParam(required = false, value="studentId") Integer studentId,
            @RequestParam(required = false, value="projectID") Integer projectID,
            Model model, Pageable pageable, User user) {
        
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        System.out.println("pageNumber" + pageNumber);
        System.out.println("pageSize" + pageSize);
        
        Page<Zadanie> tasksList = null;//zadanieService.getPaginatedTasks(pageNumber, pageSize);
        
        System.out.println("studentId" + studentId);
        if(studentId != null) {
            tasksList = zadanieService.getZadaniaStudenta(studentId, pageNumber, pageSize);
        } else if(projectID != null) { 
            tasksList = zadanieService.getZadaniaProjektu(projectID, pageNumber, pageSize);
        } else {
            tasksList = zadanieService.getPaginatedTasks(pageNumber, pageSize);
        }
        
        Integer totalPages = tasksList.getTotalPages();
        
        String userRole = lUser.getRole();
        String userName = lUser.getUserName();
        model.addAttribute("formData", new Zadanie());
        model.addAttribute("tasks",tasksList);
        model.addAttribute("mode","taskListViewPaginated");
        model.addAttribute("userRole", userRole);
        model.addAttribute("userName", userName);
        model.addAttribute("totalPages", totalPages);
   
        return "task.html";
    }
    /*
    @GetMapping("/student")
    public String getPaginatedStudents( 
        
  \\
        model.addAttribute("formData", new Student());
        model.addAttribute("students",allStudents == null ? selectedStudent : allStudents);
        model.addAttribute("mode","studentListViewPaginated");
        model.addAttribute("totalPages", totalPages);
   
        return "student.html";
    }*/
    
    @PostMapping("/task")
    public String searchTask(@Valid @ModelAttribute("formData") Zadanie formData,
                               Model model, Pageable pageable) {

        System.out.println("response " + formData.getNazwa());
        Page<Zadanie> totalTasks = zadanieService.searchByNazwa(formData.getNazwa(), 1, 2);
        Integer totalPages = totalTasks.getTotalPages();
        
        model.addAttribute("formData", new Zadanie());
        model.addAttribute("tasks",totalTasks);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("mode","taskListViewPaginated");
        
        return "task.html";
    }
    
    @GetMapping("/addTask")
    public String getAddTaskForm(Model model, Pageable pageable) {
        Zadanie task = new Zadanie();

        model.addAttribute("saveData", task);
        model.addAttribute("projects", projectsForSelect(pageable));
        model.addAttribute("students", studentsForSelect(pageable));
        model.addAttribute("mode","taskAdd");
        return "task.html";
    }
    
    @GetMapping("/editTask")
    public String getEditTaskForm(@RequestParam(value="taskId") Integer taskId, Model model, Pageable pageable) {
        Zadanie selectedTask = zadanieService.getZadanieById(taskId).get();
        
        model.addAttribute("updateData", selectedTask);
        model.addAttribute("projects", projectsForSelect(pageable));
        model.addAttribute("students", studentsForSelect(pageable));
        model.addAttribute("mode","taskEdit");
 
        return "task.html";
    }
    
    
    @PostMapping("/updateTask")
    public String updateTask(@Valid @ModelAttribute Zadanie updateData, Model model, Pageable pageable) {
        String returnValue = "task.html";
        Integer taskId = updateData.getZadanieId();
        HttpStatusCode statusCode;
        try {
            statusCode = updateZadanie(updateData, taskId).getStatusCode();
            if(statusCode.is2xxSuccessful()){
                returnValue = "redirect:/task?pageNumber=1&pageSize=2";
            } 
        } catch(Exception e) {
            System.out.println("e: " + e.getLocalizedMessage());
            model.addAttribute("formUrl","/editTask?taskId="+taskId);
            model.addAttribute("msg", e.getLocalizedMessage());
            model.addAttribute("msgError", true);
        }
       
        return returnValue;
    }
    
    @GetMapping("/deleteTask")
    public String deleteTask(@RequestParam(value="taskId") Integer taskId, Model model) {
        String statusCode = deleteZadanie(taskId).getStatusCode().toString();
       
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/task?pageNumber=1&pageSize=2";
    }
    
    @PostMapping("/addTask")
    public String addTask(@Valid @ModelAttribute Zadanie saveData, Model model, Pageable pageable) {
        String returnValue = "task.html";
        
        try {
            createZadanie(saveData).getStatusCode().toString();
            returnValue = "redirect:/task?pageNumber=1&pageSize=2";
        } catch(Exception e) {
            System.out.println("e: " + e.getLocalizedMessage());
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
