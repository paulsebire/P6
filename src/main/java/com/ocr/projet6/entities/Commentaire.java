package com.ocr.projet6.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Commentaire implements Serializable {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "contenu",columnDefinition = "text")
    @Size (min = 2, max = 1000)
    private String contenu;

    @Column(name = "date")
    private Date date;
    @ManyToOne
    @JoinColumn(name ="id_user" )
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name ="id_site" )
    private Site site;

    public Commentaire() { super(); }

    public Commentaire(String contenu) {
        this.contenu=contenu;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
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
