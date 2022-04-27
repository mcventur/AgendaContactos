package agenda.test;

import agenda.io.AgendaIO;
import agenda.modelo.AgendaContactos;
import agenda.modelo.Contacto;
import agenda.modelo.Personal;
import agenda.modelo.Relacion;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TestAgenda {

	public static void main(String[] args) {
		AgendaContactos agenda = new AgendaContactos();
		int errores = AgendaIO.importar(agenda,"resources/agenda.csv");
		System.out.println(errores + " lineas erróneas");
		System.out.println(agenda);
		separador();

		buscarContactos(agenda, "acos");
		separador();

		buscarContactos(agenda, "don");
		separador();

		felicitar(agenda);
		separador();

		personalesOrdenadosPorFecha(agenda, 'm');
		separador();
		personalesOrdenadosPorFecha(agenda, 'e');
		separador();
		personalesOrdenadosPorFecha(agenda, 'w');
		separador();

		personalesPorRelacion(agenda);
		try {
			AgendaIO.exportarPersonales(agenda, "resources/personales-relacion.txt");
		}
		catch(IOException e){
			System.out.println("Error de escritura en fichero: " + e.getMessage());
		}
		separador();

	}

	private static void buscarContactos(AgendaContactos agenda, String texto) {
		List<Contacto> resultado = agenda.buscarContactos(texto);
		System.out.println("Contactos que contienen \"" + texto + "\"");
		if (resultado.isEmpty()) {
			System.out.println("No hay contactos coincidentes");
		} else {
			resultado.forEach(contacto -> System.out.println(contacto));
		}

	}

	private static void felicitar(AgendaContactos agenda) {
		System.out.println("Fecha actual: " + LocalDate.now());
		List<Personal> resultado = agenda.felicitar();
		if (resultado.isEmpty()) {
			System.out.println("Hoy no cumple nadie");
		} else {
			System.out.println("Hoy hay que felicitar a ");
			resultado.forEach(contacto -> System.out.println(contacto));
		}

	}

	private static void personalesOrdenadosPorFecha(AgendaContactos agenda,
			char letra) {
		System.out.println("Personales en letra " + letra
				+ " ordenados de < a > fecha de nacimiento");
		List<Personal> personales = agenda.personalesEnLetra(letra);
		if (personales == null) {
			System.out.println(letra + " no está en la agenda");
		} else {
			agenda.personalesOrdenadosPorFechaNacimiento(letra)
					.forEach(contacto -> System.out.println(contacto));
		}

	}

	private static void personalesPorRelacion(AgendaContactos agenda) {
		Map<Relacion, List<String>> map = agenda.personalesPorRelacion();
		map.forEach((key, value) -> System.out.println(key + "\n\t" + value));
	}

	private static void separador() {
		System.out.println(
				"------------------------------------------------------------");

	}

}
