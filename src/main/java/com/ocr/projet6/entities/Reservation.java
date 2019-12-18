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

    private String usernameDemandeur;
    private String usernameProprietaire;
    private Date date;

    @OneToOne(mappedBy = "reservation",fetch = FetchType.LAZY)
    @JoinColumn(name ="ID_USER" )
    private Utilisateur demandeur;


    @ManyToOne
    @JoinColumn(name ="ID_TOPO" )
    private Topo topo;

    public Reservation() {super();}

    public Reservation(boolean acceptation,boolean demandeEnCours,String usernameDemandeur, String usernameProprietaire, Topo topo){
        this.acceptation=acceptation;
        this.demandeEnCours=demandeEnCours;
        this.topo=topo;
        this.usernameDemandeur=usernameDemandeur;
        this.usernameProprietaire=usernameProprietaire;

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

    public String getUsernameDemandeur() {
        return usernameDemandeur;
    }

    public void setUsernameDemandeur(String usernameDemandeur) {
        this.usernameDemandeur = usernameDemandeur;
    }

    public String getUsernameProprietaire() {
        return usernameProprietaire;
    }

    public void setUsernameProprietaire(String usernameProprietaire) {
        this.usernameProprietaire = usernameProprietaire;
    }

    public Utilisateur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Utilisateur demandeur) {
        this.demandeur = demandeur;
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
