package com.reservas.polo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.reservas.polo.model.Cliente;

public interface ClienteService {
	//
	public List<Cliente> listarTodosLosClientes();
	public Cliente registrarCliente(Cliente cliente, String clave);
	public Cliente guardarCliente(Cliente cliente);
	public Cliente findByCorreo(String correo);
	public Optional<Cliente> obtenerClientePorId(Integer id);
	public Cliente actualizarCliente(Cliente cliente);
	public void eliminarCliente(Integer id);
	//
	public List<Object[]> obtenerClientesMasReservas();
	public Page<Cliente> listarTodoPaginacion(Pageable pageable);
	public Page<Cliente> listarTodoConFiltro(String filtro, Pageable pageable);
}
