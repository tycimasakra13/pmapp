package com.project.services;

import com.project.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import com.project.repositories.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
    TaskRepository repository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.repository = taskRepository;
    }

    @Override
    public Page<Task> getTasks(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Task> getProjectTasks(Integer projectId, Pageable pageable) {
        return repository.findProjectTasks(projectId, pageable);
    }

    @Override
    public Optional<Task> getTaskById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Task insert(Task task) {
        return repository.save(task);
    }

    @Override
    @Transactional
    public void updateTask(Integer id, Task task) {
        Task taskFromDb = repository.findById(id).get();

        taskFromDb.setName(task.getName());
        taskFromDb.setDescription(task.getDescription());
        taskFromDb.setOrderBy(task.getOrderBy());

        repository.save(taskFromDb);
    }

    @Override
    @Transactional
    public void deleteTask(Integer taskId) {
        repository.deleteById(taskId);
    }
}
