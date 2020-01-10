package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.CommentaireRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.UtilisateurRepository;
import com.ocr.projet6.entities.*;
import com.ocr.projet6.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Controller
public class CommentaireController {

    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private CommentaireRepository commentaireRepository;
    @Autowired
    private IClimbMetier iClimbMetier;

    @PostMapping(value = "/site/{idSite}/commentaire/save")
    public String addCommentaire (Model model, @PathVariable(value = "idSite")Long idSite, @Valid Commentaire commentaire,
                                  @RequestParam(name="pageCommentaire",defaultValue = "0") int pageCommentaire,
                                  @RequestParam(name = "sizeCommentaire",defaultValue = "2") int sizeCommentaire){
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=null;
        if(s.isPresent()) {
            sit=s.get();
            model.addAttribute("site",sit);
            commentaire.setDate(new Date());
            commentaire.setSite(sit);
            Utilisateur utilisateur=iClimbMetier.userConnected();
            commentaire.setUtilisateur(utilisateur);
            model.addAttribute("utilisateurConnecte",utilisateur);
            commentaireRepository.save(commentaire);

        }
        return "redirect:/site/"+idSite+"/consult";
    }

    @GetMapping(value = "/site/{idSite}/commentaire/{idCommentaire}/delete")
    public String deleteCommentaire(@PathVariable("idCommentaire")Long idCommentaire,
                             Principal principal,
                             @PathVariable("idSite")Long idSite){
        Optional<Commentaire> c=commentaireRepository.findById(idCommentaire);
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Commentaire commentaire=null;
        if (c.isPresent()){
            commentaire=c.get();
            if (utilisateurConnecte.getIdUser()==commentaire.getUtilisateur().getIdUser()||utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_ADMIN)){
                commentaireRepository.deleteById(idCommentaire);
            } else return "403";
        }

        return "redirect:/site/"+idSite+"/consult";}


    @GetMapping(value = "/site/{idSite}/commentaire/{idCommentaire}/edit")
    public String editCommentaire(Model model,
                           @PathVariable("idSite") Long idSite,
                           @PathVariable("idCommentaire") Long idCommentaire) {
        Optional<Commentaire> c=commentaireRepository.findById(idCommentaire);
        Commentaire commentaire = null;
        Optional<Site> s=siteRepository.findById(idSite);
        Site site=null;
        if(c.isPresent()&&s.isPresent()) {
            commentaire = c.get();
            site=s.get();
            model.addAttribute("site",site);
            model.addAttribute("commentaire", commentaire);
        }

        return "editFormCommentaire";
    }
    @PostMapping(value = "/site/{idSite}/commentaire/{idCommentaire}/save")
    public String saveEditedCommentaire(Model model, @Valid Commentaire commentaire,
                                        @PathVariable("idSite") Long idSite,
                                        @PathVariable("idCommentaire") Long idCommentaire,
                                        BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormCommentaire";
        }
        Optional<Commentaire> c=commentaireRepository.findById(idCommentaire);
        Commentaire com=null;
        Optional<Site> s=siteRepository.findById(idSite);
        Site site=null;
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if(s.isPresent()&&c.isPresent()) {
            com=c.get();
            site=s.get();
            if (utilisateur.getIdUser()==com.getSite().getUtilisateur().getIdUser()||utilisateur.getRoles().contains(RoleEnum.ROLE_ADMIN)){
                com.setContenu(commentaire.getContenu());
                commentaire.setSite(site);
                System.out.println(com.getDate());
                model.addAttribute("site",site);
                model.addAttribute("commentaire",com);
                commentaireRepository.save(com);
                return "confirmationCommentaire";
            }
            return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }


}
