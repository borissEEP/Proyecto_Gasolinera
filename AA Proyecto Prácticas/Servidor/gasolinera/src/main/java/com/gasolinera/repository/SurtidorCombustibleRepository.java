package com.gasolinera.repository;

import com.gasolinera.clases.SurtidorCombustible;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurtidorCombustibleRepository extends JpaRepository<SurtidorCombustible, Integer> {
    List<SurtidorCombustible> findBySurtidorId(Integer surtidorId);
}
