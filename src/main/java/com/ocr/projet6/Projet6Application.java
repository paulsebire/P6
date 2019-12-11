package com.ocr.projet6;

import com.ocr.projet6.dao.*;
import com.ocr.projet6.entities.*;
import com.ocr.projet6.security.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Projet6Application implements CommandLineRunner {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private LongueurRepository longueurRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private TopoRepository topoRepository;



    public static void main(String[] args) {
       SpringApplication.run(Projet6Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        Utilisateur admin = utilisateurRepository.findByUsername("admin");
        Utilisateur user = utilisateurRepository.findByUsername("user");

        Site site1 = new Site("site1","nord");
        site1.setUtilisateur(admin);
        siteRepository.save(site1);

        Site site2 = new Site("site2","sud");
        site2.setUtilisateur(user);
        siteRepository.save(site2);


        Voie voie1 = voieRepository.save(new Voie("voie1","secteur1","3C",50,site1));
        Voie voie2 = voieRepository.save(new Voie("voie2","secteur2","4C",60,site1));
        Voie voie3 = voieRepository.save(new Voie("voie3","secteur1","5C",70,site2));
        Voie voie4 = voieRepository.save(new Voie("voie4","secteur2","6C",80,site2));

       Longueur lg1 = longueurRepository.save(new Longueur("lg1",10,voie1));
        Longueur lg2 = longueurRepository.save(new Longueur("lg2",20,voie1));
        Longueur lg3 = longueurRepository.save(new Longueur("lg3",30,voie2));
        Longueur lg4 = longueurRepository.save(new Longueur("lg4",40,voie2));
        Longueur lg5 = longueurRepository.save(new Longueur("lg5",50,voie3));
        Longueur lg6 = longueurRepository.save(new Longueur("lg6",60,voie3));
        Longueur lg7 = longueurRepository.save(new Longueur("lg7",70,voie4));
        Longueur lg8 = longueurRepository.save(new Longueur("lg8",80,voie4));


        Topo topo1=new Topo("topo1","nord");
        topo1.setSite(site1);
        topo1.setUtilisateur(admin);
        topoRepository.save(topo1);

        Topo topo2=new Topo("topo2","nord2");
        topo2.setSite(site1);
        topo2.setUtilisateur(admin);
        topo2.setDisponibilite(false);
        topoRepository.save(topo2);

        Topo topo3=new Topo("topo3","sud");
        topo3.setSite(site2);
        topo3.setUtilisateur(user);
        topoRepository.save(topo3);

        Topo topo4=new Topo("topo4","sud2");
        topo4.setSite(site2);
        topo4.setUtilisateur(user);
        topo4.setDisponibilite(false);
        topoRepository.save(topo4);
    }
}
