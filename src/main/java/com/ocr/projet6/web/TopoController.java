package com.ocr.projet6.web;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.ReservationRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.TopoRepository;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.entities.Utilisateur;
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

    @GetMapping(value = "/topo/search")
    public String topos(Model model,
                        @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                        @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo){
        Page<Topo> pageTopos= iClimbMetier.listTopo(pageTopo,sizeTopo);
        model.addAttribute("listTopo",pageTopos.getContent());
        int[] pagesTopo=new int[pageTopos.getTotalPages()];
        int paginationEnablerTopo=pagesTopo.length;
        model.addAttribute("paginationEnablerTopo",paginationEnablerTopo);
        model.addAttribute("pagesTopo",pagesTopo);
        model.addAttribute("pageCouranteTopo",pageTopo);
        model.addAttribute("sizeTopo",sizeTopo);
        return "topos";
    }

    @GetMapping(value = "/site/{idSite}/topo/search")
    public String topoBySite (Model model,
                              @PathVariable(value = "idSite")Long idSite,
                              @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                              @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo){
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
        return "topoBySite";
    }


    @GetMapping(path = "/topo/find")
    public String findTopo(Model model,
                       @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                       @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo,
                       @RequestParam(name = "motCle", defaultValue = "")String mc){
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
        return "topos";
    }

    @GetMapping (value = "/topo/add")
    public String addTopo(Model model,
                          @RequestParam(name="pageSite",defaultValue = "0") int pageSite,
                          @RequestParam(name = "sizeSite",defaultValue = "2") int sizeSite){
        Page<Site> pageSites= iClimbMetier.listSite(pageSite,sizeSite);
        model.addAttribute("listSite",pageSites.getContent());
        Topo topo=new Topo();
        model.addAttribute("topo",topo);
        return "addFormTopo";
    }

    @GetMapping(value = "/topo/{idTopo}/delete")
    public String deleteTopo(@PathVariable(value = "idTopo")Long idTopo){
        Optional<Topo> t=topoRepository.findById(idTopo);
        Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
        Topo topo=null;
        if (t.isPresent()){
            topo=t.get();
            if (utilisateurConnecte.getIdUser()==topo.getUtilisateur().getIdUser()){
                topoRepository.deleteById(idTopo);
            } else return "403";
        }


        return "profile";
    }

    @PostMapping(value = "/topo/save")
    public String saveNewTopo(Model model, @Valid Topo topo,@RequestParam("idSiteNew") Long idSiteNew, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "addFormTopo";
        }
        Optional<Site> s=siteRepository.findById(idSiteNew);
        Site sit=s.get();
        topo.setSite(sit);
        topo.setDisponibilite(true);
        Utilisateur utilisateur=iClimbMetier.userConnected();
        topo.setUtilisateur(utilisateur);
        topo.setDate(new Date());
        topoRepository.save(topo);
        model.addAttribute("topo",topo);
        return "confirmationTopo";
    }

    @GetMapping(path = "/topo/{id}/consult")
    public String consulter(Model model,
                            @PathVariable(name = "id") Long id){

            Optional<Topo> t=topoRepository.findById(id);
            Topo topo=null;
            if(t.isPresent()) {
                topo=t.get();
                model.addAttribute("topo", topo);
                Utilisateur utilisateurConnecte=iClimbMetier.userConnected();
                model.addAttribute("utilisateurConnecte",utilisateurConnecte);
                boolean demandeEnCours=iClimbMetier.demandeEnCours(utilisateurConnecte.getUsername(),topo.getId());
                System.out.println("demandeencours="+demandeEnCours);
                model.addAttribute("demandeEnCours",demandeEnCours);
                return "topoDisplay";
            }
        return "sites";
    }

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
                return "editFormTopo";
            }
            return "403";
        }
        return "redirect:/topo/"+id+"/consult";
    }

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
                return "confirmationTopo";
            }
            return "403";
        }
        return "editFormTopo";
    }





}
