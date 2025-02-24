package com.gasolinera.clases;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tanques")
public class Tanque {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "combustible_id", nullable = false)
	private Combustible combustible;
	private Double capacidadTotal;
	private Double litrosActuales;
	
	public Tanque() {}
	
	public Tanque(Combustible combustible, Double capacidadTotal, Double litrosActuales) {
        this.combustible = combustible;
        this.capacidadTotal = capacidadTotal;
        this.litrosActuales = litrosActuales;
    }
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Combustible getCombustible(){
		return combustible;
	}
	
	public void setCombustible(Combustible combustible) {
		this.combustible = combustible;
	}
	
	public Double getCapacidadTotal() {
		return capacidadTotal;
	}
	
	public void setCapacidadTotal(Double capacidadTotal) {
		this.capacidadTotal = capacidadTotal;
	}
	
	public Double getLitrosActuales() {
		return litrosActuales;
	}
	
	public void setLitrosActuales(Double litrosActuales) {
		this.litrosActuales = litrosActuales;
	}
	
}
