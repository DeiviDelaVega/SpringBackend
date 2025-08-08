package com.reservas.polo.service.Impl;

import com.reservas.polo.model.Usuario;
import com.reservas.polo.repository.UsuarioRepository;
import com.reservas.polo.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AuthUser authenticate(String username, String rawPassword) {
		Usuario u = usuarioRepository.findByCorreo(username);
		if (u == null)
			throw new RuntimeException("Usuario no encontrado");
		if (!passwordEncoder.matches(rawPassword, u.getClave()))
			throw new RuntimeException("Credenciales inválidas");
		String role = "ROLE_" + u.getRol().name(); // admin | cliente -> ROLE_admin | ROLE_cliente
		return new AuthUser(u.getCorreo(), role);
	}
}
