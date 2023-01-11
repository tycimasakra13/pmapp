package com.project.controllers;

import com.project.model.File;
import com.project.model.Project;
import com.project.model.Task;
import com.project.payload.Response;
import com.project.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.project.services.TaskService;
import com.project.services.ProjectService;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;

    private ResponseEntity<Response> upload(MultipartFile file, Integer id, Optional<ProjectService> projectService, Optional<TaskService> taskService) {
        String type = "";
        String entityId = "";
        if (projectService.isPresent()) {
            type = "projekt";
            Optional<Project> project = projectService.get().getProjectById(id);
            if (project.isEmpty()) {
                return ResponseEntity.badRequest().build();
            } else {
                entityId = project.get().getProjectId().toString();
                project.get().getFiles().add(new File(file.getOriginalFilename()));
                projectService.get().updateProject(project.get().getProjectId(), project.get());
            }
        } else if (taskService.isPresent()) {
            type = "zadanie";
            Optional<Task> task = taskService.get().getTaskById(id);
            if (task.isEmpty()) {
                return ResponseEntity.badRequest().build();
            } else {
                entityId = task.get().getTaskId().toString();
                task.get().getFiles().add(new File(file.getOriginalFilename()));
                taskService.get().updateTask(task.get().getTaskId(), task.get());
            }
        } else {
            throw new RuntimeException("Cannot create file unassigned to project or task");
        }

        String path = type + "/" + entityId + "/";
        String fileName = fileStorageService.storeFile(file, path);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .path(fileName)
                .toUriString();

        return ResponseEntity.ok().body(new Response(fileName, fileDownloadUri, file.getContentType(), file.getSize()));
    }

    @PostMapping("/zadanie/uploadFile/{zadanieId}")
    public ResponseEntity<Response> uploadToTask(@RequestParam("file") MultipartFile file, @PathVariable Integer taskId) {
        return this.upload(file, taskId, Optional.empty(), Optional.ofNullable(taskService));
    }

    @PostMapping("/zadanie/uploadMultipleFiles/{zadanieId}")
    public List<ResponseEntity<Response>> uploadMultipleToTask(@RequestParam("files") MultipartFile[] files, @PathVariable Integer taskId) {
        return Arrays.stream(files)
                .map(file -> this.uploadToTask(file, taskId))
                .collect(Collectors.toList());
    }

    @PostMapping("/projekt/uploadFile/{projektId}")
    public ResponseEntity<Response> uploadToProject(@RequestParam("file") MultipartFile file, @PathVariable Integer projectId) {
        return this.upload(file, projectId, Optional.ofNullable(projectService), Optional.empty());
    }

    @PostMapping("/projekt/uploadMultipleFiles/{projektId}")
    public List<ResponseEntity<Response>> uploadMultipleToProject(@RequestParam("files") MultipartFile[] files, @PathVariable Integer projectId) {
        return Arrays.stream(files)
                .map(file -> this.uploadToProject(file, projectId))
                .collect(Collectors.toList());
    }
}
