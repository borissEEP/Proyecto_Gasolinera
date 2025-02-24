package com.gasolinera.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gasolinera.clases.Tanque;
import com.gasolinera.repository.TanqueRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tanques")
public class TanqueController {

	private final TanqueRepository tanqueRepository;

	public TanqueController(TanqueRepository tanqueRepository) {
		this.tanqueRepository = tanqueRepository;
	}

	// Todos los tanques
	@GetMapping
	public List<Tanque> todosLosTanques() {
		return tanqueRepository.findAll();
	}

	// Un tanque por su ID
	@GetMapping("/{id}")
	public Optional<Tanque> obtenerTanquePorSuId(@PathVariable Integer id){
		return tanqueRepository.findById(id);
	}
}