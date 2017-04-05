package principal;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import service.IPasapalabraService;
import service.PasapalabraService;

public class Server {
	
	
	
	public static void main(String[] args) {

	    	System.out.println("Starting server...");
	    	
	    	if (args.length != 3) {
	    		System.out.println("args error, server won't startup.");
	    		System.out.println("usage: java [policy] [codebase] server.Server [host] [port] [server]");
				System.exit(0);
			}
			
			
	    	if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}

			String serverAddress = "//" + args[0] + ":" + args[1] + "/" + args[2];
			System.out.println(" * Server name: " + serverAddress);
			IPasapalabraService pasapalabraService = new PasapalabraService();
			
			try {
				Naming.rebind(serverAddress, pasapalabraService);
				System.out.println("Server at '" + serverAddress + "' active and waiting connections...");
			} catch (RemoteException | MalformedURLException e) {
				System.out.println("Error while starting the server.");
				e.printStackTrace();
			}
	    
	
	}
	/*
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			utilidades.AppLogger.crearLogHandler(log, Servidor.class.getName());
			//Cargar página con el FXML elegido
			Pane page =  FXMLLoader.load(Servidor.class.getResource("Servidor.fxml"));
			log.log(Level.FINEST, "Cargado fichero FXML de LogIn en el pane");


			//Añadir la página a la escena
			Scene scene = new Scene(page);
			log.log(Level.FINEST, "Añadido pane a la escena");

			//Añadir a la escena el CSS
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
						Servidor.cierre_servidor();
						BaseDeDatosPreguntas.cerrarConexion();
						BaseDeDatosUsuarios.cerrarConexion();
						Platform.exit();
						System.exit(0);
					} else {
						event.consume();
					}
				}
			});



			//Icono
			primaryStage.getIcons().add(new Image("images/iconopsp.png"));
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
			utilidades.deVentana.centrarVentana(primaryStage);
			log.log(Level.FINEST, "Centrada la ventana");





		} catch(Exception e) {
			e.printStackTrace();
			log.log(Level.SEVERE, "Error en start de Main.java", e);
		}

	}
	*/

	
}