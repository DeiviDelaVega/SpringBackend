package com.reservas.polo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservas.polo.exception.ResourceNotFoundException;
import com.reservas.polo.model.Cliente;
import com.reservas.polo.service.ClienteService;

@RestController
@RequestMapping("/api/admin")
public class ClienteAdminController {

	@Autowired
	private ClienteService servicio;

	
	//Listar todos los Clientes
		@GetMapping("/clienteAdmin")
		public List<Cliente> listarTodosLosClientes(){
			return servicio.listarTodosLosClientes();
		}
		
		@PostMapping
		public ResponseEntity<Cliente> guardarCliente(@RequestBody Cliente cliente) {
		    Cliente nuevo = servicio.guardarCliente(cliente);
		    return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
		}

		
		//Detalles
		@GetMapping("/clienteAdmin/{id}")
	    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Integer id){
	        Cliente existente = servicio.obtenerClientePorId(id)
	                .orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con el ID : " + id));
	        return ResponseEntity.ok(existente);
	    }	
		
		//Guardar
	    @PutMapping("/clienteAdmin/actualizar/{id}")
	    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Integer id,@RequestBody Cliente cliente){
	        Cliente existente = servicio.obtenerClientePorId(id)
	                .orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con el ID : " + id));

	        existente.setNombre(cliente.getNombre());
	        existente.setApellido(cliente.getApellido());
	        existente.setNroDocumento(cliente.getNroDocumento());
	        existente.setDireccion(cliente.getDireccion());
	        existente.setNumeroTelf(cliente.getNumeroTelf());
	        existente.setCorreo(cliente.getCorreo());
	        existente.setEstado(cliente.getEstado());

	        Cliente actualizado = servicio.actualizarCliente(existente);
	        return ResponseEntity.ok(actualizado);
	    }
	    
		
	    //Eliminar
	    @DeleteMapping("/clienteAdmin/{id}")
		public ResponseEntity<Map<String,Boolean>> eliminarCliente(@PathVariable Integer id) {
	    	Cliente cliente = servicio.obtenerClientePorId(id)
	    			.orElseThrow(() -> new ResourceNotFoundException("No existe el cliente con el ID : " + id));
	    
	    	servicio.eliminarCliente(id);
	    	Map<String,Boolean> respuesta = new HashMap<>();
	    	respuesta.put("eliminar", Boolean.TRUE);
	    	return ResponseEntity.ok(respuesta);
	    }

	    //Paginacion y Filtros
	    
	    @GetMapping("/clienteAdmin/paginado")
	    public Page<Cliente> listarClientesPaginados(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "5") int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        return servicio.listarTodoPaginacion(pageable);
	    }

	    @GetMapping("/clienteAdmin/paginacionFiltro")
	    public Page<Cliente> listarClientesPaginadosConFiltro(
	            @RequestParam String filtro,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "5") int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        return servicio.listarTodoConFiltro(filtro, pageable);
	    }

	
}
