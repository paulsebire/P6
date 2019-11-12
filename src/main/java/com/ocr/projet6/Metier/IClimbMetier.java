package com.ocr.projet6.Metier;

import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
import org.springframework.data.domain.Page;

public interface IClimbMetier {
    public Page<Voie> listVoie(Long idSite, int page, int size);
    public Page<Longueur> listLongueur(Long idVoie, int page, int size);
    Site consulterSite(Long idSite);
}
