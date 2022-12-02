package com.project.services;

import com.project.model.Projekt;

import java.util.List;

public interface ProjektService {
    List<Projekt> getProjekts();

    Projekt getProjektById(Integer id);

    Projekt insert(Projekt projekt);

    void updateProjekt(Integer id, Projekt projekt);

    void deleteProjekt(Integer projektId);
}
