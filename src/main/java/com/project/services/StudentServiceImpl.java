package com.project.services;

import com.project.model.Student;
import com.project.repositories.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    StudentRepository repository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    @Override
    public Page<Student> getStudents(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Student> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable) {
        return repository.findByNrIndeksuStartsWith(nrIndeksu, pageable);
    }

    @Override
    public Page<Student> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Pageable pageable) {
        return repository.findByNazwiskoStartsWithIgnoreCase(nazwisko, pageable);
    }

    @Override
    public Optional<Student> getStudentById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Student insert(Student student) {
        return repository.save(student);
    }

    @Override
    @Transactional
    public void updateStudent(Integer id, Student student) {
        Student studentFromDb = repository.findById(id).get();

        studentFromDb.setEmail(student.getEmail());
        studentFromDb.setImie(student.getImie());
        studentFromDb.setNazwisko(student.getNazwisko());
        studentFromDb.setNrIndeksu(student.getNrIndeksu());
        studentFromDb.setStacjonarny(student.getStacjonarny());

        repository.save(studentFromDb);
    }

    @Override
    @Transactional
    public void deleteStudent(Integer studentId) {
        repository.deleteById(studentId);
    }
}
