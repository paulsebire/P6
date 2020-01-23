package com.ocr.projet6.Metier;

import com.ocr.projet6.dao.*;
import com.ocr.projet6.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * this method ask in DB for a list of all sites
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page of all sites
     */
    @Override
    public Page<Site> listSite(int page, int size) {
        return siteRepository.listSite(PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of all voies for a site
     * @param idSite id of the site
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page of all voies for a site
     */
    @Override
    public Page<Voie> listVoie(Long idSite, int page, int size) {
        return voieRepository.listVoie(idSite,PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of all longueurs for a site
     * @param idSite id of the site
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page of all longueurs for a site
     */
    @Override
    public Page<Longueur> listLongueur(Long idSite, int page, int size) {
        return longueurRepository.listLongueur(idSite,PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of all topos
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page of all topos
     */
    @Override
    public Page<Topo> listTopo(int page, int size){
        return topoRepository.listTopo(PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of topos for one user
     * @param idUser id of user
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page with all topo owned by user identified by his id
     */
    @Override
    public Page<Topo> listTopoByUtilisateur(Long idUser, int page, int size){
        return topoRepository.listTopoByUtilisateur(idUser,PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of topos for one site
     * @param idSite id of the site
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page with all topos for the identified site
     */
    @Override
    public Page<Topo> listTopoBySite(Long idSite, int page, int size){
        return topoRepository.listTopoBySite(idSite,PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of comments for one site
     * @param idSite id of the site
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page with all the comments for the identified site
     */
    @Override
    public Page<Commentaire> listCommentaireBySite(Long idSite, int page, int size){
        return commentaireRepository.listCommentaireBySite(idSite,PageRequest.of(page,size));
    }


    /**
     * this method ask in DB for a list of reservation emited form one user
     * @param username name of user
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page with all the reservation emitted by user
     */
    @Override
    public Page<Reservation> listResaDemande(String username, int page, int size){
        return reservationRepository.listResaDemande(username,PageRequest.of(page, size));
    }

    /**
     * this method ask in DB if one user have already a reservation of the topo
     * @param username name of user
     * @param idTopo id of the topo
     * @return true if the user have already a demand of reservation of the topo
     */
    @Override
    public boolean demandeEnCours (String username, Long idTopo){
            List<Reservation> lr= reservationRepository.demandeEnCours(username,idTopo);
            if (lr.isEmpty()){
                return false;
            }else {
                return true;
            }
    }

    /**
     * this method ask in DB for a list of reservation received for one user
     * @param usernameProprietaire name of the owner of the topo
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page of reservation received for one user
     */
    @Override
    public Page<Reservation> demandeEnAttenteAcceptation (String usernameProprietaire, int page, int size){
        Page <Reservation> pr= reservationRepository.demandeEnAttenteAcceptation(usernameProprietaire,PageRequest.of(page,size));
        if (pr.isEmpty()){
            return pr;
        } else return reservationRepository.demandeEnAttenteAcceptation(usernameProprietaire,PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of reservation accepted by one user
     * @param username name of user
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page with all reservation accepted by one user
     */
    @Override
    public Page<Reservation> demandeAcceptees (String username, int page, int size){
        Page <Reservation> pr= reservationRepository.demandeAcceptees(username,PageRequest.of(page,size));
        if (pr.isEmpty()){
            return pr;
        } else return reservationRepository.demandeAcceptees(username,PageRequest.of(page,size));
    }

    /**
     * this method ask in DB for a list of all users
     * @param page param of PageRequest
     * @param size param of PageRequest
     * @return a page with all the users
     */
    @Override
    public Page<Utilisateur> listUtilisateur(int page, int size){
        return utilisateurRepository.listUtilisateur(PageRequest.of(page,size));
    }

    /**
     * this method get all the information about the current connected user
     * @return an user (entity)  with all the information about the logged  user
     */
    @Override
    public Utilisateur userConnected(){
        Utilisateur utilisateur= (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return utilisateur;
    }

    /**
     * this method ask in DB for a list of site who get an image for home page
     * @return list of site who get an image
     */
    @Override
    public List<Site> listSiteWithImg(){
        return siteRepository.listSiteWithImg();
    }

    /**
     *this method ask in DB for a list of reservations received for one topo
     * @param idTopo id of the topo
     * @return a list with all the active reservations on one topo
     */
    @Override
    public List<Reservation> demandeEnCoursbyTopo (Long idTopo){
        return reservationRepository.demandeEnCoursbyTopo(idTopo);
    }

    /**
     * logger
     * @return the logger
     */
    @Override
    public final Logger logger(){
        return LogManager.getLogger();
    }

}
