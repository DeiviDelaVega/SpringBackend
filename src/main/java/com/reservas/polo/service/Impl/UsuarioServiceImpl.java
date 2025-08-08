package com.reservas.polo.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reservas.polo.model.Rol;
import com.reservas.polo.model.Usuario;
import com.reservas.polo.repository.UsuarioRepository;
import com.reservas.polo.service.UsuarioService;


@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired 
	PasswordEncoder passwordEncoder;

	@Override
	public void registrarUsuario(String correo, String clave, Rol rol) {
		Usuario usuario = new Usuario();

		usuario.setCorreo(correo);
		usuario.setClave(passwordEncoder.encode(clave));
		usuario.setRol(rol);
		usuarioRepository.save(usuario);
	}
}
