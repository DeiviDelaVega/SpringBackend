package com.reservas.polo.service;

import com.reservas.polo.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaService {
    Page<Reserva> listarTodas(Pageable pageable);
    Page<Reserva> filtrarPorEstado(String estado, Pageable pageable);
    Page<Reserva> filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    Optional<Reserva> obtenerPorId(Long id);
    void actualizarEstado(Long id, String nuevoEstado);
    void eliminar(Long id);
    public List<LocalDate> obtenerFechasOcupadas(Long id);
    public Reserva guardar(Reserva reserva);
    Page<Reserva> listarReservasPorCliente(String correo, Pageable pageable);
    List<Object[]> obtenerInmueblesMasReservados();

}