package com.reservas.polo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import com.reservas.polo.dto.CreateInmuebleRequest;
import com.reservas.polo.dto.DetalleInmuebleResponse;
import com.reservas.polo.dto.EditarInmuebleRequest;
import com.reservas.polo.model.Administrador;
import com.reservas.polo.model.Inmueble;
// import com.reservas.polo.repository.ReservaRepository;
import com.reservas.polo.service.AdminService;
import com.reservas.polo.service.CloudinaryService;
import com.reservas.polo.service.InmuebleService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/api/admin/inmuebles")
public class InmuebleController {
	@Autowired
	private AdminService admiService;

	@Autowired
	private InmuebleService inmuService;

	@Autowired
	private CloudinaryService clouService;
	
	//@Autowired
	//private ReservaRepository reservaRepository;
	
	// LISTADO
	@GetMapping
    public ResponseEntity<Map<String, Object>> listar(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "filtro", required = false) String filtro,
        @RequestParam(name = "disponibilidad", required = false) String disponibilidad,
        @RequestParam(name = "adminId", required = false) Integer adminId) {

        Pageable pageRequest = PageRequest.of(page, 5); // Cuantas filas con los datos de Inmueble van haber
        Page<Inmueble> inmuebles = inmuService.listarTodoConFiltroYDisponibilidadYAdmin(filtro, disponibilidad, adminId, pageRequest);
        List<Administrador> administradores = admiService.listarTodo();

        Map<String, Object> response = new HashMap<>();
        response.put("inmuebles", inmuebles.getContent());
        response.put("currentPage", inmuebles.getNumber());
        response.put("totalItems", inmuebles.getTotalElements());
        response.put("totalPages", inmuebles.getTotalPages());
        response.put("administradores", administradores);
        response.put("filtro", filtro);
        response.put("disponibilidad", disponibilidad);
        response.put("adminId", adminId);

        return ResponseEntity.ok(response);
    }
	
	// CREAR
	@PostMapping(value = "/guardar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> guardar(
            @ModelAttribute @Valid CreateInmuebleRequest req,
            BindingResult br,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        // Validación del DTO
        if (br.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(br.getAllErrors());
        }

        // Obtener admin autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();
        Administrador admin = admiService.findByCorreo(correo);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se encontró el administrador autenticado");
        }

        boolean esNuevo = req.id() == 0;

        // Reglas de imagen
        if (esNuevo) {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe seleccionar una imagen");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permiten archivos de imagen (JPG, PNG, etc.)");
            }
        }

        // Subir imagen si llega
        String urlImagen = null;
        if (file != null && !file.isEmpty()) {
            try {
                urlImagen = clouService.SubirImagen(file);
            } catch (Exception ex) {
                ex.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error subiendo la imagen: " + ex.getMessage());
            }
        }

        // Construir entidad
        Inmueble entidad = new Inmueble();
        entidad.setId(req.id());
        entidad.setNombre(req.nombre());
        entidad.setCapacidad(req.capacidad());
        entidad.setNumeroHabitaciones(req.numeroHabitaciones());
        entidad.setDescripcion(req.descripcion());
        entidad.setServiciosIncluidos(req.serviciosIncluidos());
        entidad.setDisponibilidad(req.disponibilidad());
        entidad.setPrecioPorNoche(req.precioPorNoche());
        entidad.setLatitud(req.latitud());
        entidad.setLongitud(req.longitud());
        entidad.setAdministrador(admin);

        if (urlImagen != null) {
            entidad.setImagenHabitacion(urlImagen);
        } else if (!esNuevo) {
            try {
                Optional<Inmueble> existenteOpt = inmuService.obtenerPorId(req.id()); 
                existenteOpt.ifPresent(existente -> entidad.setImagenHabitacion(existente.getImagenHabitacion()));
            } catch (Exception ignore) {
            }
        }

        // Guardar
        try {
            inmuService.guardar(entidad);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error guardando inmueble: " + ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }
	
	// DETALLE
	@GetMapping("/detalle/{id}")
	public ResponseEntity<?> obtenerDetalle(@PathVariable int id) {
	    DetalleInmuebleResponse detalle = inmuService.obtenerDetalle(id);
	    if (detalle == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inmueble no encontrado");
	    }
	    return ResponseEntity.ok(detalle);
	}

	// EDITAR
	@PutMapping(value = "/editar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> actualizarInmueble(
            @PathVariable int id,
            @RequestPart("dto") @Valid EditarInmuebleRequest dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
		
		// Obtener inmueble
        Inmueble inmueble = inmuService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Inmueble no encontrado"));
        
        // Obtener administrador autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();
        Administrador admin = admiService.findByCorreo(correo);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No se encontró el administrador autenticado");
        }

        // Subir imagen si se envía
        if (file != null && !file.isEmpty()) {
            if (inmueble.getImagenHabitacion() != null) {
                clouService.eliminarImagenPorUrl(inmueble.getImagenHabitacion());
            }
            String url = clouService.SubirImagen(file);
            inmueble.setImagenHabitacion(url);
        }

        // Actualizar campos desde el DTO
        inmueble.setNombre(dto.nombre());
        inmueble.setCapacidad(dto.capacidad());
        inmueble.setNumeroHabitaciones(dto.numeroHabitaciones());
        inmueble.setDescripcion(dto.descripcion());
        inmueble.setServiciosIncluidos(dto.serviciosIncluidos());
        inmueble.setDisponibilidad(dto.disponibilidad());
        inmueble.setPrecioPorNoche(dto.precioPorNoche());
        inmueble.setLatitud(dto.latitud());
        inmueble.setLongitud(dto.longitud());
        inmueble.setAdministrador(admin);

        inmuService.actualizar(inmueble);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Inmueble actualizado correctamente");
        return ResponseEntity.ok(response);
    }
	
	/*
	@GetMapping("/inmuebles/{id}")
	public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
	    Inmueble inmueble = inmuService.obtenerPorId(id);
	    if (inmueble != null) {
	    	if (reservaRepository.existsByInmuebleId(id)) {
	            redirectAttributes.addFlashAttribute("errorInmueble", "No se puede eliminar el inmueble porque tiene reservas asociadas");
	            return "redirect:/admin/inmueble/inmuebles";
	        }
	        if (inmueble.getImagenHabitacion() != null && !inmueble.getImagenHabitacion().isEmpty()) {
	            clouService.eliminarImagenPorUrl(inmueble.getImagenHabitacion());
	        }
	        inmuService.eliminar(id);	        
	        redirectAttributes.addFlashAttribute("eliminado", true);
	    }
	    return "redirect:/admin/inmueble/inmuebles";
	}*/
	// Se necesita de reserva para avanzar esta parte
}