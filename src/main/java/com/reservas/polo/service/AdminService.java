package com.reservas.polo.service;

import java.util.List;
import com.reservas.polo.model.Administrador;

public interface AdminService {
	void registrarAdmin(Administrador admin, String clave);
	public abstract List<Administrador> listarTodo();
	public Administrador obtenerPorId(int id);
	public Administrador findByCorreo(String correo);
}