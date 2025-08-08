package com.reservas.polo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pago")
public class Pago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_Pago")
	private Integer idPago;

	@OneToOne
	@JoinColumn(name = "id_solicitud")
	private Reserva reserva;

	@Column(name = "Fecha_Pago")
	private LocalDateTime fechaPago;

	private BigDecimal monto;

	@Column(name = "stripe_payment_id")
	private String stripePaymentId;

	public Pago() {
	}

	public Pago(Integer idPago, Reserva reserva, LocalDateTime fechaPago, BigDecimal monto, String stripePaymentId) {
		super();
		this.idPago = idPago;
		this.reserva = reserva;
		this.fechaPago = fechaPago;
		this.monto = monto;
		this.stripePaymentId = stripePaymentId;
	}

	public Pago(Reserva reserva, LocalDateTime fechaPago, BigDecimal monto, String stripePaymentId) {
		super();
		this.reserva = reserva;
		this.fechaPago = fechaPago;
		this.monto = monto;
		this.stripePaymentId = stripePaymentId;
	}

	public Integer getIdPago() {
		return idPago;
	}

	public void setIdPago(Integer idPago) {
		this.idPago = idPago;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public LocalDateTime getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDateTime fechaPago) {
		this.fechaPago = fechaPago;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getStripePaymentId() {
		return stripePaymentId;
	}

	public void setStripePaymentId(String stripePaymentId) {
		this.stripePaymentId = stripePaymentId;
	}

}
