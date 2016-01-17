package principal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**Clase del servidor (réplica de la de Andoni Eguíluz del examen final)
 * @author Iván
 *Aquí se crea el servidor, y una interfaz para gestionarlo
 */
public class Servidor extends Application{
	public static final int PUERTO_DEL_SERVIDOR = 9898;
	public static String s2 = "";
	public static Logger log = utilidades.AppLogger.getWindowLogger(Servidor.class.getName());
	private static ServerSocket servidor = null;//El socket del servidor
	static boolean funcionando;//Si está el servidor funcionando
	static ArrayList<String> Datos_Usuario=new ArrayList<>();//Los datos del cliente que solicita algo (habría que cambiarlo, porque si no, puede que accedan varios a la vez y fastidiarla
	static ArrayList<String>Clientes_conectados=new ArrayList<>();//Los clientes conectados
	static String[]Datos_Enviar_Usuario;//Los datos a enviar al usuario (si son varios)
	public static void addMensaje(String s) {
		Servidor.s2 = s2 + s;
		if(s2.length()>300){
			Servidor.s2 = s2.substring(200);
		}
	}
	/**
	 * Hilo para iniciar el servidor
	 */	
	public void inicio_servidor() {
		(new Thread() {
			@Override
			public void run() {
				try {
					funcionando = true;
					servidor = new ServerSocket(PUERTO_DEL_SERVIDOR);

					while (funcionando) {



						Conexion_Servidor conexion=new Conexion_Servidor(servidor.accept());  

						addMensaje("Ha entrado un usuario nuevo\n");
						conexion.start();
					}} catch (Exception e) {
					} finally {
						try {
							if (servidor!=null) servidor.close();
						} catch (IOException e) {}
					}
			}
		}).start();
	}
	/**
	 * Proceso para cerrar el servidor
	 */
	public static  void cierre_servidor() {
		funcionando = false;
		if (servidor!=null)
			try {
				servidor.close();  // Cierra el servidor abierto
			} catch (IOException e) {}  
	}
	/**Clase interna-hilo para gestionar la iteración del cliente con el servidor
	 * @author Iván
	 *
	 */
	class Conexion_Servidor extends Thread{
		private Socket socket;

		public Conexion_Servidor(Socket socket) {
			this.socket = socket;
		}

		public void run(){
			String accion="";
			String dato="";
			try {
				BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()) );
				ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream() ); 
				// Envía mensaje de comunicación establecida
				out.writeObject( "ACK" );
				// Recibe la acción que va a hacer el cliente
				accion = in.readLine();

