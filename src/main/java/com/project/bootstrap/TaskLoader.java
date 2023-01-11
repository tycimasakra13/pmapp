package com.project.bootstrap;

import com.project.model.Project;
import com.project.model.Task;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.project.repositories.TaskRepository;

@Component
public class TaskLoader implements CommandLineRunner {
    public final TaskRepository taskRepository;

    public TaskLoader(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadTasks();
    }

    private void loadTasks() {
        if (taskRepository.count() == 0) {
            Project project = new Project();
            project.setProjectId(1);

            Task task = new Task();
            task.setDescription("ABC");
            task.setName("CDA");
            task.setOrderBy(1);
            task.setProject(project);
            taskRepository.save(task);
            System.out.println("Sample Tasks Loaded");
        }
    }
}
