package com.project.controllers;

import com.project.model.Projekt;
import com.project.services.ProjektService;
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
@RequestMapping("/api/projekt")
public class ProjektController {
    private final ProjektService projektService;

    @Autowired
    public ProjektController(ProjektService projektService) {
        this.projektService = projektService;
    }

    @GetMapping("/{projektId}")
    ResponseEntity<Projekt> getProjekt(@PathVariable Integer projektId) {
        return ResponseEntity.of(projektService.getProjektById(projektId));
    }

    @PostMapping
    ResponseEntity<Void> createProjekt(@Valid @RequestBody Projekt projekt) {
        Projekt createdProjekt = projektService.insert(projekt);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{projektId}").buildAndExpand(createdProjekt.getProjektId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{projektId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateProjekt(@Valid @RequestBody Projekt projekt,
                                              @PathVariable Integer projektId) {
        return projektService.getProjektById(projektId)
                .map(p -> {
                    projektService.updateProjekt(projektId, projekt);
                    return new ResponseEntity<Void>(HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{projektId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProjekt(@PathVariable Integer projektId) {
        return projektService.getProjektById(projektId).map(p -> {
            projektService.deleteProjekt(projektId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    Page<Projekt> getProjekty(Pageable pageable) {
        return projektService.getProjekts(pageable);
    }

    @GetMapping(params = "nazwa")
    Page<Projekt> getProjektyByNazwa(@RequestParam String nazwa, Pageable pageable) {
        return projektService.searchByNazwa(nazwa, pageable);
    }
}
