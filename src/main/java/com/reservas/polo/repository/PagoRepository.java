package com.reservas.polo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.reservas.polo.model.Pago;
import com.reservas.polo.model.Reserva;

public interface PagoRepository extends JpaRepository<Pago, Integer> {
	Pago findByReserva(Reserva reserva);

}
