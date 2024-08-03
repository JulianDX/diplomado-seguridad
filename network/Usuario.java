package network;

import java.io.Serializable;

public class Usuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private Double monto;
	
	public Usuario(String nombre, Double monto) {
		super();
		this.nombre = nombre;
		this.monto = monto;
	}
	
	@Override
	public String toString() {
		return "nombre=" + nombre + ", monto=" + monto;
	}




	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	
	
	
	

}
