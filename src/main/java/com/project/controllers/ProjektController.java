package com.project.controllers;

import com.project.model.Projekt;
import com.project.services.ProjektService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projekt")
public class ProjektController {
    ProjektService projektService;

    public ProjektController(ProjektService projektService) {
        this.projektService = projektService;
    }

    //The function receives a GET request, processes it and gives back a list of Projekt as a response.
    @GetMapping
    public ResponseEntity<List<Projekt>> getAllProjekts() {
        List<Projekt> projekts = projektService.getProjekts();
        return new ResponseEntity<>(projekts, HttpStatus.OK);
    }

    //The function receives a GET request, processes it, and gives back a list of Projekt as a response.
    @GetMapping({"/{projektId}"})
    public ResponseEntity<Projekt> getProjekt(@PathVariable Integer projektId) {
        return new ResponseEntity<>(projektService.getProjektById(projektId), HttpStatus.OK);
    }

    //The function receives a POST request, processes it, creates a new Projekt and saves it to the database, and returns a resource link to the created projekt.
    @PostMapping
    public ResponseEntity<Projekt> saveProjekt(@RequestBody Projekt projekt) {
        Projekt projekt1 = projektService.insert(projekt);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("projekt", "/api/projekt/" + projekt1.getProjektId().toString());
        return new ResponseEntity<>(projekt1, httpHeaders, HttpStatus.CREATED);
    }

    //The function receives a PUT request, updates the Projekt with the specified Id and returns the updated Projekt
    @PutMapping({"/{projektId}"})
    public ResponseEntity<Projekt> updateProjekt(@PathVariable("projektId") Integer projektId, @RequestBody Projekt projekt) {
        projektService.updateProjekt(projektId, projekt);
        return new ResponseEntity<>(projektService.getProjektById(projektId), HttpStatus.OK);
    }

    //The function receives a DELETE request, deletes the Projekt with the specified Id.
    @DeleteMapping({"/{projektId}"})
    public ResponseEntity<Projekt> deleteProjekt(@PathVariable("projektId") Integer projektId) {
        projektService.deleteProjekt(projektId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
