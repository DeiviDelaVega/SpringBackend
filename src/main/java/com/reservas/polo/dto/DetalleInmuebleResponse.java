package com.reservas.polo.dto;

import java.math.BigDecimal;

public record DetalleInmuebleResponse(
		int id,
	    String nombre,
	    Integer capacidad,
	    Integer numeroHabitaciones,
	    String descripcion,
	    String serviciosIncluidos,
	    String disponibilidad,
	    BigDecimal precioPorNoche,
	    String imagenHabitacion,
	    BigDecimal latitud,
	    BigDecimal longitud
) {}