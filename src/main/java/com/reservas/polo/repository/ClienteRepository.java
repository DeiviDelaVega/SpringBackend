package com.reservas.polo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.reservas.polo.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	Cliente findByCorreo(String correo);

	@Query("SELECT c FROM   Cliente c WHERE  LOWER(c.apellido) LIKE LOWER(CONCAT('%', :filtro, '%'))"
			+ "OR  c.nroDocumento  LIKE CONCAT('%', :filtro, '%')")
	Page<Cliente> filtrarPorApellidoONroDocumento(String filtro, Pageable pageable);

	@Query("SELECT r.cliente.id, r.cliente.nombre, COUNT(r) as totalReservas "
			+ "FROM Reserva r GROUP BY r.cliente.id, r.cliente.nombre ORDER BY totalReservas DESC")
	List<Object[]> contarReservasPorCliente();
}