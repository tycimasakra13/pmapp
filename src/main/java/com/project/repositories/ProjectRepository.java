package com.project.repositories;

import com.project.model.Projekt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Projekt, Integer> {
}
