package com.ocr.projet6.entities;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Collection;

@Entity
public class Site implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idSite;

    private String nameSite;

    private String localisation;

    @OneToMany (mappedBy = "site",fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private Collection<Voie> voies;

    @ManyToOne
    @JoinColumn(name ="ID_UTILISATEUR" )
    private Utilisateur utilisateur;

    public Site() { super(); }

    public Site(String nameSite, String localisation, Utilisateur utilisateur) {
        this.nameSite = nameSite;
        this.localisation = localisation;
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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
