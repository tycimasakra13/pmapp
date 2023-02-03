package com.project.services;

import com.project.config.FileStorageProperties;
import com.project.exceptions.FileNotFoundException;
import com.project.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
    public Path getStorageLocationPath() {
        return fileStorageLocation;
    }
    
    public String getDirectoryPath(String serviceType, Integer id) {
        String pathType = "project".equals(serviceType) ? "/projekt/" : "/zadanie/";
        String path = getStorageLocationPath() + pathType + id + "/";
        return path;
    }

    public String storeFile(MultipartFile file, String path) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Create directories if they don't exist already
            Files.createDirectories(this.fileStorageLocation.resolve(path));
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(path).resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteFile(String path) {
        try {
            Files.delete(this.fileStorageLocation.resolve(path));
        } catch (IOException ex) {
            throw new FileNotFoundException("File " + path + " not found");
        }
    }

    public Resource loadFileAsResource(String pathStr, String fileName) {
        try {
            Path path = Paths.get(pathStr);
            Path filePath = path.resolve(fileName).normalize();
            //Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
    
    
    public List<String> loadFiles(String path) {
        List<String> filesList = new ArrayList<>();
        try {
            Path filePath = this.fileStorageLocation.resolve(path).normalize();
            if(Files.exists(filePath)) {
                Files.list(filePath).forEach((file) -> {
                    filesList.add(file.getFileName().toString());
                });
            }

            return filesList;
            
        } catch (IOException ex) {
            throw new FileNotFoundException("File not found " + path, ex);
        }
    }
    
}
