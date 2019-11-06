package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Voie;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface VoieRepository extends JpaRepository<Voie,Long> {
    @Query("select v from Voie v where v.site.nameSite =:x order by v.site.nameSite desc ")
    public Page<Voie> listVoie(@Param("x") String nameSite, Pageable pageable);
}
