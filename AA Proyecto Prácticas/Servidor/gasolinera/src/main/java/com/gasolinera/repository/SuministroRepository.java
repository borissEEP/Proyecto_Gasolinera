package com.gasolinera.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gasolinera.clases.Suministro;

public interface SuministroRepository extends JpaRepository<Suministro, Integer> {
	boolean existsBySurtidorId(Integer id);
	List<Suministro> findBySurtidorId(Integer id);

}
