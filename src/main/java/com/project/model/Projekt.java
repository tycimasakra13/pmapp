package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projekt")
public class Projekt {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "projekt_id")
    private Long projektId = null;
    @Column(nullable = false, length = 50)
    private String nazwa;
    @Column(nullable = true, length = 1000)
    private String opis;
//    @CreationTimestamp
//    @Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
//    private LocalDateTime dataCzasUtworzenia;
//    @UpdateTimestamp
//    @Column(name = "dataczas_modyfikacji", nullable = false)
//    private LocalDateTime dataCzasModyfikacji;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "projekt_student", joinColumns = {@JoinColumn(name = "projekt_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    private Set<Student> studenci;
    
    @OneToMany(mappedBy = "projekt", cascade = CascadeType.REMOVE)
    private List<Zadanie> zadania;
     
    @ElementCollection
    private List<File> pliki;

    public Projekt() {
    }

    public List<File> getPliki() {
        return pliki;
    }

    public void setPliki(List<File> pliki) {
        this.pliki = pliki;
    }

    public List<Zadanie> getZadania() {
        return zadania;
    }

    public void setZadania(List<Zadanie> zadania) {
        this.zadania = zadania;
    }

    public Long getProjektId() {
        return projektId;
    }

    public void setProjektId(Long projektId) {
        this.projektId = projektId;
    }
    
    public String idAsString() {
        return projektId != null ? "" + projektId : null;
    }


    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

//    public String getDataCzasUtworzenia() {
//        return dataCzasUtworzenia.format(DateTimeFormatter.ISO_DATE);
//    }
//
//    public void setDataCzasUtworzenia(LocalDateTime dataCzasUtworzenia) {
//        this.dataCzasUtworzenia = dataCzasUtworzenia;
//    }
//
//    public String getDataCzasModyfikacji() {
//        return dataCzasModyfikacji.format(DateTimeFormatter.ISO_DATE);
//    }
//
//    public void setDataCzasModyfikacji(LocalDateTime dataCzasModyfikacji) {
//        this.dataCzasModyfikacji = dataCzasModyfikacji;
//    }

    public Set<Student> getStudenci() {
        return studenci;
    }

    public void setStudenci(Set<Student> studenci) {
        this.studenci = studenci;
    }
}