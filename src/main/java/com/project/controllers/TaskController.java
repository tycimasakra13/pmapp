package com.project.controllers;

import com.project.model.Task;
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
import com.project.services.TaskService;

@RestController
@RequestMapping("/api/zadanie")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{zadanieId}")
    ResponseEntity<Task> getTask(@PathVariable Integer taskId) {
        return ResponseEntity.of(taskService.getTaskById(taskId));
    }

    @PostMapping
    ResponseEntity<Void> createTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.insert(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{zadanieId}").buildAndExpand(createdTask.getTaskId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{zadanieId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateTask(@Valid @RequestBody Task task,
                                              @PathVariable Integer taskId) {
        return taskService.getTaskById(taskId)
                .map(p -> {
                    taskService.updateTask(taskId, task);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{zadanieId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        return taskService.getTaskById(taskId).map(p -> {
            taskService.deleteTask(taskId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    Page<Task> getTasks(Pageable pageable) {
        return taskService.getTasks(pageable);
    }

    @GetMapping(params = "projektId")
    Page<Task> getProjectTasks(@RequestParam Integer projectId, Pageable pageable) {
        return taskService.getProjectTasks(projectId, pageable);
    }
}
