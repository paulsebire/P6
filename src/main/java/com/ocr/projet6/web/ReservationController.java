package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.ReservationRepository;
import com.ocr.projet6.dao.TopoRepository;
import com.ocr.projet6.entities.Reservation;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.entities.Utilisateur;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {
@Autowired
private TopoRepository topoRepository;
@Autowired
private ReservationRepository reservationRepository;
@Autowired
private IClimbMetier iClimbMetier;
private static final Logger logger = LogManager.getLogger(ReservationController.class);
    /**
     * this method accept a demand of reservation of topo
     * @param model an instance of the model
     * @param idReservation id of the reservation
     * @return to the user's page of all incoming reservations
     */
    @GetMapping(value = "/topo/reservation/{id}/accepter")
    public String accepterReservation(Model model, @PathVariable(value = "id")Long idReservation){
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Optional<Reservation> r=reservationRepository.findById(idReservation) ;
        Topo topo=null;
        Reservation reservation=null;
        if (r.isPresent()){
            reservation=r.get();
            Optional<Topo> t=topoRepository.findById(reservation.getTopo().getId());
            if (t.isPresent()){
                topo=t.get();
                if (utilisateurConnecte.getIdUser()==topo.getUtilisateur().getIdUser()){
                    topo.setDisponibilite(false);
                    reservation.setAcceptation(true);
                    reservation.setDemandeEnCours(false);
                    reservationRepository.save(reservation);
                    topoRepository.save(topo);
                    List<Reservation> demandeEnCoursbyTopo =iClimbMetier.demandeEnCoursbyTopo(topo.getId());
                    if (!demandeEnCoursbyTopo.isEmpty()){
                        for (int k=0;k<demandeEnCoursbyTopo.size();k++) {
                            Long id=demandeEnCoursbyTopo.get(k).getId();
                            Optional<Reservation> resaCommune=reservationRepository.findById(id) ;
                            if (resaCommune.isPresent()){
                                Reservation reservationAutres=null;
                                reservationAutres=resaCommune.get();
                                reservationAutres.setAcceptation(false);
                                reservationAutres.setDemandeEnCours(false);
                                reservationRepository.save(reservationAutres);
                                logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" a refusé la demande de reservation du topo "+topo.getNom()+" par "+reservation.getUtilisateur().getUsername());

                            }
                        }
                    }
                    logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" a accepté la demande de reservation du topo "+topo.getNom()+" par "+reservation.getUtilisateur().getUsername());
                    return "redirect:/utilisateur/profil/reservations/recues";
                }
            }

        }
        return "403";
    }

    /**
     * this method deny a demand of reservation of topo
     * @param model instance  of model
     * @param idReservation id of the reservatioon
     * @return to the user's page of all incoming reservations
     */
    @GetMapping(value = "/topo/reservation/{id}/refuser")
    public String refuserReservation(Model model, @PathVariable(value = "id")Long idReservation){
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Optional<Reservation> r=reservationRepository.findById(idReservation) ;
        Topo topo=null;
        Reservation reservation=null;
        if (r.isPresent()){
            reservation=r.get();
            Optional<Topo> t=topoRepository.findById(reservation.getTopo().getId());
            if (t.isPresent()){
                topo=t.get();
                if (utilisateurConnecte.getIdUser()==topo.getUtilisateur().getIdUser()){
                    reservation.setAcceptation(false);
                    reservation.setDemandeEnCours(false);
                    reservationRepository.save(reservation);
                    logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" a refusé la demande de reservation du topo "+topo.getNom()+" par "+reservation.getUtilisateur().getUsername());
                    return "redirect:/utilisateur/profil/reservations/recues";
                }return "403";
            }return "redirect:/utilisateur/profil/reservations/recues";

        }return "redirect:/utilisateur/profil/reservations/recues";
    }

    /**
     * this method display all reservations emited by user
     * @param model instance of model
     * @param pageReservation the number of the page of reservation the user is browsing
     * @param sizeReservation the number of reservations by page
     * @return an user's page with all the reservation made by an user
     */
    @GetMapping(value = "/utilisateur/profil/reservations/emises" )
    public String userProfileResaEmises (Model model,
                                         @RequestParam(name="pageReservation",defaultValue = "0") int pageReservation,
                                         @RequestParam(name = "sizeReservation",defaultValue = "2") int sizeReservation){

        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        model.addAttribute("utilisateurConnecte",utilisateurConnecte);
        Page<Reservation> pageReservations = iClimbMetier.listResaDemande(utilisateurConnecte.getUsername(),pageReservation,sizeReservation);
        model.addAttribute("listResaEmise",pageReservations.getContent());
        Page<Reservation> pageReservationsRecues = iClimbMetier.demandeEnAttenteAcceptation(utilisateurConnecte.getUsername(),pageReservation,sizeReservation);
        model.addAttribute("listResaRecue",pageReservationsRecues.getContent());
        int[] pagesReservation=new int[pageReservations.getTotalPages()];
        int paginationEnablerResa=pagesReservation.length;
        model.addAttribute("paginationEnablerResa",paginationEnablerResa);
        model.addAttribute("pagesReservation",pagesReservation);
        model.addAttribute("pageCouranteReservation",pageReservation);
        model.addAttribute("sizeReservation",sizeReservation);
        boolean topoBool=false;
        model.addAttribute("topoBool",topoBool);
        boolean demandeRecuesBool=false;
        model.addAttribute("demandeRecueBool",demandeRecuesBool);
        boolean demandeEmisesBool=true;
        model.addAttribute("demandeEmisesBool",demandeEmisesBool);
        boolean demandeAccepteesBool=false;
        model.addAttribute("demandeAccepteesBool",demandeAccepteesBool);
        logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" veut accèder à toutes ses réservations émises ");
        return "profile";
    }

    /**
     * this method display all reservations received by user
     * @param model an instance of the model
     * @param pageReservation the number of the page of reservation the user is browsing
     * @param sizeReservation the number of reservations by page
     * @return an user's page with all the reservation received by an user
     */
    @GetMapping(value = "/utilisateur/profil/reservations/recues" )
    public String userProfileResaRecues (Model model,
                                         @RequestParam(name="pageReservation",defaultValue = "0") int pageReservation,
                                         @RequestParam(name = "sizeReservation",defaultValue = "2") int sizeReservation){

        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        model.addAttribute("utilisateurConnecte",utilisateurConnecte);
        Page<Reservation> pageReservations = iClimbMetier.demandeEnAttenteAcceptation(utilisateurConnecte.getUsername(),pageReservation,sizeReservation);
        model.addAttribute("listResaRecue",pageReservations.getContent());
        int[] pagesReservation=new int[pageReservations.getTotalPages()];
        int paginationEnablerResa=pagesReservation.length;
        model.addAttribute("paginationEnablerResa",paginationEnablerResa);
        model.addAttribute("pagesReservation",pagesReservation);
        model.addAttribute("pageCouranteReservation",pageReservation);
        model.addAttribute("sizeReservation",sizeReservation);
        boolean topoBool=false;
        model.addAttribute("topoBool",topoBool);
        boolean demandeRecuesBool=true;
        model.addAttribute("demandeRecueBool",demandeRecuesBool);
        boolean demandeEmisesBool=false;
        model.addAttribute("demandeEmisesBool",demandeEmisesBool);
        boolean demandeAccepteesBool=false;
        model.addAttribute("demandeAccepteesBool",demandeAccepteesBool);
        logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" veut accèder à toutes ses réservations reçues ");
        return "profile";
    }

    /**
     * this method display all reservations accepted by user
     * @param model an instance of the model
     * @param pageReservation the number of the page of reservation the user is browsing
     * @param sizeReservation the number of reservations by page
     * @return an user's page with all the reservation accepted by an user
     */
    @GetMapping(value = "/utilisateur/profil/reservations/acceptees" )
    public String userProfileResaAcceptees (Model model,
                                         @RequestParam(name="pageReservation",defaultValue = "0") int pageReservation,
                                         @RequestParam(name = "sizeReservation",defaultValue = "2") int sizeReservation){

        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        model.addAttribute("utilisateurConnecte",utilisateurConnecte);
        Page<Reservation> pageReservationsAcceptees = iClimbMetier.demandeAcceptees(utilisateurConnecte.getUsername(),pageReservation,sizeReservation);
        Page<Reservation> pageReservationsRecues = iClimbMetier.demandeEnAttenteAcceptation(utilisateurConnecte.getUsername(),pageReservation,sizeReservation);
        model.addAttribute("listResaRecue",pageReservationsRecues.getContent());
        model.addAttribute("listResaAcceptees",pageReservationsAcceptees.getContent());
        int[] pagesReservation=new int[pageReservationsAcceptees.getTotalPages()];
        int paginationEnablerResa=pagesReservation.length;
        model.addAttribute("paginationEnablerResa",paginationEnablerResa);
        model.addAttribute("pagesReservation",pagesReservation);
        model.addAttribute("pageCouranteReservation",pageReservation);
        model.addAttribute("sizeReservation",sizeReservation);
        boolean topoBool=false;
        model.addAttribute("topoBool",topoBool);
        boolean demandeRecuesBool=false;
        model.addAttribute("demandeRecueBool",demandeRecuesBool);
        boolean demandeEmisesBool=false;
        model.addAttribute("demandeEmisesBool",demandeEmisesBool);
        boolean demandeAccepteesBool=true;
        model.addAttribute("demandeAccepteesBool",demandeAccepteesBool);
        logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" veut accèder à toutes ses réservations acceptées ");
        return "profile";
    }

    /**
     * this method close a reservation
     * @param model an instance of the model
     * @param idReservation id of the reservation
     * @return a page with all the reservations accepted by an user
     */
    @GetMapping(value = "/topo/reservation/{id}/cloturer")
    public String cloturerResa(Model model, @PathVariable(value = "id")Long idReservation){
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Optional<Reservation> r=reservationRepository.findById(idReservation) ;
        Topo topo=null;
        Reservation reservation=null;
        if (r.isPresent()){
            reservation=r.get();
            Optional<Topo> t=topoRepository.findById(reservation.getTopo().getId());
            if (t.isPresent()){
                topo=t.get();
                if (utilisateurConnecte.getIdUser()==topo.getUtilisateur().getIdUser()){
                    topo.setDisponibilite(true);
                    topoRepository.save(topo);
                    reservation.setCloturer(true);
                    reservationRepository.save(reservation);
                    logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" a clôturer la demande de reservation du topo "+topo.getNom()+" par "+reservation.getUtilisateur().getUsername());
                    return "redirect:/utilisateur/profil/reservations/acceptees";
                }return "403";
            }return "redirect:/utilisateur/profil/reservations/acceptees";

        }return "redirect:/utilisateur/profil/reservations/acceptees";
    }

    /**
     * this method create a demand of reservation
     * @param model instance of the model
     * @param idTopo id of a topo
     * @return a page with all the information about a topo
     */
    @GetMapping(value = "/topo/{idTopo}/reservation")
    public String DemandedeReservation(Model model,@PathVariable("idTopo")Long idTopo){
        Optional<Topo> t=topoRepository.findById(idTopo);
        Topo topo=null;
        Utilisateur demandeur=iClimbMetier.userConnected();
        if (t.isPresent()){
            if (demandeur.getRoles().toString().contains("USER")){
                topo=t.get();
                Reservation reservation=new Reservation(false,true,false,demandeur,topo);
                reservation.setDate(new Date());
                reservationRepository.save(reservation);
                model.addAttribute("topo", topo);
                model.addAttribute("utilisateurConnecte",demandeur);
                boolean demandeEnCours=iClimbMetier.demandeEnCours(demandeur.getUsername(),topo.getId());
                model.addAttribute("demandeEnCours",demandeEnCours);
                logger.info("L'utilisateur "+demandeur.getUsername()+" a émis une demande de reservation du topo "+topo.getNom());
                return "redirect:/topo/"+idTopo+"/consult";
            }
            return "403";
        }
        return "redirect:/topo/"+idTopo+"/consult";
    }
}
