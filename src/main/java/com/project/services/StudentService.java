package com.project.services;

import com.project.model.Student;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentService {
    Page<Student> getStudents(Pageable pageable);
    
    Page<Student> getPaginatedStudents(Integer pageNumber, Integer pageSize);

    Page<Student> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable);

    Page<Student> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Integer pageNumber, Integer pageSize);

    Optional<Student> getStudentById(Integer id);

    Student insert(Student student);

    void updateStudent(Integer id, Student student, Boolean toBeDeleted);
    
    void deleteStudent(Integer studentId, Pageable pageable);
    
    List<Student> getStudentsList();
    
    Page<Student> search(String q, Integer from, Integer size) throws IOException;
}
