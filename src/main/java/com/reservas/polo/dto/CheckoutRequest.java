package com.reservas.polo.dto;

import java.math.BigDecimal;

public record CheckoutRequest(String fechaInicio,
		String fechaFin,
		BigDecimal total,
		Integer inmuebleId) {

}
