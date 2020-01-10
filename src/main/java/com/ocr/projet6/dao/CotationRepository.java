package com.ocr.projet6.dao;

import com.ocr.projet6.entities.Cotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotationRepository extends JpaRepository<Cotation,Long> {

public Cotation findByCote(String cote);
}
