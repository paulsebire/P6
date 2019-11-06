package com.ocr.projet6.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

@Entity
public class Site implements Serializable {
    @Id
    @NotNull
    private String nameSite;
    @NotNull
    private String localisation;

    @OneToMany (mappedBy = "site",fetch = FetchType.LAZY)
    private Collection<Voie> voies;

    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private Collection<Longueur> longueurs;

    public Site() { super(); }

    public Site(String nameSite, String localisation) {
        this.nameSite = nameSite;
        this.localisation = localisation;
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

    public Collection<Longueur> getLongueurs() {
        return longueurs;
    }

    public void setLongueurs(Collection<Longueur> longueurs) {
        this.longueurs = longueurs;
    }
}
