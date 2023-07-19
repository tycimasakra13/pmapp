package com.project.services;

import com.project.dao.StudentDao;
import com.project.model.Student;
import com.project.repositories.StudentRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;

@Service
public class StudentServiceImpl implements StudentService {
    StudentRepository repository;
    ZadanieService zadanieService;
    private final StudentDao studentDao;

    public StudentServiceImpl(StudentRepository studentRepository, ZadanieService zadanieService, StudentDao studentDao) {
        this.repository = studentRepository;
        this.zadanieService = zadanieService;
        this.studentDao = studentDao;
    }

    @Override
    public Page<Student> getStudents(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public Page<Student> getPaginatedStudents(Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public Page<Student> getStudentByNrIndeksu(String nrIndeksu, Pageable pageable) {
        return repository.findByNrIndeksuStartsWith(nrIndeksu, pageable);
    }

    @Override
    public Page<Student> getStudentByNazwiskoStartsWithIgnoreCase(String nazwisko, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
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
    public void updateStudent(Integer id, Student student, Boolean toBeDeleted) {
        Student studentFromDb = repository.findById(id).get();
        
        studentFromDb.setToBeDeleted(toBeDeleted);
        
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
    public List<Student> getStudentsList() {
        return repository.findAll();
    }
    
    @Override
    public Page<Student> search(String q, Integer from, Integer size) throws IOException {
        QueryBuilder query;
        q = q.toLowerCase();
        
        if (q.isEmpty()) {
            query = QueryBuilders.matchAllQuery();
        } else {

            query = QueryBuilders.multiMatchQuery(q)
                    //.field("id")
                    .field("imie")
                    .field("nazwisko")
                    .field("nrIndeksu")
                    .field("email");

                
        }
        
        Page<Student> returnedStudent = studentDao.search(query, from, size, PageRequest.of(from, size));
        
        return returnedStudent;
    }
}
