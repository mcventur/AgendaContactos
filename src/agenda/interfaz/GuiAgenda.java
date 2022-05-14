package agenda.interfaz;

import agenda.io.AgendaIO;
import agenda.modelo.AgendaContactos;
import agenda.modelo.Contacto;
import agenda.modelo.Personal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilidades.fecha.Utilidades;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GuiAgenda extends Application {
    public static final String FALTA_IMPORTAR = "Debe importar previamente los contactos";
    private AgendaContactos agenda;
    private MenuItem itemImportar;
    private MenuItem itemExportarPersonales;
    private MenuItem itemSalir;

    private MenuItem itemBuscar;
    private MenuItem itemFelicitar;

    private MenuItem itemAbout;

    private TextArea areaTexto;

    private RadioButton rbtListarTodo;
    private RadioButton rbtListarSoloNumero;
    private Button btnListar;

    private Button btnPersonalesEnLetra;
    private Button btnPersonalesOrdenadosPorFecha;

    private TextField txtBuscar;

    private Button btnClear;
    private Button btnSalir;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        agenda = new AgendaContactos(); // el modelo

        BorderPane root = crearGui();

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Agenda de contactos");
        stage.getIcons().add(new Image(getClass().getResourceAsStream(
                "/images/addressbook-icon.png")));
        scene.getStylesheets().add(getClass().getResource("/application.css")
                .toExternalForm());
        stage.show();

    }

    private BorderPane crearGui() {
        BorderPane panel = new BorderPane();
        panel.setTop(crearBarraMenu());
        panel.setCenter(crearPanelPrincipal());
        return panel;
    }

    private BorderPane crearPanelPrincipal() {
        BorderPane panel = new BorderPane();
        panel.setPadding(new Insets(10));
        panel.setTop(crearPanelLetras());

        areaTexto = new TextArea();
        areaTexto.getStyleClass().add("textarea");
        panel.setCenter(areaTexto);

        panel.setLeft(crearPanelBotones());
        return panel;
    }

    private VBox crearPanelBotones() {
        // a completar
        VBox panel = new VBox();
        panel.setPadding(new Insets(10));
        panel.setSpacing(10);
        //txtBuscar
        txtBuscar = new TextField();
        txtBuscar.setPromptText("Buscar");
        txtBuscar.setMinHeight(40);
        VBox.setMargin(txtBuscar, new Insets(0, 0, 40, 0));
        //Asignamos un evento al pulsar la tecla ENTER con el foco en el TextField. Usamos una lambda
        txtBuscar.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                buscar();
            }
        });
        //rbtListarTodo
        rbtListarTodo = new RadioButton("Listar toda la agenda");
        rbtListarTodo.setSelected(true);
        //rbtListarTodo.getStyleClass().add("botones");
        //rbtListarSoloNumero
        rbtListarSoloNumero = new RadioButton("Listar nº contactos");
        //Los añadimos al mismo grupo, para que sean excluyentes entre ellos
        ToggleGroup tg = new ToggleGroup();
        rbtListarTodo.setToggleGroup(tg);
        rbtListarSoloNumero.setToggleGroup(tg);
        //Inicializamos los botones
        btnListar = new Button("Listar");
        btnPersonalesEnLetra = new Button("Contactos personales en letra");
        btnPersonalesOrdenadosPorFecha = new Button("Contactos personales\nordenados por fecha");
        btnClear = new Button("Clear");
        btnSalir = new Button("Salir");
        //Les damos la clase CSS ".botones"
        btnListar.getStyleClass().add("botones");
        btnPersonalesEnLetra.getStyleClass().add("botones");
        btnPersonalesOrdenadosPorFecha.getStyleClass().add("botones");
        btnClear.getStyleClass().add("botones");
        btnSalir.getStyleClass().add("botones");
        //Les doy el ancho preferido de 250 a todos en la propìa clase CSS
        VBox.setMargin(btnListar, new Insets(0, 0, 40, 0));
        VBox.setMargin(btnClear, new Insets(40, 0, 0, 0));

        //Asociamos manejadores al click de los botones
        btnClear.setOnAction(e->clear());
        btnSalir.setOnAction(e -> salir());
        btnListar.setOnAction(e->listar());
        btnPersonalesEnLetra.setOnAction(e->contactosPersonalesEnLetra());
        btnPersonalesOrdenadosPorFecha.setOnAction(e->personalesOrdenadosPorFecha());


        //Añadimos al panel los elementos
        panel.getChildren().addAll(txtBuscar, rbtListarTodo, rbtListarSoloNumero, btnListar, btnPersonalesEnLetra,
                btnPersonalesOrdenadosPorFecha, btnClear, btnSalir);

        return panel;
    }

    private GridPane crearPanelLetras() {
        // a completar
        GridPane panel = new GridPane();
        //panel.setMaxSize(Integer.MAX_VALUE,Integer.MAX_VALUE);
        //Padding y espoacio vertical/horizontal entre componentes
        panel.setPadding(new Insets(10));
        panel.setHgap(5);
        panel.setVgap(5);
        String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        for (int i = 0; i < abecedario.length(); i++) {
            int fila = i / 14;
            int col = i % 14;
            Button boton = new Button(Character.toString(abecedario.charAt(i)));
            boton.getStyleClass().add("botonletra");
            boton.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
            GridPane.setHgrow(boton, Priority.ALWAYS);
            boton.setOnAction(e -> contactosEnLetra(boton.getText().charAt(0)));
            panel.add(boton, col, fila);
        }
        panel.setAlignment(Pos.CENTER);
        return panel;
    }

    private MenuBar crearBarraMenu() {
        // a completar
        MenuBar barra = new MenuBar();
        //Creo los menús
        Menu menuArchivo = new Menu("_Archivo");
        Menu menuOperaciones = new Menu("_Operaciones");
        Menu menuHelp = new Menu("_Help");
        //Los añado a la barra
        barra.getMenus().addAll(menuArchivo, menuOperaciones, menuHelp);

        //Inicializo y configuro los MenuItems
        //Importar
        itemImportar = new MenuItem("Importar");
        itemImportar.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        itemImportar.setOnAction(e -> importarAgenda());
        //ExportarPersonales
        itemExportarPersonales = new MenuItem("Exportar Personales");
        itemExportarPersonales.setDisable(true);
        itemExportarPersonales.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        itemExportarPersonales.setOnAction(e -> exportarPersonales());
        //Salir
        itemSalir = new MenuItem("Salir");
        itemSalir.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        itemSalir.setOnAction(e -> salir());
        //Los añadimos al menu
        menuArchivo.getItems().addAll(itemImportar, itemExportarPersonales, new SeparatorMenuItem(), itemSalir);
        //Buscar
        itemBuscar = new MenuItem("Buscar");
        itemBuscar.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        itemBuscar.setOnAction(e -> buscar());
        //Felicitar
        itemFelicitar = new MenuItem("Felicitar");
        itemFelicitar.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
        itemFelicitar.setOnAction(e->felicitar());
        //Los añadimos al menú Operaciones
        menuOperaciones.getItems().addAll(itemBuscar, itemFelicitar);
        //About
        itemAbout = new MenuItem("About");
        itemAbout.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        itemAbout.setOnAction(e->about());
        menuHelp.getItems().addAll(itemAbout);

        return barra;
    }

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
                itemExportarPersonales.setDisable(false);
            } catch (IOException e) {
                areaTexto.setText(e.getMessage());
            }
        }

    }

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
    private void listar() {
        clear();
        if(itemImportar.isDisable()){//Ya se ha hecho la importacion
            if (rbtListarTodo.isSelected()) {
                areaTexto.setText(agenda.toString());
            } else {
                areaTexto.setText("Número de contactos: " + agenda.totalContactos()+"");
            }
        }
        else{
            areaTexto.setText(FALTA_IMPORTAR);
        }

    }

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
        ChoiceDialog<Character> dialogo = new ChoiceDialog<>('A',Arrays.asList(arrLetras));
        dialogo.setTitle("Selector de letra");
        dialogo.setHeaderText(null);
        dialogo.setContentText("Elija letra");
        Optional<Character> resul = dialogo.showAndWait();
        return resul;
    }

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

    private void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Agenda de contactos");
        alert.setHeaderText(null);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().
                getResource("/application.css").toExternalForm());
        alert.setContentText("Mi agenda de contactos");
        alert.showAndWait();
    }

    private void clear() {
        areaTexto.setText("");
    }

    private void salir() {
        Platform.exit();
    }

    private void cogerFoco() {
        txtBuscar.requestFocus();
        txtBuscar.selectAll();

    }
}
