package com.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class Student {
    /* TODO Uzupełnij kod o zmienne reprezentujące pozostałe pola tabeli projekt (patrz rys. 1),
    .
     a następnie wygeneruj dla nich tzw. akcesory (Source -> Generate Getters and Setters),
    .
     ponadto dodaj pusty konstruktor.
    */
    @Id
    @GeneratedValue
    @Column(name="student_id") //tylko jeżeli nazwa kolumny w bazie danych ma być inna od nazwy zmiennej
    private Integer studentId;
    @Column(nullable = false, length = 50)
    private String imie;
    @Column(nullable = false, length = 50)
    private String nazwisko;
    @Column(nullable = false, length = 6)
    private String nrIndeksu;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false)
    private Boolean stacjonarny;

    public Student() {}

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
}
