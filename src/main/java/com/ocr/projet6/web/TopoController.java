package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.ReservationRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.TopoRepository;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.enums.RoleEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

@Controller
public class TopoController {
    @Autowired
    private IClimbMetier iClimbMetier;
    @Autowired
    private TopoRepository topoRepository;
    @Autowired
    private SiteRepository siteRepository;
    private static final Logger logger = LogManager.getLogger(TopoController.class);

    /**
     * this method display all the topos in the DB
     * @param model  instance of the model
     * @param pageTopo the number of the page of topos the user is browsing
     * @param sizeTopo the number of topos by page
     * @return the page with all the topos
     */
    @GetMapping(value = "/topo/search")
    public String topos(Model model,
                        @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                        @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo){
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_USER)){
            Page<Topo> pageTopos= iClimbMetier.listTopo(pageTopo,sizeTopo);
            model.addAttribute("listTopo",pageTopos.getContent());
            int[] pagesTopo=new int[pageTopos.getTotalPages()];
            int paginationEnablerTopo=pagesTopo.length;
            model.addAttribute("paginationEnablerTopo",paginationEnablerTopo);
            model.addAttribute("pagesTopo",pagesTopo);
            model.addAttribute("pageCouranteTopo",pageTopo);
            model.addAttribute("sizeTopo",sizeTopo);
            logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" veut accèder à la page des topos");
            return "topos";
        }else return "403";

    }

    /**
     * this method display all the topos for a site
     * @param model instance of the model
     * @param idSite id of site
     * @param pageTopo the number of the page of topos the user is browsing
     * @param sizeTopo the number of topos by page
     * @return  display a list of topos for a corresponding site
     */
    @GetMapping(value = "/site/{idSite}/topo/search")
    public String topoBySite (Model model,
                              @PathVariable(value = "idSite")Long idSite,
                              @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                              @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo){
        Utilisateur utilisateurConnecte = iClimbMetier.userConnected();
        if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_USER)){
            Page<Topo> pageTopos= iClimbMetier.listTopoBySite(idSite,pageTopo,sizeTopo);
            model.addAttribute("listTopo",pageTopos.getContent());
            int[] pagesTopo=new int[pageTopos.getTotalPages()];
            int paginationEnablerTopo=pagesTopo.length;
            model.addAttribute("paginationEnablerTopo",paginationEnablerTopo);
            model.addAttribute("pagesTopo",pagesTopo);
            model.addAttribute("pageCouranteTopo",pageTopo);
            model.addAttribute("sizeTopo",sizeTopo);

            Optional<Site> s=siteRepository.findById(idSite);
            Site site=null;
            if(s.isPresent()) {
                site=s.get();
            }
            model.addAttribute("site",site);
            logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" veut voir les topos du site "+site.getNameSite());
            return "topoBySite";
        }else return "403";

    }

    /**
     * this method display a list of topos who contains the keyword mc
     * @param model instance of the model
     * @param pageTopo the number of the page of topos the user is browsing
     * @param sizeTopo the number of topos by page
     * @param mc keyword
     * @return a list of topos containing  the keyword
     */
    @GetMapping(path = "/topo/find")
    public String findTopo(Model model,
                       @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                       @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo,
                       @RequestParam(name = "motCle", defaultValue = "")String mc){
        Utilisateur utilisateurConnecte= iClimbMetier.userConnected();
        if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_USER)){
            try {
                Page<Topo> pageTopos = topoRepository.chercherTopo("%" + mc + "%", PageRequest.of(pageTopo, sizeTopo));
                model.addAttribute("listTopo", pageTopos.getContent());

                int[] pagesTopos = new int[pageTopos.getTotalPages()];
                model.addAttribute("pagesTopo", pagesTopos);
                int paginationEnablerTopo = pagesTopos.length;
                model.addAttribute("paginationEnablerTopo", paginationEnablerTopo);
                model.addAttribute("pagesTopo", pagesTopos);
                model.addAttribute("pageCouranteTopo", pageTopos);
                model.addAttribute("sizeTopo", sizeTopo);
                model.addAttribute("motCle", mc);
            }catch (Exception e){
                model.addAttribute("exception",e);
                throw new RuntimeException("Topo Introuvable");
            }
            logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+"cherche un topo contenant "+mc);
            return "topos";
        }else return "403";

    }

    /**
     * this method display a form to add a topo
     * @param model instance of the model
     * @param pageSite param of PageRequest
     * @param sizeSite param of PageRequest
     * @return the form  for adding a topo
     */
    @GetMapping (value = "/topo/add")
    public String addTopo(Model model,
                          @RequestParam(name="pageSite",defaultValue = "0") int pageSite,
                          @RequestParam(name = "sizeSite",defaultValue = "9999") int sizeSite){
        Utilisateur utilisateurConnecte =iClimbMetier.userConnected();
        if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_USER)){
            Page<Site> pageSites= iClimbMetier.listSite(pageSite,sizeSite);
            model.addAttribute("listSite",pageSites.getContent());
            Topo topo=new Topo();
            model.addAttribute("topo",topo);
            return "addFormTopo";
        }else return "403";

    }

    /**
     * this method delete the corresponding topo to idTopo
     * @param idTopo  if of topo
     * @return the page profile with all the topo
     */
    @GetMapping(value = "/topo/{idTopo}/delete")
    public String deleteTopo(@PathVariable(value = "idTopo")Long idTopo){
        Optional<Topo> t=topoRepository.findById(idTopo);
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Topo topo=null;
        if (t.isPresent()){
            topo=t.get();
            if (utilisateurConnecte.getIdUser()==topo.getUtilisateur().getIdUser()){
                logger.info("L'utilisateur "+utilisateurConnecte.getUsername()+" a supprimé le topo "+topo.getNom());
                topoRepository.deleteById(idTopo);
                return "profile";
            } else return "403";
        }else return "home";

    }

    /**
     * this method save in DB a new topo
     * @param model instance of  the model
     * @param topo an object topo
     * @param idSiteNew id of the topo's site
     * @param bindingResult handle errors
     * @return a confirmation page
     */
    @PostMapping(value = "/topo/save")
    public String saveNewTopo(Model model, @Valid Topo topo,@RequestParam("idSiteNew") Long idSiteNew, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "addFormTopo";
        }
        Utilisateur utilisateur=iClimbMetier.userConnected();
        Optional<Site> s=siteRepository.findById(idSiteNew);
        Site sit=null;

        if (utilisateur.getRoles().contains(RoleEnum.ROLE_USER)){
            if (s.isPresent()){
                sit=s.get();
                topo.setSite(sit);
                topo.setDisponibilite(true);
                topo.setUtilisateur(utilisateur);
                topo.setDate(new Date());
                topoRepository.save(topo);
                model.addAttribute("topo",topo);
                logger.info("L'utilisateur "+utilisateur.getUsername()+" a créé le nouveau topo "+topo.getNom());
                return "confirmationTopo";
            }else return "addFormTopo";

        }else return "403";

    }

    /**
     * this method display the topo identified by its id
     * @param model instance of the model
     * @param id id of the topo
     * @return the page with all informations about a topo
     */
    @GetMapping(path = "/topo/{id}/consult")
    public String consulter(Model model,
                            @PathVariable(name = "id") Long id){
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Optional<Topo> t=topoRepository.findById(id);
        Topo topo=null;
        if (utilisateurConnecte.getRoles().contains(RoleEnum.ROLE_USER)){
            if(t.isPresent()) {
                topo=t.get();
                model.addAttribute("topo", topo);

                model.addAttribute("utilisateurConnecte",utilisateurConnecte);
                boolean demandeEnCours=iClimbMetier.demandeEnCours(utilisateurConnecte.getUsername(),topo.getId());
                System.out.println("demandeencours="+demandeEnCours);
                model.addAttribute("demandeEnCours",demandeEnCours);
                logger.info("L'utilisateur "+utilisateurConnecte.getUsername() +" veut consulter le topo "+topo.getNom());
                return "topoDisplay";
            }else return "home";
        }else return "403";

    }

    /**
     * this method check if user is owner of topo then display the edit form
     * @param model instance of the model
     * @param id id of the topo
     * @param pageSite param of PageRequest
     * @param sizeSite param of PageRequest
     * @return a form for editing topo
     */
    @GetMapping (value = "/topo/{id}/edit")
    public String topoEdit (Model model,@PathVariable(value = "id")Long id,
                            @RequestParam(name="pageSite",defaultValue = "0") int pageSite,
                            @RequestParam(name = "sizeSite",defaultValue = "2") int sizeSite){
        Optional<Topo> t=topoRepository.findById(id);
        Topo topo=null;
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if(t.isPresent()) {
            topo=t.get();
            if (utilisateur.getIdUser()==topo.getUtilisateur().getIdUser()){
                model.addAttribute("topo",topo);
                Page<Site> pageSites= iClimbMetier.listSite(pageSite,sizeSite);
                model.addAttribute("listSite",pageSites.getContent());
                System.out.println("topo.site.nameSite="+topo.getSite().getNameSite());
                logger.info("L'utilisateur "+utilisateur.getUsername()+"souhaite modifier le topo "+topo.getNom());
                return "editFormTopo";
            }
            return "403";
        }return "redirect:/topo/"+id+"/consult";
    }

    /**
     * this method save an edited topo
     * @param model instance of model
     * @param topo an object topo
     * @param id id of the topo
     * @param idSite id of the site
     * @param bindingResult handle errors
     * @return a confirmation page
     */
    @PostMapping(value = "/topo/{id}/save")
    public String saveEditedTopo(Model model, @Valid Topo topo,
                                 @PathVariable("id") Long id,
                                 @RequestParam(value = "idSite") Long idSite,
                                 BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormTopo";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Site site=null;
        Utilisateur utilisateur=iClimbMetier.userConnected();
        if(s.isPresent()) {
            site=s.get();
            if (utilisateur.getRoles().toString().contains("USER")){
                topo.setSite(site);
                topo.setUtilisateur(utilisateur);
                model.addAttribute("site", site);
                topo.setId(id);
                topo.setDate(new Date());
                model.addAttribute("topo",topo);
                topoRepository.save(topo);
                logger.info("L'utilisateur "+utilisateur.getUsername()+" a modifié le topo "+topo.getNom());
                return "confirmationTopo";
            }
            return "403";
        }
        return "editFormTopo";
    }





}
