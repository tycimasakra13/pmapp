package com.project.services;

import com.project.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjectService {
    Page<Project> getProjects(Pageable pageable);

    Optional<Project> getProjectById(Integer id);

    Project insert(Project projekt);

    void updateProject(Integer id, Project projekt);

    void deleteProject(Integer projektId);

    Page<Project> searchByName(String nazwa, Pageable pageable);
}
