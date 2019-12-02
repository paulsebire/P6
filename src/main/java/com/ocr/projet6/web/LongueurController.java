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
public class LongueurController {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private LongueurRepository longueurRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private IClimbMetier iClimbMetier;

    @GetMapping(value = "/sites{idSite}/voies/longueurs/add")
    public String addLongueur(Model model,@PathVariable("idSite") Long idSite,
                              @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                              @RequestParam(name = "sizeVoie",defaultValue = "2") int sizeVoie){

        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        model.addAttribute("site",sit);
        Page<Voie> pageVoies= iClimbMetier.listVoie(idSite,pageVoie,sizeVoie);
        model.addAttribute("listVoie",pageVoies.getContent());
        Longueur longueur=new Longueur();
        model.addAttribute("longueur",longueur);
        Long idVoieNew=null;
        model.addAttribute("idVoieNew",idVoieNew);
        return "addFormLongueur";
    }

    @GetMapping(value = "/longueurs/{idLongueur}/delete")
    public String deleteLongueur(@PathVariable("idLongueur") Long idLongueur,
                                 @RequestParam(name="pageLongueur",defaultValue = "0") int pageLongueur,
                                 @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur,
                                 String nameSite){
        longueurRepository.deleteById(idLongueur);
        return "redirect:/sites/consult/nomDuSite?nameSite="+nameSite+"&pageLongueur="+pageLongueur+"&sizeLongueur="+sizeLongueur;}



    @RequestMapping(value = "/sites/{idSite}/longueurs/{idLongueur}/edit")
    public  String  editLongueur(Model model,@PathVariable("idSite") Long idSite,
                                 @PathVariable("idLongueur") Long idLongueur ,
                                 @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                                 @RequestParam(name = "sizevoie",defaultValue = "2") int sizeVoie){
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        model.addAttribute("site",sit);
        Page<Voie> pageVoies= iClimbMetier.listVoie(idSite,pageVoie,sizeVoie);
        model.addAttribute("listVoie",pageVoies.getContent());
        Long idVoieNew=null;
        model.addAttribute("idVoieNew",idVoieNew);
        Optional<Longueur> l=longueurRepository.findById(idLongueur);
        Longueur lg=null;
        if(l.isPresent()) {
            lg=l.get();
            model.addAttribute("longueur",lg);
        }
        return "editFormLongueur";
    }

    @PostMapping(value = "/longueurs/{idLongueur}/save")
    public String saveEditedLongueur(Model model,
                               @Valid Longueur longueur,
                               @RequestParam("idVoieNew") Long idVoieNew,
                               @PathVariable("idLongueur") Long idLongueur,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormLongueur";
        }
        System.out.println("idVoieNew= "+idVoieNew);
        Optional<Voie> v=voieRepository.findById(idVoieNew);
        Voie voi=v.get();
        longueur.setVoie(voi);
        longueur.setIdLongueur(idLongueur);
        longueurRepository.save(longueur);
        model.addAttribute("longueur",longueur);
        return "confirmationLongueur";
    }

    @PostMapping(value = "/sites/{idSite}/voies/longueurs/save")
    public String saveNewLongueur(Model model,
                                     @Valid Longueur longueur,
                                     @RequestParam("idVoieNew") Long idVoieNew,
                                     @PathVariable("idSite") Long idSite,
                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormLongueur";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        Optional<Voie> v=voieRepository.findById(idVoieNew);
        Voie voi=v.get();
        voi.setSite(sit);
        longueur.setVoie(voi);
        longueurRepository.save(longueur);
        model.addAttribute("longueur",longueur);
        return "confirmationLongueur";
    }


}
