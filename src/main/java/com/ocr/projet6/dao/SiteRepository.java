package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Site;
import com.ocr.projet6.entities.Voie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site,Long> {

    @Query("select s from Site s order by s.idSite asc ")
    public Page<Site> listSite(Pageable pageable);

    @Query("select s from Site s where s.urlImg<>null order by s.idSite asc ")
    public List<Site> listSiteWithImg();


    @Query("select s from Site s  where s.nameSite like:x or s.localisation  like:x order by s.idSite")
    public Page<Site> chercher(@Param("x") String motCle, Pageable pageable);

}
