package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SiteController {
    @Autowired
    private IClimbMetier iClimbMetier;

    @RequestMapping(value = "/sites")
    public String sites(){
        return "sites";
    }

    @RequestMapping(value = "/consulterSite")
    public String consulter(Model model, Long idSite,
                            @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                            @RequestParam(name = "sizevoie",defaultValue = "4") int sizeVoie,
    @RequestParam(name="pageLongueur",defaultValue = "0") int pageLongueur,
    @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur){
        model.addAttribute("idSite",idSite);
        try {
            Site site = iClimbMetier.consulterSite(idSite);
            Page<Voie> pageVoies= iClimbMetier.listVoie(idSite,pageVoie,sizeVoie);
            model.addAttribute("listVoie",pageVoies.getContent());
            int[] pagesVoie=new int[pageVoies.getTotalPages()];
            model.addAttribute("paginationEnablerVoie",pagesVoie.length);
            model.addAttribute("pagesVoie",pagesVoie);
            model.addAttribute("pageCouranteVoie",pageVoie);
            model.addAttribute("site", site);

            /*Page<Longueur> pageLongueurs= iClimbMetier.listLongueur(XXX,pageLongueur,sizeLongueur);
            model.addAttribute("listLongueur",pageLongueurs.getContent());
            int[] pagesLongueur=new int[pageLongueurs.getTotalPages()];
            model.addAttribute("paginationEnablerLongueur",pagesLongueur.length);
            model.addAttribute("pagesLongueur",pagesLongueur);
            model.addAttribute("pageCourante",pageLongueur);*/

        }catch (Exception e){
            model.addAttribute("exception",e);
        }

        return "sites";
    }

    @RequestMapping(value = "/inscription")
    public String inscriptionform(){
        return "inscription";
    }

}
