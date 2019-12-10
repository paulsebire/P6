package com.ocr.projet6.security;


import com.ocr.projet6.entities.Utilisateur;
import com.ocr.projet6.enums.RoleEnum;
import com.ocr.projet6.dao.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcAutoConfiguration {

    @Autowired
    public WebMvcConfig(UtilisateurRepository utilisateurRepository) {


        List<RoleEnum> userRole = Collections.singletonList(RoleEnum.ROLE_USER);
        List<RoleEnum> adminRole = Arrays.asList(RoleEnum.ROLE_USER, RoleEnum.ROLE_ADMIN);
        Utilisateur user = new Utilisateur("user", "user", "User", "USER","user@email.com", userRole);
        Utilisateur test = new Utilisateur("test", "test", "Test", "TEST","test@email.com", userRole);
        Utilisateur adminUser = new Utilisateur("admin", "admin", "Admin", "ADMIN","admin@email.com", adminRole);
        utilisateurRepository.save(user);
        utilisateurRepository.save(test);
        utilisateurRepository.save(adminUser);
    }


}
