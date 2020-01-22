package com.ocr.projet6.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;


@Entity
public class Voie implements Serializable {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idVoie;

    @Column(name = "nomVoie")
    private String nomVoie;

    @Column(name = "secteur")
    private String secteur;


    @Column(name = "hauteurVoie")
    private double hauteurVoie;

    @ManyToOne
    @JoinColumn(name ="ID_SITE" )
    private Site site;

    @ManyToOne
    @JoinColumn(name ="ID_COTE" )
    private Cotation cotation;

    @OneToMany(mappedBy = "voie", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private Collection<Longueur> longueurs;

    public Voie() { super();}

    public Voie(String nomVoie,String secteur,Cotation cotation, double hauteurVoie, Site site) {
        this.nomVoie = nomVoie;
        this.secteur = secteur;
        this.cotation=cotation;
        this.hauteurVoie = hauteurVoie;
        this.site=site;
    }

    public Long getIdVoie() {
        return idVoie;
    }

    public void setIdVoie(Long idVoie) {
        this.idVoie = idVoie;
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

    public Cotation getCotation() {
        return cotation;
    }

    public void setCotation(Cotation cotation) {
        this.cotation = cotation;
    }

    public double getHauteurVoie() {
        return hauteurVoie;
    }

    public void setHauteurVoie(double hauteurVoie) {
        this.hauteurVoie = hauteurVoie;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Collection<Longueur> getLongueurs() {
        return longueurs;
    }

    public void setLongueurs(Collection<Longueur> longueurs) {
        this.longueurs = longueurs;
    }
}
