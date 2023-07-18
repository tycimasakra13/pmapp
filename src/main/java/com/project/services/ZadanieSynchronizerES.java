package com.project.services;

import com.project.dao.StudentDao;
import com.project.dao.ZadanieDao;
import com.project.mapper.StudentMapper;
import com.project.mapper.ZadanieMapper;
import com.project.model.Student;
import com.project.model.Zadanie;
import com.project.repositories.StudentRepository;
import com.project.repositories.ZadanieRepository;
import com.project.repositories.elastic.StudentRepositoryES;
import com.project.repository.elastic.ZadanieRepositoryES;

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
public class ZadanieSynchronizerES {
    @Autowired
    private ZadanieRepository zadanieRepository;
    
    @Autowired
    private ZadanieRepositoryES zadanieRepositoryES;
    
    @Autowired
    private ZadanieService zadanieService;
    
    @Autowired
    private ZadanieDao zadanieDao;
    
    @Autowired
    private ZadanieMapper zadanieMapper;
    
    private Boolean toBeInserted = false;
    private Boolean toBeDeleted = false;
    

    
    @Transactional
    public void synchronizeData() {
        List<Zadanie> entitiesInsertedUpdated;
        List<Zadanie> entitiesToBeDeleted;
        
        entitiesInsertedUpdated = zadanieRepository.findBySyncedFalse();
        entitiesToBeDeleted = zadanieRepository.findBytoBeDeletedTrue();
       
        entitiesInsertedUpdated.forEach(zadanie -> {
            try {
                System.out.println("zadanieID: " + zadanie.getZadanieId());
                
                List<String> searchedDocId = zadanieService.getDocId(zadanie.getZadanieId());
                
                System.out.println("searchedProject: " + searchedDocId.size());
                System.out.println("tobeinserted " + toBeInserted);
                
                
                if( searchedDocId.size() == 0 ) {
                    toBeInserted = true;
                }
                
                zadanie.setSynced(true);
                
                if (toBeInserted) {
                    System.out.println("in save");
                    toBeInserted = false;
                    zadanieDao.save(zadanie);
                } else {
                    System.out.println("in update");
                    zadanieDao.update(zadanie, searchedDocId.get(0));
                }
                
                zadanieRepository.save(zadanie);
  
            } catch (IOException e) {
                System.out.println("synchronize data error " + e);
            }
        });
        
        entitiesToBeDeleted.forEach(zadanie -> {
            try {
                System.out.println("to be deleted zadanie: " + zadanie.getZadanieId());
                Integer zadanieId = zadanie.getZadanieId();
                Integer status = zadanieService.getZadanieById(zadanieId).map(s -> {
                    zadanieService.deleteZadanie(zadanieId);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }).orElseGet(() -> ResponseEntity.notFound().build()).getStatusCode().value();
                
                System.out.println("status " + status);
                zadanieDao.delete(zadanieId);
                
  
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
