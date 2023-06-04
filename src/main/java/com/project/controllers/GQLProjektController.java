package com.project.controllers;

import com.project.model.Projekt;
import com.project.services.ProjektService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/graphql", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PUT}, path = "/graphql",
        consumes = "application/json", produces = "application/json")
public class GQLProjektController {
    private final ProjektService projektService;

    @Autowired
    public GQLProjektController(ProjektService projektService) {
        this.projektService = projektService;
    }

    @QueryMapping
    public Projekt projektById(@Argument Integer id) {
        return projektService.getProjektById(id).get();
    }
}
