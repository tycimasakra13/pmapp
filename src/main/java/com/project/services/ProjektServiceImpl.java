package com.project.services;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.repositories.ProjectRepository;
import com.project.dao.ElasticsearchDao;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProjektServiceImpl implements ProjektService {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(ElasticsearchDao.class);
    
    public ProjectRepository repository;
    private ElasticsearchDao elasticsearchDao;
    @Autowired
    public ProjektServiceImpl(ProjectRepository projectRepository, ElasticsearchDao elasticsearchDao) {
        this.repository = projectRepository;
        this.elasticsearchDao = elasticsearchDao;
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
    public Projekt insert(Projekt projekt) {
        try {
            elasticsearchDao.save(projekt);
        } catch (IOException ex) {
            logger.error("save to es projekt problem", ex);
        }
        //return repository.save(projekt);
        return projekt;
    }

    @Override
    @Transactional
    public void updateProjekt(Integer id, Projekt projekt) {
        Projekt projektFromDb = repository.findById(id).get();

        projektFromDb.setNazwa(projekt.getNazwa());
        projektFromDb.setOpis(projekt.getOpis());

        repository.save(projektFromDb);
    }

    @Override
    @Transactional
    public void deleteProjekt(Integer projektId) {
        try {
            elasticsearchDao.delete(projektId);
        } catch (IOException ex) {
            logger.error("delete from es projekt problem", ex);
        }
        repository.deleteById(projektId);
    }

    @Override
    public Page<Projekt> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }
    
    @Override
    public List<Projekt> getProjectsList() {
        return repository.findAll();
    }
}
