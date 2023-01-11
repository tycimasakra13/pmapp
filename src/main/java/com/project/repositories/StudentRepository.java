package com.project.repositories;

import com.project.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByIndexNumber(String indexNumber);

    Page<Student> findByIndexNumberStartsWith(String indexNumber, Pageable pageable);

    Page<Student> findBySNameCaseIgnore(String sName, Pageable pageable);
}
