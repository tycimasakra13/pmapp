package com.project.services;

import com.project.model.Zadanie;
import com.project.repositories.ZadanieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;

@Service
public class ZadanieServiceImpl implements ZadanieService {
    ZadanieRepository repository;

    public ZadanieServiceImpl(ZadanieRepository zadanieRepository) {
        this.repository = zadanieRepository;
    }

    @Override
    public Page<Zadanie> getZadanies(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public Page<Zadanie> getPaginatedTasks(Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public Page<Zadanie> getZadaniaProjektu(Integer projektId, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findZadaniaProjektu(projektId, pageable);
    }
    
    @Override
    public Page<Zadanie> getZadaniaStudenta(Integer studentId, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findZadaniaStudenta(studentId, pageable);
    }

    @Override
    public Optional<Zadanie> getZadanieById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Zadanie insert(Zadanie zadanie) {
        return repository.save(zadanie);
    }

    @Override
    @Transactional
    public void updateZadanie(Integer id, Zadanie zadanie) {
        Zadanie zadanieFromDb = repository.findById(id).get();

        zadanieFromDb.setNazwa(zadanie.getNazwa());
        zadanieFromDb.setOpis(zadanie.getOpis());
        zadanieFromDb.setKolejnosc(zadanie.getKolejnosc());
        zadanieFromDb.setProjekt(zadanie.getProjekt());
        zadanieFromDb.setStudent(zadanie.getStudent());

        repository.save(zadanieFromDb);
    }

    @Override
    @Transactional
    public void deleteZadanie(Integer zadanieId) {
        this.removeAssignedProject(zadanieId);
        repository.deleteById(zadanieId);
    }
    
    @Override
    @Transactional
    public void removeAssignStudent(Integer studentId, Pageable pageable) {
        //repository.deleteById(stutendId);
        Page<Zadanie> zadanieFromDb = repository.findZadaniaStudenta(studentId, pageable);
        zadanieFromDb.getContent().forEach((zadanie) -> {
            zadanie.setStudent(null);
            repository.save(zadanie);
        });
    }
    
    @Transactional
    public void removeAssignedProject(Integer zadanieId) {
        //repository.deleteById(stutendId);
        Zadanie zadanie = this.getZadanieById(zadanieId).get();
        zadanie.setProjekt(null);
        repository.save(zadanie);
    }
    
    @Override
    public Page<Zadanie> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }
}
