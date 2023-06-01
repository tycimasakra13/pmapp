package com.project.services;

import com.project.model.StudentES;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentServiceES {
    Page<StudentES> getStudents(Pageable pageable);
    
    Page<StudentES> getPaginatedStudents(Integer pageNumber, Integer pageSize);

    Page<StudentES> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable);

    Page<StudentES> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Integer pageNumber, Integer pageSize);

    Optional<StudentES> getStudentById(Integer id);

    StudentES insert(StudentES student);

    void updateStudent(Integer id, StudentES student);

    void deleteStudent(Integer studentId, Pageable pageable);
    
    List<StudentES> getStudentsList();
}
