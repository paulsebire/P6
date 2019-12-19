package com.ocr.projet6.entities;

import com.sun.xml.bind.v2.TODO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
public class Reservation implements Serializable {

    @Id @GeneratedValue
    private Long id;

    private boolean acceptation;

    private boolean demandeEnCours;

    private Date date;

    @OneToOne
    @JoinColumn(name ="id_user" )
    private Utilisateur utilisateur;


    @ManyToOne
    @JoinColumn(name ="ID_TOPO" )
    private Topo topo;

    public Reservation() {super();}

    public Reservation(boolean acceptation,boolean demandeEnCours,Utilisateur utilisateur, Topo topo){
        this.acceptation=acceptation;
        this.demandeEnCours=demandeEnCours;
        this.topo=topo;
        this.utilisateur=utilisateur;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAcceptation() {
        return acceptation;
    }

    public void setAcceptation(boolean acceptation) {
        this.acceptation = acceptation;
    }

    public boolean isDemandeEnCours() {
        return demandeEnCours;
    }

    public void setDemandeEnCours(boolean demandeEnCours) {
        this.demandeEnCours = demandeEnCours;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Topo getTopo() {
        return topo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTopo(Topo topo) {
        this.topo = topo;
    }
}
