package com.project.services;

import com.project.model.Zadanie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ZadanieService {
    Page<Zadanie> getZadanies(Pageable pageable);
    
    Page<Zadanie> getPaginatedTasks(Integer pageNumber, Integer pageSize);

    Page<Zadanie> getZadaniaProjektu(Integer projektId, Integer pageNumber, Integer pageSize);
    
    Page<Zadanie> getZadaniaStudenta(Integer studentId, Integer pageNumber, Integer pageSize);

    Optional<Zadanie> getZadanieById(Integer id);

    Zadanie insert(Zadanie zadanie);

    void updateZadanie(Integer id, Zadanie zadanie);

    void deleteZadanie(Integer zadanieId);
    
    void removeAssignStudent(Integer stutendId, Pageable pageable);
    
    Page<Zadanie> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize);
}
