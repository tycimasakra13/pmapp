package com.project.services;

import com.project.model.Zadanie;
import com.project.repositories.ZadanieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZadanieServiceImpl implements ZadanieService {
    ZadanieRepository repository;

    public ZadanieServiceImpl(ZadanieRepository zadanieRepository) {
        this.repository = zadanieRepository;
    }

    @Override
    public List<Zadanie> getZadanies() {
        List<Zadanie> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Zadanie getZadanieById(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    public Zadanie insert(Zadanie zadanie) {
        return repository.save(zadanie);
    }

    @Override
    public void updateZadanie(Integer id, Zadanie zadanie) {
        Zadanie zadanieFromDb = repository.findById(id).get();

        zadanieFromDb.setNazwa(zadanie.getNazwa());
        zadanieFromDb.setOpis(zadanie.getOpis());
        zadanieFromDb.setKolejnosc(zadanie.getKolejnosc());

        repository.save(zadanieFromDb);
    }

    @Override
    public void deleteZadanie(Integer zadanieId) {
        repository.deleteById(zadanieId);
    }
}
