package com.reservas.polo.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservas.polo.model.Cliente;
import com.reservas.polo.service.ClienteService;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

	@Autowired
	ClienteService clienteService;

	// Ejemplo: dashboard del cliente
	@GetMapping("/home")
	public String home() {
		return "home cliente";
	}
	
	// Endpoint para obtener nombre y apellido del cliente autenticado
    @GetMapping("/me")
    public Map<String, Object> meCliente(Principal principal) {
        Cliente cliente = clienteService.findByCorreo(principal.getName());
        if (cliente == null) {
            return Map.of("error", "Cliente no encontrado");
        }
        return Map.of(
            "nombre", cliente.getNombre(),
            "apellido", cliente.getApellido()
        );
    }
}