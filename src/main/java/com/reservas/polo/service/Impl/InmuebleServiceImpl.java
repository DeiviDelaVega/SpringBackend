package com.reservas.polo.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reservas.polo.dto.CreateInmuebleRequest;
import com.reservas.polo.dto.DetalleInmuebleResponse;
import com.reservas.polo.model.Inmueble;
import com.reservas.polo.repository.InmuebleRepository;
// import com.reservas.polo.repository.ReservaRepository;
import com.reservas.polo.service.InmuebleService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class InmuebleServiceImpl implements InmuebleService {

	@Autowired
	private InmuebleRepository repositorio;

	//@Autowired
	//private ReservaRepository reservaRepository;
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Inmueble> listarTodo() {
		return repositorio.findAll();
	}
	
	@Override
	public Inmueble guardar(Inmueble inmueble){
	    return repositorio.save(inmueble);
	}

	@Override
	public Optional<Inmueble> obtenerPorId(int id) {
		return repositorio.findById(id);
	}

	@Override
	public Inmueble actualizar(Inmueble inmueble) {
		return repositorio.save(inmueble);
	}

	/*
	@Override
	public void eliminar(int id) {
		if(reservaRepository.existsByInmuebleId(id)){
			throw new IllegalStateException("No se puede eliminar un inmueble con reservas asociadas");
		}
		repositorio.deleteById(id);
	}*/

	@Override
	@Transactional(readOnly = true)
	public Page<Inmueble> listarTodoPaginacion(Pageable pageable) {
		return repositorio.findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Inmueble> listarTodoConFiltro(String filtro, Pageable pageable) {
		if (filtro != null && !filtro.trim().isEmpty()) {
	        return repositorio.filtrarPorDescripcionOServicio(filtro, pageable);
	    }
		return repositorio.findAll(pageable);
	}

	@Override
	public Page<Inmueble> listarTodoConFiltroYDisponibilidad(String filtro, String disponibilidad, Pageable pageable) {
		if ((filtro == null || filtro.trim().isEmpty()) && (disponibilidad == null || disponibilidad.isEmpty())) {
	        return repositorio.findAll(pageable);
	    } else if (filtro != null && !filtro.trim().isEmpty() && disponibilidad != null && !disponibilidad.isEmpty()) {
	        return repositorio.findByFiltroAndDisponibilidad(filtro, disponibilidad, pageable);
	    } else if (filtro != null && !filtro.trim().isEmpty()) {
	        return repositorio.filtrarPorDescripcionOServicio(filtro, pageable);
	    } else {
	        return repositorio.findByDisponibilidad(disponibilidad, pageable);
	    }
	}

	@Override
	public Page<Inmueble> listarTodoConFiltroYDisponibilidadYAdmin(String filtro, String disponibilidad, Integer adminId, Pageable pageable) {
		return repositorio.findByFiltroDisponibilidadYAdministrador(
		        (filtro == null || filtro.isEmpty()) ? null : filtro,
	                (disponibilidad == null || disponibilidad.isEmpty()) ? null : disponibilidad,
	                adminId,
	                pageable
		        );
	}
	
	@Override
	public Page<Inmueble> listarConFiltrosAvanzados(String filtro, Double precioDesde, Double precioHasta,
	                                                LocalDate fechaDesde, LocalDate fechaHasta,
	                                                String estado, Pageable pageable) {

	    Specification<Inmueble> spec = Specification.where(null);

	    if (filtro != null && !filtro.isBlank()) {
	        spec = spec.and((root, query, cb) ->
	            cb.or(
	                cb.like(cb.lower(root.get("nombre")), "%" + filtro.toLowerCase() + "%"),
	                cb.like(cb.lower(root.get("descripcion")), "%" + filtro.toLowerCase() + "%"),
	                cb.like(cb.lower(root.get("serviciosIncluidos")), "%" + filtro.toLowerCase() + "%")
	            )
	        );
	    }

	    if (precioDesde != null) {
	        spec = spec.and((root, query, cb) ->
	            cb.greaterThanOrEqualTo(root.get("precioPorNoche"), BigDecimal.valueOf(precioDesde)));
	    }

	    if (precioHasta != null) {
	        spec = spec.and((root, query, cb) ->
	            cb.lessThanOrEqualTo(root.get("precioPorNoche"), BigDecimal.valueOf(precioHasta)));
	    }

	    if (estado != null && !estado.isBlank()) {
	        spec = spec.and((root, query, cb) ->
	            cb.equal(cb.lower(root.get("disponibilidad")), estado.toLowerCase()));
	    }

	    return repositorio.findAll(spec, pageable);
	}

	@Override
	public void actualizarUbicacion(Double lat, Double lng, int idInmueble) {
		repositorio.actualizarUbicacion(lat, lng, idInmueble);
	}

	@Override
	public DetalleInmuebleResponse obtenerDetalle(int id) {
	    Inmueble inmueble = em.find(Inmueble.class, id);
	    if (inmueble == null) {
	        return null;
	    }
	    return new DetalleInmuebleResponse(
	        inmueble.getId(),
	        inmueble.getNombre(),
	        inmueble.getCapacidad(),
	        inmueble.getNumeroHabitaciones(),
	        inmueble.getDescripcion(),
	        inmueble.getServiciosIncluidos(),
	        inmueble.getDisponibilidad(),
	        inmueble.getPrecioPorNoche(),
	        inmueble.getImagenHabitacion(),
	        inmueble.getLatitud(),
	        inmueble.getLongitud()
	    );
	}
}