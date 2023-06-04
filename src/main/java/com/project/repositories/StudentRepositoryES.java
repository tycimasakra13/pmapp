package com.project.repositories;

import com.project.model.StudentES2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Repository
public interface StudentRepositoryES extends ElasticsearchRepository<StudentES2, Integer> {
    Optional<StudentES2> findByNrIndeksu(String nrIndeksu);

    Page<StudentES2> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);

    Page<StudentES2> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);
}
