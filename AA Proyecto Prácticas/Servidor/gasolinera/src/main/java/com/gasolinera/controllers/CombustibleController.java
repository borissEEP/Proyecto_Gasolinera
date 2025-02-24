package com.gasolinera.controllers;

import com.gasolinera.clases.Combustible;
import com.gasolinera.repository.CombustibleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/combustibles")
public class CombustibleController {

	private final CombustibleRepository combustibleRepository;

	public CombustibleController(CombustibleRepository combustibleRepository) {
		this.combustibleRepository = combustibleRepository;
	}

	// Obtentemos todos los combustibles
	@GetMapping
	public List<Combustible> obtenerCombustibles() {
		return combustibleRepository.findAll();
	}

	// Obtenemos un combustible por ID
	@GetMapping("/{id}")
	public Optional<Combustible> obtenerCombustiblePorId(@PathVariable Integer id) {
		return combustibleRepository.findById(id);
	}
}
