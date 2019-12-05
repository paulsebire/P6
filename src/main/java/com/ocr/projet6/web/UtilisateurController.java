package com.ocr.projet6.web;



import com.ocr.projet6.dao.UtilisateurRepository;
import com.ocr.projet6.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.ocr.projet6.Metier.RoleDefinition;

import javax.validation.Valid;

import static com.ocr.projet6.Metier.RoleDefinition.userRole;


@Controller
public class UtilisateurController {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping(value = "/utilisateurs/inscription")
    public String inscriptionForm(Model model){
        Utilisateur utilisateur=new Utilisateur();
        model.addAttribute("utilisateur",utilisateur);
        return "inscription";
    }

    @PostMapping(value = "/utilisateur/create")
    public String saveNewUtilisateur(Model model, @Valid Utilisateur utilisateur, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "inscription";
        }
        utilisateur.setRoles(userRole);
        utilisateurRepository.save(utilisateur);
        model.addAttribute("utilisateur",utilisateur);
        return "confirmationUtilisateur";
    }
}
