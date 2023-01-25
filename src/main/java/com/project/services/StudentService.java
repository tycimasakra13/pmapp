package com.project.services;

import com.project.model.Projekt;
import com.project.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentService {
    Page<Student> getStudents(Pageable pageable);
    
    Page<Student> getPaginatedStudents(Integer pageNumber, Integer pageSize);

    Page<Student> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable);

    Page<Student> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);

    Optional<Student> getStudentById(Integer id);

    Student insert(Student student);

    void updateStudent(Integer id, Student student);

    void deleteStudent(Integer studentId);
}
