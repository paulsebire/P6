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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class SiteController {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private LongueurRepository longueurRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private IClimbMetier iClimbMetier;

    @RequestMapping(value = "/sites")
    public String sites(Model model){
        //model.addAttribute("site",new Site());
        return "sites";
    }

    @RequestMapping(value = "/consulterSite")
    public String consulter(Model model, String nameSite,
                            @RequestParam(name="pageVoie",defaultValue = "0") Integer pageVoie,
                            @RequestParam(name = "sizevoie",defaultValue = "2") Integer sizeVoie,
    @RequestParam(name="pageLongueur",defaultValue = "0") Integer pageLongueur,
    @RequestParam(name = "sizeLongueur",defaultValue = "2") Integer sizeLongueur){

        try {
            Site site = iClimbMetier.consulterSite(nameSite);
            model.addAttribute("idSite",site.getIdSite());
            model.addAttribute("nameSite",site.getNameSite());
            Page<Voie> pageVoies= iClimbMetier.listVoie(site.getIdSite(),pageVoie,sizeVoie);
            model.addAttribute("listVoie",pageVoies.getContent());
            int[] pagesVoie=new int[pageVoies.getTotalPages()];
            model.addAttribute("paginationEnablerVoie",pagesVoie.length);
            model.addAttribute("pagesVoie",pagesVoie);
            model.addAttribute("pageCouranteVoie",pageVoie);
            model.addAttribute("site", site);
            model.addAttribute("sizeVoie",sizeVoie);


            Page<Longueur> pageLongueurs= iClimbMetier.listLongueur(site.getIdSite(),pageLongueur,sizeLongueur);
            model.addAttribute("listLongueur",pageLongueurs.getContent());
            int[] pagesLongueur=new int[pageLongueurs.getTotalPages()];
            model.addAttribute("paginationEnablerLongueur",pagesLongueur.length);
            model.addAttribute("pagesLongueur",pagesLongueur);
            model.addAttribute("pageCouranteLongueur",pageLongueur);
            model.addAttribute("sizeLongueur",sizeLongueur);

        }catch (Exception e){
            model.addAttribute("exception",e);
        }

        return "sites";
    }

    @RequestMapping(value = "/inscription")
    public String inscriptionform(){
        return "inscription";
    }

    @RequestMapping(value = "/addFormSite")
    public String formulaireSite(Model model){
        Site site=new Site();
        model.addAttribute("site",site);
        return "addFormSite";
    }

    @RequestMapping(value = "/deleteVoie",method = RequestMethod.GET)
    public String deleteVoie(Long idVoie, Integer pageVoie, Integer sizeVoie,String nameSite){
        longueurRepository.deleteByVoie(idVoie);
        voieRepository.deleteById(idVoie);
        return "redirect:/consulterSite?pageVoie="+pageVoie+"&sizeVoie="+sizeVoie+"&nameSite="+nameSite;}
    @RequestMapping(value = "/deleteLongueur",method = RequestMethod.GET)
    public String deleteLongueur(Long idLongueur,
                                 @RequestParam(name="pageLongueur",defaultValue = "0") int pageLongueur,
                                 @RequestParam(name = "sizeLongueur",defaultValue = "2") int sizeLongueur,
                                 String nameSite){
        System.out.println("idLongueur="+idLongueur);
        longueurRepository.deleteById(idLongueur);
        return "redirect:/consulterSite?pageVoie="+pageLongueur+"&sizeVoie="+sizeLongueur+"&nameSite="+nameSite;}

    @RequestMapping(value = "/editVoie",method = RequestMethod.GET)
    public String editVoie(Model model, Long idVoie,Long idSite) {
        Optional<Voie> v=voieRepository.findById(idVoie);
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=null;
        Voie voi = null;
        if(v.isPresent()&&s.isPresent()) {
            voi = v.get();
            sit=s.get();
            voi.setSite(sit);
            model.addAttribute("voie", voi);
            model.addAttribute("site",sit);
        }
        return "editFormVoie";
    }
    @RequestMapping(value = "/saveVoie",method = RequestMethod.POST)
    public String saveVoie(Model model, @Valid Voie voie,Long idSite,Long idVoie, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "editFormVoie";
        }
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        voie.setSite(sit);
        voie.setIdVoie(idVoie);
        voieRepository.save(voie);
        model.addAttribute("voie",voie);
        model.addAttribute("site",sit);
        return "confirmationVoie";
    }
    @RequestMapping(value = "/addFormVoie")
    public String formulaireVoie(Model model,Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=s.get();
        Voie v=new Voie();
        v.setSite(sit);
        model.addAttribute("voie",v);
        return "addFormVoie";
    }


    @RequestMapping(value = "/saveSite",method = RequestMethod.POST)
    public String saveSite(Model model,@Valid Site site, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "addFormSite";
        }
        siteRepository.save(site);
        return "confirmationSite";
    }

    @RequestMapping(value = "/editSite")
    public String editSite(Model model,Long idSite){
        Optional<Site> s=siteRepository.findById(idSite);
        Site sit=null;
        if(s.isPresent()) {
            sit=s.get();
            model.addAttribute("site",sit);
        }
        return "editFormSite";
    }

    @RequestMapping(value = "/editLongueur")
    public  String  editLongueur(Model model, Long idLongueur , String nameSite, @RequestParam(name="pageVoie",defaultValue = "0") int pageVoie,
                                 @RequestParam(name = "sizevoie",defaultValue = "2") int sizeVoie){
        Site site = iClimbMetier.consulterSite(nameSite);
        model.addAttribute("site",site);
        Page<Voie> pageVoies= iClimbMetier.listVoie(site.getIdSite(),pageVoie,sizeVoie);
        model.addAttribute("listVoie",pageVoies.getContent());
        Optional<Longueur> l=longueurRepository.findById(idLongueur);
        Longueur lg=null;
        if(l.isPresent()) {
            lg=l.get();
            model.addAttribute("longueur",lg);
        }
        return "editFormLongueur";
    }

}
