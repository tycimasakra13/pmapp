package com.project.controllers;

import com.project.model.File;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.payload.Response;
import com.project.services.FileStorageService;
import com.project.services.ProjektService;
import com.project.services.ZadanieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ProjektService projektService;
    @Autowired
    private ZadanieService zadanieService;

    private ResponseEntity<Response> upload(MultipartFile file, Integer id, Optional<ProjektService> projektService, Optional<ZadanieService> zadanieService) {
        String type = "";
        String entityId = "";
        if (projektService.isPresent()) {
            type = "projekt";
            Optional<Projekt> projekt = projektService.get().getProjektById(id);
            if (projekt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            } else {
                entityId = projekt.get().getProjektId().toString();
                projekt.get().getPliki().add(new File(file.getOriginalFilename()));
                projektService.get().updateProjekt(projekt.get().getProjektId(), projekt.get());
            }
        } else if (zadanieService.isPresent()) {
            type = "zadanie";
            Optional<Zadanie> zadanie = zadanieService.get().getZadanieById(id);
            if (zadanie.isEmpty()) {
                return ResponseEntity.badRequest().build();
            } else {
                entityId = zadanie.get().getZadanieId().toString();
                zadanie.get().getPliki().add(new File(file.getOriginalFilename()));
                zadanieService.get().updateZadanie(zadanie.get().getZadanieId(), zadanie.get());
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
    public ResponseEntity<Response> uploadtoZadanie(@RequestParam("file") MultipartFile file, @PathVariable Integer zadanieId) {
        return this.upload(file, zadanieId, Optional.empty(), Optional.ofNullable(zadanieService));
    }

    @PostMapping("/zadanie/uploadMultipleFiles/{zadanieId}")
    public List<ResponseEntity<Response>> uploadMultipletoZadanie(@RequestParam("files") MultipartFile[] files, @PathVariable Integer zadanieId) {
        return Arrays.stream(files)
                .map(file -> this.uploadtoZadanie(file, zadanieId))
                .collect(Collectors.toList());
    }

    @PostMapping("/projekt/uploadFile/{projektId}")
    public ResponseEntity<Response> uploadtoProjekt(@RequestParam("file") MultipartFile file, @PathVariable Integer projektId) {
        return this.upload(file, projektId, Optional.ofNullable(projektService), Optional.empty());
    }

    @PostMapping("/projekt/uploadMultipleFiles/{projektId}")
    public List<ResponseEntity<Response>> uploadMultipletoProjekt(@RequestParam("files") MultipartFile[] files, @PathVariable Integer projektId) {
        return Arrays.stream(files)
                .map(file -> this.uploadtoProjekt(file, projektId))
                .collect(Collectors.toList());
    }
}
