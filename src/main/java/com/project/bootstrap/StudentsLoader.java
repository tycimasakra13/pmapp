package com.project.bootstrap;

import com.project.model.Student;
import com.project.repositories.StudentRepository;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StudentsLoader implements CommandLineRunner {
    public final StudentRepository studentRepository;
    public Random rd = new Random();

    public StudentsLoader(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        loadStudents();
    }
    
    
    private void loadStudents() {
        if (studentRepository.count() == 0) {
            int count = 10;
            for(int x = 1; x <= count; x++) {
                insertNewStudents(x);
            }
        }
    }
    
    private void insertNewStudents(int x) {
        int bound = x + 5;
        Boolean stacjonarny = rd.nextBoolean();
        String nrIndeksu = "ABC " + + rd.nextInt(bound) + rd.nextInt(bound) + rd.nextInt(bound);
        System.out.println("Sample Students stacjonarny " + stacjonarny);   
        Student student = new Student();
        student.setImie("Test" + x);
        student.setNazwisko("TestS " + x);
        student.setEmail("test" + x + "@test.pl");
        student.setNrIndeksu(nrIndeksu);
        student.setStacjonarny(stacjonarny);
        student.setSynced(false);
        student.setToBeDeleted(false);
        studentRepository.save(student);
        System.out.println("Sample Students Loaded " + x);   
    }
}
