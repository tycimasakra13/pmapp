package com.project.model;

import jakarta.persistence.*;

@Entity
@Table(name="zadanie")
public class Zadanie {
    @Id
    @GeneratedValue
    @Column(name="zadanie_id")
    private Integer zadanieId;
}
/* TODO Uzupełnij kod o zmienne reprezentujące pozostałe pola tabeli projekt (patrz rys. 1),
.
 a następnie wygeneruj dla nich tzw. akcesory (Source -> Generate Getters and Setters),
.
 ponadto dodaj pusty konstruktor.
*/