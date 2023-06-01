package com.project.controllers;

import com.project.model.StudentES;
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

    private final StudentServiceES studentService;
    public final StudentRepositoryES studentRepository;
    public Random rd = new Random();
    
    @Autowired
    public StudentControllerES(StudentServiceES studentService, StudentRepositoryES studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
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
        StudentES student = new StudentES();
        //student.setId(x);
        student.setImie("Test" + x);
        student.setNazwisko("TestS " + x);
        student.setEmail("test" + x + "@test.pl");
        student.setNrIndeksu("" + rd.nextInt(bound) + rd.nextInt(bound) + rd.nextInt(bound));
        student.setStacjonarny(rd.nextBoolean());
        studentRepository.save(student);
        System.out.println("Sample Students Loaded " + x);   
    }
    
    @GetMapping("/preloadStudent")
    public String preloadPro() throws IOException {
        long size = studentRepository.count();
        if (size == 0) loadStudents();
        else System.out.println("not first");
        return "index";
    }
    
    @GetMapping("/student")
    public String getPaginatedStudents( 
            @RequestParam(required = false, value="pageNumber") Integer pageNumber,
            @RequestParam(required = false, value="pageSize") Integer pageSize,
            @RequestParam(required = false, value="studentId") Integer studentId,
            Model model, Authentication authentication) {
        pageNumber = setPageNumber(pageNumber);
        pageSize = setPageSize(pageSize);
        
        StudentES selectedStudent = null;// = new Optional<Student>();
        Page<StudentES> allStudents = null;// = studentService.getPaginatedStudents(pageNumber, pageSize);
        Integer totalPages = 1;//allStudents.getTotalPages();
  
        String userRole = userService.getCurrentUserRole(authentication);
        
        if(studentId != null) {
            selectedStudent = studentService.getStudentById(studentId).get();
        } else {
//            allStudents = studentService.getPaginatedStudents(pageNumber, pageSize);
           // totalPages = allStudents.getTotalPages();
           final Pageable pageable = PageRequest.of(pageNumber, pageSize);
           allStudents = studentRepository.findAll(pageable);
           //model.addAttribute("listProductDocuments",.searchAllDocuments());
           System.out.println("total students" + allStudents.get().count());
        }
        model.addAttribute("formData", new StudentES());
        model.addAttribute("students",allStudents == null ? selectedStudent : allStudents);
        model.addAttribute("mode","studentListViewPaginated");
        model.addAttribute("userRole",userRole);
        model.addAttribute("totalPages", totalPages);
   
        return "student.html";
    }
    
    @GetMapping("/addStudent")
    public String getAddStudentForm(Model model) {
        model.addAttribute("saveData", new StudentES());
        model.addAttribute("mode","studentAdd");
        return "student.html";
    }
    
    @PostMapping("/addStudent")
    public String addStudent(@Valid @ModelAttribute("saveData") StudentES saveData, Model model, Pageable pageable) {
        
        String statusCode = createStudent(saveData).getStatusCode().toString();
   
        model.addAttribute("statusMsg", statusCode);

        return "redirect:/student?pageNumber=1&pageSize=5";
    }
    
    @GetMapping("/editStudent")
    public String getEditStudentForm(@RequestParam(value="studentId") Integer studentId, Model model) {
        StudentES selectedStudent = studentService.getStudentById(studentId).get();

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
    public String searchStudent(@Valid @ModelAttribute("formData") StudentES formData,
                               Model model, Authentication authentication) {
        
        Integer pageNumber = 1;//setPageNumber(1);
        Integer pageSize = 5;//setPageSize(0);
        
        Page<StudentES> totalStudents = studentService.getStudentByNazwiskoStartsWithIgnoreCase(formData.getNazwisko(), pageNumber, pageSize);
        Integer totalPages = totalStudents.getTotalPages();
        String userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new StudentES());
        model.addAttribute("students",totalStudents);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("userRole",userRole);
        model.addAttribute("mode","studentListViewPaginated");
        
        return "student.html";
    }
    
    @PostMapping("/updateStudent")
    public String updateStudent(@Valid @ModelAttribute StudentES updateData, Model model, Pageable pageable) {
        
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

    ResponseEntity<Void> createStudent(@Valid @RequestBody StudentES student) {
        StudentES createdStudent = studentService.insert(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{studentId}").buildAndExpand(createdStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStudent(@Valid @RequestBody StudentES student,
                                              @PathVariable Integer studentId) {
        return studentService.getStudentById(studentId)
                .map(p -> {
                    studentService.updateStudent(studentId, student);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}