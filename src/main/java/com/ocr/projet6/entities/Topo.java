package com.ocr.projet6.entities;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Topo implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private String nom;
    private String description;
    private boolean disponibilite = true;
    private Date date;
    @ManyToOne
    @JoinColumn(name ="id_user" )
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name ="id_site" )
    private Site site;

    public Topo() { super(); }

    public Topo(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date=date;
    }
}
