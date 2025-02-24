package com.gasolinera.clases;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "suministros")
public class Suministro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "surtidor_id", nullable = false)
	private Surtidor surtidor;

	@ManyToOne
	@JoinColumn(name = "combustible_id", nullable = false)
	private Combustible combustible;

	private Double cantidadLitros;
	private Double importe;
	
	private LocalDateTime fecha;

	public Suministro() {
		this.fecha = LocalDateTime.now();
	}


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

    public Double getCantidadLitros() {
        return cantidadLitros;
    }

    public void setCantidadLitros(Double cantidadLitros) {
        this.cantidadLitros = cantidadLitros;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
