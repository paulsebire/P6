package com.ocr.projet6.Metier;

import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

public interface IClimbMetier {
    public Page<Voie> listVoie(Long idSite, int page, int size);
    public Page<Longueur> listLongueur(Long idSite, int page, int size);
    Site  consulterSite(String nameSite);
}
