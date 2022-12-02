package com.project.services;

import com.project.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();

    Student getStudentById(Integer id);

    Student insert(Student student);

    void updateStudent(Integer id, Student student);

    void deleteStudent(Integer studentId);
}
