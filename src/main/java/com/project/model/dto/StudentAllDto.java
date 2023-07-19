package com.project.model.dto;

import java.util.Date;
import java.util.Set;


public class StudentAllDto {
    private Integer id;
    private String imie;
    private String nazwisko;
    private String nrIndeksu;
    private String email;
    private Boolean stacjonarny;
    private Date modificationDate;
    private Set<StudentDto> studenci;
    //    private List<zadanieDto> zadania;
//    private List<FileDto> pliki;
}