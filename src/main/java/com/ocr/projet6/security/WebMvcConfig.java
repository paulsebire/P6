package com.ocr.projet6.security;


import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.enums.RoleEnum;
import com.ocr.projet6.dao.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    public WebMvcConfig(UtilisateurRepository utilisateurRepository) {
        // Ceci n'est pas à recopier en production
        List<RoleEnum> userRole = Collections.singletonList(RoleEnum.USER);
        List<RoleEnum> adminRole = Arrays.asList(RoleEnum.USER, RoleEnum.ADMINISTRATOR);
        Utilisateur user = new Utilisateur("user", "{noop}user", "User", "USER","user@email.com", userRole);
        Utilisateur adminUser = new Utilisateur("admin", "{noop}admin", "Admin", "ADMIN","admin@email.com", adminRole);
        utilisateurRepository.save(user);
        utilisateurRepository.save(adminUser);
    }


}
