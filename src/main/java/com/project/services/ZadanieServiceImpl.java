package com.project.services;

import com.project.dao.ZadanieDao;
import com.project.model.Zadanie;
import com.project.repositories.ZadanieRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@Service
public class ZadanieServiceImpl implements ZadanieService {
    ZadanieRepository repository;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ZadanieServiceImpl.class);

    @Autowired
    private ZadanieDao zadanieDao;
    
    public ZadanieServiceImpl(ZadanieRepository zadanieRepository) {
        this.repository = zadanieRepository;
    }

    @Override
    public Page<Zadanie> getZadanies(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public Page<Zadanie> getPaginatedTasks(Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    public Page<Zadanie> getZadaniaProjektu(Integer projektId, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findZadaniaProjektu(projektId, pageable);
    }
    
    @Override
    public Page<Zadanie> getZadaniaStudenta(Integer studentId, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findZadaniaStudenta(studentId, pageable);
    }

    @Override
    public Optional<Zadanie> getZadanieById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Zadanie insert(Zadanie zadanie) {
        logger.error("Last projekt id is: {}", zadanie.getZadanieId());
        zadanie.setSynced(false);
        repository.save(zadanie);
        logger.debug("Saved projekt[{}]", zadanie.getZadanieId());
        
        return zadanie;
    }

    @Override
    @Transactional
    public void updateZadanie(Integer id, Zadanie zadanie, Boolean toBeDeleted) {
        Zadanie zadanieFromDb = repository.findById(id).get();

        if ( toBeDeleted ) {
            zadanieFromDb.setToBeDeleted(toBeDeleted);
        } else {
            zadanieFromDb.setNazwa(zadanie.getNazwa());
            zadanieFromDb.setOpis(zadanie.getOpis());
            zadanieFromDb.setKolejnosc(zadanie.getKolejnosc());
            zadanieFromDb.setProjekt(zadanie.getProjekt());
            zadanieFromDb.setStudent(zadanie.getStudent());
            zadanieFromDb.setSynced(false);
        }

        repository.save(zadanieFromDb);
    }

    @Override
    @Transactional
    public void deleteZadanie(Integer zadanieId) {
        logger.debug("Zadanie: {}", zadanieId);
        if (zadanieId != null) {
            this.removeAssignedProject(zadanieId);
            repository.deleteById(zadanieId);
        }
        
        logger.debug("Zadanie deleted: {}", zadanieId);
    }
    
    @Override
    @Transactional
    public void removeAssignStudent(Integer studentId, Pageable pageable) {
        //repository.deleteById(studentId);
        Page<Zadanie> zadanieFromDb = repository.findZadaniaStudenta(studentId, pageable);
        zadanieFromDb.getContent().forEach((zadanie) -> {
            zadanie.setStudent(null);
            repository.save(zadanie);
        });
    }
    
    @Transactional
    public void removeAssignedProject(Integer zadanieId) {
        //repository.deleteById(zadanieId);
        Zadanie zadanie = this.getZadanieById(zadanieId).get();
        zadanie.setProjekt(null);
        repository.save(zadanie);
    }
    
    @Override
    public Page<Zadanie> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }

    @Override
    public List<String> getDocId(Integer zadanieId) throws IOException {
        QueryBuilder query;
       
       query = QueryBuilders.matchQuery("zadanieId", zadanieId);

//           query = QueryBuilders.multiMatchQuery(projektId)
//                   .field("projektId");

       List<String> returnedDocId = zadanieDao.getDocId(query);
      
       return returnedDocId;
    }
    
    @Override
    public Page<Zadanie> search(String q, Integer from, Integer size) throws IOException {
        QueryBuilder query;
        
        if(q.isEmpty()) {
            query = QueryBuilders.matchAllQuery();
        } else {
            query = QueryBuilders.multiMatchQuery(q)
                    .field("opis")
                    .field("nazwa");
        }
        
        Page<Zadanie> returnedZadanie = zadanieDao.search(query, from, size, PageRequest.of(0, 5));

        return returnedZadanie;
    }
}
