package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "projekt")
public class Projekt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projekt_id")
    private Integer projektId;
    @Column(nullable = false, length = 50)
    private String nazwa;
    @Column(nullable = true, length = 1000)
    private String opis;
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime modificationDate;

    @Column(name = "synced")
    private Boolean synced;
    
    @Column(name = "toBeDeleted")
    private Boolean toBeDeleted;

    public Boolean getToBeDeleted() {
        return toBeDeleted;
    }

    public void setToBeDeleted(Boolean toBeDeleted) {
        this.toBeDeleted = toBeDeleted;
    }

    public Boolean isSynced() {
        return synced;
    }

    public void setSynced(Boolean synced) {
        this.synced = synced;
    }
    
    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    
    
    
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

    public Integer getProjektId() {
        return projektId;
    }

    public void setProjektId(Integer projektId) {
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