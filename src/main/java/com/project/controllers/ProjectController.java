package com.project.controllers;

import com.project.model.Project;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import com.project.services.ProjectService;

@RestController
@RequestMapping("/api/projekt")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{projektId}")
    ResponseEntity<Project> getProject(@PathVariable Integer projectId) {
        return ResponseEntity.of(projectService.getProjectById(projectId));
    }

    @PostMapping
    ResponseEntity<Void> createProject(@Valid @RequestBody Project project) {
        Project createdProject = projectService.insert(project);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{projektId}").buildAndExpand(createdProject.getProjectId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{projektId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateProject(@Valid @RequestBody Project project,
                                              @PathVariable Integer projectId) {
        return projectService.getProjectById(projectId)
                .map(p -> {
                    projectService.updateProject(projectId, project);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{projektId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer projectId) {
        return projectService.getProjectById(projectId).map(p -> {
            projectService.deleteProject(projectId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    Page<Project> getProjects(Pageable pageable) {
        return projectService.getProjects(pageable);
    }

    @GetMapping(params = "nazwa")
    Page<Project> getProjectsByName(@RequestParam String name, Pageable pageable) {
        return projectService.searchByName(name, pageable);
    }
}
