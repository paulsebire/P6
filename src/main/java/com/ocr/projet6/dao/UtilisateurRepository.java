package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    Utilisateur findByUsername(String username);
    Utilisateur findByEmail(String email);

    @Query("select u from Utilisateur u order by u.idUser asc ")
     Page<Utilisateur> listUtilisateur(Pageable pageable);


    @Query("select u from Utilisateur u  where u.username like:x or u.lastname like:x or u.firstname like:x or u.email like:x order by u.idUser")
     Page<Utilisateur> chercherUtilisateur(@Param("x") String motCle, Pageable pageable);

}

