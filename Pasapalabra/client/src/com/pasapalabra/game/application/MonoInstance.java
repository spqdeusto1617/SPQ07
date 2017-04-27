package com.pasapalabra.game.application;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MonoInstance extends Application {
	//Se define un logger
	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(MonoInstance.class.getName());
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Cargar página con el FXML elegido
		Pane page =  FXMLLoader.load(Main.class.getResource("/fxml/VVacia.fxml"));
		log.log(Level.FINEST, "Cargado FXML VVacia para rutina monoinstancia");
		
		//Añadir la página a la escena
		Scene scene = new Scene(page);
		log.log(Level.FINEST, "Creada escena y añadido el pane");
		
		//Añadir a la escena el CSS
		scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		log.log(Level.FINEST, "Cargado CSS y añadido a la escena");
		
		//Añadir un escuchador para cuando se cierre la ventana (Evita errores)
//		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//		    @Override public void handle(WindowEvent t) {
//		        System.out.println("...CLOSED");
//		    }
//		});
		
		//Icono
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconopsp.png")));
		log.log(Level.FINEST, "Añadido icono a la ventana");
		//Título de la ventana
		log.log(Level.FINEST, "Añadido título a la ventana");
		primaryStage.setTitle("Pasapalabra");
		
		//Poner escena
		primaryStage.setScene(scene);
		log.log(Level.FINEST, "Añadida escena a la ventana");
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		//Mostrar ventana
//		primaryStage.show();
		//En este caso no me interesa mostrar la ventana siquiera.
		
		//Necesitamos el entorno de javafx para crear sobre este una alerta.
		//Creamos la alerta de tipo error
		Alert alert = new Alert(AlertType.ERROR);
		log.log(Level.FINEST, "Creada alerta de tipo error.");
		//Ponemos el título
		alert.setTitle("Error");
		log.log(Level.FINEST, "Titulo de la alerta ahora es Error");
		//Añadimos la cabecera
		alert.setHeaderText("Error de instancia");
		log.log(Level.FINEST, "La cabecera de la alerta ahora es Error de instancia.");
		//Añadimos contenido
		alert.setContentText("\nNo puedes tener la misma aplicación abierta más de una vez. "
				+ ""
				+ "Es por motivos de seguridad. "
				+ "\n\nSi cree que no tiene la aplicación abierta pruebe "
				+ "a comprobar en el Administrador de Tareas si tiene funcionando la aplicación en segundo plano.\n\n");
		log.log(Level.FINEST, "Seteado contenido a la alerta. Se muestra ahora:");
		//Mostramos la alerta y la ventana del fondo se queda paralizada esperando respuesta. En este caso no hay ventana detrás
		alert.showAndWait();
		log.log(Level.FINEST, "Alerta mostrada y cerrada. Se cerrará la ventana y el hilo.");
		//Cerramos el programa
		Platform.exit();
	}
	
	public static void main(String[] args) {
		//Carga la ventana y fin de la rutina.
		launch();
		//Programa terminado por fin del main y por Platform.exit(); del método launch.
	}
}
