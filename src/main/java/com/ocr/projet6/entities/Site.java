package com.ocr.projet6.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Site implements Serializable {
    @Id @GeneratedValue
    private Long idSite;
    @NotNull
    private String nameSite;
    @NotNull
    private String localisation;

    @OneToMany (mappedBy = "site",fetch = FetchType.LAZY)
    private Collection<Voie> voies;


    public Site() { super(); }

    public Site(String nameSite, String localisation) {
        this.nameSite = nameSite;
        this.localisation = localisation;
    }

    public Long getIdSite() {
        return idSite;
    }

    public void setIdSite(Long idSite) {
        this.idSite = idSite;
    }

    public String getNameSite() {
        return nameSite;
    }

    public void setNameSite(String nameSite) {
        this.nameSite = nameSite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Collection<Voie> getVoies() {
        return voies;
    }

    public void setVoies(Collection<Voie> voies) {
        this.voies = voies;
    }

}
