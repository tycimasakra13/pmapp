package com.project.model;

import java.util.Date;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "projekt")
public class ProjektES {
    private Integer id;
    private String nazwa;
    private String opis;
    private Date modificationDate;
    
    public ProjektES() {}

    public Integer getProjektId() {
        return id;
    }

    public void setProjektId(Integer projektId) {
        this.id = projektId;
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

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
    
    
}
