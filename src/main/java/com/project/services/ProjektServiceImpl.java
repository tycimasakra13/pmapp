package com.project.services;

import com.project.model.Projekt;
import com.project.repositories.ProjectRepository;
import com.project.dao.ProjectDao;
import com.project.mapper.ProjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.LoggerFactory;

@Service
public class ProjektServiceImpl implements ProjektService {
    public ProjectRepository repository;
    private ProjectMapper projectMapper;
    private final ProjectDao projectDao;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ProjectDao.class);
    
    @Autowired
    public ProjektServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, ProjectDao projectDao) {
        this.repository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectDao = projectDao;
    }

    @Override
    public Page<Projekt> getProjekts(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public Page<Projekt> getPaginatedProjects(Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findAll(pageable);

    }
    
    @Override
    public Optional<Projekt> getProjektById(Integer id) {
        return repository.findById(id);
    }
    
    @Override
    public Page<Projekt> getProjektByIdPaginated(Integer projektId, Pageable pageable) {
        return repository.findAllByprojektId(projektId, pageable);
    }
    
    @Override
    public Projekt insert(Projekt projekt) {
        //try {
            //Integer lastProjektId = repository.findTopByOrderByProjektIdDesc().get().getProjektId();
            logger.error("Last projekt id is: {}", projekt.getProjektId());
            
            //projekt.setProjektId(lastProjektId + 1);
            //elasticsearchDao.save(projekt);
            projekt.setSynced(false);
            projekt.setToBeDeleted(false);
            repository.save(projekt);
        //} catch (IOException e) {
            //logger.error("Problem with insert in PSI", e);
        //}
        
        logger.debug("Saved projekt[{}]", projekt.getProjektId());
        return projekt;
    }
//    
//    @Override
//    public ProjektDto insert(ProjektDto projektDto) {
////        try {
////            elasticsearchDao.save(projekt);
////        } catch (IOException ex) {
////            logger.error("save to es projekt problem", ex);
////        }
//        Projekt projekt = this.repository.save(this.msm.projektDtoToProjekt(projektDto));
//        return this.msm.projektToProjektDto(projekt);
//        //return projekt;
//    }

    @Override
    @Transactional
    public void updateProjekt(Integer id, Projekt projekt, Boolean toBeDeleted) {
        Projekt projektFromDb = repository.findById(id).get();
        
        if ( toBeDeleted ) {
            projektFromDb.setToBeDeleted(toBeDeleted);
        } else {
            projektFromDb.setNazwa(projekt.getNazwa());
            projektFromDb.setOpis(projekt.getOpis());
            projektFromDb.setSynced(false);
        }

        repository.save(projektFromDb);
    }

    @Override
    @Transactional
    public void deleteProjekt(Integer projektId) {
        logger.debug("Projekt: {}", projektId);
        
        if(projektId != null) {
            repository.deleteById(projektId);

        }
        
        logger.debug("Projekt deleted: {}", projektId);
    }

    @Override
    public Page<Projekt> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }
    
    @Override
    public List<String> getDocId(Integer projektId) throws IOException {
        QueryBuilder query;
       
       query = QueryBuilders.matchQuery("projektId", projektId);

//           query = QueryBuilders.multiMatchQuery(projektId)
//                   .field("projektId");

       List<String> returnedDocId = projectDao.getDocId(query);
      
       return returnedDocId;
    }
    
    @Override
    public Page<Projekt> search(String q, Integer from, Integer size) throws IOException {
       QueryBuilder query;
       
       if(q.isEmpty()) {
           query = QueryBuilders.matchAllQuery();
       } else {
           query = QueryBuilders.multiMatchQuery(q)
                   .field("nazwa")
                   .field("opis");
       }

       Page<Projekt> returnedProjekt = projectDao.search(query, from, size, PageRequest.of(from, size));
      
       return returnedProjekt;
    }
    
    
    
    @Override
    public List<Projekt> getProjectsList() {
        return repository.findAll();
    }
}
