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
            int count = 10;
            for(int x = 1; x <= count; x++) {
                insertNewProject(x);
            }
        }
    }
    
    private void insertNewProject(int x) {
        Zadanie zadanie = new Zadanie();
        zadanie.setZadanieId(x);
        Projekt projekt = new Projekt();
        projekt.setOpis("ABC " + x);
        projekt.setNazwa("CDA " + x);
        projekt.setZadania(List.of(zadanie));
        projektRepository.save(projekt);
        System.out.println("Sample Projekts Loaded " + x);   
    }
}
