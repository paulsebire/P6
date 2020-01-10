package com.ocr.projet6.web;

import com.ocr.projet6.Metier.ClimbMetierImpl;
import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.CotationRepository;
import com.ocr.projet6.dao.LongueurRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class VoieController {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private IClimbMetier iClimbMetier;
    @Autowired
    private CotationRepository cotationRepository;


    @GetMapping(value = "/site/{idSite}/voie/add")
    public String addVoie(Model model,@PathVariable("idSite") Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Utilisateur utilisateur=iClimbMetier.userConnected();
        List<Cotation> cotationList=cotationRepository.findAll();
        Long idCotation=null;
        Voie voie=new Voie();
        if (s.isPresent()){
            Site sit=s.get();
            if (utilisateur.getIdUser()==sit.getUtilisateur().getIdUser()){
                voie.setSite(sit);
                model.addAttribute("listCotation",cotationList);
                model.addAttribute("cotationId",idCotation);
                model.addAttribute("voie",voie);
                return "addFormVoie";
            } return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }

    @GetMapping(value = "/site/{idSite}/voie/{idVoie}/delete")
    public String deleteVoie(@PathVariable("idVoie")Long idVoie,
                             @PathVariable("idSite")Long idSite,
                             @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                             @RequestParam(name = "sizeVoie",defaultValue = "2") int sizeVoie){
        Optional<Voie> v=voieRepository.findById(idVoie);
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Voie voie=null;
        if (v.isPresent()){
            voie=v.get();
            if (utilisateurConnecte.getIdUser()==voie.getSite().getUtilisateur().getIdUser()){
                voieRepository.deleteById(idVoie);
            } else return "403";
        }

        return "redirect:/site/"+idSite+"/consult?pageVoie="+pageVoie+"&sizeVoie="+sizeVoie;}


    @GetMapping(value = "/site/{idSite}/voie/{idVoie}/edit")
    public String editVoie(Model model,
                           @PathVariable("idSite") Long idSite,
                           @PathVariable("idVoie") Long idVoie) {
        Optional<Voie> v=voieRepository.findById(idVoie);
        Optional<Site> s=siteRepository.findById(idSite);
        List<Cotation> cotationList=cotationRepository.findAll();
        Utilisateur utilisateur=iClimbMetier.userConnected();
        Site sit=null;
        Voie voi = null;
        Long cotationId=null;
        if(v.isPresent()&&s.isPresent()) {
            voi = v.get();
            sit=s.get();
            voi.setSite(sit);
            if (utilisateur.getIdUser()==sit.getUtilisateur().getIdUser()){
                model.addAttribute("voie", voi);
                model.addAttribute("cotationId",cotationId);
                model.addAttribute("listCotation",cotationList);
                return "editFormVoie";
            }return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }

    @PostMapping(value = "/site/{idSite}/voie/{idVoie}/save")
    public String saveEditedVoie(Model model, @Valid Voie voie,
                           @PathVariable("idSite") Long idSite,
                           @PathVariable("idVoie") Long idVoie,
                           @RequestParam("cotationId") Long cotationId,
                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormVoie";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Optional<Cotation> c=cotationRepository.findById(cotationId);
        Utilisateur utilisateur= iClimbMetier.userConnected();
        if (s.isPresent()&&c.isPresent()){
            Cotation cote=c.get();
            Site sit=s.get();
            voie.setSite(sit);
            if (idVoie!=null) voie.setIdVoie(idVoie);
            formatField(voie);
            if (utilisateur.getIdUser()==sit.getUtilisateur().getIdUser()){
                voie.setCotation(cote);
                voieRepository.save(voie);
                model.addAttribute("voie",voie);
                return "confirmationVoie";
            }return "403";

        }
        return "redirect:/site/"+idSite+"/consult";
    }
    @PostMapping(value = "/site/{idSite}/voie/save")
    public String saveNewVoie(Model model, @Valid Voie voie,
                           @PathVariable("idSite") Long idSite,
                           @RequestParam("cotationId") Long cotationId,
                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormVoie";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Optional<Cotation> c=cotationRepository.findById(cotationId);
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if (s.isPresent() && c.isPresent()){
            Cotation cote=c.get();
            Site sit=s.get();
            if (utilisateur.getIdUser()==sit.getUtilisateur().getIdUser()){
                voie.setCotation(cote);
                voie.setSite(sit);
                voieRepository.save(voie);
                model.addAttribute("voie",voie);
                return "confirmationVoie";
            }
            return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }

    public void formatField(Voie voie){
        String formatedName= ClimbMetierImpl.formatString(voie.getNomVoie());
        voie.setNomVoie(formatedName);

        String formatedSecteur= ClimbMetierImpl.formatString(voie.getSecteur());
        voie.setSecteur(formatedSecteur);
        return;
    }




}
