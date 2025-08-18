package com.reservas.polo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroAdminRequest(
		@NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
		String nombre, 
		
		@NotBlank(message = "El apellido no puede estar vacío")
        @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
		String apellido,
		
		@NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos")
		String nroDocumento, 
		
		@NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
		String telefono, 
		
		@NotBlank(message = "El correo es obligatorio")
        @Email(message = "Debe ingresar un correo válido")
        @Size(max = 50, message = "El correo no puede superar los 50 caracteres")
		String correo,
		
		@NotBlank(message = "La clave es obligatoria")
        @Size(min = 6, max = 50, message = "La clave debe tener entre 6 y 50 caracteres")
		String clave) {
}
