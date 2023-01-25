package com.project.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "userId")
    private Integer userID;
    @Column(nullable = false, length = 50)
    private String userName;
    @Column(nullable = true, length = 1000)
    private String password;
    @Column(nullable = true, length = 1000)
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @CreationTimestamp
    @Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
    private LocalDateTime dataCzasUtworzenia;

    public User() {}

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public LocalDateTime getDataCzasUtworzenia() {
        return dataCzasUtworzenia;
    }

    public void setDataCzasUtworzenia(LocalDateTime dataCzasUtworzenia) {
        this.dataCzasUtworzenia = dataCzasUtworzenia;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}