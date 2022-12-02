package com.project.services;

import com.project.model.Projekt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProjektService {
    Page<Projekt> getProjekts(Pageable pageable);

    Optional<Projekt> getProjektById(Integer id);

    Projekt insert(Projekt projekt);

    void updateProjekt(Integer id, Projekt projekt);

    void deleteProjekt(Integer projektId);

    Page<Projekt> searchByNazwa(String nazwa, Pageable pageable);
}
