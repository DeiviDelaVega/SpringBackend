package com.reservas.polo.service;

import com.reservas.polo.model.Pago;
import com.reservas.polo.model.Reserva;

import java.util.List;

public interface PagoService {
    void guardar(Pago pago);
    List<Pago> listarTodos();
    Pago buscarPorId(Integer id);
    void eliminar(Pago pago);
    Pago buscarPorReserva(Reserva reserva);

}
