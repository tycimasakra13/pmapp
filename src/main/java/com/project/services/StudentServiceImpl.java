package com.project.services;

import com.project.model.Student;
import com.project.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    StudentRepository repository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    @Override
    public List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Student getStudentById(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    public Student insert(Student student) {
        return repository.save(student);
    }

    @Override
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
    public void deleteStudent(Integer studentId) {
        repository.deleteById(studentId);
    }
}
