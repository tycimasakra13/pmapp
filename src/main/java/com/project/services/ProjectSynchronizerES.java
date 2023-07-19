package com.project.services;

import com.project.dao.ProjectDao;
import com.project.mapper.ProjectMapper;
import com.project.model.Projekt;
import com.project.repositories.ProjectRepository;
import com.project.repository.elastic.ProjectRepositoryES;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
@Service
public class ProjectSynchronizerES {
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ProjectRepositoryES projectRepositoryES;
    
    @Autowired
    private ProjektService projektService;
    
    @Autowired
    private ProjectDao projectDao;
    
    @Autowired
    private ProjectMapper projectMapper;
    
    private Boolean toBeInserted = false;
    private Boolean toBeDeleted = false;
    

    
    @Transactional
    public void synchronizeData() {
        List<Projekt> entitiesInsertedUpdated;
        List<Projekt> entitiesToBeDeleted;
        
        entitiesInsertedUpdated = projectRepository.findBySyncedFalse();
        entitiesToBeDeleted = projectRepository.findBytoBeDeletedTrue();
       
        entitiesInsertedUpdated.forEach(projekt -> {
            try {
                List<String> searchedDocId = projektService.getDocId(projekt.getProjektId());
                
                if( searchedDocId.isEmpty() ) {
                    toBeInserted = true;
                }
                
                projekt.setSynced(true);
                
                if (toBeInserted) {
                    toBeInserted = false;
                    projectDao.save(projekt);
                    
                } else {
                    projectDao.update(projekt, searchedDocId.get(0));
                }
                
                projectRepository.save(projekt);
  
            } catch (IOException e) {
                System.out.println("synchronize data error " + e);
            }
        });
        
        entitiesToBeDeleted.forEach(projekt -> {
            try {
                Integer projektId = projekt.getProjektId();
                Integer status = projektService.getProjektById(projektId).map(p -> {
                    projektService.deleteProjekt(projektId);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                }).orElseGet(() -> ResponseEntity.notFound().build()).getStatusCode().value();
                
                projectDao.delete(projektId);
                
  
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
