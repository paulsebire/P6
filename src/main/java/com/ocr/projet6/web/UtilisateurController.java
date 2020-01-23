package com.ocr.projet6.web;


import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.UtilisateurRepository;
import com.ocr.projet6.entities.Reservation;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class UtilisateurController {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private IClimbMetier iClimbMetier;


    /**
     * this method return the inscription form
     * @param model instance of model
     * @return the form for inscription
     */
    @GetMapping(value = "/utilisateur/inscription")
    public String inscriptionForm(Model model){
        Utilisateur utilisateur=new Utilisateur();
        iClimbMetier.logger().info("Un visiteur veut accéder au formulaire d'inscription");
        model.addAttribute("utilisateur",utilisateur);
        return "inscription";
    }

    /**
     * this method check if username and email are available
     * then add the new user to DB
     * @param model instance of model
     * @param utilisateur  an object utilisateur
     * @param bindingResult handle the errors
     * @return a confirmation page
     */
    @PostMapping(value = "/utilisateur/create")
    public String saveNewUtilisateur(Model model, @Valid Utilisateur utilisateur, BindingResult bindingResult){
            if (utilisateurRepository.findByUsername(utilisateur.getUsername())==null || utilisateurRepository.findByEmail(utilisateur.getEmail())==null){
                utilisateur.getRoles().add(RoleEnum.ROLE_USER);
                utilisateurRepository.save(utilisateur);
                model.addAttribute("utilisateur",utilisateur);
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" a été ajouté a la DB");
                return "confirmationUtilisateur";
            }else{
                if (utilisateurRepository.findByUsername(utilisateur.getUsername())!=null ){
                    bindingResult.rejectValue("utilisateur.username", "utilisateur.username", "ce pseudo est déjà utilisé :(");
                }
                if (utilisateurRepository.findByEmail(utilisateur.getEmail())!=null){
                    bindingResult.rejectValue("utilisateur.email", "utilisateur.email", "cet e-mail est déjà associé à un autre compte :(");
                }
                return "redirect:/utilisateur/inscription";
            }


    }

    /**
     * this method get all the topo owned by the loged user
     * and return to his profile page
     * @param model instance of the model
     * @param pageTopo the number of the page the user  is browsing
     * @param sizeTopo the number of topo displayed by page
     * @return the profile page with all the topos the user own
     */
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
        iClimbMetier.logger().info("L'utilisateur "+utilisateurConnecte.getUsername()+" accède à sa page de topos");
        return "profile";
    }


    /**
     * this method check the user and return the edition form for profile
     * @param model instance of th model
     * @param idUser id of user
     * @return  the  form for editing profile
     */
    @GetMapping(value = "/utilisateur/{idUser}/edit")
    public String editUser(Model model,
                           @PathVariable(value = "idUser")Long idUser){
        Optional<Utilisateur> u=utilisateurRepository.findById(idUser);
        Utilisateur utilisateur=null;
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        model.addAttribute("utilisateurConnecte",utilisateurConnecte);
        if(u.isPresent()) {
            utilisateur=u.get();
            if (utilisateur.getIdUser()==utilisateurConnecte.getIdUser()){
                model.addAttribute("utilisateur",utilisateur);
                iClimbMetier.logger().info("L'utilisateur "+utilisateurConnecte.getUsername()+" accède à sa page d'édition de profile");
                return "editFormUtilisateur";
            }else return "403";
        }
        return "profile";

    }

    /**
     *this method check the user and the disponibility of username and email
     * return confirmation page
     * @param model instance of the model
     * @param utilisateur an object utilisateur
     * @param idUser  id of the user
     * @param bindingResult handle the errors
     * @return a confirmation page
     */
    @PostMapping(value = "/utilisateur/{idUser}/save")
    public String saveEditedUtilisateur(Model model, @Valid Utilisateur utilisateur,
                                 @PathVariable("idUser") Long idUser,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormUtilisateur";
        }
        Optional<Utilisateur> u=utilisateurRepository.findById(idUser);
        Utilisateur utilisateurDB=null;
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        if (utilisateurRepository.findByUsername(utilisateur.getUsername())==null && utilisateurRepository.findByEmail(utilisateur.getEmail())==null ) {
            if (utilisateurConnecte.getIdUser()==idUser){
                utilisateurDB.setUsername(utilisateur.getUsername());
                utilisateurDB.setLastname(utilisateur.getLastname());
                utilisateurDB.setFirstname(utilisateur.getFirstname());
                utilisateurDB.setEmail(utilisateur.getEmail());
                utilisateurDB.setPassword(utilisateur.getPassword());
                model.addAttribute("utilisateur",utilisateurDB);
                utilisateurRepository.save(utilisateurDB);
                iClimbMetier.logger().info("L'utilisateur "+utilisateurConnecte+" a modifié ses informations");
                return "confirmationEditedUtilisateur";
            }else return "403";
        }else {
            if (utilisateurRepository.findByUsername(utilisateur.getUsername())!=null ){
                bindingResult.rejectValue("utilisateur.username", "utilisateur.username", "ce pseudo est déjà utilisé :(");
            }
            if (utilisateurRepository.findByEmail(utilisateur.getEmail())!=null){
                bindingResult.rejectValue("utilisateur.email", "utilisateur.email", "cet e-mail est déjà associé à un autre compte :(");
            }
            return "redirect:/utilisateur/"+idUser+"/edit";
        }


    }

    /**
     * this method check the role of user and display administration page
     * @param model instance oof the model
     * @param pageUtilisateur  the number of the page of user the admin is browsing
     * @param sizeUtilisateur the number of users by page
     * @return the administration page
     */
    @GetMapping(value = "/administration")
    public String administration(Model model,
                        @RequestParam(name="pageUtilisateur",defaultValue = "0") int pageUtilisateur,
                        @RequestParam(name = "sizeUtilisateur",defaultValue = "4") int sizeUtilisateur){

        Page<Utilisateur> pageUtilisateurs= iClimbMetier.listUtilisateur(pageUtilisateur,sizeUtilisateur);
        model.addAttribute("listUtilisateur",pageUtilisateurs.getContent());
        int[] pagesUtilisateur=new int[pageUtilisateurs.getTotalPages()];
        int paginationEnablerUtilisateur=pagesUtilisateur.length;
        Utilisateur utilisateurConnecte = iClimbMetier.userConnected();
        if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_ADMIN)){
            model.addAttribute("paginationEnablerUtilisateur",paginationEnablerUtilisateur);
            model.addAttribute("pagesUtilisateur",pagesUtilisateur);
            model.addAttribute("pageCouranteUtilisateur",pageUtilisateur);
            model.addAttribute("sizeUtilisateur",sizeUtilisateur);
            iClimbMetier.logger().info("L'administrateur " +utilisateurConnecte.getUsername()+ "veut accéder à la page d'administration");
            return "administration";
        }else return "403";

    }

    /**
     * this method check if user is admin and give admin right to another user
     * @param model instance of the model
     * @param idUser id of user
     * @param pageUtilisateur the number of the page of user the admin is browsing
     * @param sizeUtilisateur the number of users by page
     * @return the administration page
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" est devenu admin");
                return "redirect:/administration?pageUtilisateur="+pageUtilisateur;
            }else return "administration";
        }else return "403";
    }

    /**
     * this method check if user is admin and give user right to another user
     * @param model instance of the model
     * @param idUser id of user
     * @param pageUtilisateur the number of the page of user the admin is browsing
     * @param sizeUtilisateur the number of users by page
     * @return the administration page
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" est devenu utilisateur");
                return "redirect:/administration?pageUtilisateur="+pageUtilisateur;
            }else return "administration";
        }else return "403";
    }

    /**
     * this method will try to find an user with keyword "mc"
     * and display a list of user who contains the keyword
     * @param model instance of the model
     * @param mc keyword
     * @param pageUtilisateur the number of the page of user the admin is browsing
     * @param sizeUtilisateur the number of users by page
     * @return the administration page
     */
    @GetMapping(value = "/utilisateur/find")
    public String findUser(Model model,
                           @RequestParam(value = "motCle", defaultValue = "")String mc,
                           @RequestParam(name="pageUtilisateur",defaultValue = "0") int pageUtilisateur,
                           @RequestParam(name = "sizeUtilisateur",defaultValue = "4") int sizeUtilisateur){
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
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
        iClimbMetier.logger().info("L'administrateur " +utilisateurConnecte.getUsername()+ "cherche un utilisatuer contenant "+mc);
        return "administration";
    }

}
