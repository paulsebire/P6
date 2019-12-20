package com.ocr.projet6.web;

import com.ocr.projet6.Metier.ClimbMetierImpl;
import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.LongueurRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.entities.Voie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping(value = "/site/{idSite}/voie/longueur/add")
    public String addLongueur(Model model,@PathVariable("idSite") Long idSite,
                              @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                              @RequestParam(name = "sizeVoie",defaultValue = "2") int sizeVoie){
        Utilisateur utilisateur=userConnected();
        if (utilisateur.getRoles().toString().contains("USER")) {
            Optional<Site> s = siteRepository.findById(idSite);
            Site sit = s.get();
            model.addAttribute("site", sit);
            Page<Voie> pageVoies = iClimbMetier.listVoie(idSite, pageVoie, sizeVoie);
            model.addAttribute("listVoie", pageVoies.getContent());
            Longueur longueur = new Longueur();
            model.addAttribute("longueur", longueur);
            Long idVoieNew = null;
            model.addAttribute("idVoieNew", idVoieNew);
            return "addFormLongueur";
        }
        return "403";
    }

    @GetMapping(value = "/site/{idSite}/longueur/{idLongueur}/delete")
    public String deleteLongueur(@PathVariable("idLongueur") Long idLongueur,
                                 @RequestParam(name="pageLongueur",defaultValue = "0") int pageLongueur,
                                 @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur,
                                 @PathVariable("idSite") Long idSite){
        Optional<Longueur> l=longueurRepository.findById(idLongueur);
        Utilisateur utilisateurConnecte=userConnected();
        Longueur longueur=null;
        if (l.isPresent()){
            longueur=l.get();
            if (utilisateurConnecte.getIdUser()==longueur.getVoie().getSite().getUtilisateur().getIdUser()&&utilisateurConnecte.getRoles().toString().contains("USER")){
                longueurRepository.deleteById(idLongueur);
            } else return "403";
        }

        return "redirect:/site/"+idSite+"/consult&pageLongueur="+pageLongueur+"&sizeLongueur="+sizeLongueur;}



    @RequestMapping(value = "/site/{idSite}/longueur/{idLongueur}/edit")
    public  String  editLongueur(Model model,@PathVariable("idSite") Long idSite,
                                 @PathVariable("idLongueur") Long idLongueur ,
                                 @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                                 @RequestParam(name = "sizevoie",defaultValue = "2") int sizeVoie){
        Utilisateur utilisateur=userConnected();
        Optional<Site> s=siteRepository.findById(idSite);
        if (s.isPresent()) {
            Site sit = s.get();
            if (utilisateur.getRoles().toString().contains("USER") && utilisateur.getIdUser() == sit.getUtilisateur().getIdUser()) {
                model.addAttribute("site", sit);
                Page<Voie> pageVoies = iClimbMetier.listVoie(idSite, pageVoie, sizeVoie);
                model.addAttribute("listVoie", pageVoies.getContent());
                Long idVoieNew = null;
                model.addAttribute("idVoieNew", idVoieNew);
                Optional<Longueur> l = longueurRepository.findById(idLongueur);
                Longueur lg = null;
                if (l.isPresent()) {
                    lg = l.get();
                    model.addAttribute("longueur", lg);
                }
                return "editFormLongueur";
            }
            return "403";
        }return "redirect:/site/"+idSite+"/consult";
    }

    @PostMapping(value = "/longueur/{idLongueur}/save")
    public String saveEditedLongueur(Model model,
                               @Valid Longueur longueur,
                               @RequestParam("idVoieNew") Long idVoieNew,
                               @PathVariable("idLongueur") Long idLongueur,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormLongueur";
        }
        Utilisateur utilisateur=userConnected();
        Optional<Voie> v=voieRepository.findById(idVoieNew);
        if (v.isPresent()){
            Voie voi=v.get();
            if (utilisateur.getIdUser()==voi.getSite().getUtilisateur().getIdUser()){
                longueur.setVoie(voi);
                longueur.setIdLongueur(idLongueur);
                formatField(longueur);
                longueurRepository.save(longueur);
                model.addAttribute("longueur",longueur);
                return "confirmationLongueur";
            }
            return "403";
        }
        return "editFormLongueur";
    }

    @PostMapping(value = "/site/{idSite}/voie/longueur/save")
    public String saveNewLongueur(Model model,
                                     @Valid Longueur longueur,
                                     @RequestParam("idVoieNew") Long idVoieNew,
                                     @PathVariable("idSite") Long idSite,
                                     BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormLongueur";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Optional<Voie> v=voieRepository.findById(idVoieNew);
        Utilisateur utilisateur=userConnected();
        if (s.isPresent()&&v.isPresent()){
            Site sit=s.get();
            Voie voi=v.get();
            if (utilisateur.getIdUser()==sit.getUtilisateur().getIdUser()){
                voi.setSite(sit);
                longueur.setVoie(voi);
                formatField(longueur);
                longueurRepository.save(longueur);
                model.addAttribute("longueur",longueur);
                return "confirmationLongueur";
            }
             return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }


    public void formatField(Longueur longueur){
        String formatedName= ClimbMetierImpl.formatString(longueur.getNomLongueur());
        longueur.setNomLongueur(formatedName);
        return;
    }
    public static Utilisateur userConnected(){
        Utilisateur utilisateurConnecte= (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return utilisateurConnecte;
    }
}
