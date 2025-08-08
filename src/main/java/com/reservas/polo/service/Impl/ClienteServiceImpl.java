package com.reservas.polo.service.Impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.reservas.polo.model.Cliente;
import com.reservas.polo.model.Rol;
import com.reservas.polo.repository.ClienteRepository;
import com.reservas.polo.repository.UsuarioRepository;
import com.reservas.polo.service.ClienteService;
import com.reservas.polo.service.EmailService;
import com.reservas.polo.service.UsuarioService;

import jakarta.mail.MessagingException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {
	private ClienteRepository clienteRepository;
	private UsuarioService usuarioService;
	private final EmailService emailService;

	public ClienteServiceImpl(ClienteRepository clienteRepo, UsuarioRepository usuarioRepo,
			UsuarioService usuarioService, EmailService emailService) {
		this.clienteRepository = clienteRepo;
		this.usuarioService = usuarioService;
		this.emailService = emailService;
	}

	@Override
	@Transactional
	public void registrarCliente(Cliente cliente, String clave) {
		cliente.setFechaRegistro(LocalDateTime.now());
		clienteRepository.save(cliente);
		usuarioService.registrarUsuario(cliente.getCorreo(), clave, Rol.cliente);

		String asunto = "Bienvenido a Polo Web Reservas";

		String cuerpoHtml = "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<h2 style='color: #2E86C1;'>¡Bienvenido a Polo Web Reservas! 🚀</h2>" + "<p>Hola <strong>"
				+ cliente.getNombre() + "</strong>,</p>"
				+ "<p>Gracias por registrarte con nosotros. Estamos encantados de tenerte.</p>"
				+ "<p>Si tienes alguna consulta, no dudes en contactarnos.</p>"
				+ "<p>Saludos cordiales,<br/>El equipo de Polo Web Reservas</p>" + "</body>" + "</html>";
		try {
			emailService.sendHtmlEmail(cliente.getCorreo(), asunto, cuerpoHtml);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> listarTodosLosClientes() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente findByCorreo(String correo) {
		return clienteRepository.findByCorreo(correo);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente obtenerClientePorId(Integer id) {
		return clienteRepository.findById(id).get();
	}

	@Override
	public Cliente actualizarCliente(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	@Override
	public void eliminarCliente(Integer id) {
		clienteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> listarTodoPaginacion(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> listarTodoConFiltro(String filtro, Pageable pageable) {
		if (filtro == null || filtro.trim().isEmpty()) {
			return clienteRepository.findAll(pageable);
		}
		return clienteRepository.filtrarPorApellidoONroDocumento(filtro.trim(), pageable);
	}

	@Override
	public List<Object[]> obtenerClientesMasReservas() {
		return clienteRepository.contarReservasPorCliente();
	}
}