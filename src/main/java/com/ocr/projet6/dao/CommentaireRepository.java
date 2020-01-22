package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Commentaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {
    @Query(value = "select c from Commentaire c inner join fetch c.site s  where s.idSite=:idSite order by c.date  desc ",
            countQuery = "select count (c) from Commentaire c inner join c.site s where s.idSite=:idSite")
    public Page<Commentaire> listCommentaireBySite(@Param("idSite")Long idSite, Pageable pageable);

    @Query(value = "select c from Commentaire c inner join fetch c.site s inner join fetch c.utilisateur u where s.idSite=:idSite and u.idUser=:idUser order by c.date  desc ",
            countQuery = "select count (c) from Commentaire c inner join c.site s inner join c.utilisateur u where s.idSite=:idSite and u.idUser=:idUser")
    public List<Commentaire> listCommentaireBySiteByUser(@Param("idSite")Long idSite, @Param("idUser")Long idUser);
}
