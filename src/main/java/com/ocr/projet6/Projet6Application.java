package com.ocr.projet6;

import com.ocr.projet6.dao.LongueurRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
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



    public static void main(String[] args) {
       SpringApplication.run(Projet6Application.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
        Site site1 = siteRepository.save(new Site("site1","nord"));
        Site site2 = siteRepository.save(new Site("site2","Sud"));

        Voie voie1 = voieRepository.save(new Voie("voie1","secteur1","3C",50,site1));
        Voie voie2 = voieRepository.save(new Voie("voie2","secteur2","4C",60,site1));
        Voie voie3 = voieRepository.save(new Voie("voie3","secteur1","5C",70,site2));
        Voie voie4 = voieRepository.save(new Voie("voie4","secteur2","6C",80,site2));

       Longueur lg1 = longueurRepository.save(new Longueur("lg1",10,site1));
        Longueur lg2 = longueurRepository.save(new Longueur("lg2",20,site1));
        Longueur lg3 = longueurRepository.save(new Longueur("lg3",30,site1));
        Longueur lg4 = longueurRepository.save(new Longueur("lg4",40,site1));
        Longueur lg5 = longueurRepository.save(new Longueur("lg5",50,site2));
        Longueur lg6 = longueurRepository.save(new Longueur("lg6",60,site2));
        Longueur lg7 = longueurRepository.save(new Longueur("lg7",70,site2));
        Longueur lg8 = longueurRepository.save(new Longueur("lg8",80,site2));
    }
}
