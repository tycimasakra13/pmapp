package com.project.services;

import com.project.model.Projekt;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjektService {
    Page<Projekt> getProjekts(Pageable pageable);
            
    Page<Projekt> getPaginatedProjects(Integer pageNumber, Integer pageSize);
    
    Optional<Projekt> getProjektById(Integer id);

    Projekt insert(Projekt projekt);

    void updateProjekt(Integer id, Projekt projekt);

    void deleteProjekt(Integer projektId);

    Page<Projekt> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize);
    
    List<Map<Integer, String>> getProjectsForSelect();
}
