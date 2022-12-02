package com.project.services;

import com.project.model.Projekt;
import com.project.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjektServiceImpl implements ProjektService {
    ProjectRepository repository;

    public ProjektServiceImpl(ProjectRepository projectRepository) {
        this.repository = projectRepository;
    }

    @Override
    public List<Projekt> getProjekts() {
        List<Projekt> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Projekt getProjektById(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    public Projekt insert(Projekt projekt) {
        return repository.save(projekt);
    }

    @Override
    public void updateProjekt(Integer id, Projekt projekt) {
        Projekt projektFromDb = repository.findById(id).get();

        projektFromDb.setNazwa(projekt.getNazwa());
        projektFromDb.setOpis(projekt.getOpis());

        repository.save(projektFromDb);
    }

    @Override
    public void deleteProjekt(Integer projektId) {
        repository.deleteById(projektId);
    }
}
