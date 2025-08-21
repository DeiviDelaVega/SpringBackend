package com.reservas.polo.controller;

import com.reservas.polo.model.Cliente;
import com.reservas.polo.model.Pago;
import com.reservas.polo.model.Reserva;
import com.reservas.polo.service.ClienteService;
import com.reservas.polo.service.EmailService;
import com.reservas.polo.service.PagoService;
import com.reservas.polo.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cliente/misreservas")
public class MisReservasController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ClienteService clienteService;

    // Si no está configurado, queda en null y simplemente no se envía correo al admin
    @Value("${admin.email:#{null}}")
    private String correoAdmin;

    /**
     * Listado paginado de reservas del cliente autenticado + metadatos del cliente.
     * Ej: GET /api/cliente/misreservas?page=0
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarMisReservas(
            @RequestParam(defaultValue = "0") int page,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        String correoCliente = principal.getName();
        Cliente cliente = clienteService.findByCorreo(correoCliente);
        if (cliente == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Cliente no encontrado"));
        }

        boolean esActivo = cliente.getEstado() == Cliente.EstadoCliente.activo;
        boolean modalSancion = cliente.getEstado() == Cliente.EstadoCliente.sancionado;

        Pageable pageable = PageRequest.of(page, 5);
        Page<Reserva> reservas = reservaService.listarReservasPorCliente(correoCliente, pageable);

        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("hasPrevious", reservas.hasPrevious());
        pageInfo.put("hasNext", reservas.hasNext());
        pageInfo.put("paginaActual", reservas.getNumber() + 1);
        pageInfo.put("totalPaginas", reservas.getTotalPages());
        pageInfo.put("first", reservas.isFirst());
        pageInfo.put("last", reservas.isLast());
        pageInfo.put("totalElementos", reservas.getTotalElements());
        pageInfo.put("size", reservas.getSize());

        // Enviamos solo datos esenciales del cliente (evitamos campos innecesarios)
        Map<String, Object> clienteLite = Map.of(
                "id", cliente.getIdCliente(),
                "nombre", cliente.getNombre(),
                "apellido", cliente.getApellido(),
                "correo", cliente.getCorreo(),
                "estado", cliente.getEstado().name()
        );

        Map<String, Object> resp = new HashMap<>();
        resp.put("esActivo", esActivo);
        resp.put("modalSancion", modalSancion);
        resp.put("alerta", modalSancion ? "Su cuenta ha sido sancionada. Comuníquese con administración." : null);
        resp.put("nombreCliente", cliente.getNombre());
        resp.put("cliente", clienteLite);
        resp.put("reservas", reservas.getContent());
        resp.put("page", pageInfo);

        return ResponseEntity.ok(resp);
    }

    /**
     * Detalle de una reserva del cliente (verifica pertenencia).
     * GET /api/cliente/misreservas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> detalleReservaCliente(@PathVariable Long id, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }

        Optional<Reserva> reservaOpt = reservaService.obtenerPorId(id);
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.status(404).body("No encontrado");
        }

        Reserva reserva = reservaOpt.get();
        if (reserva.getCliente() == null ||
                !principal.getName().equalsIgnoreCase(reserva.getCliente().getCorreo())) {
            return ResponseEntity.status(403).body("No autorizado");
        }

        return ResponseEntity.ok(reserva);
    }

    /**
     * Solicitud de reembolso (elimina Pago y Reserva). Verifica pertenencia.
     * Por REST correcto: DELETE /api/cliente/misreservas/{id}/reembolso
     */
    @DeleteMapping("/{id}/reembolso")
    public ResponseEntity<String> reembolsoDelete(@PathVariable Long id, Principal principal) {
        return procesarReembolsoInterno(id, principal);
    }

    /**
     * Alias de compatibilidad con el flujo anterior (GET).
     * GET /api/cliente/misreservas/reembolso/{id}
     */
    @GetMapping("/reembolso/{id}")
    public ResponseEntity<String> reembolsoGetCompat(@PathVariable Long id, Principal principal) {
        return procesarReembolsoInterno(id, principal);
    }

    private ResponseEntity<String> procesarReembolsoInterno(Long id, Principal principal) {
        try {
            if (principal == null || principal.getName() == null) {
                return ResponseEntity.status(401).body("No autenticado");
            }

            Optional<Reserva> reservaOpt = reservaService.obtenerPorId(id);
            if (reservaOpt.isEmpty()) {
                return ResponseEntity.status(404).body("No encontrado");
            }

            Reserva reserva = reservaOpt.get();

            // Seguridad: la reserva debe pertenecer al cliente autenticado
            if (reserva.getCliente() == null ||
                    !principal.getName().equalsIgnoreCase(reserva.getCliente().getCorreo())) {
                return ResponseEntity.status(403).body("No autorizado");
            }

            Pago pago = pagoService.buscarPorReserva(reserva);
            if (pago != null) {
                pagoService.eliminar(pago);
            }

            reservaService.eliminar(id);

            // Enviar correo al admin si está configurado
            if (correoAdmin != null && !correoAdmin.isBlank() && pago != null) {
                try {
                    emailService.enviarReembolsoAdmin(correoAdmin, reserva, pago);
                } catch (MessagingException e) {
                    // No romper el flujo si falla el correo
                    System.err.println("Error al enviar correo de reembolso: " + e.getMessage());
                }
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error");
        }
    }
}
