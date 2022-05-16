package agenda.controlador;

import agenda.io.AgendaIO;
import agenda.modelo.AgendaContactos;
import agenda.modelo.Contacto;
import agenda.modelo.Personal;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ControladorAgenda {

	private static final String FALTA_IMPORTAR = "Es necesario importar los contactos antes de ejecutar esta función";
	AgendaContactos agenda;
	@FXML
	private TextArea areaTexto;

	@FXML
	private MenuBar barraMenu;

	@FXML
	private Button btnClear;

	@FXML
	private Button btnListar;

	@FXML
	private Button btnPersonalesFechaNac;

	@FXML
	private Button btnPesonalesEnLetra;

	@FXML
	private Button btnSalir;

	@FXML
	private MenuItem itemAbout;

	@FXML
	private MenuItem itemBuscar;

	@FXML
	private MenuItem itemExportar;

	@FXML
	private MenuItem itemFelicitar;

	@FXML
	private MenuItem itemImportar;

	@FXML
	private MenuItem itemSalir;

	@FXML
	private GridPane panelLetras;

	@FXML
	private RadioButton rbtListarNumero;

	@FXML
	private RadioButton rbtListarTodos;

	@FXML
	private ToggleGroup toogleLista;

	@FXML
	private TextField txtBuscar;

	public ControladorAgenda() {
		agenda = new AgendaContactos();
	}

	@FXML
	void initialize(){
		//En esta función asignamos los eventos correspondientes a cada elemento
		for (Node child : panelLetras.getChildren()) {
			if(child instanceof Button){
				((Button) child).setOnAction(e->contactosEnLetra(((Button) child).getText().charAt(0)));
			}
		}
	}
	@FXML
	private void importarAgenda() {
		FileChooser selector = new FileChooser();
		selector.setTitle("Abrir fichero csv");
		selector.setInitialDirectory(new File("."));
		selector.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("csv",
						"*.csv"));
		File f = selector.showOpenDialog(null);
		if (f != null) {
			System.out.println("Fichero elegido: "
					+ f.getName());
			try {
				int errores = AgendaIO.importar(agenda, f.getAbsolutePath());
				areaTexto.setText("Agenda importada\nLíneas erróneas: " + errores);
				itemImportar.setDisable(true);
				itemExportar.setDisable(false);
			} catch (IOException e) {
				areaTexto.setText(e.getMessage());
			}
		}

	}
	@FXML
	private void exportarPersonales() {
		FileChooser selector = new FileChooser();
		selector.setTitle("Exportar contactos personales por relación");
		selector.setInitialDirectory(new File("."));
		selector.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("txt",
						"*.txt"));
		File f = selector.showSaveDialog(null);
		if (f != null) {
			try {
				AgendaIO.exportarPersonales(agenda, f.getAbsolutePath());
				areaTexto.setText("Exportados datos personales");
			} catch (IOException e) {
				areaTexto.setText(e.getMessage());
			}
		}

	}

	/**
	 *
	 */
	@FXML
	private void listar() {
		clear();
		if(itemImportar.isDisable()){//Ya se ha hecho la importacion
			if (rbtListarTodos.isSelected()) {
				areaTexto.setText(agenda.toString());
			} else {
				areaTexto.setText("Número de contactos: " + agenda.totalContactos()+"");
			}
		}
		else{
			areaTexto.setText(FALTA_IMPORTAR);
		}

	}
	@FXML
	private void personalesOrdenadosPorFecha() {
		clear();
		if (itemImportar.isDisable()) {
			Optional<Character> resul = choiceAbecedario();
			if (resul.isPresent()) {
				Character opcion = resul.get();
				List<Personal> contactos = agenda.personalesOrdenadosPorFechaNacimiento(opcion);
				if(contactos.isEmpty()){
					areaTexto.setText("No se han encontrado contactos para la letra " + opcion);
				}
				else{
					StringBuilder sb = new StringBuilder("Personales en la letra " + opcion + " ordenados por fecha" +
							"de nacimiento \n\n");
					for (Personal c : contactos) {
						sb.append(c).append("\n");
					}
					areaTexto.setText(sb.toString());
				}
			}
			else {
				//se ha pulsado cancel en el ChoiceDialog
			}
		}
		else {
			areaTexto.setText(FALTA_IMPORTAR);
		}

	}
	@FXML
	private void contactosPersonalesEnLetra() {
		clear();
		if (itemImportar.isDisable()) {
			Optional<Character> resul = choiceAbecedario();
			if (resul.isPresent()) {
				Character opcion = resul.get();
				List<Personal> contactos = agenda.personalesEnLetra(opcion);

				try {
					if(contactos.isEmpty()){
						areaTexto.setText("No se han encontrado contactos para la letra " + opcion);
					}
					else{
						StringBuilder sb = new StringBuilder("Personales en la letra " + opcion + "\n\n");
						for (Personal c : contactos) {
							sb.append(c).append("\n");
						}
						areaTexto.setText(sb.toString());
					}
				} catch (NullPointerException e) {
					areaTexto.setText("No se han encontrado contactos para la letra " + opcion);
				}
			}
			else {
				//se ha pulsado cancel en el ChoiceDialog
				//No hacemos nada
			}
		}
		else {
			areaTexto.setText(FALTA_IMPORTAR);
		}

	}

	private Optional<Character> choiceAbecedario() {
		String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
		Character[] arrLetras = new Character[abecedario.length()];
		for (int i = 0; i < abecedario.length(); i++) {
			arrLetras[i]=abecedario.charAt(i);
		}
		ChoiceDialog<Character> dialogo = new ChoiceDialog<>('A', Arrays.asList(arrLetras));
		dialogo.setTitle("Selector de letra");
		dialogo.setHeaderText(null);
		dialogo.setContentText("Elija letra");
		Optional<Character> resul = dialogo.showAndWait();
		return resul;
	}
	@FXML
	private void contactosEnLetra(char letra) {
		clear();
		if(itemImportar.isDisable()){//Ya se ha hecho la importacion
			Set<Contacto> contactos = agenda.contactosEnLetra(letra);
			if(contactos==null || contactos.isEmpty()){
				areaTexto.setText("No hay contactos en la letra " + letra);
			}
			else{
				StringBuilder sb = new StringBuilder("Contactos en la letra " + letra + "\n\n");
				for (Contacto c : contactos) {
					sb.append(c).append("\n");
				}
				areaTexto.setText(sb.toString());
			}

		}
		else{
			areaTexto.setText(FALTA_IMPORTAR);
		}
	}
	@FXML
	private void felicitar() {
		clear();
		if(itemImportar.isDisable()){//Si ya se han importado los contactos
			StringBuilder sb = new StringBuilder("Hoy es " + fechaHoy() + "\n\n");
			List<Personal> felicidades = agenda.felicitar();
			if(felicidades.isEmpty()){
				sb.append("No hay contactos para felicitar");
			}
			else{
				sb.append("Hoy hay que felicitar a\n\n");
				for (Personal p : felicidades) {
					sb.append(p).append("\n");
				}
			}
			areaTexto.setText(sb.toString());
		}
		else{
			areaTexto.setText("Debe importar los contactos antes de poder felicitar");
		}
	}

	private String fechaHoy() {
		LocalDate hoy = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		return hoy.format(dtf).toString();
	}


	@FXML
	private void buscar() {
		clear();
		String buscar = txtBuscar.getText();
		if (buscar.isEmpty()) {
			areaTexto.setText("Introduzca el texto a buscar");
		} else {
			StringBuilder texto = new StringBuilder("Contactos en la agenda que contienen '" + buscar + "'\n\n");
			List<Contacto> encontrados = agenda.buscarContactos(buscar);
			if (encontrados.isEmpty()) {
				texto.append("No se han encontrado contactos");
			} else {
				for (Contacto contacto : encontrados) {
					texto.append(contacto).append("\n");
				}
			}
			areaTexto.setText(texto.toString());
		}
		cogerFoco();

	}
	@FXML
	private void about() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About Agenda de contactos");
		alert.setHeaderText(null);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().
				getResource("/agenda/vista/application.css").toExternalForm());
		alert.setContentText("Mi agenda de contactos");
		alert.showAndWait();
	}
	@FXML
	private void clear() {
		areaTexto.setText("");
	}
	@FXML
	private void salir() {
		Platform.exit();
	}

	private void cogerFoco() {
		txtBuscar.requestFocus();
		txtBuscar.selectAll();

	}
}
