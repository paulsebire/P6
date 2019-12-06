package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.LongueurRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @RequestMapping(value = "/search")
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
            Page<Site> pageSites = siteRepository.chercher("%" + mc + "%", PageRequest.of(pageSite, sizeSite));
            model.addAttribute("listSite", pageSites.getContent());

            int[] pagesSites = new int[pageSites.getTotalPages()];
            System.out.println("nbSites=" + pagesSites.length);
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
                            @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur
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
            model.addAttribute("listLongueur",pageLongueurs.getContent());
            int[] pagesLongueur=new int[pageLongueurs.getTotalPages()];
            model.addAttribute("paginationEnablerLongueur",pagesLongueur.length);
            model.addAttribute("pagesLongueur",pagesLongueur);
            model.addAttribute("pageCouranteLongueur",pageLongueur);
            model.addAttribute("sizeLongueur",sizeLongueur);
        }catch (Exception e){
            model.addAttribute("exception",e);
        }

        return "sitesDisplay";
    }



    @GetMapping(value = "/site/add")
    public String addSite(Model model){
        Site site=new Site();
        System.out.println("idSite"+site.getIdSite());
        model.addAttribute("site",site);
        return "addFormSite";
    }

    @PostMapping(value = "/site/save")
    public String saveSite(Model model,@Valid Site site, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "addFormSite";
        }
        siteRepository.save(site);
        model.addAttribute("site",site);
        return "confirmationSite";
    }

    @RequestMapping(value = "/site/id/{idSite}/edit")
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


}
