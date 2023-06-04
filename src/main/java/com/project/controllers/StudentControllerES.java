package com.project.controllers;

import com.project.model.StudentES2;
import com.project.repositories.StudentRepositoryES;
import com.project.services.StudentServiceES;
import com.project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("")
public class StudentControllerES {

    private final StudentServiceES studentServiceES;
    public final StudentRepositoryES studentRepositoryES;
    public Random rd = new Random();
    
    @Autowired
    public StudentControllerES(StudentServiceES studentServiceES, StudentRepositoryES studentRepositoryES) {
        this.studentServiceES = studentServiceES;
        this.studentRepositoryES = studentRepositoryES;
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
    
    private void loadStudents() throws IOException {
        
        //Long size = studentRepository.count();
        //System.out.println("Sample Students Loaded size " + size); 
       // if (size == 0) {
            int count = 10;
            for(int x = 1; x <= count; x++) {
                insertNewStudents(x);
            }
        //}
        //else System.out.println("nnot first");
        
    }
    
    private void insertNewStudents(int x) throws IOException {
        int bound = x + 5;
        StudentES2 student = new StudentES2();
        //student.setId(x);
        student.setImie("Test" + x);
        student.setNazwisko("TestS " + x);
        student.setEmail("test" + x + "@test.pl");
        student.setNrIndeksu("" + rd.nextInt(bound) + rd.nextInt(bound) + rd.nextInt(bound));
        student.setStacjonarny(rd.nextBoolean());
        studentRepositoryES.save(student);
        System.out.println("Sample Students Loaded " + x);   
    }
    
    @GetMapping("/preloadStudent")
    public String preloadPro() throws IOException {
        long size = studentRepositoryES.count();
        if (size == 0) loadStudents();
        else System.out.println("not first");
        return "index";
    }
    
    @GetMapping("/studentES")
    public String getPaginatedStudents( 
            @RequestParam(required = false, value="pageNumber") Integer pageNumber,
            @RequestParam(required = false, value="pageSize") Integer pageSize,
            @RequestParam(required = false, value="studentId") Integer studentId,
            Model model, Authentication authentication) {
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        
        StudentES2 selectedStudent = null;// = new Optional<Student>();
        Page<StudentES2> allStudents = null;// = studentService.getPaginatedStudents(pageNumber, pageSize);
        Integer totalPages = 1;//allStudents.getTotalPages();
  
        String userRole = userService.getCurrentUserRole(authentication);
        
        if(studentId != null) {
            selectedStudent = studentServiceES.getStudentById(studentId).get();
        } else {
//            allStudents = studentService.getPaginatedStudents(pageNumber, pageSize);
           // totalPages = allStudents.getTotalPages();
           final Pageable pageable = PageRequest.of(pageNumber, pageSize);
           allStudents = studentRepositoryES.findAll(pageable);
           //model.addAttribute("listProductDocuments",.searchAllDocuments());
           System.out.println("total students" + allStudents.get().count());
        }
        model.addAttribute("formData", new StudentES2());
        model.addAttribute("students",allStudents == null ? selectedStudent : allStudents);
        model.addAttribute("mode","studentListViewPaginated");
        model.addAttribute("userRole",userRole);
        model.addAttribute("totalPages", totalPages);
   
        return "student.html";
    }
    
    @GetMapping("/addStudentES")
    public String getAddStudentForm(Model model) {
        model.addAttribute("saveData", new StudentES2());
        model.addAttribute("mode","studentAdd");
        return "student.html";
    }
    
    @PostMapping("/addStudentES")
    public String addStudent(@Valid @ModelAttribute("saveData") StudentES2 saveData, Model model, Pageable pageable) {
        
        String statusCode = createStudent(saveData).getStatusCode().toString();
   
        model.addAttribute("statusMsg", statusCode);

        return "redirect:/student?pageNumber=1&pageSize=5";
    }
    
    @GetMapping("/editStudentES")
    public String getEditStudentForm(@RequestParam(value="studentId") Integer studentId, Model model) {
        StudentES2 selectedStudent = studentServiceES.getStudentById(studentId).get();

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
    
    @PostMapping("/studentES")
    public String searchStudent(@Valid @ModelAttribute("formData") StudentES2 formData,
                               Model model, Authentication authentication) {
        
        Integer pageNumber = 1;//setPageNumber(1);
        Integer pageSize = 5;//setPageSize(0);
        
        Page<StudentES2> totalStudents = studentServiceES.getStudentByNazwiskoStartsWithIgnoreCase(formData.getNazwisko(), pageNumber, pageSize);
        Integer totalPages = totalStudents.getTotalPages();
        String userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new StudentES2());
        model.addAttribute("students",totalStudents);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("userRole",userRole);
        model.addAttribute("mode","studentListViewPaginated");
        
        return "student.html";
    }
    
    @PostMapping("/updateStudentES")
    public String updateStudent(@Valid @ModelAttribute StudentES2 updateData, Model model, Pageable pageable) {
        
        ResponseEntity<Void> updateStud = studentServiceES.getStudentById(updateData.getId())
                .map(p -> {
                    studentServiceES.updateStudent(updateData.getId(), updateData);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
        String statusCode = updateStud.getStatusCode().toString();
        
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/student?pageNumber=1&pageSize=5";
    }
    
    @GetMapping("/deleteStudentES")
    public String deleteStudent(@RequestParam(value="studentId") Integer studentId, Model model, Pageable pageable) {
        ResponseEntity<Void> deleteStud = studentServiceES.getStudentById(studentId).map(p -> {
            studentServiceES.deleteStudent(studentId, pageable);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
        
        
        String statusCode = deleteStud.getStatusCode().toString();
        model.addAttribute("statusMsg", statusCode);
        return "redirect:/student?pageNumber=1&pageSize=5";
    }

    ResponseEntity<Void> createStudent(@Valid @RequestBody StudentES2 student) {
        StudentES2 createdStudent = studentServiceES.insert(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{studentId}").buildAndExpand(createdStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{studentId}ES")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStudent(@Valid @RequestBody StudentES2 student,
                                              @PathVariable Integer studentId) {
        return studentServiceES.getStudentById(studentId)
                .map(p -> {
                    studentServiceES.updateStudent(studentId, student);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}