package com.project.repositories;

import com.project.model.Zadanie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZadanieRepository extends JpaRepository<Zadanie, Integer> {
    @Query("SELECT z FROM Zadanie z WHERE z.projekt.projektId = :projektId")
    Page<Zadanie> findZadaniaProjektu(@Param("projektId") Integer projektId, Pageable pageable);

    @Query("SELECT z FROM Zadanie z WHERE z.projekt.projektId = :projektId")
    List<Zadanie> findZadaniaProjektu(@Param("projektId") Integer projektId);
    
    @Query("SELECT z FROM Zadanie z WHERE z.student.id = :studentId")
    Page<Zadanie> findZadaniaStudenta(@Param("studentId") Integer studentId, Pageable pageable);
    
    Page<Zadanie> findByNazwaContainingIgnoreCase(String nazwa, Pageable pageable);

    List<Zadanie> findBySyncedFalse();
    
    List<Zadanie> findBytoBeDeletedTrue();
    
    Boolean existsByZadanieId(Integer zadanieId);
}
