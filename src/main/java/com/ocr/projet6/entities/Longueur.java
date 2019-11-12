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
    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_VOIE")
    private Voie voie;

    public Longueur() { super();}

    public Longueur(@NotNull String nomLongueur, @NotNull int hauteurLongueur, Voie voie) {
        this.nomLongueur = nomLongueur;
        this.hauteurLongueur = hauteurLongueur;
        this.voie=voie;

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


    public Voie getVoie() {
        return voie;
    }

    public void setVoie(Voie voie) {
        this.voie = voie;
    }
}
