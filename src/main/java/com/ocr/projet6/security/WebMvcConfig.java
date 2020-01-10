package com.ocr.projet6.security;


import com.ocr.projet6.dao.CotationRepository;
import com.ocr.projet6.entities.Cotation;
import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.enums.RoleEnum;
import com.ocr.projet6.dao.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.*;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcAutoConfiguration {

    @Autowired
    public WebMvcConfig(UtilisateurRepository utilisateurRepository) {


        Set<RoleEnum> userRole = new HashSet<>();
        userRole.add(RoleEnum.ROLE_USER);

        Set<RoleEnum> adminRole =new HashSet<>();
        adminRole.add(RoleEnum.ROLE_USER);
        adminRole.add(RoleEnum.ROLE_ADMIN);

        Utilisateur user = new Utilisateur("user", "user", "User", "USER","user@email.com", userRole);
        Utilisateur test = new Utilisateur("test", "test", "Test", "TEST","test@email.com", userRole);
        Utilisateur adminUser = new Utilisateur("admin", "admin", "Admin", "ADMIN","admin@email.com", adminRole);
        utilisateurRepository.save(user);
        utilisateurRepository.save(test);
        utilisateurRepository.save(adminUser);
    }



}
