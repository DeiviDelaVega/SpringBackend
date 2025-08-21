package com.reservas.polo.controller;

import com.reservas.polo.service.ClienteService;
import com.reservas.polo.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/reportes")
public class AdminReportesController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClienteService clienteService;

    /**
     * TOP de inmuebles más reservados.
     * GET /api/admin/reportes/inmuebles-mas-reservados
     * Devuelve: [{ "inmueble": "Depto A", "total": 12 }, ...]
     */
    @GetMapping("/inmuebles-mas-reservados")
    public ResponseEntity<List<Map<String, Object>>> verInmueblesMasReservados() {
        List<Object[]> datos = reservaService.obtenerInmueblesMasReservados();
        List<Map<String, Object>> resp = new ArrayList<>();

        for (Object[] fila : datos) {
            String nombre = (fila[0] != null) ? fila[0].toString() : null;
            Number total = (fila[1] instanceof Number) ? (Number) fila[1] : null;

            Map<String, Object> item = new HashMap<>();
            item.put("inmueble", nombre);
            item.put("total", total);
            resp.add(item);
        }

        return ResponseEntity.ok(resp);
    }

    /**
     * TOP de clientes con más reservas.
     * GET /api/admin/reportes/clientes-mas-reservas
     * Devuelve: [{ "idCliente": 5, "nombre": "Juan", "totalReservas": 8 }, ...]
     */
    @GetMapping("/clientes-mas-reservas")
    public ResponseEntity<List<Map<String, Object>>> verClientesMasReservas() {
        List<Object[]> datos = clienteService.obtenerClientesMasReservas();
        List<Map<String, Object>> resp = new ArrayList<>();

        for (Object[] fila : datos) {
            // Query actual: (id, nombre, total)
            Number id = (fila[0] instanceof Number) ? (Number) fila[0] : null;
            String nombre = (fila[1] != null) ? fila[1].toString() : null;
            Number total = (fila[2] instanceof Number) ? (Number) fila[2] : null;

            Map<String, Object> item = new HashMap<>();
            item.put("idCliente", id);
            item.put("nombre", nombre);
            item.put("totalReservas", total);
            resp.add(item);
        }

        return ResponseEntity.ok(resp);
    }
}
