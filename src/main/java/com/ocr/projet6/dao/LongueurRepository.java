package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Longueur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.TypedQuery;


public interface LongueurRepository extends JpaRepository<Longueur,Long> {
    @Query("select l from Longueur l where l.voie.idVoie =:x order by l.voie.idVoie desc ")
    public Page<Longueur> listLongueur(@Param("x") Long idVoie, Pageable pageable);

    /*
    @Query
    public void longueurWithMultipleJoinOnVoieAndSite(){
    TypedQuery<Longueur> query=entityManager.createQuery(
    "SELECT l from Longueur l
    JOIN voie v
    JOIN site s
    WHERE
    )

*/
}
