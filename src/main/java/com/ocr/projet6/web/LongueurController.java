package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.LongueurRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.entities.Voie;
import com.ocr.projet6.enums.RoleEnum;
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

    /**
     * this method display the add form for longueur
     * @param model
     * @param idSite
     * @param pageVoie
     * @param sizeVoie
     * @return
     */
    @GetMapping(value = "/site/{idSite}/voie/longueur/add")
    public String addLongueur(Model model,@PathVariable("idSite") Long idSite,
                              @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                              @RequestParam(name = "sizeVoie",defaultValue = "2") int sizeVoie){
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if (utilisateur.getRoles().toString().contains("USER")) {
            Optional<Site> s = siteRepository.findById(idSite);
            Site sit = null;
            if (s.isPresent()){
                sit=s.get();
                model.addAttribute("site", sit);
                Page<Voie> pageVoies = iClimbMetier.listVoie(idSite, pageVoie, sizeVoie);
                model.addAttribute("listVoie", pageVoies.getContent());
                Longueur longueur = new Longueur();
                model.addAttribute("longueur", longueur);
                Long idVoieNew = null;
                model.addAttribute("idVoieNew", idVoieNew);
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+"veut ajouter une longueur au site "+sit.getNameSite());
                return "addFormLongueur";
            }else return "redirect:/site/"+idSite+"/consult";

        }
        return "403";
    }

    /**
     * this method delete the longueur identified by idLongueur
     * @param idLongueur
     * @param pageLongueur
     * @param sizeLongueur
     * @param idSite
     * @return
     */
    @GetMapping(value = "/site/{idSite}/longueur/{idLongueur}/delete")
    public String deleteLongueur(@PathVariable("idLongueur") Long idLongueur,
                                 @RequestParam(name="pageLongueur",defaultValue = "0") int pageLongueur,
                                 @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur,
                                 @PathVariable("idSite") Long idSite){
        Optional<Longueur> l=longueurRepository.findById(idLongueur);
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Longueur longueur=null;
        if (l.isPresent()){
            longueur=l.get();
            if (utilisateurConnecte.getIdUser()==longueur.getVoie().getSite().getUtilisateur().getIdUser()&&utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_USER)){
                iClimbMetier.logger().info("L'utilisateur "+utilisateurConnecte.getUsername()+" a supprimé la longueur "+longueur.getNomLongueur());
                longueurRepository.deleteById(idLongueur);
            } else return "403";
        }

        return "redirect:/site/"+idSite+"/consult&pageLongueur="+pageLongueur+"&sizeLongueur="+sizeLongueur;}


    /**
     * this method display the form to edit a longueur
     * @param model
     * @param idSite
     * @param idLongueur
     * @param pageVoie
     * @param sizeVoie
     * @return
     */
    @RequestMapping(value = "/site/{idSite}/longueur/{idLongueur}/edit")
    public  String  editLongueur(Model model,@PathVariable("idSite") Long idSite,
                                 @PathVariable("idLongueur") Long idLongueur ,
                                 @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                                 @RequestParam(name = "sizevoie",defaultValue = "2") int sizeVoie){
        Utilisateur utilisateur=iClimbMetier.userConnected();
        Optional<Site> s=siteRepository.findById(idSite);
        Optional<Longueur> l = longueurRepository.findById(idLongueur);
        if (s.isPresent() && l.isPresent()) {
            Site sit = s.get();
             Longueur lg = l.get();
            if (utilisateur.getRoles().toString().contains("USER") && utilisateur.getIdUser() == sit.getUtilisateur().getIdUser()) {
                model.addAttribute("site", sit);
                Page<Voie> pageVoies = iClimbMetier.listVoie(idSite, pageVoie, sizeVoie);
                model.addAttribute("listVoie", pageVoies.getContent());
                Long idVoieNew = null;
                model.addAttribute("idVoieNew", idVoieNew);
                model.addAttribute("longueur", lg);
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" veut modifier la longueur "+ lg.getNomLongueur());
                return "editFormLongueur";
            }else return "403";
        }return "redirect:/site/"+idSite+"/consult";
    }

    /**
     * this method save in Db an edited Longueur
     * @param model
     * @param longueur
     * @param idVoieNew
     * @param idLongueur
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/longueur/{idLongueur}/save")
    public String saveEditedLongueur(Model model,
                               @Valid Longueur longueur,
                               @RequestParam("idVoieNew") Long idVoieNew,
                               @PathVariable("idLongueur") Long idLongueur,
                               BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormLongueur";
        }
        Utilisateur utilisateur=iClimbMetier.userConnected();
        Optional<Voie> v=voieRepository.findById(idVoieNew);
        if (v.isPresent()){
            Voie voi=v.get();
            if (utilisateur.getIdUser()==voi.getSite().getUtilisateur().getIdUser() && utilisateur.getRoles().contains(RoleEnum.ROLE_USER)){
                longueur.setVoie(voi);
                longueur.setIdLongueur(idLongueur);
                longueurRepository.save(longueur);
                model.addAttribute("longueur",longueur);
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" a modifié la longueur "+longueur.getNomLongueur());
                return "confirmationLongueur";
            } else  return "403";
        } else return "editFormLongueur";
    }

    /**
     * this method save in Db a new Longueur
     * @param model
     * @param longueur
     * @param idVoieNew
     * @param idSite
     * @param bindingResult
     * @return
     */
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
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if (s.isPresent()&&v.isPresent()){
            Site sit=s.get();
            Voie voi=v.get();
            if (utilisateur.getIdUser()==sit.getUtilisateur().getIdUser()){
                voi.setSite(sit);
                longueur.setVoie(voi);
                longueurRepository.save(longueur);
                model.addAttribute("longueur",longueur);
                iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" a ajouté une longueur à la voie "+voi.getNomVoie()+" du site "+sit.getNameSite());
                return "confirmationLongueur";
            }
             return "403";
        }
        return "redirect:/site/"+idSite+"/consult";
    }



}
