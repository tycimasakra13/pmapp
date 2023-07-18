package com.project.bootstrap;

import com.project.model.Projekt;
import com.project.model.Student;
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
        //if (zadanieRepository.count() == 0) {
            int count = 10;
            for(int x = 1; x <= count; x++) {
                if(!zadanieRepository.existsByZadanieId(count)) {
                    insertNewStudents(x);
                }
            }
        //}
        

    }
    
    private void insertNewStudents(int x) {
        Projekt projekt = new Projekt();
        projekt.setProjektId(10);

        Student student = new Student();
        student.setId(12);

        Zadanie zadanie = new Zadanie();
        zadanie.setOpis("ABC" + x);
        zadanie.setNazwa("CDA" + x);
        zadanie.setSynced(false);
        zadanie.setToBeDeleted(false);
        zadanie.setProjekt(projekt);
        zadanie.setStudent(student);
        zadanieRepository.save(zadanie);
        System.out.println("Sample Zadanies Loaded " + x);
    }
}
