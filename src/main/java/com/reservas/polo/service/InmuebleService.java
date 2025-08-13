package com.reservas.polo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.reservas.polo.dto.DetalleInmuebleResponse;
import com.reservas.polo.model.Inmueble;

public interface InmuebleService {
	public List<Inmueble> listarTodo();
	public Page<Inmueble> listarTodoPaginacion(Pageable pageable);
	public Page<Inmueble> listarTodoConFiltro(String filtro, Pageable pageable);
	public Page<Inmueble> listarTodoConFiltroYDisponibilidad(String filtro, String disponibilidad, Pageable pageable);
	public Page<Inmueble> listarTodoConFiltroYDisponibilidadYAdmin(String filtro, String disponibilidad, Integer adminId, Pageable pageable);
	public Inmueble guardar(Inmueble inmueble);
	public Optional<Inmueble> obtenerPorId(int id);
	public DetalleInmuebleResponse obtenerDetalle(int id);
	public Inmueble actualizar(Inmueble inmueble);
	//public void eliminar(int id);
	public Page<Inmueble> listarConFiltrosAvanzados( String filtro,Double precioDesde, Double precioHasta,LocalDate fechaDesde,
		    LocalDate fechaHasta, String estado,Pageable pageable);
	public void actualizarUbicacion(Double lat, Double lng, int idInmueble);
}