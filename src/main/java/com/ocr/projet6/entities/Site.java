package com.ocr.projet6.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Collection;

@Entity
public class Site implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idSite;

    @Column(name = "nameSite")
    private String nameSite;

    @Column(name = "localisation")
    private String localisation;

    @Column(name = "officiel")
    private boolean officiel;

    @Column(name = "urlImg")
    private String urlImg;

    @OneToMany (mappedBy = "site",fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private Collection<Voie> voies;

    @ManyToOne
    @JoinColumn(name ="id_user" )
    private Utilisateur utilisateur;

    public Site() { super(); }

    public Site(String nameSite, String localisation) {
        this.nameSite = nameSite;
        this.localisation = localisation;
    }

    public boolean isOfficiel() {
        return officiel;
    }

    public void setOfficiel(boolean officiel) {
        this.officiel = officiel;
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

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
