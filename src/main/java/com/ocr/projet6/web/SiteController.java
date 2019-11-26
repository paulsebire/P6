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

    @RequestMapping(value = "/sites/search")
    public String sites(Model model){
        return "sites";
    }

    @GetMapping(path = "/sites/consult/nomDuSite")
    public String consulter(Model model,
                            @RequestParam(name = "nameSite", defaultValue = "") String nameSite,
                            @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                            @RequestParam(name = "sizevoie",defaultValue = "2") int sizeVoie,
                            @RequestParam(name="pageLongueur",defaultValue = "0") int pageLongueur,
                            @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur){
        try {
            Site site = iClimbMetier.consulterSite(nameSite);
            model.addAttribute("idSite",site.getIdSite());
            Page<Voie> pageVoies= iClimbMetier.listVoie(site.getIdSite(),pageVoie,sizeVoie);
            model.addAttribute("listVoie",pageVoies.getContent());
            int[] pagesVoie=new int[pageVoies.getTotalPages()];
            int paginationEnablerVoie=pagesVoie.length;
            if (paginationEnablerVoie<=1) pageVoie=0;
            model.addAttribute("paginationEnablerVoie",paginationEnablerVoie);
            model.addAttribute("pagesVoie",pagesVoie);
            model.addAttribute("pageCouranteVoie",pageVoie);
            model.addAttribute("site", site);
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

        return "sites";
    }

    @GetMapping(value = "/utilisateurs/inscription")
    public String inscriptionForm(){
        return "inscription";
    }

    @GetMapping(value = "/sites/add")
    public String addSite(Model model){
        Site site=new Site();
        System.out.println("idSite"+site.getIdSite());
        model.addAttribute("site",site);
        return "addFormSite";
    }

    @PostMapping(value = "/sites/save")
    public String saveSite(Model model,@Valid Site site, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "addFormSite";
        }
        siteRepository.save(site);
        model.addAttribute("site",site);
        return "confirmationSite";
    }

    @RequestMapping(value = "/sites/id/{idSite}/edit")
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
