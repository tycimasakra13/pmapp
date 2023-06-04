package com.project.services;

import com.project.model.StudentES2;
import com.project.repositories.StudentRepositoryES;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;

@Service
public class StudentServiceImplES implements StudentServiceES {
    StudentRepositoryES repositoryES;
    ZadanieService zadanieService;

    public StudentServiceImplES(StudentRepositoryES studentRepository, ZadanieService zadanieService) {
        this.repositoryES = studentRepository;
        this.zadanieService = zadanieService;
    }

    @Override
    public Page<StudentES2> getStudents(Pageable pageable) {
        return repositoryES.findAll(pageable);
    }
    
    @Override
    public Page<StudentES2> getPaginatedStudents(Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repositoryES.findAll(pageable);
    }

    @Override
    public Page<StudentES2> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable) {
        return repositoryES.findByNrIndeksuStartsWith(nrIndeksu, pageable);
    }

    @Override
    public Page<StudentES2> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repositoryES.findByNazwiskoStartsWithIgnoreCase(nazwisko, pageable);
    }

    @Override
    public Optional<StudentES2> getStudentById(Integer id) {
        return repositoryES.findById(id);
    }

    @Override
    public StudentES2 insert(StudentES2 student) {
        return repositoryES.save(student);
    }

    @Override
    @Transactional
    public void updateStudent(Integer id, StudentES2 student) {
        StudentES2 studentFromDb = repositoryES.findById(id).get();

        studentFromDb.setEmail(student.getEmail());
        studentFromDb.setImie(student.getImie());
        studentFromDb.setNazwisko(student.getNazwisko());
        studentFromDb.setNrIndeksu(student.getNrIndeksu());
        studentFromDb.setStacjonarny(student.getStacjonarny());

        repositoryES.save(studentFromDb);
    }

    @Override
    @Transactional
    public void deleteStudent(Integer studentId, Pageable pageable) {
        zadanieService.removeAssignStudent(studentId, pageable);
        repositoryES.deleteById(studentId);
    }
    
    @Override
    public List<StudentES2> getStudentsList() {
        return (List<StudentES2>) repositoryES.findAll();
    }
}
