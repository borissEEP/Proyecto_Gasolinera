package com.gasolinera.controllers;

import com.gasolinera.clases.Surtidor;
import com.gasolinera.clases.Combustible;
import com.gasolinera.clases.SurtidorCombustible;
import com.gasolinera.repository.SurtidorRepository;
import com.gasolinera.repository.CombustibleRepository;
import com.gasolinera.repository.SurtidorCombustibleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/surtidores-combustibles")
public class SurtidorCombustibleController {

    private final SurtidorCombustibleRepository surtidorCombustibleRepository;
    private final SurtidorRepository surtidorRepository;
    private final CombustibleRepository combustibleRepository;

    public SurtidorCombustibleController(SurtidorCombustibleRepository surtidorCombustibleRepository,
                                         SurtidorRepository surtidorRepository,
                                         CombustibleRepository combustibleRepository) {
        this.surtidorCombustibleRepository = surtidorCombustibleRepository;
        this.surtidorRepository = surtidorRepository;
        this.combustibleRepository = combustibleRepository;
    }

    // Asignamos un combustible a un surtidor
    @PostMapping("/{surtidorId}/{combustibleId}")
    public SurtidorCombustible asignarCombustibleASurtidor(@PathVariable Integer surtidorId,
                                                           @PathVariable Integer combustibleId) {
        Optional<Surtidor> surtidorOpt = surtidorRepository.findById(surtidorId);
        Optional<Combustible> combustibleOpt = combustibleRepository.findById(combustibleId);

        if (surtidorOpt.isEmpty() || combustibleOpt.isEmpty()) {
            throw new RuntimeException("Surtidor o combustible no encontrado");
        }

        // Antes verificamos si la asignación ya existe
        List<SurtidorCombustible> asignacionesExistentes = surtidorCombustibleRepository.findBySurtidorId(surtidorId);
        for (SurtidorCombustible asignacion : asignacionesExistentes) {
            if (asignacion.getCombustible().getId().equals(combustibleId)) {
                throw new RuntimeException("Este surtidor ya tiene asignado este combustible");
            }
        }

        // Creamos la nueva asignación
        SurtidorCombustible nuevaAsignacion = new SurtidorCombustible();
        nuevaAsignacion.setSurtidor(surtidorOpt.get());
        nuevaAsignacion.setCombustible(combustibleOpt.get());

        return surtidorCombustibleRepository.save(nuevaAsignacion);
    }

    // Obtenemos los combustibles asignados a un surtidor
    @GetMapping("/{surtidorId}")
    public List<SurtidorCombustible> obtenerCombustiblesPorSurtidor(@PathVariable Integer surtidorId) {
        return surtidorCombustibleRepository.findBySurtidorId(surtidorId);
    }
}
