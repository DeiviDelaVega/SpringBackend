package com.reservas.polo.dto;

public record RegistroClienteRequest(String nombre, String apellido, String nroDocumento, String direccion,
		String numeroTelf, String correo, String clave) {

}
