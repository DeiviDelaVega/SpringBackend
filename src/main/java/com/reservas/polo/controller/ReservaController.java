package com.reservas.polo.controller;

import com.reservas.polo.model.Reserva;
import com.reservas.polo.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Listar todas con paginación
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarReservas(
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Reserva> reservas = reservaService.listarTodas(pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("reservas", reservas.getContent());
        response.put("currentPage", reservas.getNumber());
        response.put("totalItems", reservas.getTotalElements());
        response.put("totalPages", reservas.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // Filtrar por estado con paginación
    @GetMapping("/filtrar-estado")
    public ResponseEntity<Map<String, Object>> filtrarPorEstado(
            @RequestParam String estado,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Reserva> reservas = reservaService.filtrarPorEstado(estado, pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("reservas", reservas.getContent());
        response.put("currentPage", reservas.getNumber());
        response.put("totalItems", reservas.getTotalElements());
        response.put("totalPages", reservas.getTotalPages());
        response.put("estado", estado);

        return ResponseEntity.ok(response);
    }

    // Filtrar por rango de fechas con paginación
    @GetMapping("/filtrar-fechas")
    public ResponseEntity<Map<String, Object>> filtrarPorFechas(
            @RequestParam("fechaInicio") LocalDate fechaInicio,
            @RequestParam("fechaFin") LocalDate fechaFin,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Reserva> reservas = reservaService.filtrarPorFechas(fechaInicio, fechaFin, pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("reservas", reservas.getContent());
        response.put("currentPage", reservas.getNumber());
        response.put("totalItems", reservas.getTotalElements());
        response.put("totalPages", reservas.getTotalPages());
        response.put("fechaInicio", fechaInicio);
        response.put("fechaFin", fechaFin);

        return ResponseEntity.ok(response);
    }

    // Detalle de una reserva
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> detalleReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return ResponseEntity.ok(reserva);
    }

    // Actualizar estado de una reserva
    @PutMapping("/{id}/estado")
    public ResponseEntity<Map<String, String>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam("estado") String nuevoEstado) {

        reservaService.actualizarEstado(id, nuevoEstado);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Estado de la reserva actualizado correctamente");
        return ResponseEntity.ok(response);
    }

    // Eliminar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminar(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Reserva eliminada correctamente");
        return ResponseEntity.ok(response);
    }
}
