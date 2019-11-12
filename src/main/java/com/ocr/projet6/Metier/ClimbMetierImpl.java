package com.ocr.projet6.Metier;

import com.ocr.projet6.dao.LongueurRepository;
import com.ocr.projet6.dao.SiteRepository;
import com.ocr.projet6.dao.VoieRepository;
import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ClimbMetierImpl implements IClimbMetier {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private LongueurRepository longueurRepository;


    @Override
    public Page<Voie> listVoie(Long idSite, int page, int size) {
        return voieRepository.listVoie(idSite,PageRequest.of(page,size));
    }

    @Override
    public Page<Longueur> listLongueur(Long idVoie, int page, int size) {
        return longueurRepository.listLongueur(idVoie,PageRequest.of(page,size));
    }

    @Override
    public Site consulterSite(Long idSite) {
        Optional<Site> site = siteRepository.findById(idSite);
        Site sit =null;
        if (site.isPresent()){
            sit=site.get();
        }else{
            throw new RuntimeException("Compte Introuvable");
        }
        return sit;
    }
}
