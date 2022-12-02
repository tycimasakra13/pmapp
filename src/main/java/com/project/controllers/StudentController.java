package com.project.controllers;

import com.project.model.Student;
import com.project.services.StudentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //The function receives a GET request, processes it and gives back a list of Student as a response.
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    //The function receives a GET request, processes it, and gives back a list of Student as a response.
    @GetMapping({"/{studentId}"})
    public ResponseEntity<Student> getStudent(@PathVariable Integer studentId) {
        return new ResponseEntity<>(studentService.getStudentById(studentId), HttpStatus.OK);
    }

    //The function receives a POST request, processes it, creates a new Student and saves it to the database, and returns a resource link to the created student.
    @PostMapping
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        Student student1 = studentService.insert(student);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("student", "/api/student/" + student1.getStudentId().toString());
        return new ResponseEntity<>(student1, httpHeaders, HttpStatus.CREATED);
    }

    //The function receives a PUT request, updates the Student with the specified Id and returns the updated Student
    @PutMapping({"/{studentId}"})
    public ResponseEntity<Student> updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        studentService.updateStudent(studentId, student);
        return new ResponseEntity<>(studentService.getStudentById(studentId), HttpStatus.OK);
    }

    //The function receives a DELETE request, deletes the Student with the specified Id.
    @DeleteMapping({"/{studentId}"})
    public ResponseEntity<Student> deleteStudent(@PathVariable("studentId") Integer studentId) {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
