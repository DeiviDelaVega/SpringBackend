package com.reservas.polo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reservas.polo.model.Administrador;
import com.reservas.polo.model.Inmueble;
// import com.reservas.polo.repository.ReservaRepository;
import com.reservas.polo.service.AdminService;
import com.reservas.polo.service.CloudinaryService;
import com.reservas.polo.service.InmuebleService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
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
	
	/*
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, 
						@RequestParam(name = "filtro", required = false) String filtro, 
						@RequestParam(name = "disponibilidad", required = false) String disponibilidad,
						@RequestParam(name = "adminId", required = false) Integer adminId,
						Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 5);	// Cuantas filas con los datos de Inmueble van haber
		Page<Inmueble> inmuebles = inmuService.listarTodoConFiltroYDisponibilidadYAdmin(filtro, disponibilidad, adminId, pageRequest);
		PageRender<Inmueble> pageRender = new PageRender<>("/admin/inmueble/inmuebles", inmuebles);
		List<Administrador> administradores = admiService.listarTodo();
		
		boolean hayFiltros = 
		        (filtro != null && !filtro.trim().isEmpty()) ||
		        (disponibilidad != null && !disponibilidad.trim().isEmpty()) ||
		        adminId != null;
		
		modelo.addAttribute("titulo","Lista de Inmuebles");
		modelo.addAttribute("inmuebles", inmuebles);
		modelo.addAttribute("lista", inmuebles.getContent());
		modelo.addAttribute("page", pageRender);
		modelo.addAttribute("filtro", filtro);
		modelo.addAttribute("disponibilidad", disponibilidad); 
		modelo.addAttribute("adminId", adminId);
	    modelo.addAttribute("administradores", administradores);
	    modelo.addAttribute("hayFiltros", hayFiltros);
	    
		return "admin/inmueble/MantInmueble";
	}*/

	/*
	@GetMapping("/inmuebles/nuevo")
	public String mostrarFormularioDeRegistro(Model modelo) {
		Inmueble inmueble = new Inmueble();
		inmueble.setAdministrador(new Administrador());
		modelo.addAttribute("inmueble", inmueble);
		return "admin/inmueble/CrearInmueble";
	}

	@PostMapping("/inmuebles")
	public String guardar(@Valid @ModelAttribute("inmueble") Inmueble inmueble,
	                      BindingResult result,
	                      @RequestParam("file") MultipartFile file,
	                      RedirectAttributes redirectAttributes,
	                      Model model) {
		
	    boolean tieneErrores = result.getFieldErrors().stream()
	        .anyMatch(error -> !error.getField().equals("imagenHabitacion"));

	    if (inmueble.getId() == 0 && (file == null || file.isEmpty())) {
	        model.addAttribute("error", "Debe seleccionar una imagen");
	        return "admin/inmueble/CrearInmueble";
	    }
	    
	    if (tieneErrores) {
	        return "admin/inmueble/CrearInmueble";
	    }
	    
	    if (inmueble.getLatitud() == null || inmueble.getLongitud() == null) {
	        model.addAttribute("error", "Debe seleccionar una ubicación en el mapa");
	        return "admin/inmueble/CrearInmueble";
	    }
	    
	    if (inmueble.getId() == 0) { 
	        if (file == null || file.isEmpty()) {
	            model.addAttribute("error", "Debe seleccionar una imagen");
	            return "admin/inmueble/CrearInmueble";
	        }

	        String contentType = file.getContentType();
	        if (contentType == null || !contentType.startsWith("image/")) {
	            model.addAttribute("error", "Solo se permiten archivos de imagen (JPG, PNG, etc)");
	            return "admin/inmueble/CrearInmueble";
	        }

	        String url = clouService.SubirImagen(file);
	        inmueble.setImagenHabitacion(url);
	    }

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String correo = auth.getName();
	    Administrador admin = admiService.findByCorreo(correo);
	    inmueble.setAdministrador(admin);

	    inmuService.guardar(inmueble);
	    redirectAttributes.addFlashAttribute("agregado", true);
	    return "redirect:/admin/inmueble/inmuebles";
	}

	@GetMapping("/inmuebles/detalle/{id}")
	public String verDetalleInmueble(@PathVariable int id, Model modelo) {
		modelo.addAttribute("inmueble", inmuService.obtenerPorId(id));
		return "admin/inmueble/DetalleInmueble";
	}

	@GetMapping("/inmuebles/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable int id, Model modelo) {
		modelo.addAttribute("inmueble", inmuService.obtenerPorId(id));
		return "admin/inmueble/EditarInmueble";
	}
	
	@PostMapping("/inmuebles/{id}")
	public String actualizar(@PathVariable int id,
	                         @ModelAttribute("inmueble") Inmueble inmueble,
	                         @RequestParam(value = "file", required = false) MultipartFile file,
	                         Model model,
	                         RedirectAttributes redirectAttributes) {
		try {
	        Inmueble inmuebleExistente = inmuService.obtenerPorId(id);
	        inmuebleExistente.setId(id);
	        inmuebleExistente.setNombre(inmueble.getNombre());
	        inmuebleExistente.setCapacidad(inmueble.getCapacidad());
	        inmuebleExistente.setNumeroHabitaciones(inmueble.getNumeroHabitaciones());
	        inmuebleExistente.setDescripcion(inmueble.getDescripcion());
	        inmuebleExistente.setServiciosIncluidos(inmueble.getServiciosIncluidos());
	        inmuebleExistente.setDisponibilidad(inmueble.getDisponibilidad());
	        inmuebleExistente.setPrecioPorNoche(inmueble.getPrecioPorNoche());

	        if (file != null && !file.isEmpty()) {
	            if (inmuebleExistente.getImagenHabitacion() != null && !inmuebleExistente.getImagenHabitacion().isEmpty()) {
	                clouService.eliminarImagenPorUrl(inmuebleExistente.getImagenHabitacion());
	            }
	            String url = clouService.SubirImagen(file);
	            inmuebleExistente.setImagenHabitacion(url);
	        }

	        inmuebleExistente.setLatitud(inmueble.getLatitud());
	        inmuebleExistente.setLongitud(inmueble.getLongitud());
	        inmuebleExistente.setAdministrador(inmueble.getAdministrador());

	        inmuService.actualizar(inmuebleExistente);
	        redirectAttributes.addFlashAttribute("actualizado", true);
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("actualizado", false);
	    }
	    return "redirect:/admin/inmueble/inmuebles";
	}
	*/
	
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
}