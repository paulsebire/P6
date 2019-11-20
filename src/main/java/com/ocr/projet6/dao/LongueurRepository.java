package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Longueur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface LongueurRepository extends JpaRepository<Longueur,Long> {
    @Query(value = "select l from Longueur l inner join fetch l.voie v inner join fetch v.site s where s.idSite=:idSite order by l.idLongueur",
    countQuery = "select count (l) from Longueur l inner join l.voie v inner join v.site s where s.idSite=:idSite")
    public Page<Longueur> listLongueur(@Param("idSite") Long idSite, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value="delete from Longueur l where l.voie.idVoie=:idVoie")
    void deleteByVoie(Long idVoie);
}
