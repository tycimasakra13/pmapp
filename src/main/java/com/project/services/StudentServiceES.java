package com.project.services;

import com.project.model.StudentES2;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentServiceES {
    Page<StudentES2> getStudents(Pageable pageable);
    
    Page<StudentES2> getPaginatedStudents(Integer pageNumber, Integer pageSize);

    Page<StudentES2> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable);

    Page<StudentES2> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Integer pageNumber, Integer pageSize);

    Optional<StudentES2> getStudentById(Integer id);

    StudentES2 insert(StudentES2 student);

    void updateStudent(Integer id, StudentES2 student);

    void deleteStudent(Integer studentId, Pageable pageable);
    
    List<StudentES2> getStudentsList();
}
