package com.ocr.projet6.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {

    @RequestMapping(value = "/utilisateur/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/")
    public String home(){
        return "redirect:/search";
    }

    @RequestMapping(value = "/403")
    public String accessDenied(){
        return "403";
    }
}
