package com.project.model.dto;

import com.project.model.File;
import com.project.model.Projekt;
import com.project.model.Student;
import java.time.LocalDateTime;
import java.util.List;

public class ZadanieDto {
    private Integer zadanieId;
    private String opis;
    private Integer kolejnosc;
    private String nazwa;
    private LocalDateTime createDate;
    private LocalDateTime modificationDate;
    private Boolean synced;
    private Boolean toBeDeleted;
    private Projekt projekt;
    private Student student;
    private List<File> pliki;
    
    
    public ZadanieDto() {}

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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Boolean getSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    public Boolean getToBeDeleted() {
        return toBeDeleted;
    }

    public void setToBeDeleted(Boolean toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<File> getPliki() {
        return pliki;
    }

    public void setPliki(List<File> pliki) {
        this.pliki = pliki;
    }

    
    
}
