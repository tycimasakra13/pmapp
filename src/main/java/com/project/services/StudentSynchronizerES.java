package com.project.services;

import com.project.dao.StudentDao;
import com.project.mapper.StudentMapper;
import com.project.model.Student;
import com.project.repositories.StudentRepository;
import com.project.repositories.elastic.StudentRepositoryES;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service
public class StudentSynchronizerES {
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private StudentRepositoryES studentRepositoryES;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentDao studentDao;
    
    @Autowired
    private StudentMapper studentMapper;
    
    private Boolean toBeInserted = false;
    private Boolean toBeDeleted = false;
    

    
    @Transactional
    public void synchronizeData() {
        List<Student> entitiesInsertedUpdated;
        List<Student> entitiesToBeDeleted;
        
        entitiesInsertedUpdated = studentRepository.findBySyncedFalse();
        entitiesToBeDeleted = studentRepository.findBytoBeDeletedTrue();
       
        entitiesInsertedUpdated.forEach(student -> {
            try {
                Page<Student> totalStudents = studentService.search(
                        student.getImie(), 0, 5);
                
                if( totalStudents.getNumberOfElements() == 0 ) {
                    toBeInserted = true;
                    
                }
                student.setSynced(true);
                
                if (toBeInserted) {
                    studentDao.save(student);
                } else {
                    studentDao.update(student);
                }
                
                studentRepository.save(student);
  
            } catch (IOException e) {
                System.out.println("synchronize data error " + e);
            }
        });
        
        entitiesToBeDeleted.forEach(student -> {
            try {
                Integer studentId = student.getId();
                Integer status = studentService.getStudentById(studentId).map(s -> {
                    studentService.deleteStudent(studentId, PageRequest.of(0, 5));
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }).orElseGet(() -> ResponseEntity.notFound().build()).getStatusCode().value();
                
                studentDao.delete(studentId);
                
  
            } catch (IOException e) {
                System.out.println("delete data error " + e);
            }
        });
    }
}
//    private ProjectRepository projectRepository;
//    private ProjectRepositoryES projectRepositoryES;
//    private MapStructMapper mapStructMapper;
//    
//    private static final Logger LOG = LoggerFactory.getLogger(ElasticSynchronizer.class);
//    
//    @Autowired
//    public ElasticSynchronizer(ProjectRepository projectRepository, ProjectRepositoryES projectRepositoryES, MapStructMapper mapStructMapper) {
//        this.projectRepository = projectRepository;
//        this.projectRepositoryES = projectRepositoryES;
//        this.mapStructMapper = mapStructMapper;
//    }
//    
//    @Scheduled(fixedDelay = 1000)
//    public void scheduleFixedDelayTask() {
//        System.out.println(
//          "Fixed delay task - " + System.currentTimeMillis() / 1000);
//    }
//
//    @Scheduled(cron = "0 */1 * * * *")
//    @Transactional
//    public void sync() {
//        System.out.println("Start Syncing = " + LocalDateTime.now());
//        this.syncProjects();
//        System.out.println("End Syncing = " + LocalDateTime.now());
//    }
//    
//    private void syncProjects() {
//
//        Specification<Projekt> projektSpecification = (root, criteriaQuery, criteriaBuilder) -> getModificationDatePredicate(criteriaBuilder, root);
//        List<Projekt> projektList;
//        if (projectRepositoryES.count() == 0) {
//            projektList = projectRepository.findAll();
//        } else {
//            projektList = projectRepository.findAll(projektSpecification);
//        }
//        for(Projekt projekt: projektList) {
//            LOG.info("Syncing Projekt - {}", projekt.getProjektId());
//            projectRepositoryES.save(this.mapStructMapper.projektEsToProjekt(projekt));
//        }
//    }
//    
//    private static Predicate getModificationDatePredicate(CriteriaBuilder cb, Root<?> root) {
//       System.out.println("interval: " + Constants.INTERVAL_IN_MILLISECONDE);
//       System.out.println("mdate: " + Constants.MODIFICATION_DATE);
//        Expression<Timestamp> currentTime;
//        currentTime = cb.currentTimestamp();
//        Expression<Timestamp> currentTimeMinus = cb.literal(
//                new Timestamp(System.currentTimeMillis() -
//                (Constants.INTERVAL_IN_MILLISECONDE)));
//        return cb.between(root.<Date>get(Constants.MODIFICATION_DATE),
//                currentTimeMinus,
//                currentTime
//        );
//    }
//}
