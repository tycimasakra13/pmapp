package com.project.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
public class File {
    @NotNull
    @Size(max = 200)
    private String name;

    public File(String name) {
        this.name = name;
    }

    public File() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}