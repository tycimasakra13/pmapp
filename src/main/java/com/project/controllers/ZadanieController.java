package com.project.controllers;

import com.project.model.Zadanie;
import com.project.services.ZadanieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/zadanie")
public class ZadanieController {
    private final ZadanieService zadanieService;

    @Autowired
    public ZadanieController(ZadanieService zadanieService) {
        this.zadanieService = zadanieService;
    }

    @GetMapping("/{zadanieId}")
    ResponseEntity<Zadanie> getZadanie(@PathVariable Integer zadanieId) {
        return ResponseEntity.of(zadanieService.getZadanieById(zadanieId));
    }

    @PostMapping
    ResponseEntity<Void> createZadanie(@Valid @RequestBody Zadanie zadanie) {
        Zadanie createdZadanie = zadanieService.insert(zadanie);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{zadanieId}").buildAndExpand(createdZadanie.getZadanieId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{zadanieId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateZadanie(@Valid @RequestBody Zadanie zadanie,
                                              @PathVariable Integer zadanieId) {
        return zadanieService.getZadanieById(zadanieId)
                .map(p -> {
                    zadanieService.updateZadanie(zadanieId, zadanie);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{zadanieId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteZadanie(@PathVariable Integer zadanieId) {
        return zadanieService.getZadanieById(zadanieId).map(p -> {
            zadanieService.deleteZadanie(zadanieId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    Page<Zadanie> getZadanies(Pageable pageable) {
        return zadanieService.getZadanies(pageable);
    }

    @GetMapping(params = "projektId")
    Page<Zadanie> getZadaniaProjektu(@RequestParam Integer projektId, Pageable pageable) {
        return zadanieService.getZadaniaProjektu(projektId, pageable);
    }
}
