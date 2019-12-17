package com.ocr.projet6.Metier;

import com.ocr.projet6.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.ui.Model;

public interface IClimbMetier {
    public Page<Site> listSite( int page, int size);
    public Page<Voie> listVoie(Long idSite, int page, int size);
    public Page<Longueur> listLongueur(Long idSite, int page, int size);
    public Page<Topo> listTopo(int page, int size);
    public Page<Topo> listTopoByUtilisateur(Long idUser, int page, int size);
    public Page<Topo> listTopoBySite(Long idSite, int page, int size);
    public Page<Commentaire> listCommentaireBySite(Long idSite, int page, int size);
}
