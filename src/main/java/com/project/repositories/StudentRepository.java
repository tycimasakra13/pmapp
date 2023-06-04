package com.project.repositories;

import com.project.model.Student;
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
}
