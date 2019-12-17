package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.CommentaireRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.entities.Commentaire;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.entities.Voie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
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
            Utilisateur utilisateur=userConnected();
            commentaire.setUtilisateur(utilisateur);
            model.addAttribute("utilisateurConnecte",utilisateur);
            commentaireRepository.save(commentaire);

/*
            Page<Commentaire> pageCommentaires= iClimbMetier.listCommentaireBySite(sit.getIdSite(),pageCommentaire,sizeCommentaire);
            model.addAttribute("listCommentaire",pageCommentaires.getContent());
            int[] pagesCommentaire=new int[pageCommentaires.getTotalPages()];
            int paginationEnablerCommentaire=pagesCommentaire.length;
            if (paginationEnablerCommentaire<=1) pageCommentaire=0;
            model.addAttribute("paginationEnablerCommentaire",paginationEnablerCommentaire);
            model.addAttribute("pagesCommentaire",pagesCommentaire);
            model.addAttribute("pageCouranteVoie",pageCommentaire);
            model.addAttribute("sizeCommentaire",sizeCommentaire);
*/

        }
        return "redirect:/site/"+idSite+"/consult";
    }

    public Utilisateur userConnected(){
        Utilisateur utilisateurConnecte= (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return utilisateurConnecte;
    }

}
