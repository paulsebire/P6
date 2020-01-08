package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.ReservationRepository;
import com.ocr.projet6.dao.TopoRepository;
import com.ocr.projet6.entities.Reservation;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ReservationController {
@Autowired
private TopoRepository topoRepository;
@Autowired
private ReservationRepository reservationRepository;
@Autowired
private IClimbMetier iClimbMetier;

    @GetMapping(value = "/topo/{id}/reservation/accepter")
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
                    reservation.setAcceptation(true);
                    reservation.setDemandeEnCours(false);
                    reservationRepository.save(reservation);
                    return "redirect:/utilisateur/profil/reservations/recues";
                }
            }

        }
        return "403";
    }
    @GetMapping(value = "/topo/{id}/reservation/refuser")
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
                    return "redirect:/utilisateur/profil/reservations/recues";
                }
            }

        }
        return "403";
    }
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
        return "profile";
    }

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
        return "profile";
    }
    @GetMapping(value = "/utilisateur/profil/reservations/acceptees" )
    public String userProfileResaAcceptees (Model model,
                                         @RequestParam(name="pageReservation",defaultValue = "0") int pageReservation,
                                         @RequestParam(name = "sizeReservation",defaultValue = "2") int sizeReservation){

        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        model.addAttribute("utilisateurConnecte",utilisateurConnecte);
        Page<Reservation> pageReservations = iClimbMetier.demandeAcceptees(utilisateurConnecte.getUsername(),pageReservation,sizeReservation);
        model.addAttribute("listResaRecue",pageReservations.getContent());
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
        boolean demandeEmisesBool=false;
        model.addAttribute("demandeEmisesBool",demandeEmisesBool);
        boolean demandeAccepteesBool=true;
        model.addAttribute("demandeAccepteesBool",demandeAccepteesBool);

        return "profile";
    }

}
