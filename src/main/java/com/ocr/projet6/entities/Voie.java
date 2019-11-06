package com.ocr.projet6.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
public class Voie implements Serializable {

    @Id
    @NotNull
    private String nomVoie;
    @NotNull
    private String secteur;
    @NotNull
    private  String difficulte;
    @NotNull
    private int hauteurVoie;

    @ManyToOne
    @JoinColumn(name ="ID_SITE" )
    private Site site;


    public Voie() { super();}

    public Voie(@NotNull String nomVoie, @NotNull String secteur, @NotNull String difficulte, @NotNull int hauteurVoie, Site site) {
        this.nomVoie = nomVoie;
        this.secteur = secteur;
        this.difficulte = difficulte;
        this.hauteurVoie = hauteurVoie;
        this.site=site;
    }

    public String getNomVoie() {
        return nomVoie;
    }

    public void setNomVoie(String nomVoie) {
        this.nomVoie = nomVoie;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(String difficulte) {
        this.difficulte = difficulte;
    }

    public int getHauteurVoie() {
        return hauteurVoie;
    }

    public void setHauteurVoie(int hauteurVoie) {
        this.hauteurVoie = hauteurVoie;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}
