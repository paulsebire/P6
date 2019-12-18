package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query(value = "select r from Reservation r  where r.usernameDemandeur=:username order by r.date desc")
    public Page<Reservation> listResaDemande(@Param("username")String username, Pageable pageable);


    @Query(value = "select r from Reservation r inner join fetch r.topo t  where r.usernameDemandeur=:username and t.id=:idTopo and r.demandeEnCours=:bool",
          countQuery = "select count (r) from Reservation r inner join r.topo t  where r.usernameDemandeur=:username and t.id=:idTopo and r.demandeEnCours=:bool")
    public List<Reservation> demandeEnCours (@Param("username")String username, @Param("idTopo") Long idTopo,@Param("bool")boolean bool);
}
