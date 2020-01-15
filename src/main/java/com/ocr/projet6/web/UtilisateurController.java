package com.ocr.projet6.web;


import com.ocr.projet6.Metier.ClimbMetierImpl;
import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.entities.Reservation;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.dao.UtilisateurRepository;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.enums.RoleEnum;
import com.ocr.projet6.security.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

import static com.ocr.projet6.Metier.RoleDefinition.adminRole;
import static com.ocr.projet6.Metier.RoleDefinition.userRole;


@Controller
public class UtilisateurController {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private IClimbMetier iClimbMetier;



    @GetMapping(value = "/utilisateur/inscription")
    public String inscriptionForm(Model model){
        Utilisateur utilisateur=new Utilisateur();
        model.addAttribute("utilisateur",utilisateur);
        return "inscription";
    }

    @PostMapping(value = "/utilisateur/create")
    public String saveNewUtilisateur(Model model, @Valid Utilisateur utilisateur, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
                return "inscription"+"&error="+bindingResult.getGlobalError();
        } else{
            if (utilisateurRepository.findByUsername(utilisateur.getUsername())!=null || utilisateurRepository.findByEmail(utilisateur.getEmail())!=null){
                return "inscription";
            }else{
                utilisateur.getRoles().add(RoleEnum.ROLE_USER);
                utilisateurRepository.save(utilisateur);
                model.addAttribute("utilisateur",utilisateur);
                return "confirmationUtilisateur";
            }

        }
    }

    @GetMapping(value = "/utilisateur/profil/topos" )
    public String userProfiletopo (Model model,
                               @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                               @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo){

        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        model.addAttribute("utilisateurConnecte",utilisateurConnecte);
        Page<Topo> pageTopos = iClimbMetier.listTopoByUtilisateur(utilisateurConnecte.getIdUser(),pageTopo,sizeTopo);
        model.addAttribute("listTopo",pageTopos.getContent());
        Page<Reservation> pageReservationsRecues = iClimbMetier.demandeEnAttenteAcceptation(utilisateurConnecte.getUsername(),pageTopo,sizeTopo);
        model.addAttribute("listResaRecue",pageReservationsRecues.getContent());
        int[] pagesTopo=new int[pageTopos.getTotalPages()];
        int paginationEnablerTopo=pagesTopo.length;
        model.addAttribute("paginationEnablerTopo",paginationEnablerTopo);
        model.addAttribute("pagesTopo",pagesTopo);
        model.addAttribute("pageCouranteTopo",pageTopo);
        model.addAttribute("sizeTopo",sizeTopo);
        boolean topoBool=true;
        model.addAttribute("topoBool",topoBool);
        boolean demandeRecuesBool=false;
        model.addAttribute("demandeRecueBool",demandeRecuesBool);
        boolean demandeEmisesBool=false;
        model.addAttribute("demandeEmisesBool",demandeEmisesBool);
        boolean demandeAccepteesBool=false;
        model.addAttribute("demandeAccepteesBool",demandeAccepteesBool);
        return "profile";
    }



    @GetMapping(value = "/utilisateur/{idUser}/edit")
    public String editUser(Model model,
                           @PathVariable(value = "idUser")Long idUser){
        Optional<Utilisateur> u=utilisateurRepository.findById(idUser);
        Utilisateur utilisateur=null;
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        model.addAttribute("utilisateurConnecte",utilisateurConnecte);
        if(u.isPresent()) {
            utilisateur=u.get();
            model.addAttribute("utilisateur",utilisateur);
            if (utilisateur.getIdUser()==utilisateurConnecte.getIdUser()){
                return "editFormUtilisateur";
            }else return "403";
        }
        return "profile";

    }

    @PostMapping(value = "/utilisateur/{idUser}/save")
    public String saveEditedUtilisateur(Model model, @Valid Utilisateur utilisateur,
                                 @PathVariable("idUser") Long idUser,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormUtilisateur";
        }
        utilisateur.setIdUser(idUser);
        model.addAttribute("utilisateur",utilisateur);
        utilisateurRepository.save(utilisateur);
        return "confirmationEditedUtilisateur";
    }

    @GetMapping(value = "/administration")
    public String administration(Model model,
                        @RequestParam(name="pageUtilisateur",defaultValue = "0") int pageUtilisateur,
                        @RequestParam(name = "sizeUtilisateur",defaultValue = "4") int sizeUtilisateur){

        Page<Utilisateur> pageUtilisateurs= iClimbMetier.listUtilisateur(pageUtilisateur,sizeUtilisateur);
        model.addAttribute("listUtilisateur",pageUtilisateurs.getContent());
        int[] pagesUtilisateur=new int[pageUtilisateurs.getTotalPages()];
        int paginationEnablerUtilisateur=pagesUtilisateur.length;
        model.addAttribute("paginationEnablerUtilisateur",paginationEnablerUtilisateur);
        model.addAttribute("pagesUtilisateur",pagesUtilisateur);
        model.addAttribute("pageCouranteUtilisateur",pageUtilisateur);
        model.addAttribute("sizeUtilisateur",sizeUtilisateur);
        return "administration";
    }


    @GetMapping(value = "/utilisateur/{idUser}/adminRight")
    public String setRoleAdmin (Model model,@PathVariable(value = "idUser")Long idUser,
                                @RequestParam(name="pageUtilisateur",defaultValue = "0") int pageUtilisateur,
                                @RequestParam(name = "sizeUtilisateur",defaultValue = "4") int sizeUtilisateur){
        Optional<Utilisateur> u=utilisateurRepository.findById(idUser);
        Utilisateur utilisateur=null;
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_ADMIN)){
            if (u.isPresent()){
                utilisateur=u.get();
                utilisateur.getRoles().add(RoleEnum.ROLE_ADMIN);
                utilisateurRepository.save(utilisateur);
                return "redirect:/administration?pageUtilisateur="+pageUtilisateur;
            }else return "administration";
        }else return "403";
    }

    @GetMapping(value = "/utilisateur/{idUser}/userRight")
    public String setRoleUser (Model model,@PathVariable(value = "idUser")Long idUser,
                                @RequestParam(name="pageUtilisateur",defaultValue = "0") int pageUtilisateur,
                                @RequestParam(name = "sizeUtilisateur",defaultValue = "4") int sizeUtilisateur){
        Optional<Utilisateur> u=utilisateurRepository.findById(idUser);
        Utilisateur utilisateur=null;
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        if (utilisateurConnecte.getRoles().toString().contains("ADMIN")==true){
            if (u.isPresent()){
                utilisateur=u.get();
                utilisateur.getRoles().remove(RoleEnum.ROLE_ADMIN);
                utilisateurRepository.save(utilisateur);

                return "redirect:/administration?pageUtilisateur="+pageUtilisateur;
            }else return "administration";
        }else return "403";
    }

    @GetMapping(value = "/utilisateur/find")
    public String findUser(Model model,
                           @RequestParam(value = "motCle", defaultValue = "")String mc,
                           @RequestParam(name="pageUtilisateur",defaultValue = "0") int pageUtilisateur,
                           @RequestParam(name = "sizeUtilisateur",defaultValue = "4") int sizeUtilisateur){
        try {
            Page<Utilisateur> pageUtilisateurs = utilisateurRepository.chercherUtilisateur("%" + mc + "%", PageRequest.of(pageUtilisateur, sizeUtilisateur));
            model.addAttribute("listUtilisateur", pageUtilisateurs.getContent());

            int[] pagesUtilisateurs = new int[pageUtilisateurs.getTotalPages()];
            model.addAttribute("pagesUtilisateur", pagesUtilisateurs);
            int paginationEnablerUtilisateur = pagesUtilisateurs.length;
            model.addAttribute("paginationEnablerUtilisateur", paginationEnablerUtilisateur);
            model.addAttribute("pagesUtilisateur", pagesUtilisateurs);
            model.addAttribute("pageCouranteUtilisateur", pageUtilisateurs);
            model.addAttribute("sizeUtilisateur", sizeUtilisateur);
            model.addAttribute("motCle", mc);
        }catch (Exception e){
            model.addAttribute("exception",e);
            throw new RuntimeException("Utilisateur Introuvable");
        }
        return "administration";
    }

}
