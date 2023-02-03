package com.project.services;

import com.project.model.Projekt;
import com.project.model.Zadanie;
import com.project.repositories.ProjectRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjektServiceImpl implements ProjektService {
    public ProjectRepository repository;
    @Autowired
    public ProjektServiceImpl(ProjectRepository projectRepository) {
        this.repository = projectRepository;
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
        return repository.save(projekt);
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
        repository.deleteById(projektId);
    }

    @Override
    public Page<Projekt> searchByNazwa(String nazwa, Integer pageNumber, Integer pageSize) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return repository.findByNazwaContainingIgnoreCase(nazwa, pageable);
    }
    
    @Override
    public List<Map<Integer, String>> getProjectsForSelect() {
        return repository.getProjectsListForSelect();
    }
}
