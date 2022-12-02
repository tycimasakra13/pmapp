package com.project.bootstrap;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.repositories.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjektLoader implements CommandLineRunner {
    public final ProjectRepository projektRepository;

    public ProjektLoader(ProjectRepository projektRepository) {
        this.projektRepository = projektRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadProjekts();
    }

    private void loadProjekts() {
        if (projektRepository.count() == 0) {
            Zadanie zadanie = new Zadanie();
            zadanie.setZadanieId(1);
            Projekt projekt = new Projekt();
            projekt.setOpis("ABC");
            projekt.setNazwa("CDA");
            projekt.setZadania(List.of(zadanie));
            projektRepository.save(projekt);
            System.out.println("Sample Projekts Loaded");
        }
    }
}
