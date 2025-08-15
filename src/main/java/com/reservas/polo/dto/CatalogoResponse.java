package com.reservas.polo.dto;

import java.util.List;

import com.reservas.polo.model.Inmueble;

public class CatalogoResponse {
	private List<Inmueble> inmuebles;
    private int totalPaginas;
    private int paginaActual;
    private String alerta;
    private boolean modalSancion;
    private String motivo;
	public List<Inmueble> getInmuebles() {
		return inmuebles;
	}
	public void setInmuebles(List<Inmueble> inmuebles) {
		this.inmuebles = inmuebles;
	}
	public int getTotalPaginas() {
		return totalPaginas;
	}
	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}
	public int getPaginaActual() {
		return paginaActual;
	}
	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}
	public String getAlerta() {
		return alerta;
	}
	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}
	public boolean isModalSancion() {
		return modalSancion;
	}
	public void setModalSancion(boolean modalSancion) {
		this.modalSancion = modalSancion;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}
