package com.reservas.polo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reserva")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Solicitud")
    private Integer id;

    @Column(name = "Fecha_Solicitud", nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();


    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_Cliente")
    private Cliente cliente;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_Inmueble")
    private Inmueble inmueble;

    @NotNull
    @Column(name = "Fecha_Inicio_Reserva")
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "Fecha_Fin_Reserva")
    private LocalDate fechaFin;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "Tarjeta", message = "Solo se permite el método de pago 'Tarjeta'")
    @Column(name = "Metodo_Pago")
    private String metodoPago;

    @NotNull
    @DecimalMin(value = "0.01")
    @Column(name = "Monto_Total")
    private BigDecimal montoTotal;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "Solicitado|Aprobado|Cancelado|Finalizado")
    @Column(name = "Estado_Reserva")
    private String estadoReserva = "Solicitado";

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Inmueble getInmueble() {
		return inmueble;
	}

	public void setInmueble(Inmueble inmueble) {
		this.inmueble = inmueble;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public String getEstadoReserva() {
		return estadoReserva;
	}

	public void setEstadoReserva(String estadoReserva) {
		this.estadoReserva = estadoReserva;
	}
    
    
}
