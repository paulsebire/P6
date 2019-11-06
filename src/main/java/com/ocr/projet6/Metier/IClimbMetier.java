package com.ocr.projet6.Metier;

import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
import org.springframework.data.domain.Page;

public interface IClimbMetier {
    public Page<Voie> listVoie(String nameSite, int page, int size);
    public Page<Longueur> listLongueur(String nameSite, int page, int size);
    Site consulterSite(String nameSite);
}
