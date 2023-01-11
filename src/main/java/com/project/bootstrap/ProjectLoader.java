package com.project.bootstrap;

import com.project.model.Project;
import com.project.model.Task;
import com.project.repositories.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectLoader implements CommandLineRunner {
    public final ProjectRepository projectRepository;

    public ProjectLoader(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadProjects();
    }

    private void loadProjects() {
        if (projectRepository.count() == 0) {
            Task task = new Task();
            task.setTaskId(1);
            Project project = new Project();
            project.setDescription("ABC");
            project.setName("CDA");
            project.setTasks(List.of(task));
            projectRepository.save(project);
            System.out.println("Sample Projects Loaded");
        }
    }
}
