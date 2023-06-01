package com.project.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(indexName = "Users")
public class UserES implements UserDetails {    
    @Id
    private Integer userID;
    
    @Field(type = FieldType.Text, name = "username")
    private String username;
    
    @Field(type = FieldType.Text, name = "password")
    private String password;
    
    @Field(type = FieldType.Double, name = "role")
    private double role;
    
    @CreationTimestamp
    @Field(name = "dataczas_utworzenia")
    private LocalDateTime dataCzasUtworzenia;
    
    public UserES() {}
    
    public UserES(String username, String password) {
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

    public double getRole() {
        return role;
    }

    public void setRole(double role) {
        this.role = role;
    }

    public LocalDateTime getDataCzasUtworzenia() {
        return dataCzasUtworzenia;
    }

    public void setDataCzasUtworzenia(LocalDateTime dataCzasUtworzenia) {
        this.dataCzasUtworzenia = dataCzasUtworzenia;
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
