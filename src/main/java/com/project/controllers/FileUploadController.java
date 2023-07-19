package com.project.controllers;

import com.project.model.File;
import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.payload.Response;
import com.project.services.FileStorageService;
import com.project.services.ProjektService;
import com.project.services.UserService;
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
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("")
public class FileUploadController {

    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ProjektService projektService;
    @Autowired
    private ZadanieService zadanieService;
    @Autowired
    private UserService userService;
    
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
                projektService.get().updateProjekt(projekt.get().getProjektId(), projekt.get(), false);
            }
        } else if (zadanieService.isPresent()) {
            type = "zadanie";
            Optional<Zadanie> zadanie = zadanieService.get().getZadanieById(id);
            if (zadanie.isEmpty()) {
                return ResponseEntity.badRequest().build();
            } else {
                entityId = zadanie.get().getZadanieId().toString();
                zadanie.get().getPliki().add(new File(file.getOriginalFilename()));
                zadanieService.get().updateZadanie(zadanie.get().getZadanieId(), zadanie.get(), false);
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
    
    
    @GetMapping("/files")
    public String getFilesList(@RequestParam("serviceType") String serviceType,
            @RequestParam(value="pageNumber") Integer pageNumber,
            @RequestParam(value="pageSize") Integer pageSize,
            @RequestParam("id") Integer id, Pageable pageable,
            Model model, Authentication authentication) {
        //final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        String path = fileStorageService.getDirectoryPath(serviceType, id);
        List<String> fileSet = fileStorageService.loadFiles(path);
 
        //Page<String> listFiles = new PageImpl<>(fileSet,PageRequest.of(pageNumber -1, pageSize),fileSet.size());
        PagedListHolder listFiles = new PagedListHolder(fileSet);
        listFiles.setPageSize(pageSize); // number of items per page
        listFiles.setPage(pageNumber - 1);      // set to first page
        Boolean showInfo = false;
        if (listFiles.getNrOfElements() == 0) {
            showInfo = true;
        }

        String msg = "Files not found.";
        String userRole = userService.getCurrentUserRole(authentication);
        model.addAttribute("formData", new File());
        model.addAttribute("userRole",userRole);
        model.addAttribute("files",listFiles);
        model.addAttribute("mode", "filesView");
        model.addAttribute("totalPages", listFiles.getPageCount());
        model.addAttribute("id", id);
        model.addAttribute("msgInfo", showInfo);
        model.addAttribute("msg", msg);
        model.addAttribute("serviceType",serviceType);

        return "file.html";
    }
    
    //add new project
    @GetMapping("/addFile")
    public String getAddFileForm(Model model, @RequestParam("id") Integer id,
            @RequestParam("serviceType") String serviceType) {
        model.addAttribute("id", id);
        model.addAttribute("mode","fileAdd");
        model.addAttribute("serviceType",serviceType);
        return "file.html";
    }
    
    @PostMapping("/addFile")
    public String uploadFiles(@RequestParam("file") MultipartFile[] files,
            @RequestParam("id") Integer id,
            @RequestParam("serviceType") String serviceType,
            Model model, Pageable pageable) {

        String statusCodeMsg = "";
        
        switch(serviceType) {
            case "project":
                uploadMultipletoProjekt(files, id).forEach((status)->{
                    final HttpStatusCode statusCode = status.getStatusCode();
                    if(statusCode.isError()) {
                        System.out.println(statusCode.toString());
                    }
                });
                break;
            case "task":
                uploadMultipletoZadanie(files, id).forEach((status) -> {
                    final HttpStatusCode statusCode = status.getStatusCode();
                    if(statusCode.isError()) {
                        System.out.println(statusCode.toString());
                    }
                });
                break;
            default:
                statusCodeMsg = "unknown service type";
                break;
        }
   
        model.addAttribute("statusMsg", statusCodeMsg);
        return "redirect:/files?id=" + id + "&serviceType=" + serviceType
                + "&pageNumber=1&pageSize=5";
    
    }
    
    @GetMapping("/deleteFile")
    public String deleteSelectedFiled(@RequestParam("id") Integer id,
            @RequestParam("serviceType") String serviceType,
            @RequestParam("fileName") String fileName) {
        
        String path = fileStorageService.getDirectoryPath(serviceType, id) + fileName;

        deleteSelectedFile(path);
        return "redirect:/files?id=" + id + "&serviceType=" + serviceType
                + "&pageNumber=1&pageSize=5";
    }
    
    public ResponseEntity<Response> uploadtoZadanie(@RequestParam("file") MultipartFile file, @PathVariable Integer zadanieId) {
        return this.upload(file, zadanieId, Optional.empty(), Optional.ofNullable(zadanieService));
    }

    public List<ResponseEntity<Response>> uploadMultipletoZadanie(@RequestParam("files") MultipartFile[] files, @PathVariable Integer zadanieId) {
        return Arrays.stream(files)
                .map(file -> this.uploadtoZadanie(file, zadanieId))
                .collect(Collectors.toList());
    }

    public ResponseEntity<Response> uploadtoProjekt(@RequestParam("file") MultipartFile file, @PathVariable Integer projektId) {
        return this.upload(file, projektId, Optional.ofNullable(projektService), Optional.empty());
    }

    public List<ResponseEntity<Response>> uploadMultipletoProjekt(@RequestParam("files") MultipartFile[] files, @PathVariable Integer projektId) {
        return Arrays.stream(files)
                .map(file -> this.uploadtoProjekt(file, projektId))
                .collect(Collectors.toList());
    }
    
    public void deleteSelectedFile(String path) {
        fileStorageService.deleteFile(path);
    }
    
    
}
