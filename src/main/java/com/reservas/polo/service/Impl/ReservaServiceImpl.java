package com.reservas.polo.service.Impl;

import com.reservas.polo.model.Reserva;
import com.reservas.polo.repository.ReservaRepository;
import com.reservas.polo.service.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public Page<Reserva> listarTodas(Pageable pageable) {
        return reservaRepository.findAll(pageable);
    }

    @Override
    public Page<Reserva> filtrarPorEstado(String estado, Pageable pageable) {
        return reservaRepository.findByEstadoReserva(estado, pageable);
    }

    @Override
    public Page<Reserva> filtrarPorFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        return reservaRepository.findByFechaInicioBetween(fechaInicio, fechaFin, pageable);
    }

    @Override
    public Optional<Reserva> obtenerPorId(Long id) {
        return reservaRepository.findById(id);
    }

 

    @Override
    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }


	@Override
	public List<LocalDate> obtenerFechasOcupadas(Long id) {
		 List<Reserva> reservas = reservaRepository.findByInmuebleId(id);
		 List<LocalDate> fechasOcupadas = new ArrayList<>();

		    for (Reserva reserva : reservas) {
		        LocalDate inicio = reserva.getFechaInicio();
		        LocalDate fin = reserva.getFechaFin();
		        while (!inicio.isAfter(fin)) {
		            fechasOcupadas.add(inicio);
		            inicio = inicio.plusDays(1);
		        }
		    }

		    return fechasOcupadas;
	}

	@Override
	public Reserva guardar(Reserva reserva) {
		return reservaRepository.save(reserva);
	}


	@Override
	public Page<Reserva> listarReservasPorCliente(String correo, Pageable pageable) {
	    return reservaRepository.findByCorreoCliente(correo, pageable);
	}
	


	@Override
	public void actualizarEstado(Long id, String nuevoEstado) {
	    Optional<Reserva> opt = reservaRepository.findById(id);
	    if (opt.isPresent()) {
	        Reserva reserva = opt.get();
	        reserva.setEstadoReserva(nuevoEstado);
	        reservaRepository.save(reserva);
	    }
	}


	
	@Override
	public List<Object[]> obtenerInmueblesMasReservados() {
	    return reservaRepository.contarReservasPorInmueble();
	}
}