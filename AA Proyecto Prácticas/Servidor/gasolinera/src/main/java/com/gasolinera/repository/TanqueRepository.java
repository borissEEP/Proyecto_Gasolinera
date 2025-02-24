package com.gasolinera.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gasolinera.clases.Tanque;

public interface TanqueRepository extends JpaRepository<Tanque, Integer> {

	Optional<Tanque> findByCombustibleId(Integer id);

}
