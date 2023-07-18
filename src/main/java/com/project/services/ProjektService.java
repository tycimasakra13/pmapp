package com.project.services;

import com.project.model.Projekt;
import com.project.model.dto.ProjektDto;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjektService {
    Page<Projekt> getProjekts(Pageable pageable);
            
    Page<Projekt> getPaginatedProjects(Integer pageNumber, Integer pageSize);
    
    Optional<Projekt> getProjektById(Integer id);
    
    Page<Projekt> getProjektByIdPaginated(Integer projektId, Pageable pageable);

    Projekt insert(Projekt projekt);
    //ProjektDto insert(ProjektDto projektDto);

    void updateProjekt(Integer id, Projekt projekt, Boolean toBeDeleted);

    void deleteProjekt(Integer projektId);

    Page<Projekt> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize);
    
    List<String> getDocId(Integer projektId) throws IOException;
    
    Page<Projekt> search(String q, Integer from, Integer size) throws IOException;
    
    List<Projekt> getProjectsList();
}
