package com.pasapalabra.game.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.pasapalabra.game.dao.mongodb.MongoConnection;
import com.pasapalabra.game.service.IPasapalabraService;
import com.pasapalabra.game.service.PasapalabraService;
import com.pasapalabra.game.utilities.AppLogger;
import com.pasapalabra.game.utilities.WindowUtilites;

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
	
	public static String serverPort;
	
	public static String serviceName;
	
	private static boolean serverOnline = true;
	
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
			
			IPasapalabraService pasapalabraService = new PasapalabraService();
			
			try {
				Naming.rebind(serverAddress,  UnicastRemoteObject.exportObject(pasapalabraService, 0));
				serverPort = args[1];
				serviceName = args[2];
				log.log(Level.INFO, "Server at '" + serverAddress + "' active and waiting connections...");
			} catch (RemoteException | MalformedURLException e) {
				log.log(Level.SEVERE, "Error trying to registry the server", e);
				serverOnline = false;
			}
	    
			launch(args);
	
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		if(!serverOnline){
		Alert alert = new Alert(AlertType.ERROR);
		
		alert.setTitle("Error while starting the server.");
		
		alert.setHeaderText("Impossible to start the server");
		
		alert.setContentText("An error occurred during the start of the server.\nPlease, make"
				+ " sure your connection is working and you have inicializate the RMI registry properly");
		
		alert.showAndWait();
		
		log.log(Level.INFO, "Exiting from the server");

		System.exit(0);
		}
		try {
			AppLogger.crearLogHandler(log, Server.class.getName());
			//Load the main pane
			Pane page =  FXMLLoader.load(Server.class.getResource("/fxml/Server.fxml"));
			log.log(Level.FINEST, "FXML file loaded in the Pane");


			//Add page to the scene
			Scene scene = new Scene(page);
			log.log(Level.FINEST, "Added pane to the scene");

			//Add the css
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			log.log(Level.FINEST, "Css added to the scene");

			//Use this in the server
			//It may be used in the future.
			//Add a listener in case the window is closed 
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Exit form the application");
					alert.setHeaderText("Are you sure you want to terminate the server?");
					alert.setContentText("If you exit from the server, all progress will be lost, are you sure about that?");

					alert.initModality(Modality.APPLICATION_MODAL);
					alert.initOwner((Stage)event.getSource());
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						Platform.exit();
						log.log(Level.INFO, "Exiting from the application");
						System.exit(0);
					} else {
						event.consume();
					}
				}
			});



			//Icon
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/iconopsp.png")));
			log.log(Level.FINEST, "Added icon to the main window");
			//Window´s title
			primaryStage.setTitle("Pasapalabra - Server");
			log.log(Level.FINEST, "Added title to the window");


			//Put scene
			primaryStage.setScene(scene);
			log.log(Level.FINEST, "Added scene to the window");

			//The window can´t be resizable 
			primaryStage.setResizable(false);
			//We disable the property all the elements form the windows to being resizable
			primaryStage.initStyle(StageStyle.UTILITY);
			log.log(Level.FINEST, "All the recize button disabled");

			primaryStage.sizeToScene();
			//Show window
			primaryStage.show();
			log.log(Level.FINEST, "Window shown");
			//Center window
			WindowUtilites.centerWindow(primaryStage);
			log.log(Level.FINEST, "Window centered");

		} catch(Exception e) {
			//e.printStackTrace();
			log.log(Level.SEVERE, "Error while starting Main.java", e);
		}

	}
	

	
}