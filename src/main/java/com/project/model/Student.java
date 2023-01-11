package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue
    @Column(name = "student_id")
    private Integer studentId;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 100)
    private String sName;
    @Column(nullable = false, length = 20)
    private String indexNumber;
    @Column(nullable = true, length = 50)
    private String email;
    @Column(nullable = false)
    private Boolean fullTime;
    @JsonIgnore
    @ManyToMany(mappedBy = "studenci", cascade = CascadeType.REMOVE)
    private Set<Project> projects;

    public Student() {
    }

    public Student(String name, String sName, String indexNumber, Boolean fullTime) {
        this.name = name;
        this.sName = sName;
        this.indexNumber = indexNumber;
        this.fullTime = fullTime;
    }

    public Student(String name, String sName, String indexNumber, String email,
                   Boolean fullTime) {
        this.name = name;
        this.sName = sName;
        this.indexNumber = indexNumber;
        this.email = email;
        this.fullTime = fullTime;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFullTime() {
        return fullTime;
    }

    public void setFullTime(Boolean fullTime) {
        this.fullTime = fullTime;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
