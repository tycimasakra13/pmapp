package com.project.controllers;

import com.project.services.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class FileDownloadController {
    @Autowired
    private FileStorageService fileStorageService;

    public ResponseEntity<Resource> downloadFile(String path, String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(path, fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            return ResponseEntity.noContent().build();
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping("/downloadFile")
    public ResponseEntity<Resource> downloadSelectedFile(@RequestParam("fileName") String fileName,
            @RequestParam("id") Integer id, 
            @RequestParam("serviceType") String serviceType,
            HttpServletRequest request) {

        String path = fileStorageService.getDirectoryPath(serviceType, id);
        
        if (path != null) {
            return downloadFile(path, fileName, request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
