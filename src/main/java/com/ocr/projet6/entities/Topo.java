package com.ocr.projet6.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Topo implements Serializable {
    @Id @GeneratedValue
    @Column(name = "id_topo")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "disponibilite")
    private boolean disponibilite = true;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name ="id_user" )
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name ="id_site" )
    private Site site;

    @OneToMany(mappedBy = "topo",fetch = FetchType.LAZY)
    private Collection<Reservation> reservations;

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

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }
}
