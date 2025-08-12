package com.reservas.polo.repository;

import com.reservas.polo.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Page<Reserva> findByEstadoReserva(String estado, Pageable pageable);
    Page<Reserva> findByFechaInicioBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    List<Reserva> findByInmuebleId(Long idInmueble);
    @Query("SELECT r FROM Reserva r WHERE r.cliente.correo = :correo")
    Page<Reserva> findByCorreoCliente(@Param("correo") String correo, Pageable pageable);
    @Query("SELECT r.inmueble.nombre, COUNT(r) FROM Reserva r GROUP BY r.inmueble.nombre ORDER BY COUNT(r) DESC")
    List<Object[]> contarReservasPorInmueble();

}