package com.ocr.projet6.Metier;

import com.ocr.projet6.entities.*;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IClimbMetier {
    Page<Site> listSite( int page, int size);
    Page<Voie> listVoie(Long idSite, int page, int size);
    Page<Longueur> listLongueur(Long idSite, int page, int size);
    Page<Topo> listTopo(int page, int size);
    Page<Topo> listTopoByUtilisateur(Long idUser, int page, int size);
    Page<Topo> listTopoBySite(Long idSite, int page, int size);
    Page<Commentaire> listCommentaireBySite(Long idSite, int page, int size);
    Page<Reservation> listResaDemande(String username, int page, int size);
    boolean demandeEnCours (String username, Long idTopo);
    Page<Reservation> demandeEnAttenteAcceptation (String usernameProprietaire, int page, int size);
    Page<Reservation> demandeAcceptees (String username,int page, int size);
    Page<Utilisateur> listUtilisateur(int page, int size);
    Utilisateur userConnected();
    List<Site> listSiteWithImg();
    List<Reservation> demandeEnCoursbyTopo (Long idTopo);

}
