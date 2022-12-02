package com.project.controllers;

import com.project.model.Zadanie;
import com.project.services.ZadanieService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zadanie")
public class ZadanieController {
    ZadanieService zadanieService;

    public ZadanieController(ZadanieService zadanieService) {
        this.zadanieService = zadanieService;
    }

    //The function receives a GET request, processes it and gives back a list of Zadanie as a response.
    @GetMapping
    public ResponseEntity<List<Zadanie>> getAllZadanies() {
        List<Zadanie> zadanies = zadanieService.getZadanies();
        return new ResponseEntity<>(zadanies, HttpStatus.OK);
    }

    //The function receives a GET request, processes it, and gives back a list of Zadanie as a response.
    @GetMapping({"/{zadanieId}"})
    public ResponseEntity<Zadanie> getZadanie(@PathVariable Integer zadanieId) {
        return new ResponseEntity<>(zadanieService.getZadanieById(zadanieId), HttpStatus.OK);
    }

    //The function receives a POST request, processes it, creates a new Zadanie and saves it to the database, and returns a resource link to the created zadanie.
    @PostMapping
    public ResponseEntity<Zadanie> saveZadanie(@RequestBody Zadanie zadanie) {
        Zadanie zadanie1 = zadanieService.insert(zadanie);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("zadanie", "/api/zadanie/" + zadanie1.getZadanieId().toString());
        return new ResponseEntity<>(zadanie1, httpHeaders, HttpStatus.CREATED);
    }

    //The function receives a PUT request, updates the Zadanie with the specified Id and returns the updated Zadanie
    @PutMapping({"/{zadanieId}"})
    public ResponseEntity<Zadanie> updateZadanie(@PathVariable("zadanieId") Integer zadanieId, @RequestBody Zadanie zadanie) {
        zadanieService.updateZadanie(zadanieId, zadanie);
        return new ResponseEntity<>(zadanieService.getZadanieById(zadanieId), HttpStatus.OK);
    }

    //The function receives a DELETE request, deletes the Zadanie with the specified Id.
    @DeleteMapping({"/{zadanieId}"})
    public ResponseEntity<Zadanie> deleteZadanie(@PathVariable("zadanieId") Integer zadanieId) {
        zadanieService.deleteZadanie(zadanieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
