package com.reservas.polo.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateInmuebleRequest(
		int id, // 0 = nuevo

		@NotBlank String nombre,

		@NotNull @Min(1) Integer capacidad,

		@NotNull @Min(1) Integer numeroHabitaciones,

		@NotBlank String descripcion,

		@NotBlank String serviciosIncluidos,

		@NotBlank String disponibilidad,

		@NotNull @DecimalMin("0.01") BigDecimal precioPorNoche,

		@NotNull BigDecimal latitud,

		@NotNull BigDecimal longitud) {
}
