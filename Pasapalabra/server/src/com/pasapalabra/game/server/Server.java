package com.pasapalabra.game.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.pasapalabra.game.dao.QuestionDAO;
import com.pasapalabra.game.dao.UserDAO;
import com.pasapalabra.game.dao.mongodb.MongoConnection;
import com.pasapalabra.game.dao.mongodb.QuestionMongoDAO;
import com.pasapalabra.game.dao.mongodb.UserMongoDAO;
import com.pasapalabra.game.model.Question;
import com.pasapalabra.game.model.User;
import com.pasapalabra.game.service.IPasapalabraService;
import com.pasapalabra.game.service.PasapalabraService;
import com.pasapalabra.game.utilities.AppLogger;
import com.pasapalabra.game.utilities.deVentana;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Server extends Application{
	
	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(Server.class.getName());
	public static MongoConnection mongoConnection;
	public static void main(String[] args) {

	    	System.out.println("Starting server...");
	    	
	    	if (args.length != 3) {
	    		System.out.println("args error, server won't startup.");
	    		System.out.println("usage: java [policy] [codebase] server.Server [host] [port] [server]");
				System.exit(0);
			}
	    	System.out.println(System.getProperty("java.security.policy"));
	    	System.out.println(System.getProperty("java.rmi.server.codebase"));
			
	    	
	    	MongoClientOptions.Builder options = MongoClientOptions.builder();
	    	options.socketKeepAlive(true);
	    	MongoClient mongoClient = new MongoClient("127.0.0.1:27017", options.build());
	    	mongoConnection = new MongoConnection("com.pasapalabra.game.model", mongoClient, "pasapalabra"); 	
	    	
			String serverAddress = "//" + args[0] + ":" + args[1] + "/" + args[2];
			System.out.println(" * Server name: " + serverAddress);
			IPasapalabraService pasapalabraService = new PasapalabraService();
			
			try {
				Naming.rebind(serverAddress,  UnicastRemoteObject.exportObject(pasapalabraService, 0));
				System.out.println("Server at '" + serverAddress + "' active and waiting connections...");
			} catch (RemoteException | MalformedURLException e) {
				System.out.println("Error while starting the server.");
				e.printStackTrace();
			}
	    
			launch(args);
	
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			AppLogger.crearLogHandler(log, Server.class.getName());
			//Cargar página con el FXML elegido
			Pane page =  FXMLLoader.load(Server.class.getResource("/fxml/Servidor.fxml"));
			log.log(Level.FINEST, "Cargado fichero FXML de LogIn en el pane");


			//Añadir la página a la escena
			Scene scene = new Scene(page);
			log.log(Level.FINEST, "Añadido pane a la escena");

			//Añadir a la escena el CSS
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			log.log(Level.FINEST, "Añadido css a la escena");

			//Usarse para servidor.
			//Puede que se necesite algún día.
			//Añadir un escuchador para cuando se cierre la ventana 
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Salir de la aplicación");
					alert.setHeaderText("¿Está segur@ de que desea salir de la aplicación?");
					alert.setContentText("Si sale de la aplicación, todos los usuarios conectados al servidor perderán el progreso que estén haciendo en este momento.\n\n¿Está segur@?");

					alert.initModality(Modality.APPLICATION_MODAL);
					alert.initOwner((Stage)event.getSource());
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						Platform.exit();
						System.exit(0);
					} else {
						event.consume();
					}
				}
			});



			//Icono
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconopsp.png")));
			log.log(Level.FINEST, "Añadido icono a la ventana");
			//Título de la ventana
			primaryStage.setTitle("Pasapalabra - Servidor");
			log.log(Level.FINEST, "Añadido título a la ventana");


			//Poner escena
			primaryStage.setScene(scene);
			log.log(Level.FINEST, "Añadida escena a la ventana");

			//No se puede hacer resize
			primaryStage.setResizable(false);
			//Por ello, desactivamos los botones para hacer resize (maximizar)
			primaryStage.initStyle(StageStyle.UTILITY);
			log.log(Level.FINEST, "Desactivados botones resize de la ventana");

			primaryStage.sizeToScene();
			//Mostrar ventana
			primaryStage.show();
			log.log(Level.FINEST, "Ventana mostrada");
			//Centrar ventana
			deVentana.centrarVentana(primaryStage);
			log.log(Level.FINEST, "Centrada la ventana");

		} catch(Exception e) {
			e.printStackTrace();
			log.log(Level.SEVERE, "Error en start de Main.java", e);
		}

	}
	

	
}