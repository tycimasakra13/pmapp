package com.project.repositories.elastic;

import com.project.model.elastic.StudentES;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Repository
public interface StudentRepositoryES extends ElasticsearchRepository<StudentES, Integer> {
    Optional<StudentES> findByNrIndeksu(String nrIndeksu);

    Page<StudentES> findByNrIndeksuStartsWith(String nrIndeksu, Pageable pageable);

    Page<StudentES> findByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable);
}
