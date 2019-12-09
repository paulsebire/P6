package com.ocr.projet6.web;

import com.ocr.projet6.Metier.ClimbMetierImpl;
import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.dao.TopoRepository;
import com.ocr.projet6.entities.Topo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TopoController {
    @Autowired
    private IClimbMetier iClimbMetier;
    @Autowired
    private TopoRepository topoRepository;

    @RequestMapping(value = "/topo/search")
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

    @GetMapping(path = "/topo/find")
    public String findTopo(Model model,
                       @RequestParam(name="pageTopo",defaultValue = "0") int pageTopo,
                       @RequestParam(name = "sizeTopo",defaultValue = "2") int sizeTopo,
                       @RequestParam(name = "motCle", defaultValue = "")String mc){
        try {
            String formatedMc = ClimbMetierImpl.formatString(mc);
            Page<Topo> pageTopos = topoRepository.chercherTopo("%" + formatedMc + "%", PageRequest.of(pageTopo, sizeTopo));
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
}
