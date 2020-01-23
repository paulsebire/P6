package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.entities.*;
import com.ocr.projet6.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
public class SiteController {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private IClimbMetier iClimbMetier;

    /**
     * this method display the page with all sites
     * @param model  an instance of the model
     * @param pageSite the number of the page of site the user is browsing
     * @param sizeSite the number of sites by page
     * @return a page with all the sites in the DB
     */
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
        iClimbMetier.logger().info("la page des sites a été demandée");
        return "sites";
    }

    /**
     * this method wil display all sites containing the keyword mc
     * @param model instance of  the model
     * @param pageSite the number of the page of site the user is browsing
     * @param sizeSite the number of sites by page
     * @param mc keyword
     * @return a page with all the sites containing the keywword
     */
    @GetMapping(path = "/site/find")
    public String find(Model model,
                       @RequestParam(name="pageSite",defaultValue = "0") int pageSite,
                       @RequestParam(name = "sizeSite",defaultValue = "2") int sizeSite,
                       @RequestParam(name = "motCle", defaultValue = "")String mc){
        try {
            Page<Site> pageSites = siteRepository.chercher("%" + mc + "%", PageRequest.of(pageSite, sizeSite));
            model.addAttribute("listSite", pageSites.getContent());

            int[] pagesSites = new int[pageSites.getTotalPages()];
            model.addAttribute("pagesSite", pagesSites);
            int paginationEnablerSite = pagesSites.length;
            model.addAttribute("paginationEnablerSite", paginationEnablerSite);
            model.addAttribute("pagesSite", pagesSites);
            model.addAttribute("pageCouranteSite", pageSites);
            model.addAttribute("sizeSite", sizeSite);
            model.addAttribute("motCle", mc);
            iClimbMetier.logger().info("La liste des sites contenant "+mc+" a été demandée");
        }catch (Exception e){
            model.addAttribute("exception",e);
            throw new RuntimeException("Site Introuvable");
        }
        return "sites";
    }

    /**
     * this method display all the informations about one site identified by idSite
     * @param model instance of model
     * @param idSite id of site
     * @param pageVoie the number of the page of voie the user is browsing
     * @param sizeVoie the number of voies by page
     * @param pageLongueur the number of the page of longueur the user is browsing
     * @param sizeLongueur the number of longueurs by page
     * @param pageCommentaire the number of the page of comments the user is browsing
     * @param sizeCommentaire the number of comments by page
     * @return a page with all the informations about a site
     */
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

            Utilisateur utilisateur=iClimbMetier.userConnected();
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

            List<Commentaire> listComByUser = iClimbMetier.listCommentaireBySiteByUser(idSite,utilisateur.getIdUser());
            int nbCombyUser = listComByUser.size();
            model.addAttribute("nbComByUser",nbCombyUser);
            iClimbMetier.logger().info("La page du site "+site.getNameSite()+"a été demandée par "+utilisateur.getUsername());
        }catch (Exception e){
            model.addAttribute("exception",e);
        }

        return "sitesDisplay";
    }


    /**
     * this method check the user role and display the add form for site
     * @param model instance of the model
     * @return a form for adding a site
     */
    @GetMapping(value = "/site/add")
    public String addSite(Model model){
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if (utilisateur.getRoles().toString().contains("USER")){
            Site site=new Site();
            site.setOfficiel(false);
            model.addAttribute("site",site);
            iClimbMetier.logger().info("l'utilisateur " + utilisateur.getUsername() + " souhaite ajouter un nouveau site");
            return "addFormSite";
        }
        return "403";
    }

    /**
     * this method save in DB a new site
     * @param model instance of the model
     * @param site an object site
     * @param bindingResult handle errors
     * @return a confirmation page
     */
    @PostMapping(value = "/site/save")
    public String saveNewSite(Model model,@Valid Site site, BindingResult bindingResult){
       if (bindingResult.hasErrors()){
            return "addFormSite";
        }
        Utilisateur utilisateur=iClimbMetier.userConnected();
       if (utilisateur.getRoles().toString().contains("USER")){
            site.setUtilisateur(utilisateur);
            siteRepository.save(site);
            model.addAttribute("site",site);
            iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" a créé le nouveau site"+site.getNameSite());
            return "confirmationSite";
        }
        return "403";
    }

    /**
     * thids method save an edited site
     * @param model an instance of the model
     * @param site an object site
     * @param idSite id of the site
     * @param bindingResult handle errors
     * @return  a confirmation page
     */
    @PostMapping(value = "/site/{idSite}/save")
    public String saveEditedSite(Model model, @Valid Site site,
                                 @PathVariable("idSite") Long idSite,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormVoie";
        }
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if (utilisateur.getRoles().toString().contains("USER") && utilisateur.getIdUser()==site.getUtilisateur().getIdUser()){
        site.setIdSite(idSite);
        model.addAttribute("site",site);
        siteRepository.save(site);
        iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+" a édité le site"+site.getNameSite());
        return "confirmationSite";
        }
        return "403";
    }

    /**
     * this method check if user is admin and give the status official to a site
     * @param idSite id of the site
     * @return the  page with all the information about a site
     */
    @GetMapping(value = "/site/{idSite}/edit/officiel")
    public String rendreOfficiel(@PathVariable("idSite") Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Site site=null;
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if (utilisateur.getRoles().contains(RoleEnum.ROLE_ADMIN)){
            if(s.isPresent()) {
                site=s.get();
                site.setOfficiel(!site.isOfficiel());
                siteRepository.save(site);
                iClimbMetier.logger().info("L'administateur "+utilisateur.getUsername()+" a rendu le site "+site.getNameSite()+" officiel");
            }
            return "redirect:/site/"+idSite+"/consult";
        }
        return "403";
    }

    /**
     * this method check if user is owner of site and display the edit form for site
     * @param model instance of the model
     * @param idSite id of the site
     * @return a form for editing site
     */
    @RequestMapping(value = "/site/{idSite}/edit")
    public String editSite(Model model,
                           @PathVariable("idSite") Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Site site=null;
        Utilisateur utilisateur=iClimbMetier.userConnected();
            if (s.isPresent()) {
                site = s.get();
                if (utilisateur.getIdUser()==site.getUtilisateur().getIdUser()&& utilisateur.getRoles().contains(RoleEnum.ROLE_USER)){
                    model.addAttribute("site", site);
                    iClimbMetier.logger().info("L'utilisateur "+utilisateur.getUsername()+"veut modifier le site "+site.getNameSite());
                    return "editFormSite";
                }return "403";
            }return "redirect:/site/"+idSite+"/consult";
    }



}
