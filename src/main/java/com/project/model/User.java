package com.project.model;

import jakarta.persistence.Table;
import java.util.Collection; 
import java.util.List;
import jakarta.persistence.Column; 
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; 
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetails; 

@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userID;
    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = true, length = 1000)
    private String password;
    @Column(nullable = true, length = 1000)
    private String role;

    @CreationTimestamp
    @Column(name = "dataczas_utworzenia", nullable = false, updatable = false)
    private LocalDateTime dataCzasUtworzenia;

    public User() {}
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override 
    public Collection<? extends GrantedAuthority> getAuthorities() { 
       return List.of(() -> "read"); 
    }
   
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
    
    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
        public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
     @Override 
   public boolean isAccountNonExpired() { 
      return true; 
   } 
   
    @Override
   public boolean isAccountNonLocked() { 
      return true; 
   } 
   @Override public boolean isCredentialsNonExpired() { 
      return true; 
   } 
   @Override public boolean isEnabled() { 
   return true; 
   } 
}