				addMensaje("Quiere: "+accion+"\n");
				out.writeObject( "ACK" );
				switch (accion) {
				case "Comprobar":
					//TODO: terminar esto(faltan noticias)
					addMensaje("Comprobación realizada\n");
					break;
				case "Crear_Usuario":


					addMensaje("Empiezo a recibir la información\n");
					do{

						dato=in.readLine();
						addMensaje("Ha introducido: "+dato+"\n");
						if(!"FIN".equals(dato)){
							Datos_Usuario.add(dato);
						}
						out.writeObject("ACK");

					}while(!"FIN".equals(dato));
					try{	
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
						Date startDate = null;
						try {
							startDate = df.parse(Datos_Usuario.get(3));
							String newDateString = df.format(startDate);

						} catch (ParseException e) {

						}
						if(Datos_Usuario.get(4)==null){
							Datos_Usuario.add(4, "Default_Image");
						}
						BaseDeDatosUsuarios.nuevoUsuario(Datos_Usuario.get(0), Datos_Usuario.get(1), Datos_Usuario.get(2),startDate , Datos_Usuario.get(4));
						out.writeObject("BIEN");
					}catch (Exception e) {

						if(e.getMessage().endsWith("Usuario repetido")){
							out.writeObject("USER");

						}
						if(e.getMessage().endsWith("Email repetido")){
							out.writeObject("MAIL");

						}
					}

					break;
				case "Log":

					addMensaje("Empiezo a recibir la información\n");

					do{

						dato=in.readLine();
						addMensaje("Ha introducido: "+dato+"\n");
						if(!"FIN".equals(dato)){
							Datos_Usuario.add(dato);
						}
						out.writeObject("ACK");

					}while(!"FIN".equals(dato));
					try{
						Datos_Enviar_Usuario=BaseDeDatosUsuarios.getUsuarioPorNombreOID(Datos_Usuario.get(0), null, Datos_Usuario.get(1));
						out.writeObject("BIEN");
						for (String string : Datos_Enviar_Usuario) {

							out.writeObject(string);
							addMensaje("Le envío: "+string+"\n");
							dato = in.readLine();
							if (!"ACK".equals(dato)) throw new IOException( "Conexión errónea: " + dato );
						}
						out.writeObject("FIN");

						dato = in.readLine();
						if (!"ACK".equals(dato)) throw new IOException( "Conexión errónea: " + dato );
						Clientes_conectados.add(Datos_Enviar_Usuario[0]);
						System.out.println(Datos_Enviar_Usuario[0]);
					}catch (SQLException e) {
						out.writeObject("SQL");

					}
					break;
				case "Jugar":
					//TODO: terminar esto


					break;
				case "Imagen":

					addMensaje("Empiezo a recibir la información\n");


					out.writeObject("ACK");
					Datos_Usuario.add(in.readLine());
					out.writeObject("ACK");
					Datos_Usuario.add(in.readLine());
					try{
						BaseDeDatosUsuarios.cambiarDatosUsuario(Datos_Usuario.get(0), null, null, Datos_Usuario.get(1));
						out.writeObject("OK");
					}catch(Exception a){
						throw new IOException("Error al meter datos");
					}
					break;
				case "Pass":

					addMensaje("Empiezo a recibir la información\n");
					out.writeObject("ACK");
					Datos_Usuario.add(in.readLine());
					out.writeObject("ACK");
					Datos_Usuario.add(in.readLine());
					try{
						BaseDeDatosUsuarios.cambiarDatosUsuario(Datos_Usuario.get(0), null, Datos_Usuario.get(1), null);
						out.writeObject("OK");
					}catch(Exception a){
						throw new IOException("Error al meter datos");
					}


					break;
				case "Mail":

					out.writeObject("ACK");
					Datos_Usuario.add(in.readLine());
					out.writeObject("ACK");
					Datos_Usuario.add(in.readLine());
					try{
						BaseDeDatosUsuarios.cambiarDatosUsuario(Datos_Usuario.get(0), Datos_Usuario.get(1), null, null);
						out.writeObject("OK");
					}catch(Exception a){
						throw new IOException("Error al meter datos");
					}

					break;


				case "Estadisticas":
					//TODO: terminar esto


					break;
				case "Eliminar":
					addMensaje("Empiezo a recibir la información\n");
					do{

						dato=in.readLine();
						addMensaje("Ha introducido: "+dato+"\n");
						if(!"FIN".equals(dato)){
							Datos_Usuario.add(dato);
						}
						out.writeObject("ACK");

					}while(!"FIN".equals(dato));
					try{
						BaseDeDatosUsuarios.eliminar_Usuario(Datos_Usuario.get(0), Datos_Usuario.get(2));
						Clientes_conectados.remove(Datos_Usuario.get(0));
						out.writeObject("BIEN");
						addMensaje("Eliminado con éxito\n");
					}catch (SQLException e) {
						out.writeObject("SQL");
					}
					break;
				case "Delog":
					dato=in.readLine();
					Clientes_conectados.remove(dato);
					out.writeObject("BIEN");
				default:
					break;
				}

				Datos_Usuario.removeAll(Datos_Usuario);
				out.reset();

				accion = in.readLine();

				if (!"END".equals(accion)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + accion );
				out.writeObject( "ACK" );

				addMensaje("Conexión finalizada\n");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println( "Error a la hora de cerrar la conexión con el cliente." );
				}
			}


		}


	}

	/** Clase de ventana visual del servidor
	 */



	public static void main(String[] args) {
		BaseDeDatosPreguntas.iniciarConexion();
		BaseDeDatosUsuarios.iniciarConexion();
		Servidor s = new Servidor();
		s.inicio_servidor();
		addMensaje("Esperando conexión con algún cliente...\n");
		launch(args);
		try { Thread.sleep(10); } catch (InterruptedException e) {}
	}
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
			    		alert.showAndWait();
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
}
