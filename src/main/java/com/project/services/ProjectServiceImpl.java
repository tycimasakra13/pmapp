package com.project.services;

import com.project.model.Project;
import com.project.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    public ProjectRepository repository;
    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.repository = projectRepository;
    }

    @Override
    public Page<Project> getProjects(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Project> getProjectById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Project insert(Project project) {
        return repository.save(project);
    }

    @Override
    @Transactional
    public void updateProject(Integer id, Project project) {
        Project projectFromDb = repository.findById(id).get();

        projectFromDb.setName(project.getName());
        projectFromDb.setDescription(project.getDescription());

        repository.save(projectFromDb);
    }

    @Override
    @Transactional
    public void deleteProject(Integer projectId) {
        repository.deleteById(projectId);
    }

    @Override
    public Page<Project> searchByName(String name, Pageable pageable) {
        return repository.findByNameCaseIgnore(name, pageable);
    }
}
