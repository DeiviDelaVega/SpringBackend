package com.reservas.polo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "Administrador")
public class Administrador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_Administrador")
	private Integer id;

	@NotNull
	@Size(max = 100)
	@Column(name = "Nombre", nullable = false, length = 100)
	private String nombre;

	@NotNull
	@Size(max = 100)
	@Column(name = "Apellido", nullable = false, length = 100)
	private String apellido;

	@NotNull
	@Size(max = 50)
	@Column(name = "Nro_Documento", nullable = false, length = 50, unique = true)
	private String nroDocumento;

	@NotNull
	@Size(max = 50)
	@Column(name = "Telefono", nullable = false, length = 50)
	private String telefono;

	@NotNull
	@Email
	@Size(max = 50)
	@Column(name = "Correo", nullable = false, length = 50, unique = true)
	private String correo;

	public Administrador() {

	}

	public Administrador(Integer id, @NotNull @Size(max = 100) String nombre, @NotNull @Size(max = 100) String apellido,
			@NotNull @Size(max = 50) String nroDocumento, @NotNull @Size(max = 50) String telefono,
			@NotNull @Email @Size(max = 50) String correo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nroDocumento = nroDocumento;
		this.telefono = telefono;
		this.correo = correo;
	}

	public Administrador(@NotNull @Size(max = 100) String nombre, @NotNull @Size(max = 100) String apellido,
			@NotNull @Size(max = 50) String nroDocumento, @NotNull @Size(max = 50) String telefono,
			@NotNull @Email @Size(max = 50) String correo) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.nroDocumento = nroDocumento;
		this.telefono = telefono;
		this.correo = correo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
}
