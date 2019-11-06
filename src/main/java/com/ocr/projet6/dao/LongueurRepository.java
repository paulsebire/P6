package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Longueur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LongueurRepository extends JpaRepository<Longueur,Long> {
    @Query("select l from Longueur l where l.site.nameSite =:x order by l.site desc ")
    public Page<Longueur> listLongueur(@Param("x") String nomSite, Pageable pageable);
}
