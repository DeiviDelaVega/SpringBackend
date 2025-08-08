package com.reservas.polo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @Email
    @Column(nullable = false, unique = true)
    private String correo;

    @NotNull
    @Column(nullable = false)
    private String clave;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Rol rol;
    
	public Usuario() {
		
	}

	public Usuario(@Email String correo, @NotNull String clave, @NotNull Rol rol) {
		this.correo = correo;
		this.clave = clave;
		this.rol = rol;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
    
    
    

}