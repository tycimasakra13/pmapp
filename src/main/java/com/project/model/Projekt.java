package com.project.model;

import jakarta.persistence.*;

@Entity
@Table(name="projekt") //potrzebne tylko jeżeli nazwa tabeli w bazie danych ma być inna od nazwy klasy
public class Projekt {
    @Id
    @GeneratedValue
    @Column(name="projekt_id") //tylko jeżeli nazwa kolumny w bazie danych ma być inna od nazwy zmiennej
    private Integer projektId;
    @Column(nullable = false, length = 50)
    private String nazwa;

    /* TODO Uzupełnij kod o zmienne reprezentujące pozostałe pola tabeli projekt (patrz rys. 1),
    .
     a następnie wygeneruj dla nich tzw. akcesory (Source -> Generate Getters and Setters),
    .
     ponadto dodaj pusty konstruktor.
    */
    public Projekt(){
    }
    public Integer getProjektId() {
        return projektId;
    }
    public void setProjektId(Integer projektId) {
        this.projektId = projektId;
    }
    public String getNazwa() {
        return nazwa;
    }
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
}
/* TODO Getters and Setters
.
*/