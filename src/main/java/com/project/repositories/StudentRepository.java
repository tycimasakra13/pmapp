package com.project.repositories;

import com.project.model.Student;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByNrIndeksu(String nrIndeksu);

    Page<Student> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);

    Page<Student> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);

    Optional<Student> findTopByOrderByIdDesc();
    
    List<Student> findByModificationDateBefore(LocalDateTime timestamp);
    
    List<Student> findBySyncedFalse();
    
    List<Student> findBytoBeDeletedTrue();
}
