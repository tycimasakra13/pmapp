package com.project.services;

import com.project.model.Zadanie;
import com.project.repositories.ZadanieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Page<Zadanie> getZadaniaProjektu(Integer projektId, Pageable pageable) {
        return repository.findZadaniaProjektu(projektId, pageable);
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

        repository.save(zadanieFromDb);
    }

    @Override
    @Transactional
    public void deleteZadanie(Integer zadanieId) {
        repository.deleteById(zadanieId);
    }
}
