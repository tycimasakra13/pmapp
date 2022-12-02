package com.project.bootstrap;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.repositories.ZadanieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ZadanieLoader implements CommandLineRunner {
    public final ZadanieRepository zadanieRepository;

    public ZadanieLoader(ZadanieRepository zadanieRepository) {
        this.zadanieRepository = zadanieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadZadanies();
    }

    private void loadZadanies() {
        if (zadanieRepository.count() == 0) {
            Projekt projekt = new Projekt();
            projekt.setProjektId(1);

            Zadanie zadanie = new Zadanie();
            zadanie.setOpis("ABC");
            zadanie.setNazwa("CDA");
            zadanie.setKolejnosc(1);
            zadanie.setProjekt(projekt);
            zadanieRepository.save(zadanie);
            System.out.println("Sample Zadanies Loaded");
        }
    }
}
