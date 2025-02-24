package com.gasolinera.clases;

import jakarta.persistence.*;

@Entity
@Table(name = "surtidor_combustible")
public class SurtidorCombustible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "surtidor_id", nullable = false)
    private Surtidor surtidor;

    @ManyToOne
    @JoinColumn(name = "combustible_id", nullable = false)
    private Combustible combustible;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Surtidor getSurtidor() {
        return surtidor;
    }

    public void setSurtidor(Surtidor surtidor) {
        this.surtidor = surtidor;
    }

    public Combustible getCombustible() {
        return combustible;
    }

    public void setCombustible(Combustible combustible) {
        this.combustible = combustible;
    }
}
