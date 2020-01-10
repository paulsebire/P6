package com.ocr.projet6.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Cotation implements Serializable {
    @Id @GeneratedValue
    @Column(name = "ID_COTE")
    private Long id;

    private String cote;

    @OneToMany(mappedBy = "cotation", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Collection<Voie> voies;

    public Cotation() { super(); }

    public Cotation(String cote) {
        this.cote = cote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCote() {
        return cote;
    }

    public void setCote(String cote) {
        this.cote = cote;
    }

    public Collection<Voie> getVoies() {
        return voies;
    }

    public void setVoies(Collection<Voie> voies) {
        this.voies = voies;
    }
}
