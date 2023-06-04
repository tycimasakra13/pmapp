package com.project.controllers;

import com.project.model.Student;
import com.project.services.StudentService;
import com.project.services.UserService;
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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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
            pageSize = 5;
        }
        
        return pageSize;
    }
    
//    @GetMapping("/")
//    public String getStudents(Model model, Pageable pageable) {
//        model.addAttribute("students", studentService.getStudents(pageable));
//        return "student.html";
//    }
    
    @GetMapping("/student")
    public String getPaginatedStudents( 
            @RequestParam(required = false, value="pageNumber") Integer pageNumber,
            @RequestParam(required = false, value="pageSize") Integer pageSize,
            @RequestParam(required = false, value="studentId") Integer studentId,
            Model model, Authentication authentication) {
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        
        Student selectedStudent = null;// = new Optional<Student>();
        Page<Student> allStudents = null;// = studentService.getPaginatedStudents(pageNumber, pageSize);
        Integer totalPages = 1;//allStudents.getTotalPages();
  
        String userRole = userService.getCurrentUserRole(authentication);
        
        if(studentId != null) {
            selectedStudent = studentService.getStudentById(studentId).get();
        } else {
            allStudents = studentService.getPaginatedStudents(pageNumber, pageSize);
            totalPages = allStudents.getTotalPages();
        }
        model.addAttribute("formData", new Student());
        model.addAttribute("students",allStudents == null ? selectedStudent : allStudents);
        model.addAttribute("mode","studentListViewPaginated");
        model.addAttribute("userRole",userRole);
        model.addAttribute("totalPages", totalPages);
   
        return "student.html";
    }
    
    @GetMapping("/addStudent")
    public String getAddStudentForm(Model model) {
        model.addAttribute("saveData", new Student());
        model.addAttribute("mode","studentAdd");
        return "student.html";
    }
    
    @PostMapping("/addStudent")
    public String addStudent(@Valid @ModelAttribute("saveData") Student saveData, Model model, Pageable pageable) {
        
        String statusCode = createStudent(saveData).getStatusCode().toString();
   
        model.addAttribute("statusMsg", statusCode);

        return "redirect:/student?pageNumber=1&pageSize=5";
    }
    
    @GetMapping("/editStudent")
    public String getEditStudentForm(@RequestParam(value="studentId") Integer studentId, Model model) {
        Student selectedStudent = studentService.getStudentById(studentId).get();

        String name = selectedStudent.getImie();
        String sName = selectedStudent.getNazwisko();
        String email = selectedStudent.getEmail();
        String indexNumber = selectedStudent.getNrIndeksu();
        
        model.addAttribute("updateData", selectedStudent);
        model.addAttribute("mode","studentEdit");
        model.addAttribute("name", name);
        model.addAttribute("sName", sName);
        model.addAttribute("email", email);
        model.addAttribute("indexNumber", indexNumber);
 
        return "student.html";
    }
    
    @PostMapping("/student")
    public String searchStudent(@Valid @ModelAttribute("formData") Student formData,
                               Model model, Authentication authentication) {
        
        Integer pageNumber = 1;//setPageNumber(1);
        Integer pageSize = 5;//setPageSize(0);
        
        Page<Student> totalStudents = studentService.getStudentByNazwiskoStartsWithIgnoreCase(formData.getNazwisko(), pageNumber, pageSize);
        Integer totalPages = totalStudents.getTotalPages();
        String userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new Student());
        model.addAttribute("students",totalStudents);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("userRole",userRole);
        model.addAttribute("mode","studentListViewPaginated");
        
        return "student.html";
    }
    
    @PostMapping("/updateStudent")
    public String updateStudent(@Valid @ModelAttribute Student updateData, Model model, Pageable pageable) {
        
        ResponseEntity<Void> updateStud = studentService.getStudentById(updateData.getId())
                .map(p -> {
                    studentService.updateStudent(updateData.getId(), updateData);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
        String statusCode = updateStud.getStatusCode().toString();
        
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/student?pageNumber=1&pageSize=5";
    }
    
    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam(value="studentId") Integer studentId, Model model, Pageable pageable) {
        ResponseEntity<Void> deleteStud = studentService.getStudentById(studentId).map(p -> {
            studentService.deleteStudent(studentId, pageable);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
        
        
        String statusCode = deleteStud.getStatusCode().toString();
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/student?pageNumber=1&pageSize=5";
    }

    ResponseEntity<Void> createStudent(@Valid @RequestBody Student student) {
        Student createdStudent = studentService.insert(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{studentId}").buildAndExpand(createdStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStudent(@Valid @RequestBody Student student,
                                              @PathVariable Integer studentId) {
        return studentService.getStudentById(studentId)
                .map(p -> {
                    studentService.updateStudent(studentId, student);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
