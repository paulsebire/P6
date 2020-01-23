package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Commentaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {
    @Query(value = "select c from Commentaire c inner join fetch c.site s  where s.idSite=:idSite order by c.date  desc ",
            countQuery = "select count (c) from Commentaire c inner join c.site s where s.idSite=:idSite")
     Page<Commentaire> listCommentaireBySite(@Param("idSite")Long idSite, Pageable pageable);


}
