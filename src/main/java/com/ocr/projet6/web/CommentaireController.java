package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.CommentaireRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.entities.Commentaire;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * this method check user and add a comment to a site identified by idSite
     * @param model
     * @param idSite
     * @param commentaire
     * @param pageCommentaire
     * @param sizeCommentaire
     * @return
     */
    @PostMapping(value = "/site/{idSite}/commentaire/save")
    public String addCommentaire (Model model, @PathVariable(value = "idSite")Long idSite, @Valid Commentaire commentaire,
                                  @RequestParam(name="pageCommentaire",defaultValue = "0") int pageCommentaire,
                                  @RequestParam(name = "sizeCommentaire",defaultValue = "2") int sizeCommentaire){
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=null;
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if(s.isPresent()) {
            sit=s.get();
            if (utilisateur.getRoles().contains(RoleEnum.ROLE_USER)){
                model.addAttribute("site",sit);
                commentaire.setDate(new Date());
                commentaire.setSite(sit);
                commentaire.setUtilisateur(utilisateur);
                model.addAttribute("utilisateurConnecte",utilisateur);
                commentaireRepository.save(commentaire);
            }else return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }

    /**
     * this method delete a comment on a site identified by idSite
     * @param idCommentaire
     * @param principal
     * @param idSite
     * @return
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateurConnecte.getUsername()+" a supprimer le commentaire "+commentaire.getId());
                commentaireRepository.deleteById(idCommentaire);
            } else return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }

    /**
     * this method display the form to edit a comment
     * @param model
     * @param idSite
     * @param idCommentaire
     * @return
     */
    @GetMapping(value = "/site/{idSite}/commentaire/{idCommentaire}/edit")
    public String editCommentaire(Model model,
                           @PathVariable("idSite") Long idSite,
                           @PathVariable("idCommentaire") Long idCommentaire) {
        Optional<Commentaire> c=commentaireRepository.findById(idCommentaire);
        Commentaire commentaire = null;
        Optional<Site> s=siteRepository.findById(idSite);
        Site site=null;
        Utilisateur utilisateurConnecte = iClimbMetier.userConnected();
        if(c.isPresent()&&s.isPresent()) {
            commentaire = c.get();
            site=s.get();
            if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_ADMIN)||utilisateurConnecte.getIdUser()==commentaire.getUtilisateur().getIdUser()){
                model.addAttribute("site",site);
                model.addAttribute("commentaire", commentaire);
                iClimbMetier.logger().info("L'utilisateur "+utilisateurConnecte.getUsername()+" veut éditer le commentaire "+commentaire.getId());
                return "editFormCommentaire";
            }else return "403";
        }else return "redirect:/site/"+idSite+"/consult";
    }

    /**
     * this method check user and save an edited comment to a site identified by idSite
     * @param model
     * @param commentaire
     * @param idSite
     * @param idCommentaire
     * @param bindingResult
     * @return
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" a édité le commentaire "+com.getId());
                return "confirmationCommentaire";
            }
            return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }


}
