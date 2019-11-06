package com.ocr.projet6.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Entity
public class Longueur implements Serializable {
    @Id @GeneratedValue
    private Long idLongueur;
    @NotNull
    private String nomLongueur;
    @NotNull
    private int hauteurLongueur;

    @ManyToOne
    @JoinColumn(name ="ID_SITE" )
    private Site site;

    public Longueur() { super();}

    public Longueur(@NotNull String nomLongueur, @NotNull int hauteurLongueur, Site site) {
        this.nomLongueur = nomLongueur;
        this.hauteurLongueur = hauteurLongueur;
        this.site=site;

    }

    public Long getIdLongueur() {
        return idLongueur;
    }

    public void setIdLongueur(Long idLongueur) {
        this.idLongueur = idLongueur;
    }

    public String getNomLongueur() {
        return nomLongueur;
    }

    public void setNomLongueur(String nomLongueur) {
        this.nomLongueur = nomLongueur;
    }

    public int getHauteurLongueur() {
        return hauteurLongueur;
    }

    public void setHauteurLongueur(int hauteurLongueur) {
        this.hauteurLongueur = hauteurLongueur;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
