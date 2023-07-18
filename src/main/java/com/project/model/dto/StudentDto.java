package com.project.model.dto;

import java.util.Set;

public class StudentDto {
   private Integer id;
   private String imie;
   private String nazwisko;
   private String nrIndeksu;
   private String email;
   private Boolean stacjonarny;
   private Set<ProjektDto> projekty;

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

    public Set<ProjektDto> getProjekty() {
        return projekty;
    }

    public void setProjekty(Set<ProjektDto> projekty) {
        this.projekty = projekty;
    }
   
   
}
