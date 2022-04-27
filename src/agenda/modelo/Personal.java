package agenda.modelo;

import utilidades.fecha.Utilidades;

import java.time.LocalDate;

public class Personal extends Contacto{
	public static final String FIRMA = "Un abrazo!";
	private LocalDate fechaNac;
	private Relacion relacion;

	public Personal(String nombre, String apellidos, String telefono, String email, String fechaNac, Relacion relacion) {
		super(nombre, apellidos, telefono, email);
		this.fechaNac= Utilidades.parsear(fechaNac);
		this.relacion = relacion;
	}

	public LocalDate getFechaNac() {
		return fechaNac;
	}

	/**
	 * Dos setters para la fechaNac, sobrecargados:
	 * uno que recoge un LocalDate, y otro un String.
	 * No sé si le sacaré partido a ambos más adelante
	 */
	public void setFechaNac(LocalDate fechaNac) {
		this.fechaNac = fechaNac;
	}
	public void setFechaNac(String fechaNac) {
		this.fechaNac = Utilidades.parsear(fechaNac);
	}

	public Relacion getRelacion() {
		return relacion;
	}

	public void setRelacion(Relacion relacion) {
		this.relacion = relacion;
	}

	@Override
	public String toString() {
		return super.toString() + "\n" +
				"fecha nacimiento: " + Utilidades.formatear(fechaNac)
				+ "\n" + "relación: " + relacion + "\n";
	}

	public boolean esCumpleaños(){
		LocalDate hoy = LocalDate.now();
		return hoy.getMonthValue()==fechaNac.getMonthValue() &&
				hoy.getDayOfMonth()==fechaNac.getDayOfMonth();
	}

	@Override
	public String getFirmaEmail() {
		return FIRMA;
	}

}
