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
public class VoieController {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private LongueurRepository longueurRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private IClimbMetier iClimbMetier;

    @GetMapping(value = "/site/{idSite}/voies/add")
    public String addVoie(Model model,@PathVariable("idSite") Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        Voie voie=new Voie();
        voie.setSite(sit);
        model.addAttribute("voie",voie);
        return "addFormVoie";
    }

    @GetMapping(value = "/site/{idSite}/voies/{idVoie}/delete")
    public String deleteVoie(@PathVariable("idVoie")Long idVoie,
                             @PathVariable("idSite")Long idSite,
                             @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                             @RequestParam(name = "sizeVoie",defaultValue = "2") int sizeVoie){
            longueurRepository.deleteByVoie(idVoie);
            voieRepository.deleteById(idVoie);
        return "redirect:/sites/"+idSite+"/consult?pageVoie="+pageVoie+"&sizeVoie="+sizeVoie;}


    @GetMapping(value = "/site/{idSite}/voies/{idVoie}/edit")
    public String editVoie(Model model,
                           @PathVariable("idSite") Long idSite,
                           @PathVariable("idVoie") Long idVoie) {
        Optional<Voie> v=voieRepository.findById(idVoie);
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=null;
        Voie voi = null;
        if(v.isPresent()&&s.isPresent()) {
            voi = v.get();
            sit=s.get();
            voi.setSite(sit);
            model.addAttribute("voie", voi);
        }
        return "editFormVoie";
    }

    @PostMapping(value = "/site/{idSite}/voie/{idVoie}/save")
    public String saveEditedVoie(Model model, @Valid Voie voie,
                           @PathVariable("idSite") Long idSite,
                           @PathVariable("idVoie") Long idVoie,
                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormVoie";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        voie.setSite(sit);
        if (idVoie!=null) voie.setIdVoie(idVoie);
        voieRepository.save(voie);
        model.addAttribute("voie",voie);
        return "confirmationVoie";
    }
    @PostMapping(value = "/site/{idSite}/voie/save")
    public String saveNewVoie(Model model, @Valid Voie voie,
                           @PathVariable("idSite") Long idSite,
                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormVoie";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        voie.setSite(sit);
        voieRepository.save(voie);
        model.addAttribute("voie",voie);
        return "confirmationVoie";
    }




}
