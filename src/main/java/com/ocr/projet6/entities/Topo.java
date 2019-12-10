package com.ocr.projet6.entities;

import javax.persistence.*;
import java.io.Serializable;
@Entity
public class Topo implements Serializable {
    @Id @GeneratedValue
    private Long id;
    private String nom;
    private String description;
    private boolean disponibilite;
    @ManyToOne
    @JoinColumn(name ="id_user" )
    private Utilisateur utilisateur;

    public Topo() { super(); }

    public Topo(String nom, String description, boolean disponibilite, Utilisateur utilisateur) {
        this.nom = nom;
        this.description = description;
        this.disponibilite = disponibilite;
        this.utilisateur = utilisateur;
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
}
