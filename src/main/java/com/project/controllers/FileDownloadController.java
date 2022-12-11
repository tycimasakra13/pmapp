package com.project.controllers;

import com.project.model.File;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.services.FileStorageService;
import com.project.services.ProjektService;
import com.project.services.ZadanieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileDownloadController {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ProjektService projektService;
    @Autowired
    private ZadanieService zadanieService;

    public ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

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

    @GetMapping("/zadanie/{zadanieId}/{fileName:.+}")
    public ResponseEntity<Resource> getZadanieFile(@PathVariable String fileName, @PathVariable Integer zadanieId, HttpServletRequest request) {
        Optional<Zadanie> zadanie = zadanieService.getZadanieById(zadanieId);
        if (zadanie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        File fl = zadanie.get().getPliki().stream().filter(f -> f.getName().equals(fileName)).findAny().orElse(null);

        if (fl != null) {
            return downloadFile(fl.getName(), request);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/projekt/{projektId}/{fileName:.+}")
    public ResponseEntity<Resource> getProjektFile(@PathVariable String fileName, @PathVariable Integer projektId, HttpServletRequest request) {
        Optional<Projekt> projekt = projektService.getProjektById(projektId);
        if (projekt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        File fl = projekt.get().getPliki().stream().filter(f -> f.getName().equals(fileName)).findAny().orElse(null);

        if (fl != null) {
            return downloadFile(fl.getName(), request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
