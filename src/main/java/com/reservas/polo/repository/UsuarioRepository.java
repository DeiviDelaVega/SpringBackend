package com.reservas.polo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reservas.polo.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	Usuario findByCorreo(String correo);

}
