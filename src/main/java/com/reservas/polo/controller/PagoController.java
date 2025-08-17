package com.reservas.polo.controller;

import com.reservas.polo.dto.CheckoutRequest;
import com.reservas.polo.dto.CheckoutResponse;
import com.reservas.polo.model.Cliente;
import com.reservas.polo.model.Inmueble;
import com.reservas.polo.model.Pago;
import com.reservas.polo.model.Reserva;
import com.reservas.polo.service.ClienteService;
import com.reservas.polo.service.InmuebleService;
import com.reservas.polo.service.PagoService;
import com.reservas.polo.service.ReservaService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/pago")
public class PagoController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    // URL base del FRONT (para success/cancel de Stripe)
    // ej: http://localhost:4200  |  https://tusitio.com
    @Value("${app.frontend.base-url}")
    private String frontBaseUrl;

    private final ClienteService clienteService;
    private final InmuebleService inmuebleService;
    private final ReservaService reservaService;
    private final PagoService pagoService;

    public PagoController(ClienteService clienteService,
                              InmuebleService inmuebleService,
                              ReservaService reservaService,
                              PagoService pagoService) {
        this.clienteService = clienteService;
        this.inmuebleService = inmuebleService;
        this.reservaService = reservaService;
        this.pagoService = pagoService;
    }

    @PostConstruct
    public void init() { Stripe.apiKey = stripeSecretKey; }

    /**
     * 1) Angular manda fechas/total/id → creamos Session de Stripe y devolvemos la URL
     */
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> crearCheckout(@RequestBody CheckoutRequest req,
                                                          HttpSession session) throws StripeException {
        if (req == null || req.inmuebleId() == null || req.total() == null) {
            return ResponseEntity.badRequest().build();
        }

        long amount = req.total().multiply(new BigDecimal("100")).longValue(); // total en centavos

        // Guardar en HttpSession para usarlo en /finalizar
        session.setAttribute("fechaInicio", req.fechaInicio());
        session.setAttribute("fechaFin", req.fechaFin());
        session.setAttribute("total", req.total().toPlainString());
        session.setAttribute("inmuebleId", req.inmuebleId());

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontBaseUrl + "/cliente/pago-exitoso?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontBaseUrl + "/cliente/pago-error")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("pen")
                                .setUnitAmount(amount)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Reserva de Inmueble")
                                        .build())
                                .build())
                        .build())
                .build();

        Session stripeSession = Session.create(params);
        session.setAttribute("stripePaymentId", stripeSession.getPaymentIntent());

        return ResponseEntity.ok(new CheckoutResponse(stripeSession.getUrl()));
    }

    /**
     * 2) Stripe redirige al FRONT con ?session_id=...  → Angular llama a este endpoint
     *    Aquí verificamos con Stripe y creamos Reserva + Pago.
     */
    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarReserva(@RequestParam("session_id") String sessionId,
                                              Principal principal,
                                              HttpSession httpSession) {
        try {
            if (principal == null || principal.getName() == null) {
                return ResponseEntity.status(401).body("Debes iniciar sesión.");
            }

            // Cliente autenticado
            Cliente cliente = clienteService.findByCorreo(principal.getName());
            if (cliente == null) return ResponseEntity.badRequest().body("Cliente no encontrado.");

            // Datos temporales guardados antes del checkout
            String fechaInicioStr = (String) httpSession.getAttribute("fechaInicio");
            String fechaFinStr    = (String) httpSession.getAttribute("fechaFin");
            String totalStr       = (String) httpSession.getAttribute("total");
            Integer inmuebleId    = (Integer) httpSession.getAttribute("inmuebleId");

            if (fechaInicioStr == null || fechaFinStr == null || totalStr == null || inmuebleId == null) {
                return ResponseEntity.badRequest().body("Sesión expirada. Intenta nuevamente.");
            }

            // Verificar pago con Stripe
            Session stripeSession = Session.retrieve(sessionId);
            if (!"paid".equals(stripeSession.getPaymentStatus())) {
                return ResponseEntity.badRequest().body("Pago no confirmado por Stripe.");
            }
            String paymentIntentId = stripeSession.getPaymentIntent();

            // Inmueble
            Inmueble inmueble = inmuebleService.obtenerPorId(inmuebleId)
                    .orElse(null);
            if (inmueble == null) return ResponseEntity.badRequest().body("Inmueble no encontrado.");

            // Crear Reserva
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(LocalDate.parse(fechaInicioStr));
            reserva.setFechaFin(LocalDate.parse(fechaFinStr));
            reserva.setMontoTotal(new BigDecimal(totalStr));
            reserva.setMetodoPago("Tarjeta");
            reserva.setEstadoReserva("Solicitado");
            reserva.setCliente(cliente);
            reserva.setInmueble(inmueble);
            reservaService.guardar(reserva);

            // Crear Pago
            Pago pago = new Pago();
            pago.setReserva(reserva);
            pago.setFechaPago(java.time.LocalDateTime.now());
            pago.setMonto(new BigDecimal(totalStr));
            pago.setStripePaymentId(paymentIntentId);
            pagoService.guardar(pago);

            // Limpiar datos de sesión
            httpSession.removeAttribute("fechaInicio");
            httpSession.removeAttribute("fechaFin");
            httpSession.removeAttribute("total");
            httpSession.removeAttribute("inmuebleId");
            httpSession.removeAttribute("stripePaymentId");

            return ResponseEntity.ok("OK");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("Error inesperado");
        }
    }
}
