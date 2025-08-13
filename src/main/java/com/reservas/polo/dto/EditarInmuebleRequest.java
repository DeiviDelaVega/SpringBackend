package com.reservas.polo.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public record EditarInmuebleRequest(
		String nombre,
        int capacidad,
        int numeroHabitaciones,
        String descripcion,
        String serviciosIncluidos,
        String disponibilidad,
        BigDecimal precioPorNoche,
        BigDecimal latitud,
        BigDecimal longitud,
        Integer administradorId
) {}