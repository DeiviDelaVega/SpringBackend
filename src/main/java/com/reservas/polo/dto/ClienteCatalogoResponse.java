package com.reservas.polo.dto;

public class ClienteCatalogoResponse {
	private String nombreCliente;
    private boolean modalSancion;
    private String alerta;
    
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public boolean isModalSancion() {
		return modalSancion;
	}
	public void setModalSancion(boolean modalSancion) {
		this.modalSancion = modalSancion;
	}
	public String getAlerta() {
		return alerta;
	}
	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}
    
    
}
