package com.project.controllers;

import com.project.model.Student;
import com.project.model.User;
import com.project.services.StudentService;
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
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
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
            pageSize = 10;
        }
        
        return pageSize;
    }
    
    @GetMapping("/")
    public String getStudents(Model model, Pageable pageable) {
        model.addAttribute("students", studentService.getStudents(pageable));
        return "student.html";
    }
    
    @GetMapping("/student")
    public String getPaginatedStudents( 
            @RequestParam(value="pageNumber") Integer pageNumber,
            @RequestParam(value="pageSize") Integer pageSize,
            Model model, Pageable pageable, User user) {
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        Page<Student> allStudents = studentService.getPaginatedStudents(pageNumber, pageSize);
        Integer totalPages = allStudents.getTotalPages();


        model.addAttribute("formData", new Student());
        model.addAttribute("students",allStudents);
        model.addAttribute("mode","studentListViewPaginated");
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
   
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "student.html";
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
                               Model model, Pageable pageable) {
        
        Integer pageNumber = setPageNumber(0);
        Integer pageSize = setPageSize(0);
        
        System.out.println("response " + formData.getNazwisko());
        Page<Student> totalStudents = studentService.getStudentByNazwiskoStartsWithIgnoreCase(formData.getNazwisko(), pageNumber, pageSize);
        Integer totalPages = totalStudents.getTotalPages();
        
        model.addAttribute("formData", new Student());
        model.addAttribute("students",totalStudents);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("mode","studentListViewPaginated");
        
        return "student.html";
    }
    
    @PostMapping("/updateStudent")
    public String updateStudent(@Valid @ModelAttribute Student updateData, Model model, Pageable pageable) {
        
        ResponseEntity<Void> updateStud = studentService.getStudentById(updateData.getStudentId())
                .map(p -> {
                    studentService.updateStudent(updateData.getStudentId(), updateData);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
        String statusCode = updateStud.getStatusCode().toString();
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "student.html";
    }
    
    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam(value="studentId") Integer studentId, Model model) {
        ResponseEntity<Void> deleteStud = studentService.getStudentById(studentId).map(p -> {
            studentService.deleteStudent(studentId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
        
        
        String statusCode = deleteStud.getStatusCode().toString();
        System.out.println(statusCode);
        model.addAttribute("statusMsg", statusCode);
        return "student.html";
    }

    ResponseEntity<Void> createStudent(@Valid @RequestBody Student student) {
        Student createdStudent = studentService.insert(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{studentId}").buildAndExpand(createdStudent.getStudentId()).toUri();
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
