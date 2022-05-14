package agenda.controlador;

import agenda.modelo.AgendaContactos;
import javafx.fxml.FXML;

public class ControladorAgenda {
	private AgendaContactos agenda; // el modelo;

	public ControladorAgenda() {
		this.agenda = new AgendaContactos();
	}

	@FXML
	public void initialize() {

	}

}
