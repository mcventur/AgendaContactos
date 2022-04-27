package agenda.modelo;

import java.util.Random;

public class Profesional extends Contacto{
	String empresa;

	/**
	 * Constructor de la clase Profesional
	 * El nombre de la empresa se guarda siempre capitalizado,
	 */
	public Profesional(String nombre, String apellidos, String telefono, String email, String empresa) {
		super(nombre, apellidos, telefono, email);
		this.empresa = capitaliza(empresa); //capitaliza está declarado en la clase Contacto, como método estático
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	@Override
	public String toString() {
		return super.toString() + "\n" +
				"empresa: " + empresa + "\n";
	}

	/**
	 * Función añadida para capitalizar el nombre de la empresa
	 */
	private String capitaliza(String dato){
		String[] palabras = dato.split(" ");
		String retorno="";
		for (String p : palabras) {
			if(!p.isEmpty()){
				retorno += p.toUpperCase().charAt(0) + p.toLowerCase().substring(1);
				retorno+=" ";
			}
		}
		return retorno.trim();
	}

	@Override
	public String getFirmaEmail() {
		String[] firmasPosibles = {"Atentamente", "Saludos", "Saludos cordiales" , "Mis mejores deseos"};
		int posAl= new Random().nextInt(firmasPosibles.length);
		return firmasPosibles[posAl];
	}
}
