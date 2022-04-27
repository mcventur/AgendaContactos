package agenda.modelo;

public abstract class Contacto implements Comparable<Contacto>{
	private String nombre;
	private String apellidos;
	private String telefono;
	private String email;

	public Contacto(String nombre, String apellidos, String telefono,
			String email) {
		this.nombre = nombre.toUpperCase();
		this.apellidos = apellidos.toUpperCase();
		this.telefono = telefono;
		this.email = email.toLowerCase();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * De un contacto se quiere saber cuál es la primera letra de su apellido
	 * @return un char con la primera letra del apellido
	 */
	public char getPrimeraLetra(){
		return apellidos.charAt(0);
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}

	/**
	 * Dos contactos son iguales si además de pertenecer a la misma clase tienen el mismo apellido, nombre y
	 * email.
	 */
	@Override
	public boolean equals(Object o) {
		//Comprobaciones estándar
		if(o==null) return false; //o es null => no iguales
		if(this==o) return true; // o y this son el mismo objeto => iguales (idénticos)
		if(this.getClass()!=o.getClass()) return false; //Son de tipos distintos => NO iguales

		//Comprobaciones adicionales
		Contacto contacto = (Contacto) o;
		return this.nombre.equals(contacto.nombre) &&
				this.apellidos.equals(contacto.getApellidos()) &&
				this.email.equals(contacto.getEmail());
	}

	/**
	 * Dos contactos se pueden comparar por apellido (ascendente). Si coincide el apellido entonces se considera el
	 * nombre. Este será su criterio natural de ordenación.
	 * @return 	entero negativo si this es anterior a o.
	 * 			0 si son equivalentes en orden.
	 * 			entero positivo si this es posterior a o.
	 */
	@Override
	public int compareTo(Contacto o) {
		int compApellidos = this.apellidos.compareTo(o.getApellidos());
		if(compApellidos == 0){
			return this.nombre.compareTo(o.getNombre());
		}
		else{ //se podría quitar el else. Pero lo dejo para que se vea explícitamente el flujo
			return compApellidos;
		}
	}

	/**
	 * Todos los contactos tienen una firma de email que generan cuando envían un mensaje. El texto concreto de
	 * esta firma depende del tipo de contacto.
	 * @return la firma del contacto en String
	 */
	public abstract String getFirmaEmail();

	/**
	 *
	 * @return la representación textual del Contacto
	 */
	@Override
	public String toString() {
		return apellidos + ", " + nombre +
				" (" + getClass().getSimpleName().toUpperCase() + ")" + "\n"
				+ "Tfno: " + telefono + " | email: " + email;
	}
	/**
	 * Añadida al proyecto base
	 * Devuelve el nombre completo (apellidos y nombre) de un contacto.
	 */
	public String getNombreCompleto() {
		return getApellidos() + " " + getNombre();
	}
}
