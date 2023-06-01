package com.project.services;

import com.project.model.StudentES;
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
    StudentRepositoryES repository;
    ZadanieService zadanieService;

    public StudentServiceImplES(StudentRepositoryES studentRepository, ZadanieService zadanieService) {
        this.repository = studentRepository;
        this.zadanieService = zadanieService;
    }

    @Override
    public Page<StudentES> getStudents(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public Page<StudentES> getPaginatedStudents(Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public Page<StudentES> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable) {
        return repository.findByNrIndeksuStartsWith(nrIndeksu, pageable);
    }

    @Override
    public Page<StudentES> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findByNazwiskoStartsWithIgnoreCase(nazwisko, pageable);
    }

    @Override
    public Optional<StudentES> getStudentById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public StudentES insert(StudentES student) {
        return repository.save(student);
    }

    @Override
    @Transactional
    public void updateStudent(Integer id, StudentES student) {
        StudentES studentFromDb = repository.findById(id).get();

        studentFromDb.setEmail(student.getEmail());
        studentFromDb.setImie(student.getImie());
        studentFromDb.setNazwisko(student.getNazwisko());
        studentFromDb.setNrIndeksu(student.getNrIndeksu());
        studentFromDb.setStacjonarny(student.getStacjonarny());

        repository.save(studentFromDb);
    }

    @Override
    @Transactional
    public void deleteStudent(Integer studentId, Pageable pageable) {
        zadanieService.removeAssignStudent(studentId, pageable);
        repository.deleteById(studentId);
    }
    
    @Override
    public List<StudentES> getStudentsList() {
        return (List<StudentES>) repository.findAll();
    }
}
