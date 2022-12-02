package com.project.repositories;

import com.project.model.Zadanie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZadanieRepository extends CrudRepository<Zadanie, Integer> {
}
