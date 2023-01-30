package com.project.services;

import com.project.model.Zadanie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ZadanieService {
    Page<Zadanie> getZadanies(Pageable pageable);
    
    Page<Zadanie> getPaginatedTasks(Integer pageNumber, Integer pageSize);

    Page<Zadanie> getZadaniaProjektu(Integer projektId, Pageable pageable);

    Optional<Zadanie> getZadanieById(Integer id);

    Zadanie insert(Zadanie zadanie);

    void updateZadanie(Integer id, Zadanie zadanie);

    void deleteZadanie(Integer zadanieId);
}
