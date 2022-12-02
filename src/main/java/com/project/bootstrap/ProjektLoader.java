package com.project.bootstrap;

import com.project.model.Projekt;
import com.project.repositories.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            Projekt projekt = new Projekt();
            projekt.setOpis("ABC");
            projekt.setNazwa("CDA");
            projektRepository.save(projekt);
            System.out.println("Sample Projekts Loaded");
        }
    }
}
