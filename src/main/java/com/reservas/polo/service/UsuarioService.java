package com.reservas.polo.service;

import com.reservas.polo.model.Rol;

public interface UsuarioService {

	void registrarUsuario(String correo, String clave, Rol rol);
}
