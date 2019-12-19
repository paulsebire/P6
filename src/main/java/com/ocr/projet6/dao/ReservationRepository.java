package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    @Query(value = "select r from Reservation r inner join fetch r.utilisateur d where d.username=:username order by r.date desc",
    countQuery="select count (r) from Reservation r inner join r.utilisateur d where d.username=:username")
    public Page<Reservation> listResaDemande(@Param("username")String username, Pageable pageable);


    @Query(value = "select r from Reservation r inner join fetch r.topo t inner join fetch r.utilisateur d where d.username=:username and t.id=:idTopo and r.demandeEnCours=true",
          countQuery = "select count (r) from Reservation r inner join r.topo t  inner join  r.utilisateur d where d.username=:username and t.id=:idTopo and r.demandeEnCours=true")
    public List<Reservation> demandeEnCours (@Param("username")String username, @Param("idTopo") Long idTopo);

    @Query(value = "select r from Reservation r inner join fetch r.topo t inner join fetch t.utilisateur proprio where proprio.username=:username and r.demandeEnCours=true and r.acceptation=false ",
            countQuery = "select count (r) from Reservation r inner join r.topo t inner join t.utilisateur proprio where proprio.username=:username and r.demandeEnCours=true and r.acceptation=false ")
    public Page<Reservation> demandeEnAttenteAcceptation (@Param("username")String username, Pageable pageable);

    @Query(value = "select r from Reservation r inner join fetch r.topo t inner join fetch t.utilisateur propio inner join fetch r.utilisateur d where (propio.username=:username or d.username=:username ) and r.demandeEnCours=false and r.acceptation=true ",
            countQuery = "select count (r) from Reservation r inner join  r.topo t inner join  t.utilisateur propio inner join r.utilisateur d where (propio.username=:username or d.username=:username ) and r.demandeEnCours=false and r.acceptation=true ")
    public Page<Reservation> demandeAcceptees (@Param("username")String username, Pageable pageable);
}
