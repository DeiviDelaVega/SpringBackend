package com.reservas.polo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reservas.polo.dto.LoginRequest;
import com.reservas.polo.dto.LoginResponse;
import com.reservas.polo.dto.RegistroAdminRequest;
import com.reservas.polo.dto.RegistroClienteRequest;
import com.reservas.polo.model.Administrador;
import com.reservas.polo.model.Cliente;
import com.reservas.polo.security.JwtService;
import com.reservas.polo.service.AdminService;
import com.reservas.polo.service.AuthService;
import com.reservas.polo.service.CaptchaService;
import com.reservas.polo.service.ClienteService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final ClienteService clienteService;
	private final AdminService adminService;
	private final AuthService authService;
	private final JwtService jwtService;
	private final CaptchaService captchaService;

	public AuthController(ClienteService clienteService, AdminService adminService, AuthService authService,
			JwtService jwtService, CaptchaService captchaService) {
		super();
		this.clienteService = clienteService;
		this.adminService = adminService;
		this.authService = authService;
		this.jwtService = jwtService;
		this.captchaService = captchaService;
	}
	
	@PostMapping("/captcha")
	  public ResponseEntity<Map<String, String>> captcha() throws IOException {
	    return ResponseEntity.ok(captchaService.generate());
	  }


	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
		
		if (!captchaService.verifyAndInvalidate(req.captchaId(), req.captchaCode())) {
		      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		    }
		
	    var user = authService.authenticate(req.email(), req.password()); // debe devolver email y role (admin|cliente)

	    String role = user.role();
	    role = (role != null && role.startsWith("ROLE_")) ? role : "ROLE_" + role;

	    String token = jwtService.generate(user.email(), role); // ✅ rol real

	    return ResponseEntity.ok(new LoginResponse(token, role, user.email()));
	}


	@PostMapping("/registro/cliente") // Consulta y respuesta de formulario Cliente
	public ResponseEntity<Void> registrarCliente(@RequestBody RegistroClienteRequest r) {
		var c = new Cliente();
		c.setNombre(r.nombre());
		c.setApellido(r.apellido());
		c.setNroDocumento(r.nroDocumento());
		c.setDireccion(r.direccion());
		c.setNumeroTelf(r.numeroTelf());
		c.setCorreo(r.correo());
		clienteService.registrarCliente(c, r.clave());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/registro/admin") // Consulta y respuesta de formulario Admin
	public ResponseEntity<Void> registrarAdmin(@RequestBody RegistroAdminRequest r) {
		var a = new Administrador();
		a.setNombre(r.nombre());
		a.setApellido(r.apellido());
		a.setNroDocumento(r.nroDocumento());
		a.setTelefono(r.telefono());
		a.setCorreo(r.correo());
		adminService.registrarAdmin(a, r.clave());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/me")
	public Map<String, Object> me(Authentication auth) {
        return Map.of("email", auth.getName(), "role", auth.getAuthorities().stream().findFirst().get().getAuthority());
    }
}
