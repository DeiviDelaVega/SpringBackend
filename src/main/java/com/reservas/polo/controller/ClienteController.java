package com.reservas.polo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

	@GetMapping("/ping")
	public String ping() {
		return "ok cliente";
	}

	// Ejemplo: dashboard del cliente
	@GetMapping("/home")
	public String home() {
		return "home cliente";
	}
}