package com.pasapalabra.game.application;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.controllers.EventosJuego;
import com.pasapalabra.game.utilidades.Acciones_servidor;
import com.pasapalabra.game.utilidades.Conexion_cliente;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


/**Clase principal que crea la aplicación y la muestra en pantalla.
 * @author asier.gutierrez
 *
 */
public class Main extends Application {

	//Se define un logger.

	public static Logger log = com.pasapalabra.game.utilidades.AppLogger.getWindowLogger(Main.class.getName());

	@Override
	public void start(Stage primaryStage) {

		try {
			com.pasapalabra.game.utilidades.AppLogger.crearLogHandler(log, Main.class.getName());
			//Cargar página con el FXML elegido  
			Pane page =  FXMLLoader.load(getClass().getResource("/com.pasapalabra/game/windows/LogIn.fxml")); //Lo he dejado así porque sino, a la hora de exportarlo a un jar, no funciona el propio jar
			log.log(Level.FINEST, "Cargado fichero FXML de LogIn en el pane");

			//Añadir la página a la escena
			Scene scene = new Scene(page);
			log.log(Level.FINEST, "Añadido pane a la escena");

			//Añadir a la escena el CSS
			scene.getStylesheets().add(getClass().getResource("/com.pasapalabra/game/application/application.css").toExternalForm());
			log.log(Level.FINEST, "Añadido css a la escena");

			//Usarse para servidor.
			//Puede que se necesite algún día.
			//Añadir un escuchador para cuando se cierre la ventana 
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override public void handle(WindowEvent t) {
					if(!com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.isEmpty()){
						String[] Datos=new String[1];
						if(EventosJuego.juegoEnCurso==false){
							
							Datos[0]=Conexion_cliente.Datos_Usuario.get(0);
							try {
								Conexion_cliente.lanzaConexion(Conexion_cliente.Ip_Local, Acciones_servidor.Delog.toString(),Datos);
								
							} catch (Exception e) {
								//TODO: gestionar la excepción(¿mensaje de error?)
							} 
						}else{
							Datos[0]=Conexion_cliente.Datos_Usuario.get(0);
							System.out.println("Nos rendimos, al cerrar la ventana");
							com.pasapalabra.game.utilidades.Conexion_cliente.Respuesta="Rendirse";
							try {
								com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local, com.pasapalabra.game.utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
								Conexion_cliente.lanzaConexion(Conexion_cliente.Ip_Local, Acciones_servidor.Delog.toString(),Datos);
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Error e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						
					}
				}
			});



			//Icono
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/pasapalabra/game/images/iconopsp.png")));
			log.log(Level.FINEST, "Añadido icono a la ventana");
			//Título de la ventana
			primaryStage.setTitle("Pasapalabra");
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
			com.pasapalabra.game.utilidades.deVentana.centrarVentana(primaryStage);
			log.log(Level.FINEST, "Centrada la ventana");





		} catch(Exception e) {
			e.printStackTrace();
			log.log(Level.SEVERE, "Error en start de Main.java", e);
		}
	}


	/**Método main de la clase.
	 * @param args Argumentos de consola (no se utilizan)
	 */
	public static void main(String[] args) {
				if (lockInstance("block.dat")){
					//Aquí se carga toda la aplicación
					launch(args);
					log.log(Level.FINEST, "Ventana cargada. FIN HILO MAIN.JAVA");
//					//Hilo main muerto.
				}else{
					log.log(Level.INFO, "Ventana NO cargada. Ya hay una instancia de la clase. Se ejecuta hilo de advertencia soporte único a monoinstancia");
					//Cargamos el hilo de rutina monoinstancia. Nuestro hilo main muere después.
					RutinaMonoInstancia.main(args);
				}
	}

	private static boolean lockInstance(final String lockFile) {
		try {
			log.log(Level.FINEST, "Se empieza a bloquear un archivo.");
			//Creamos un nuevo archivo con un nombre
			final File file = new File(lockFile);
			//El archivo es creado y se le añade un acceso aleatorio. READ WRITING
			final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			//Se bloquea el archivo.
			final FileLock fileLock = randomAccessFile.getChannel().tryLock();
			log.log(Level.FINEST, "Archivo bloqueado");
			/*Si se ha bloqueado bien entonces se crea una rutina para cuando se cierre el programa que eliminará todos los archivos creados,
			 *Todos ellos necesarios para la monoinstancia.
			 */
			if (fileLock != null) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						try {
							fileLock.release();
							log.log(Level.FINEST, "Eliminado lock");
							randomAccessFile.close();
							log.log(Level.FINEST, "Cerrado el acceso aleatorio");
							file.delete();
							log.log(Level.FINEST, "Eliminado archivo");
						} catch (Exception e) {
							System.out.println("Unable to remove lock file: " + lockFile +  e);
							log.log(Level.WARNING, "El bloqueo del archivo ha sufrido un problema al cerrar el programa", e);
						}
						log.log(Level.FINEST, "Toca cerrar conexión con servidor.");
					}
				});
				log.log(Level.FINEST, "Archivo creado y bloqueado satisfactoriamente: true.");
				return true;
			}
		} catch (Exception e) {
			System.out.println("Imposibilidad para crear o bloquear archivo: " + lockFile + e);
			log.log(Level.FINEST, "Imposibilidad para crear o bloquear archivo: " + lockFile, e);
		}
		log.log(Level.FINEST, "El archivo ya está creado. Devuelve false.");
		return false;
	}
}
