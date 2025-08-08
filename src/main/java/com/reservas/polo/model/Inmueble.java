package com.reservas.polo.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Inmueble")
public class Inmueble {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_Inmueble")
	private int id;

	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
	@Column(name = "Nombre", nullable = false, length = 100)
	private String nombre;

	@NotNull(message = "La capacidad no puede estar vacio")
	@Positive(message = "La capacidad debe ser un número positivo")
	@Column(name = "Capacidad", nullable = false)
	private int capacidad;

	@NotNull(message = "El numero de habitaciones no puede estar vacio")
	@Positive(message = "El numero de habitaciones debe ser un número positivo")
	@Column(name = "Numero_Habitaciones", nullable = false)
	private int numeroHabitaciones;

	@NotNull
	@NotBlank(message = "La descripcion no puede estar vacio")
	@Size(max = 300, message = "La descripcion no debe exceder los 300 caracteres")
	@Column(name = "Descripcion", length = 300, nullable = false)
	private String descripcion;

	@NotNull
	@NotBlank(message = "Los servicios incluidos no pueden estar vacios")
	@Size(max = 200, message = "Los servicios incluidos no deben exceder los 200 caracteres")
	@Column(name = "Servicios_Incluidos", length = 200, nullable = false)
	private String serviciosIncluidos;

	@Column(name = "Disponibilidad", length = 2)
	private String disponibilidad;

	@NotNull(message = "El precio por noche no puede estar vacio")
	@Positive(message = "El precio por noche debe ser un número positivo")
	@Column(name = "Precio_Por_Noche", precision = 10, scale = 2, nullable = false)
	private BigDecimal precioPorNoche;

	@Size(max = 300, message = "La imagen de la habitacion no debe exceder los 300 caracteres")
	@Column(name = "Imagen_Habitacion", length = 300, nullable = false)
	private String imagenHabitacion;

	@NotNull(message = "Debe seleccionar un administrador")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_Administrador")
	private Administrador administrador;

	@NotNull(message = "Debe seleccionar una ubicación")
	@Column(name = "Latitud", precision = 10, scale = 8)
	private BigDecimal latitud;

	@NotNull(message = "Debe seleccionar una ubicación")
	@Column(name = "Longitud", precision = 11, scale = 8)
	private BigDecimal longitud;

	public Inmueble(
			@NotBlank(message = "El nombre no puede estar vacío") @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres") String nombre,
			@NotNull(message = "La capacidad no puede estar vacio") @Positive(message = "La capacidad debe ser un número positivo") int capacidad,
			@NotNull(message = "El numero de habitaciones no puede estar vacio") @Positive(message = "El numero de habitaciones debe ser un número positivo") int numeroHabitaciones,
			@NotNull @NotBlank(message = "La descripcion no puede estar vacio") @Size(max = 300, message = "La descripcion no debe exceder los 300 caracteres") String descripcion,
			@NotNull @NotBlank(message = "Los servicios incluidos no pueden estar vacios") @Size(max = 200, message = "Los servicios incluidos no deben exceder los 200 caracteres") String serviciosIncluidos,
			String disponibilidad,
			@NotNull(message = "El precio por noche no puede estar vacio") @Positive(message = "El precio por noche debe ser un número positivo") BigDecimal precioPorNoche,
			@NotNull(message = "La imagen de la habitacion no puede estar vacia") @Size(max = 300, message = "La imagen de la habitacion no debe exceder los 300 caracteres") String imagenHabitacion,
			@NotNull(message = "Debe seleccionar un administrador") Administrador administrador,
			@NotNull(message = "Debe seleccionar una ubicación") BigDecimal latitud,
			@NotNull(message = "Debe seleccionar una ubicación") BigDecimal longitud) {
		this.nombre = nombre;
		this.capacidad = capacidad;
		this.numeroHabitaciones = numeroHabitaciones;
		this.descripcion = descripcion;
		this.serviciosIncluidos = serviciosIncluidos;
		this.disponibilidad = disponibilidad;
		this.precioPorNoche = precioPorNoche;
		this.imagenHabitacion = imagenHabitacion;
		this.administrador = administrador;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public Inmueble() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public int getNumeroHabitaciones() {
		return numeroHabitaciones;
	}

	public void setNumeroHabitaciones(int numeroHabitaciones) {
		this.numeroHabitaciones = numeroHabitaciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getServiciosIncluidos() {
		return serviciosIncluidos;
	}

	public void setServiciosIncluidos(String serviciosIncluidos) {
		this.serviciosIncluidos = serviciosIncluidos;
	}

	public String getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(String disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public BigDecimal getPrecioPorNoche() {
		return precioPorNoche;
	}

	public void setPrecioPorNoche(BigDecimal precioPorNoche) {
		this.precioPorNoche = precioPorNoche;
	}

	public String getImagenHabitacion() {
		return imagenHabitacion;
	}

	public void setImagenHabitacion(String imagenHabitacion) {
		this.imagenHabitacion = imagenHabitacion;
	}

	public BigDecimal getLatitud() {
		return latitud;
	}

	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}

	public BigDecimal getLongitud() {
		return longitud;
	}

	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}

	public Administrador getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Administrador administrador) {
		this.administrador = administrador;
	}
}