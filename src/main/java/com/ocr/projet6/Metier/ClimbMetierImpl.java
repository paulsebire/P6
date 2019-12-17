package com.ocr.projet6.Metier;

import com.ocr.projet6.dao.*;
import com.ocr.projet6.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;
@Service
public class ClimbMetierImpl implements IClimbMetier {
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private VoieRepository voieRepository;
    @Autowired
    private LongueurRepository longueurRepository;
    @Autowired
    private TopoRepository topoRepository;
    @Autowired
    private CommentaireRepository commentaireRepository;

    @Override
    public Page<Site> listSite(int page, int size) {
        return siteRepository.listSite(PageRequest.of(page,size));
    }

    @Override
    public Page<Voie> listVoie(Long idSite, int page, int size) {
        return voieRepository.listVoie(idSite,PageRequest.of(page,size));
    }

    @Override
    public Page<Longueur> listLongueur(Long idSite, int page, int size) {
        return longueurRepository.listLongueur(idSite,PageRequest.of(page,size));
    }

    @Override
    public Page<Topo> listTopo(int page, int size){
        return topoRepository.listTopo(PageRequest.of(page,size));
    }

    public static String formatString (String str){
        String formatStr = str.toLowerCase();
        return formatStr;
    }

    @Override
    public Page<Topo> listTopoByUtilisateur(Long idUser, int page, int size){
        return topoRepository.listTopoByUtilisateur(idUser,PageRequest.of(page,size));
    }

    @Override
    public Page<Topo> listTopoBySite(Long idSite, int page, int size){
        return topoRepository.listTopoBySite(idSite,PageRequest.of(page,size));
    }

    @Override
    public Page<Commentaire> listCommentaireBySite(Long idSite, int page, int size){
        return commentaireRepository.listCommentaireBySite(idSite,PageRequest.of(page,size));
    }
}
