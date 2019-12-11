package com.ocr.projet6.web;

import com.ocr.projet6.Metier.ClimbMetierImpl;
import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.TopoRepository;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.dao.UtilisateurRepository;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.security.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.ocr.projet6.Metier.RoleDefinition;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import java.security.Principal;

import static com.ocr.projet6.Metier.RoleDefinition.userRole;


@Controller
public class UtilisateurController {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private IClimbMetier iClimbMetier;
    @Autowired
    private TopoRepository topoRepository;

    @GetMapping(value = "/utilisateur/inscription")
    public String inscriptionForm(Model model){
        Utilisateur utilisateur=new Utilisateur();
        model.addAttribute("utilisateur",utilisateur);
        return "inscription";
    }

    @PostMapping(value = "/utilisateur/create")
    public String saveNewUtilisateur(Model model, @Valid Utilisateur utilisateur, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            try{

            }catch (Exception e){
                model.addAttribute("error",e);
                return "inscription"+"&error="+e.getMessage();
            }

        }
        utilisateur.setRoles(userRole);
        System.out.println("usernameController= "+utilisateur.getUsername());
        utilisateurRepository.save(utilisateur);

        model.addAttribute("utilisateur",utilisateur);
        return "confirmationUtilisateur";
    }

    @GetMapping(value = "/utilisateur/profil" )
    public String userProfile (Model model,Principal principal,
                               @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                               @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo){
        String userName = principal.getName();
        Utilisateur utilisateurConnecte=(Utilisateur)utilisateurService.loadUserByUsername(userName);
        model.addAttribute("utilisateur",utilisateurConnecte);


        Page<Topo> pageTopos = iClimbMetier.listTopoByUtilisateur(utilisateurConnecte.getIdUser(),pageTopo,sizeTopo);
        System.out.println("listtopos = " + pageTopos.getContent());
        model.addAttribute("listTopo",pageTopos.getContent());
        int[] pagesTopo=new int[pageTopos.getTotalPages()];
        int paginationEnablerTopo=pagesTopo.length;
        model.addAttribute("paginationEnablerTopo",paginationEnablerTopo);
        model.addAttribute("pagesTopo",pagesTopo);
        model.addAttribute("pageCouranteTopo",pageTopo);
        model.addAttribute("sizeTopo",sizeTopo);
        return "profile";
    }

}
