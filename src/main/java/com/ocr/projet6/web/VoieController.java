package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.CotationRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.Cotation;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.entities.Voie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
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

    /**
     * this method check the user and return to add Voie form
     * @param model instance of the model
     * @param idSite id of site
     * @return the form to add a voie
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername() +" veut créer une nouvelle voie pour le site "+sit.getNameSite());
                return "addFormVoie";
            } return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }

    /**
     * this method check the user and delete the voie
     * @param idVoie id of voie
     * @param idSite id  of site
     * @param pageVoie the number of the page user is browsing
     * @param sizeVoie the number of element  displayed by page
     * @return the page with all informations about a site
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateurConnecte.getUsername() +" veut supprimer la voie "+voie.getNomVoie()+" du site "+voie.getSite().getNameSite());
            } else return "403";
        }

        return "redirect:/site/"+idSite+"/consult?pageVoie="+pageVoie+"&sizeVoie="+sizeVoie;}

    /**
     * his method check the user and return to edit Voie form
     * @param model instance of the model
     * @param idSite id of site
     * @param idVoie id of voie
     * @return the form to edit a voie
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername() +" veut éditer la voie "+voi.getNomVoie()+" pour le site "+sit.getNameSite());
                return "editFormVoie";
            }return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }

    /**
     * this method check the user and save the edited voie in the DB
     * return confirmation page
     * @param model instance of the model
     * @param voie an object  voie
     * @param idSite id of a site
     * @param idVoie id of a voie
     * @param cotationId id of a cotation
     * @param bindingResult handle the errors
     * @return a confirmation page
     */
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
            if (utilisateur.getIdUser()==sit.getUtilisateur().getIdUser()){
                voie.setCotation(cote);
                voieRepository.save(voie);
                model.addAttribute("voie",voie);
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername() +" a sauvegardé l'édition de la voie "+voie.getNomVoie() +" pour le site "+sit.getNameSite());
                return "confirmationVoie";
            }return "403";

        }
        return "redirect:/site/"+idSite+"/consult";
    }

    /**
     * this method check the user and save the new voie in the DB
     * return confirmation page
     * @param model instance of model
     * @param voie an object voie
     * @param idSite id of a site
     * @param cotationId id of a cotation
     * @param bindingResult handle the errors
     * @return  a confirmation page
     */
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
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername() +" a sauvegardé la nouvelle voie "+voie.getNomVoie() +" pour le site "+sit.getNameSite());
                return "confirmationVoie";
            }
            return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }




}
