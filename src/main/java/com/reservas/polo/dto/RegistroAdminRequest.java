package com.reservas.polo.dto;

public record RegistroAdminRequest(String nombre, String apellido, String nroDocumento, 
		String telefono, String correo, String clave) {
}
