package com.ocr.projet6.entities;

import javax.persistence.*;
import java.io.Serializable;
@Entity
public class Longueur implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idLongueur;

    @Column(name = "nomLongueur")
    private String nomLongueur;

    @Column(name = "hauteurLongueur")
    private double hauteurLongueur;
    @ManyToOne
    @JoinColumn(name = "ID_VOIE")
    private Voie voie;

    public Longueur() { super();}

    public Longueur( String nomLongueur, double hauteurLongueur, Voie voie) {
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

    public double getHauteurLongueur() {
        return hauteurLongueur;
    }

    public void setHauteurLongueur(double hauteurLongueur) {
        this.hauteurLongueur = hauteurLongueur;
    }


    public Voie getVoie() {
        return voie;
    }

    public void setVoie(Voie voie) {
        this.voie = voie;
    }
}
