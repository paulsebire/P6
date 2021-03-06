package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Topo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopoRepository extends JpaRepository <Topo,Long> {

    @Query("select t from Topo t order by t.date desc ")
     Page<Topo> listTopo(Pageable pageable);

    @Query("select t from Topo t  where t.nom like:x or t.description like:x order by t.date desc")
     Page<Topo> chercherTopo(@Param("x") String motCle, Pageable pageable);

    @Query(value = "select t from Topo t inner join fetch t.utilisateur u  where u.idUser=:idUser order by t.date desc",
            countQuery = "select count (t) from Topo t inner join t.utilisateur u where u.idUser=:idUser")
     Page<Topo> listTopoByUtilisateur(@Param("idUser")Long idUser, Pageable pageable);

    @Query(value = "select t from Topo t inner join fetch t.site s  where s.idSite=:idSite order by t.date desc",
            countQuery = "select count (t) from Topo t inner join t.site s where s.idSite=:idSite")
     Page<Topo> listTopoBySite(@Param("idSite")Long idSite, Pageable pageable);
}

