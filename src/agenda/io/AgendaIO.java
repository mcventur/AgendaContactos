package agenda.io;

import agenda.modelo.*;

import java.io.*;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * Utilidades para cargar la agenda
 */
public class AgendaIO {

	public static int importar(AgendaContactos agenda, String nomFichero) throws IOException{
		int errores=0;
		Contacto c = null;
		InputStream input = AgendaIO.class.getClassLoader()
				.getResourceAsStream(nomFichero);
		try(BufferedReader entrada = new BufferedReader(new InputStreamReader(input))){
			String linea = entrada.readLine();
			while(linea!=null){
				try {
					c = parsearLinea(linea);
					agenda.añadirContacto(c);
				}
				catch(RuntimeException|ContactoExcepcion e){
					errores++;
					System.out.println(e.getMessage());
				}
				linea = entrada.readLine();
			}
		}
		return errores;
	}

	private static Contacto parsearLinea(String linea) throws DateTimeParseException, IllegalArgumentException, ContactoExcepcion {
		String[] trozos=linea.split(",");
		//Quito espacios sobrantes de todos los trozos
		for (int i = 0; i < trozos.length; i++) {
			trozos[i] = trozos[i].trim();
		}
		if(trozos[0].equals("1")){
			return new Profesional(trozos[1],trozos[2],trozos[3],trozos[4],trozos[5]);
		}
		else{
			Relacion rel= Relacion.valueOf(trozos[6].toUpperCase());
			return new Personal(trozos[1],trozos[2],trozos[3],trozos[4],trozos[5],rel);
		}

	}

	/**
	 * Exporta los contactos personales de la agenda, agrupados por relación, en
	 * el fichero de texto indicado como parámetro
	 *
	 * @param agenda la agenda que contiene los contactos personales a exportar
	 * @param nomFichero el nombre del fichero donde se realizará la exportación
	 */
	public static void exportarPersonales(AgendaContactos agenda, String nomFichero) throws IOException {
		Map<Relacion, List<String>> map = agenda.personalesPorRelacion();
		try(PrintWriter salida = new PrintWriter(new BufferedWriter(new FileWriter(nomFichero)))){
			map.forEach((key, value) -> salida.println(key + "\n\t" + value));
			System.out.println("Exportados personales agrupados por relación");
		}
	}

}
