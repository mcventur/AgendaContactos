package agenda.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestAgenda extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass()
			                    .getResource("/agenda/vista/GuiAgenda.fxml"));
			Scene scene = new Scene(root, 1100, 700);
			scene.getStylesheets()
			                    .add(getClass().getResource("/application.css")
			                                        .toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Agenda de contactos con Scene Builder");
			primaryStage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
