package com.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class ProjektAllDto {
    private Integer projektId;
    private String nazwa;
    private String opis;
    private Date modificationDate;
    private Set<StudentDto> studenci;
//    private List<zadanieDto> zadania;
//    private List<FileDto> pliki;

    
}