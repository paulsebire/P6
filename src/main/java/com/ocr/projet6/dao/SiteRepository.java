package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site,Long> {

    public Site findSiteByNameSite(String nameSite);

}
