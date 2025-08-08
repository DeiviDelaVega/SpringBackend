package com.reservas.polo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reservas.polo.model.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

	public Administrador findByCorreo(String correo);

}