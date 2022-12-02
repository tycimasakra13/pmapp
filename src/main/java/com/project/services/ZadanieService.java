package com.project.services;

import com.project.model.Zadanie;

import java.util.List;

public interface ZadanieService {
    List<Zadanie> getZadanies();

    Zadanie getZadanieById(Integer id);

    Zadanie insert(Zadanie zadanie);

    void updateZadanie(Integer id, Zadanie zadanie);

    void deleteZadanie(Integer zadanieId);
}
