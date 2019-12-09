package com.ocr.projet6.Metier;

import com.ocr.projet6.entities.Longueur;
import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Topo;
import com.ocr.projet6.entities.Voie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;

public interface IClimbMetier {
    public Page<Site> listSite( int page, int size);
    public Page<Voie> listVoie(Long idSite, int page, int size);
    public Page<Longueur> listLongueur(Long idSite, int page, int size);
    public Page<Topo> listTopo(int page, int size);
    public Page<Topo> listTopoByUtilisateur(Long utlisateurId, int page, int size);

}
