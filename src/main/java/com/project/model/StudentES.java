package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

import java.util.Set;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "student")
public class StudentES {
    @Id
    private Integer id;
    
    @Field(type = FieldType.Text, name = "imie")
    private String imie;
    
    @Field(type = FieldType.Text, name = "nazwisko")
    private String nazwisko;
    
    @Field(type = FieldType.Text, name = "nrIndeksu")
    private String nrIndeksu;
    
    @Field(type = FieldType.Text, name = "email")
    private String email;
    
    @Field(type = FieldType.Boolean, name = "stacjonarny")
    private Boolean stacjonarny;
    
    @OneToMany(mappedBy = "student")
    private List<Zadanie> zadania;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "studenci", cascade = CascadeType.REMOVE)
    private Set<Projekt> projekty;

    public List<Zadanie> getZadania() {
        return zadania;
    }

    public void setZadania(List<Zadanie> zadania) {
        this.zadania = zadania;
    }

    public StudentES() {
    }

    public StudentES(String imie, String nazwisko, String nrIndeksu, Boolean stacjonarny) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrIndeksu = nrIndeksu;
        this.stacjonarny = stacjonarny;
    }

    public StudentES(String imie, String nazwisko, String nrIndeksu, String email,
                   Boolean stacjonarny) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrIndeksu = nrIndeksu;
        this.email = email;
        this.stacjonarny = stacjonarny;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getNrIndeksu() {
        return nrIndeksu;
    }

    public void setNrIndeksu(String nrIndeksu) {
        this.nrIndeksu = nrIndeksu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStacjonarny() {
        return stacjonarny;
    }

    public void setStacjonarny(Boolean stacjonarny) {
        this.stacjonarny = stacjonarny;
    }

    public Set<Projekt> getProjekty() {
        return projekty;
    }

    public void setProjekty(Set<Projekt> projekty) {
        this.projekty = projekty;
    }
}
