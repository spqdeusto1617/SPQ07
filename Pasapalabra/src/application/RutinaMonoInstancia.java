package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RutinaMonoInstancia extends Application {

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Cargar página con el FXML elegido
		Pane page =  FXMLLoader.load(Main.class.getResource("../windows/VVacia.fxml"));
		
		//Añadir la página a la escena
		Scene scene = new Scene(page);
		
		//Añadir a la escena el CSS
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		//Añadir un escuchador para cuando se cierre la ventana (Evita errores)
//		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//		    @Override public void handle(WindowEvent t) {
//		        System.out.println("...CLOSED");
//		    }
//		});
		
		//Icono
		primaryStage.getIcons().add(new Image("images/iconopsp.png"));
		//Título de la ventana
		primaryStage.setTitle("Pasapalabra");
		
		//Poner escena
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		//Mostrar ventana
//		primaryStage.show();
		//En este caso no me interesa mostrar la ventana siquiera.
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error de instancia");
		alert.setContentText("\nNo puedes tener la misma aplicación abierta más de una vez. "
				+ ""
				+ "Es por motivos de seguridad. "
				+ "\n\nSi cree que no tiene la aplicación abierta pruebe "
				+ "a comprobar en el Administrador de Tareas si tiene funcionando la aplicación en segundo plano.\n\n");
		alert.showAndWait();
		Platform.exit();
	}
	
	public static void main(String[] args) {
		launch();
		//TODO Tiene que funcionar esto
		

	}

}
