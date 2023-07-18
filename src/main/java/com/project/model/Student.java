package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Set;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(nullable = false, length = 50)
    private String imie;
    
    @Column(nullable = false, length = 100)
    private String nazwisko;
    
    @Column(nullable = false, length = 20)
    private String nrIndeksu;
    
    @Column(nullable = true, length = 50)
    private String email;
    
    @Column(nullable = false)
    private Boolean stacjonarny;
    
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime modificationDate;

    @Column(name = "synced")
    private Boolean synced;
    
    @Column(name = "toBeDeleted")
    private Boolean toBeDeleted;
    
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

    public Student() {
    }

    public Student(String imie, String nazwisko, String nrIndeksu, Boolean stacjonarny) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrIndeksu = nrIndeksu;
        this.stacjonarny = stacjonarny;
    }

    public Student(String imie, String nazwisko, String nrIndeksu, String email,
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

    public void setId(Integer studentId) {
        this.id = studentId;
    }
    
    public String idAsString() {
        return id != null ? "" + id : null;
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
}
