package com.project.services;

import com.project.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskService {
    Page<Task> getTasks(Pageable pageable);

    Page<Task> getProjectTasks(Integer projektId, Pageable pageable);

    Optional<Task> getTaskById(Integer id);

    Task insert(Task task);

    void updateTask(Integer id, Task task);

    void deleteTask(Integer taskId);
}
