package com.reservas.polo.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservas.polo.dto.CatalogoResponse;
import com.reservas.polo.dto.ClienteCatalogoResponse;
import com.reservas.polo.model.Cliente;
import com.reservas.polo.model.Cliente.EstadoCliente;
import com.reservas.polo.model.Inmueble;
import com.reservas.polo.service.ClienteService;
import com.reservas.polo.service.InmuebleService;
import com.reservas.polo.service.ReservaService;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/cliente/catalogo")
public class CatalogoController {
	@Autowired
    private InmuebleService inmuebleService;

    @Autowired
    private ClienteService servicio;

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ClienteCatalogoResponse homeCliente(Principal principal) {
        String clienteEmail = principal.getName();
        Cliente cliente = servicio.findByCorreo(clienteEmail);

        ClienteCatalogoResponse response = new ClienteCatalogoResponse();
        if (cliente != null) {
            response.setNombreCliente(cliente.getNombre() + " " + cliente.getApellido());
            if (cliente.getEstado() == EstadoCliente.sancionado) {
                response.setModalSancion(true);
                response.setAlerta("Su cuenta ha sido sancionada por infringir las normas del sistema.");
            }
        } else {
            response.setNombreCliente("Desconocido");
            response.setModalSancion(false);
        }
        return response;
    }

    @GetMapping("/verInmueble")
    public CatalogoResponse verCatalogoInmuebles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "filtro", defaultValue = "") String filtro,
            @RequestParam(name = "precioDesde", required = false) Double precioDesde,
            @RequestParam(name = "precioHasta", required = false) Double precioHasta,
            @RequestParam(name = "fechaDesde", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(name = "fechaHasta", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(name = "estado", required = false) String estado,
            Principal principal) {

    	
    	String correo = principal.getName();
        Cliente cliente = servicio.findByCorreo(correo);

        //Devuelve lista aunq este vacia
        CatalogoResponse response = new CatalogoResponse();
        response.setInmuebles(new ArrayList<>());
        
        //Si cliente esta sancionado
        if (cliente.getEstado() == EstadoCliente.sancionado) {
            response.setAlerta("Su cuenta ha sido sancionada por infringir las normas. No puede visualizar inmuebles.");
            response.setModalSancion(true);
            response.setMotivo("Su cuenta ha sido sancionada por infringir las normas del servicio.");
            return response;
        }

        // Cliente activo + paginacion
        PageRequest pageable = PageRequest.of(page, 5);
        Page<Inmueble> pagina = inmuebleService.listarConFiltrosAvanzados(
                filtro, precioDesde, precioHasta, fechaDesde, fechaHasta, estado, pageable);

        System.out.println("Resultados obtenidos del servicio: " + pagina.getContent().size());
        response.setInmuebles(pagina.getContent() != null ? pagina.getContent() : new ArrayList<>());
        response.setTotalPaginas(pagina.getTotalPages());
        response.setPaginaActual(page);

        //Si no hay resultados, se dispara la alerta
        if (pagina.isEmpty()) {
            response.setAlerta("No se encontraron inmuebles con los filtros ingresados.");
        }

        return response;
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Inmueble> verDetalleInmueble(@PathVariable Integer id) {
        return inmuebleService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ocupadas/{idInmueble}")
    public ResponseEntity<List<String>> obtenerFechasOcupadas(@PathVariable Long idInmueble) {
        List<LocalDate> fechas = reservaService.obtenerFechasOcupadas(idInmueble);
        List<String> iso = new ArrayList<>();
        if (fechas != null) {
            for (LocalDate f : fechas) iso.add(DateTimeFormatter.ISO_LOCAL_DATE.format(f));
        }
        return ResponseEntity.ok(iso);
    }

    @GetMapping("/terminos")
    public ResponseEntity<String> mostrarTerminosCondiciones() {
        return ResponseEntity.ok("Aquí iría el texto de los términos y condiciones");
    }

    @GetMapping("/MotivoSancion")
    public ResponseEntity<String> verMotivoSancion() {
        return ResponseEntity.ok("Motivo de sanción: incumplimiento de normas del servicio.");
    }
}
