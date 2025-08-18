package com.reservas.polo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "Cliente", uniqueConstraints = { @UniqueConstraint(columnNames = "Correo"),
		@UniqueConstraint(columnNames = "nroDocumento") })
@NamedQuery(name = "Cliente", query = "select e from Cliente e")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_Cliente")
	private int idCliente;

	@NotNull
	@Column(nullable = false)
	private String nombre;

	@NotNull
	@Column(nullable = false)
	private String apellido;

	@NotNull
	@Column(name = "Nro_Documento", nullable = false, unique = true)
	@Size(min = 8, max = 8, message = "El numero de documento debe  tener 8 digitos")
	@Pattern(regexp = "\\d{8}", message = "El número de documento debe contener solo números")
	private String nroDocumento;

	@NotNull
	@Column(nullable = false)
	private String direccion;

	@NotNull
	@Size(min = 9, max = 9, message = "El número de teléfono debe tener 9 dígitos")
	@Pattern(regexp = "\\d{9}", message = "El número de teléfono debe contener solo números")
	@Column(name = "Numero_Telf", nullable = false)
	private String numeroTelf;

	@NotNull
	@Column(name = "FechaRegistro", nullable = false)
	private LocalDateTime fechaRegistro;

	@Email
	@NotNull
	@Column(name = "Correo", nullable = false, unique = true)
	private String correo;

	public enum EstadoCliente {
		activo, sancionado
	}

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EstadoCliente estado = EstadoCliente.activo;

	public Cliente() {

	}

	public Cliente(int idCliente, @NotNull String nombre, @NotNull String apellido,
			@NotNull @Size(min = 8, max = 8, message = "El numero de documento debe  tener 8 digitos") @Pattern(regexp = "\\d{8}", message = "El número de documento debe contener solo números") String nroDocumento,
			@NotNull String direccion,
			@NotNull @Size(min = 9, max = 9, message = "El número de teléfono debe tener 9 dígitos") @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe contener solo números") String numeroTelf,
			@NotNull LocalDateTime fechaRegistro, @Email @NotNull String correo, EstadoCliente estado) {
		super();
		this.idCliente = idCliente;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nroDocumento = nroDocumento;
		this.direccion = direccion;
		this.numeroTelf = numeroTelf;
		this.fechaRegistro = fechaRegistro;
		this.correo = correo;
		this.estado = estado;
	}

	public Cliente(@NotNull String nombre, @NotNull String apellido,
			@NotNull @Size(min = 8, max = 8, message = "El numero de documento debe  tener 8 digitos") @Pattern(regexp = "\\d{8}", message = "El número de documento debe contener solo números") String nroDocumento,
			@NotNull String direccion,
			@NotNull @Size(min = 9, max = 9, message = "El número de teléfono debe tener 9 dígitos") @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe contener solo números") String numeroTelf,
			@NotNull LocalDateTime fechaRegistro, @Email @NotNull String correo, EstadoCliente estado) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.nroDocumento = nroDocumento;
		this.direccion = direccion;
		this.numeroTelf = numeroTelf;
		this.fechaRegistro = fechaRegistro;
		this.correo = correo;
		this.estado = estado;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNumeroTelf() {
		return numeroTelf;
	}

	public void setNumeroTelf(String numeroTelf) {
		this.numeroTelf = numeroTelf;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public EstadoCliente getEstado() {
		return estado;
	}

	public void setEstado(EstadoCliente estado) {
		this.estado = estado;
	}

}
