package com.ocr.projet6.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("USER","ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("{noop}1234").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/utilisateurs/login");
        http.authorizeRequests().antMatchers("/monProfil","/addFormSite","/deleteVoie","/deleteLongueur","/editVoie",
                "/saveVoie","/addFormVoie","/saveSite","/editSite","/editLongueur").hasRole("USER");
        http.authorizeRequests().antMatchers("/administration").hasRole("ADMIN");
        http.exceptionHandling().accessDeniedPage("/403");
    }
}
