package com.gasolinera.controllers;

import com.gasolinera.clases.Surtidor;
import com.gasolinera.repository.SurtidorRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/surtidores")
public class SurtidorController {

    private final SurtidorRepository surtidorRepository;

    public SurtidorController(SurtidorRepository surtidorRepository) {
        this.surtidorRepository = surtidorRepository;
    }
    
    // Obtenemos todos los surtidores
    @GetMapping
    public List<Surtidor> obtenerSurtidores() {
        return surtidorRepository.findAll();
    }

    // Surtidor por su ID
    @GetMapping("/{id}")
    public Optional<Surtidor> obtenerSurtidorPorId(@PathVariable Integer id) {
        return surtidorRepository.findById(id);
    }

    // Crear un nuevo surtidor
    @PostMapping
    public ResponseEntity<Surtidor> crearSurtidor(@RequestBody Surtidor surtidor) {
        Surtidor nuevoSurtidor = surtidorRepository.save(surtidor);
        URI location = URI.create("/surtidores/" + nuevoSurtidor.getId());
        return ResponseEntity.created(location).body(nuevoSurtidor);
    }
    
    // Editar un surtidor existente (opcional)
    @PutMapping("/{id}")
    public Surtidor actualizarSurtidor(@PathVariable Integer id, @RequestBody Surtidor surtidor) {
        if (surtidorRepository.existsById(id)) {
            surtidor.setId(id);
            return surtidorRepository.save(surtidor);
        }
        return null;
    }

    // Borramos un surtidor por ID
    @DeleteMapping("/{id}")
    public void eliminarSurtidor(@PathVariable Integer id) {
        surtidorRepository.deleteById(id);
    }

}
