package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "zadanie")
public class Zadanie {
    @Id
    @GeneratedValue
    @Column(name = "zadanie_id", nullable = false)
    private Integer zadanieId;

    @Column(length = 1000, nullable = false)
    private String opis;

    @Column
    private Integer kolejnosc;
    
    @Column(nullable = false, length = 50)
    private String nazwa;
    
    @CreationTimestamp
    @Column(name = "data_dodania", nullable = false)
    private LocalDateTime dataDodania;
    
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "projekt_id", nullable = false)
    private Projekt projekt;
    
//    @JsonIgnore
//    @ManyToOne()
//    @JoinColumn(name = "student_id", nullable = true)
//    private StudentES student;
//
//    public StudentES getStudent() {
//        return student;
//    }
//
//    public void setStudent(StudentES student) {
//        this.student = student;
//    }

    @ElementCollection
    private List<File> pliki;

    public List<File> getPliki() {
        return pliki;
    }

    public void setPliki(List<File> pliki) {
        this.pliki = pliki;
    }

    public Integer getZadanieId() {
        return zadanieId;
    }

    public void setZadanieId(Integer zadanieId) {
        this.zadanieId = zadanieId;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Integer getKolejnosc() {
        return kolejnosc;
    }

    public void setKolejnosc(Integer kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getDataDodania() {
        return dataDodania.format(DateTimeFormatter.ISO_DATE);
    }

    public void setDataDodania(LocalDateTime dataDodania) {
        this.dataDodania = dataDodania;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }
}
