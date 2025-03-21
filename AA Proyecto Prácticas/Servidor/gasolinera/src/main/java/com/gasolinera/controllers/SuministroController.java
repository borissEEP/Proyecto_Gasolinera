package com.gasolinera.controllers;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gasolinera.clases.Combustible;
import com.gasolinera.clases.Suministro;
import com.gasolinera.clases.Surtidor;
import com.gasolinera.clases.Tanque;
import com.gasolinera.repository.CombustibleRepository;
import com.gasolinera.repository.SuministroRepository;
import com.gasolinera.repository.SurtidorRepository;
import com.gasolinera.repository.TanqueRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/suministros")
public class SuministroController {

	private final SuministroRepository suministroRepository;
	private final SurtidorRepository surtidorRepository;
	private final CombustibleRepository combustibleRepository;
	private final TanqueRepository tanqueRepository;

	private SuministroController(SuministroRepository suministroRepository, SurtidorRepository surtidorRepository,
			CombustibleRepository combustibleRepository, TanqueRepository tanqueRepository) {

		this.suministroRepository = suministroRepository;
		this.surtidorRepository = surtidorRepository;
		this.combustibleRepository = combustibleRepository;
		this.tanqueRepository = tanqueRepository;
	}

	// Registramos un suministro de combustible (venta de gasolina)
	@PostMapping
	public ResponseEntity<Suministro> crearSuministro(@RequestBody Suministro suministro) {
	    // Validaciones previas
	    if (suministro.getSurtidor() == null || suministro.getSurtidor().getId() == null) {
	        return ResponseEntity.badRequest().body(null);
	    }

	    if (suministro.getCombustible() == null || suministro.getCombustible().getId() == null) {
	        return ResponseEntity.badRequest().body(null);
	    }

	    if (suministro.getCantidadLitros() == null || suministro.getCantidadLitros() <= 0) {
	        return ResponseEntity.badRequest().body(null);
	    }

	    // Buscamos un surtidor y combustible en la base de datos
	    Optional<Surtidor> surtidorOptional = surtidorRepository.findById(suministro.getSurtidor().getId());
	    Optional<Combustible> combustibleOptional = combustibleRepository.findById(suministro.getCombustible().getId());
	    Optional<Tanque> tanqueOptional = tanqueRepository.findByCombustibleId(suministro.getCombustible().getId());

	    if (surtidorOptional.isEmpty() || combustibleOptional.isEmpty() || tanqueOptional.isEmpty()) {
	        return ResponseEntity.badRequest().body(null);
	    }

	    Tanque tanque = tanqueOptional.get();
	    if (suministro.getCantidadLitros() > tanque.getLitrosActuales()) {
	        return ResponseEntity.badRequest().body(null);
	    }

	    // Asignamos datos al suministro
	    suministro.setSurtidor(surtidorOptional.get());
	    suministro.setCombustible(combustibleOptional.get());
	    suministro.setFecha(LocalDateTime.now());
	    suministro.setImporte(suministro.getCantidadLitros() * combustibleOptional.get().getPrecio());

	    // Restamos litros del tanque
	    tanque.setLitrosActuales(tanque.getLitrosActuales() - suministro.getCantidadLitros());
	    tanqueRepository.save(tanque);

	    // Guardamos el suministro en la base de datos
	    Suministro nuevoSuministro = suministroRepository.save(suministro);

	    // Devolvemos código 201 Created con la URL del nuevo suministro
	    URI location = URI.create("/suministros/" + nuevoSuministro.getId());
	    return ResponseEntity.created(location).body(nuevoSuministro);
	}


	// Obtenemos todos los suministros
	@GetMapping
	private List<Suministro> getAllSuministros() {
		return suministroRepository.findAll();
	}

	// Generamos un reporte después de 25 suministros aleatorios
	@GetMapping("/reporte")
	private ResponseEntity<?> generarReporte() {
		List<Suministro> suministros = suministroRepository.findAll();

		if (suministros.size() < 25) {
			return ResponseEntity.badRequest().body("Error: Se necesitan al menos 25 suministros para generar el reporte.");
		}

		Map<String, Double> litrosVendidosPorCombustible = new HashMap<>();
		Map<String, Double> importeFacturadoPorCombustible = new HashMap<>();
		double totalFacturado = 0.0;

		// Obtener litros restantes en cada tanque
		List<Tanque> tanques = tanqueRepository.findAll();
		Map<String, Double> litrosRestantesPorTanque = new HashMap<>();
		for (Tanque tanque : tanques) {
			litrosRestantesPorTanque.put(tanque.getCombustible().getNombre(), tanque.getLitrosActuales());
		}

		// Recorrer los suministros y calcular los totales
		for (Suministro suministro : suministros) {
			String combustibleNombre = suministro.getCombustible().getNombre();
			double litros = suministro.getCantidadLitros();
			double importe = suministro.getImporte();

			litrosVendidosPorCombustible.put(combustibleNombre,
					litrosVendidosPorCombustible.getOrDefault(combustibleNombre, 0.0) + litros);

			importeFacturadoPorCombustible.put(combustibleNombre,
					importeFacturadoPorCombustible.getOrDefault(combustibleNombre, 0.0) + importe);

			totalFacturado += importe;

			// Restamos los litros servidos del tanque correspondiente
			if (litrosRestantesPorTanque.containsKey(combustibleNombre)) {
				litrosRestantesPorTanque.put(combustibleNombre,
						litrosRestantesPorTanque.get(combustibleNombre) - litros);
			}
		}

		// Construir la respuesta adecuada para el JSON
		Map<String, Object> reporte = new HashMap<>();
		reporte.put("litrosVendidosPorCombustible", litrosVendidosPorCombustible);
		reporte.put("importeFacturadoPorCombustible", importeFacturadoPorCombustible);
		reporte.put("totalFacturado", totalFacturado);
		reporte.put("litrosRestantesPorTanque", litrosRestantesPorTanque);

		return ResponseEntity.ok(reporte);
	}

	// Borramos un suministro por su ID
	@DeleteMapping("/{id}")
	private ResponseEntity<Void> borrarSuministro(@PathVariable Integer id) {
		if (!suministroRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		suministroRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

}
