package com.ocr.projet6.security;

import com.ocr.projet6.Metier.IClimbMetier;
import com.ocr.projet6.entities.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SecurityController {
    @Autowired
    private IClimbMetier iClimbMetier;

    @RequestMapping(value = "/home")
    public String home(Model model){
        List<Site> listSiteWithImg= iClimbMetier.listSiteWithImg();
        model.addAttribute("listSite",listSiteWithImg);
        return "home";
    }
    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/")
    public String home(){
        return "redirect:/home";
    }

    @RequestMapping(value = "/403")
    public String accessDenied(){
        return "403";
    }

    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String redirect404() {
        return "redirect:/home";
    }



}
