package com.reservas.polo.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservas.polo.model.Administrador;
import com.reservas.polo.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	AdminService adminService;
	
	@GetMapping("/ping")
	public String ping() {
		return "ok admin";
	}

  @GetMapping("/home")
  public String home() {
    return "home admin";
  }
  
	//Endpoint para obtener nombre y apellido del administrador autenticado
	@GetMapping("/me")
	public Map<String, Object> meAdmin(Principal principal) {
		Administrador admin = adminService.findByCorreo(principal.getName());
		if (admin == null) {
			return Map.of("error", "Administrador no encontrado");
		}
		return Map.of(
				"nombre", admin.getNombre(),
				"apellido", admin.getApellido()
				);
	}
}
