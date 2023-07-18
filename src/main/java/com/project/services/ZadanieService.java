package com.project.services;

import com.project.model.Zadanie;
import java.io.IOException;
import java.util.List;
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

    void updateZadanie(Integer id, Zadanie zadanie, Boolean toBeDeleted);

    void deleteZadanie(Integer zadanieId);
    
    void removeAssignStudent(Integer stutendId, Pageable pageable);
    
    Page<Zadanie> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize);

    List<String> getDocId(Integer projektId) throws IOException;
    
    Page<Zadanie> search(String q, Integer from, Integer size) throws IOException;
}
