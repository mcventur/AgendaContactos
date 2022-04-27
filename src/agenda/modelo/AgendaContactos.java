package agenda.modelo;

import java.util.*;

public class AgendaContactos {
	private Map<Character, Set<Contacto>> agenda;

	public AgendaContactos() {
		agenda = new TreeMap<>();
	}

	public void añadirContacto(Contacto c) {
		char inicial=Character.toUpperCase(c.getPrimeraLetra());

		if(agenda.containsKey(inicial)){
			agenda.get(inicial).add(c);
		}
		else{
			TreeSet<Contacto> tsContactos = new TreeSet<>();
			tsContactos.add(c);
			agenda.put(inicial,tsContactos);
		}
	}

	public Set<Contacto> contactosEnLetra(char letra) {
		return agenda.get(letra);
	}

	public int totalContactos() {
		int total=0;
		for (Character inicial : agenda.keySet()) {
			total += agenda.get(inicial).size();
		}
		return total;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n").append("AGENDA DE CONTACTOS").append("\n");
		for (Character inicial : agenda.keySet()) {
			Set<Contacto> setContactos=agenda.get(inicial);
			sb.append("\n" + inicial).append(" (" + setContactos.size() + " contacto/s)");
			sb.append("\n--------------\n");
			for (Contacto c : setContactos) {
				sb.append(c.toString()).append("\n");
			}
		}
		sb.append("\n("+ totalContactos() +" contacto/s)\n");
		return sb.toString();
	}

	public List<Contacto> buscarContactos(String texto) {
		List<Contacto> retorno= new ArrayList<>();
		texto=texto.toUpperCase();
		for (Character inicial : agenda.keySet()) {
			Set<Contacto> setContactos=agenda.get(inicial);
			for (Contacto c : setContactos) {
				if(c.getNombre().toUpperCase().contains(texto)
				|| c.getApellidos().toUpperCase().contains(texto)){
					retorno.add(c);
				}
			}
		}
		return retorno;
	}

	public List<Personal> personalesEnLetra(char letra) {
		//Paso la letra a máyuscula
		letra = charMayus(letra);

		if(agenda.containsKey(letra)){
			List<Personal> retorno=new ArrayList<>();
			Set<Contacto> setContactos=agenda.get(letra);
			for (Contacto c : setContactos) {
				if(c instanceof Personal){
					retorno.add((Personal)c);
				}
			}
			return retorno;
		}
		else return null;
	}

	private char charMayus(char letra) {
		String strLetra= String.valueOf(letra).toUpperCase();
		letra = strLetra.charAt(0);
		return letra;
	}

	public List<Personal> felicitar() {
		List<Personal> retorno = new ArrayList<>();
		for (Character inicial : agenda.keySet()) {
			Set<Contacto> setContactos=agenda.get(inicial);
			for (Contacto c : setContactos) {
				if(c instanceof Personal){
					Personal p=(Personal) c;
					if(p.esCumpleaños()){
						retorno.add(p);
					}
				}
			}
		}
		return retorno;
	}

	/**
	 * Devuelve un nuevo map en el que aparecen solo contactos
	 * personales pero organizados de forma que la clave en el nuevo
	 * map es la relación (un enumerado) y el valor asociado una
	 * colección List de cadenas con los apellidos y nombre de todos los
	 * contactos personales que hay en la agenda. Las claves se
	 * recuperan en el orden natural del enumerado.
	 */
	public Map<Relacion,List<String>> personalesPorRelacion() {
		Map<Relacion,List<String>> retorno=new TreeMap<>();
		for (Character inicial : agenda.keySet()) {
			//Podemos recorrer sólo los contactos personales usando una de las funciones anteriores
			List<Personal> listaPersonal=personalesEnLetra(inicial);
			for (Personal p : listaPersonal) {
				if(retorno.containsKey(p.getRelacion())){
					retorno.get(p.getRelacion()).add(p.getNombreCompleto());
				}
				else{
					List<String> listPersonaRelacion=new ArrayList<>();
					listPersonaRelacion.add(p.getNombreCompleto());
					retorno.put(p.getRelacion(),listPersonaRelacion);
				}
			}
		}
		return retorno;
	}

	public List<Personal> personalesOrdenadosPorFechaNacimiento(char letra) {
		letra = charMayus(letra);
		List<Personal> retorno=new ArrayList<>();
		if(agenda.containsKey(letra)){
			//Recuperamos la lista
			retorno=personalesEnLetra(letra);
			//La ordenamos usando una clase anónima que implementa comparator
			Collections.sort(retorno, new Comparator<Personal>() {
				@Override
				public int compare(Personal o1, Personal o2) {
					return o1.getFechaNac().compareTo(o2.getFechaNac());
				}
			});
		}
		return retorno;
	}

}
