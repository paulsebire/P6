package com.ocr.projet6.web;

import com.ocr.projet6.Metier.ClimbMetierImpl;
import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.LongueurRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.UtilisateurRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.*;
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

@Controller
public class SiteController {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private LongueurRepository longueurRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private IClimbMetier iClimbMetier;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @RequestMapping(value = "/site/search")
    public String sites(Model model,
                        @RequestParam(name="pageSite",defaultValue = "0") int pageSite,
                        @RequestParam(name = "sizeSite",defaultValue = "4") int sizeSite){
        Page<Site> pageSites= iClimbMetier.listSite(pageSite,sizeSite);
        model.addAttribute("listSite",pageSites.getContent());
        int[] pagesSite=new int[pageSites.getTotalPages()];
        int paginationEnablerSite=pagesSite.length;
        model.addAttribute("paginationEnablerSite",paginationEnablerSite);
        model.addAttribute("pagesSite",pagesSite);
        model.addAttribute("pageCouranteSite",pageSite);
        model.addAttribute("sizeSite",sizeSite);
        return "sites";
    }


    @GetMapping(path = "/site/find")
    public String find(Model model,
                       @RequestParam(name="pageSite",defaultValue = "0") int pageSite,
                       @RequestParam(name = "sizeSite",defaultValue = "2") int sizeSite,
                       @RequestParam(name = "motCle", defaultValue = "")String mc){
        try {
            String formatedMc = ClimbMetierImpl.formatString(mc);
            Page<Site> pageSites = siteRepository.chercher("%" + formatedMc + "%", PageRequest.of(pageSite, sizeSite));
            model.addAttribute("listSite", pageSites.getContent());

            int[] pagesSites = new int[pageSites.getTotalPages()];
            model.addAttribute("pagesSite", pagesSites);
            int paginationEnablerSite = pagesSites.length;
            model.addAttribute("paginationEnablerSite", paginationEnablerSite);
            model.addAttribute("pagesSite", pagesSites);
            model.addAttribute("pageCouranteSite", pageSites);
            model.addAttribute("sizeSite", sizeSite);
            model.addAttribute("motCle", mc);
        }catch (Exception e){
            model.addAttribute("exception",e);
            throw new RuntimeException("Site Introuvable");
        }
        return "sites";
    }

    @GetMapping(path = "/site/{idSite}/consult")
    public String consulter(Model model,
                            @PathVariable(name = "idSite") Long idSite,
                            @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                            @RequestParam(name = "sizeVoie",defaultValue = "2") int sizeVoie,
                            @RequestParam(name="pageLongueur",defaultValue = "0") int pageLongueur,
                            @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur,
                            @RequestParam(name="pageCommentaire",defaultValue = "0") int pageCommentaire,
                            @RequestParam(name = "sizeCommentaire",defaultValue = "2") int sizeCommentaire
                            ){
        try {
            Optional<Site> s=siteRepository.findById(idSite);
            Site site=null;
            if(s.isPresent()) {
                site=s.get();
            }
            model.addAttribute("site", site);
            Page<Voie> pageVoies= iClimbMetier.listVoie(site.getIdSite(),pageVoie,sizeVoie);
            model.addAttribute("listVoie",pageVoies.getContent());
            int[] pagesVoie=new int[pageVoies.getTotalPages()];
            int paginationEnablerVoie=pagesVoie.length;
            if (paginationEnablerVoie<=1) pageVoie=0;
            model.addAttribute("paginationEnablerVoie",paginationEnablerVoie);
            model.addAttribute("pagesVoie",pagesVoie);
            model.addAttribute("pageCouranteVoie",pageVoie);
            model.addAttribute("sizeVoie",sizeVoie);



            Page<Longueur> pageLongueurs= iClimbMetier.listLongueur(site.getIdSite(),pageLongueur,sizeLongueur);
            System.out.println("lislongueur"+pageLongueurs.getContent());
            model.addAttribute("listLongueur",pageLongueurs.getContent());
            int[] pagesLongueur=new int[pageLongueurs.getTotalPages()];
            model.addAttribute("paginationEnablerLongueur",pagesLongueur.length);
            model.addAttribute("pagesLongueur",pagesLongueur);
            model.addAttribute("pageCouranteLongueur",pageLongueur);
            model.addAttribute("sizeLongueur",sizeLongueur);

            Utilisateur utilisateur=userConnected();
            model.addAttribute("utilisateurConnecte",utilisateur);

            Page<Commentaire> pageCommentaires= iClimbMetier.listCommentaireBySite(site.getIdSite(),pageCommentaire,sizeCommentaire);
            model.addAttribute("listCommentaire",pageCommentaires.getContent());
            int[] pagesCommentaire=new int[pageCommentaires.getTotalPages()];
            int paginationEnablerCommentaire=pagesCommentaire.length;
            if (paginationEnablerCommentaire<=1) pageCommentaire=0;
            model.addAttribute("paginationEnablerCommentaire",paginationEnablerCommentaire);
            model.addAttribute("pagesCommentaire",pagesCommentaire);
            model.addAttribute("pageCouranteCommentaire",pageCommentaire);
            model.addAttribute("sizeCommentaire",sizeCommentaire);
        }catch (Exception e){
            model.addAttribute("exception",e);
        }

        return "sitesDisplay";
    }



    @GetMapping(value = "/site/add")
    public String addSite(Model model){
        Site site=new Site();
        site.setOfficiel(false);
        model.addAttribute("site",site);
        return "addFormSite";
    }

    @PostMapping(value = "/site/save")
    public String saveSite(Model model,@Valid Site site, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "addFormSite";
        }

        Utilisateur utilisateur=userConnected();
        site.setUtilisateur(utilisateur);
        formatField(site);
        siteRepository.save(site);
        model.addAttribute("site",site);
        return "confirmationSite";
    }

    @PostMapping(value = "/site/{idSite}/save")
    public String saveEditedSite(Model model, @Valid Site site,
                                 @PathVariable("idSite") Long idSite,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormVoie";
        }
        site.setIdSite(idSite);
        model.addAttribute("site",site);
        siteRepository.save(site);
        return "confirmationSite";
    }

    @GetMapping(value = "/site/{idSite}/edit/officiel")
    public String rendreOfficiel(@PathVariable("idSite") Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Site site=null;
        if(s.isPresent()) {
            site=s.get();
        }
        site.setOfficiel(!site.isOfficiel());
        siteRepository.save(site);
        return "redirect:/site/"+idSite+"/consult";
    }


    @RequestMapping(value = "/site/{idSite}/edit")
    public String editSite(Model model,
                           @PathVariable("idSite") Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=null;
        if(s.isPresent()) {
            sit=s.get();
            model.addAttribute("site",sit);
        }
        return "editFormSite";
    }

    public Utilisateur userConnected(){
        Utilisateur utilisateurConnecte= (Utilisateur)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return utilisateurConnecte;
    }

    public  void formatField(Site site){
        String formatedLocalisation= ClimbMetierImpl.formatString(site.getLocalisation());
        site.setLocalisation(formatedLocalisation);

        String formatedName= ClimbMetierImpl.formatString(site.getNameSite());
        site.setNameSite(formatedName);
        return;
    }

}
