package com.ocr.projet6.Metier;

import com.ocr.projet6.dao.*;
import com.ocr.projet6.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ClimbMetierImpl implements IClimbMetier {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private LongueurRepository longueurRepository;
    @Autowired
    private TopoRepository topoRepository;
    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Site> listSite(int page, int size) {
        return siteRepository.listSite(PageRequest.of(page,size));
    }

    @Override
    public Page<Voie> listVoie(Long idSite, int page, int size) {
        return voieRepository.listVoie(idSite,PageRequest.of(page,size));
    }

    @Override
    public Page<Longueur> listLongueur(Long idSite, int page, int size) {
        return longueurRepository.listLongueur(idSite,PageRequest.of(page,size));
    }

    @Override
    public Page<Topo> listTopo(int page, int size){
        return topoRepository.listTopo(PageRequest.of(page,size));
    }

    public static String formatString (String str){
        String formatStr = str.toLowerCase();
        return formatStr;
    }

    @Override
    public Page<Topo> listTopoByUtilisateur(Long idUser, int page, int size){
        return topoRepository.listTopoByUtilisateur(idUser,PageRequest.of(page,size));
    }

    @Override
    public Page<Topo> listTopoBySite(Long idSite, int page, int size){
        return topoRepository.listTopoBySite(idSite,PageRequest.of(page,size));
    }

    @Override
    public Page<Commentaire> listCommentaireBySite(Long idSite, int page, int size){
        return commentaireRepository.listCommentaireBySite(idSite,PageRequest.of(page,size));
    }

    @Override
    public List<Commentaire> listCommentaireBySiteByUser(Long idSite, Long idUser){
        return commentaireRepository.listCommentaireBySiteByUser(idSite,idUser);
    }

    @Override
    public Page<Reservation> listResaDemande(String username, int page, int size){
        return reservationRepository.listResaDemande(username,PageRequest.of(page, size));
    }
    @Override
    public boolean demandeEnCours (String username, Long idTopo){
            List<Reservation> lr= reservationRepository.demandeEnCours(username,idTopo);
            if (lr.isEmpty()){
                return false;
            }else {
                return true;
            }
    }

    @Override
    public Page<Reservation> demandeEnAttenteAcceptation (String usernameProprietaire, int page, int size){
        Page <Reservation> pr= reservationRepository.demandeEnAttenteAcceptation(usernameProprietaire,PageRequest.of(page,size));
        if (pr.isEmpty()){
            return pr;
        } else return reservationRepository.demandeEnAttenteAcceptation(usernameProprietaire,PageRequest.of(page,size));
    }

    @Override
    public Page<Reservation> demandeAcceptees (String username, int page, int size){
        Page <Reservation> pr= reservationRepository.demandeAcceptees(username,PageRequest.of(page,size));
        if (pr.isEmpty()){
            return pr;
        } else return reservationRepository.demandeAcceptees(username,PageRequest.of(page,size));
    }

    @Override
    public Page<Utilisateur> listUtilisateur(int page, int size){
        return utilisateurRepository.listUtilisateur(PageRequest.of(page,size));
    }

    @Override
    public Utilisateur userConnected(){
        Utilisateur utilisateur= (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return utilisateur;
    }

    @Override
    public List<Site> listSiteWithImg(){
        return siteRepository.listSiteWithImg();
    }

}
