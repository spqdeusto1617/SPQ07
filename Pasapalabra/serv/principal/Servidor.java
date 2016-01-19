package principal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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
	static TreeMap<String, ArrayList<Tipo_Amigo>>Amigos_usuarios=new TreeMap<>();
	static TreeMap<String, ArrayList<Socket>>MatchMaking=new TreeMap<String, ArrayList<Socket>>();
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
					leeDeFichero();
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
			ArrayList<String> Datos_Usuario=new ArrayList<>();

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
						ArrayList<Tipo_Amigo>Tipo=new ArrayList<Tipo_Amigo>();
						Amigos_usuarios.put(Datos_Usuario.get(0), Tipo);

					}catch (Exception e) {
						e.printStackTrace();
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

					if(Clientes_conectados.contains(Datos_Usuario.get(0))){
						out.writeObject("ACTIVO");
					}
					else{
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
						}catch (SQLException e) {
							out.writeObject("SQL");

						}
					}
					break;
				case "Jugar":

					ArrayList<Preguntas>Preguntas_Jugador1=new ArrayList<>();
					ArrayList<Preguntas>Preguntas_Jugador2=new ArrayList<>();

					dato=in.readLine();
					if(!MatchMaking.containsKey(dato)){
						MatchMaking.put(dato, new ArrayList<Socket>());
					}
					if(MatchMaking.get(dato).size()==0){
						MatchMaking.get(dato).add(socket);
						System.out.println("Lo añado a espera y me voy");
						return;
					}
					else{
						Socket socket2=MatchMaking.get(dato).get(0);
						MatchMaking.get(dato).remove(0);
						BufferedReader in2 = new BufferedReader( new InputStreamReader(socket2.getInputStream()) );
						ObjectOutputStream out2 = new ObjectOutputStream( socket2.getOutputStream() ); 
						out.writeObject("Encontrado");
						out2.writeObject("Encontrado");
						int Preguntas_bien=0;
						int Preguntas_mal=0;
						if(dato.equals("Todos")||dato.equals("Geografía")||dato.equals("Entretenimiento")||dato.equals("Ciencia")||dato.equals("Historia")||dato.equals("Arte")||dato.equals("Deportes")){
							boolean ya_hay_n=false;
							for(char alphabet = 'a'; alphabet <= 'z';alphabet++) {

								if(alphabet=='o'&&ya_hay_n==false){
									alphabet--;
									ya_hay_n=true;
									Preguntas_Jugador1.add(BaseDeDatosPreguntas.obtenerpreguntasportipo(Tipo_pregunta.Todos, 'ñ'));
									Preguntas_Jugador2.add(BaseDeDatosPreguntas.obtenerpreguntasportipo(Tipo_pregunta.Todos, 'ñ'));
								}
								else{
									Preguntas_Jugador1.add(BaseDeDatosPreguntas.obtenerpreguntasportipo(Tipo_pregunta.Todos, alphabet));
									Preguntas_Jugador2.add(BaseDeDatosPreguntas.obtenerpreguntasportipo(Tipo_pregunta.Todos, alphabet));
								}
							}
							System.out.println("Encontrado emparejamiento");
							out.writeObject("Ok");
							out2.writeObject("Ok");
							out.writeObject("Turno");
							out2.writeObject("No turno");
							boolean Rendirse_jugador_1=false;
							boolean Todas_respondidas_jugador_1=false;
							boolean Rendirse_jugador_2=false;
							boolean Todas_respondidas_jugador_2=false;
							boolean Tiene_Turno_j1=true;
							int Preguntas_bien_jugador_1=0;
							int Preguntas_mal_jugador1=0;
							String Nombre_j1=in.readLine();

							int Preguntas_bien_jugador_2=0;
							String Nombre_j2=in2.readLine();
							out.writeObject(Nombre_j1);

							out2.writeObject(Nombre_j2);
							int Preguntas_mal_jugador2=0;
							while((Todas_respondidas_jugador_1==false&&Rendirse_jugador_1==false)||(Todas_respondidas_jugador_2==false&&Rendirse_jugador_2==false)){

								for(int i=0;i<Preguntas_Jugador1.size();i++) {
									if(!Preguntas_Jugador1.get(i).respondida==true){
										dato=in.readLine();
										if (!"OK".equals(dato)) throw new IOException( "Conexión errónea: " + dato );

										out.writeObject(Preguntas_Jugador1.get(i).Pregunta);

										out.writeObject(Preguntas_Jugador1.get(i).letra);
										dato=in.readLine();
										System.out.println(dato+" respuesta del usuario");
										if(dato.equals("Pasapalabra")){
											out.writeObject("Ok");
										}
										else if(dato.equals("Rendirse")){
											if(Tiene_Turno_j1){
												Rendirse_jugador_1=true;

												i=Preguntas_Jugador1.size();
												for(int j=0;j<Preguntas_Jugador1.size();j++){
													if(Preguntas_Jugador1.get(j).respondida==false){
														Preguntas_mal_jugador1++;
													}
												}
											}else{
												Rendirse_jugador_2=true;

												i=Preguntas_Jugador2.size();
												for(int j=0;j<Preguntas_Jugador2.size();j++){
													if(Preguntas_Jugador2.get(j).respondida==false){
														Preguntas_mal_jugador2++;
													}
												}	
											}
										}
										else if(dato.equalsIgnoreCase(Preguntas_Jugador1.get(i).Respuesta)){
											if(Tiene_Turno_j1){
												Preguntas_bien_jugador_1++;
												Preguntas_Jugador1.get(i).respondida=true;
												out.writeObject("Bien");
											}else{
												Preguntas_bien_jugador_2++;
												Preguntas_Jugador2.get(i).respondida=true;
												out.writeObject("Bien");
											}
										}
										else{
											if(Tiene_Turno_j1){
												Preguntas_mal_jugador1++;
												Preguntas_Jugador1.get(i).respondida=true;
												out.writeObject("Mal");
												i=Preguntas_Jugador1.size();
												Tiene_Turno_j1=false;
											}
											else{
												Preguntas_mal_jugador2++;
												Preguntas_Jugador2.get(i).respondida=true;
												out.writeObject("Mal");
												i=Preguntas_Jugador2.size();

												Tiene_Turno_j1=true;
											}
										}
										Todas_respondidas_jugador_1=TodasRespondidas(Preguntas_Jugador1);
										Todas_respondidas_jugador_2=TodasRespondidas(Preguntas_Jugador2);
										System.out.println("Todas respondidas: "+Todas_respondidas_jugador_1);

									}

								}
							}
							out.reset();
							out.writeObject("Fin");
							System.out.println("Despues de enviar fin, envío las preguntas");
							out.writeObject(Preguntas_bien_jugador_1);
							System.out.println("Preguntas bien enviadas");
							out.writeObject(Preguntas_mal_jugador1);
							System.out.println("Preguntas mal enviadas");
							addMensaje("Juego acabado");

							return;
							//in.readLine();
							//if (!"Ok".equals(dato)) throw new IOException( "Conexión errónea: " + dato );
						}
						else{
							out.writeObject("Error");
							return;
						}
					}
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
						Amigos_usuarios.remove(Datos_Usuario.get(0));
						out.writeObject("BIEN");
						addMensaje("Eliminado con éxito\n");
					}catch (SQLException e) {
						out.writeObject("SQL");
					}
					break;
				case "Delog":
					try{
						dato=in.readLine();
						Clientes_conectados.remove(dato);
						out.writeObject("BIEN");
					}catch (Exception e) {
						out.writeObject("ERROR");
					}
					break;
				case "Amigos":
					dato=in.readLine();
					out.writeObject("ACK");
					String nombre_Usuario=dato;
					if(Amigos_usuarios.get(nombre_Usuario).size()>0){

						out.writeObject("OK");
						//						for (int i=0;i<Amigos_usuarios.get(dato).size();i++) {
						//							try{
						//								System.out.println(Amigos_usuarios.get(dato).get(i).Nombre_usuario);
						//								BaseDeDatosUsuarios.nuevoUsuario(Amigos_usuarios.get(dato).get(i).Nombre_usuario, "0", "0", new Date(), "0");
						//								BaseDeDatosUsuarios.eliminar_Usuario(Amigos_usuarios.get(dato).get(i).Nombre_usuario, "0");
						//								Amigos_usuarios.get(dato).remove(i);
						//							}catch(Exception a){
						//								a.printStackTrace();
						//							}
						//						}

						for (int i=0;i<Amigos_usuarios.get(nombre_Usuario).size();i++) {
							if(Clientes_conectados.contains(Amigos_usuarios.get(nombre_Usuario).get(i).Nombre_usuario)){
								Amigos_usuarios.get(nombre_Usuario).get(i).setEstado_amigo(true);
							}
							else{
								Amigos_usuarios.get(nombre_Usuario).get(i).setEstado_amigo(false);
							}
							out.writeObject(Amigos_usuarios.get(nombre_Usuario).get(i).Nombre_usuario);
							out.writeObject(Amigos_usuarios.get(nombre_Usuario).get(i).Solicitud_aceptada);
							out.writeObject(Amigos_usuarios.get(nombre_Usuario).get(i).Solicitud_enviada);
							out.writeObject(Amigos_usuarios.get(nombre_Usuario).get(i).Solicitud_pendiente);
							out.writeObject(Amigos_usuarios.get(nombre_Usuario).get(i).Estado_amigo);
							addMensaje("Le envío: "+Amigos_usuarios.get(nombre_Usuario).get(i)+"\n");
							dato = in.readLine();
							if (!"ACK".equals(dato)) throw new IOException( "Conexión errónea: " + dato );
						}
						out.writeObject("FIN");
					}else{
						out.writeObject("NADIE");
					}

					break;
				case"Add_Amigo":
					dato=in.readLine();
					String amigo="";
					if(Amigos_usuarios.containsKey(dato)){
						amigo=dato;
						out.writeObject("OK");
						dato=in.readLine();
						if(dato.equals("OK")){
							//TODO: "enviar" solicitud
							dato=in.readLine();

							try{
								if(!Amigos_usuarios.get(dato).contains(new Tipo_Amigo(amigo, false, false, false))){
									Amigos_usuarios.get(dato).add(new Tipo_Amigo(amigo, false, true, true));


									Amigos_usuarios.get(amigo).add(new Tipo_Amigo(dato, false, false, true));
									out.writeObject("OK");
								}
								else {
									out.writeObject("REPE");	
								}
							}catch(Exception a){
								System.out.println("Error");
								a.printStackTrace();
							}

							//Amigos_usuarios.put(dato, new Tipo_Amigo(dato, false, true, true));

						}
						else if(dato.equals("NO_OK")){
							//NADA
						}
					}
					else{
						out.writeObject("NO_CONTIENE");
					}
					break;
				case "Acept_Amigo":
					dato=in.readLine();
					String amigo2=dato;


					out.writeObject("OK");
					dato=in.readLine();
					if(dato.equals("OK")){
						//TODO: "enviar" solicitud
						dato=in.readLine();

						try{

							Amigos_usuarios.get(dato).remove(amigo2);
							Amigos_usuarios.get(amigo2).remove(dato);


							Amigos_usuarios.get(dato).add(new Tipo_Amigo(amigo2, true, false, false));


							Amigos_usuarios.get(amigo2).add(new Tipo_Amigo(dato, true, false, false));

						}catch(Exception a){
							System.out.println("Error");
							a.printStackTrace();
						}

						//Amigos_usuarios.put(dato, new Tipo_Amigo(dato, false, true, true));
						out.writeObject("OK");
					}

					break;
				case "Delete_Amigo":
					dato=in.readLine();
					String amigo3=dato;


					out.writeObject("OK");
					dato=in.readLine();
					if(dato.equals("OK")){
						//TODO: "enviar" solicitud
						dato=in.readLine();

						try{

							Amigos_usuarios.get(dato).remove(amigo3);


							Amigos_usuarios.get(amigo3).remove(dato);

							out.writeObject("OK");

						}catch(Exception a){
							out.writeObject("NO_CONTIENE");
						}

						//Amigos_usuarios.put(dato, new Tipo_Amigo(dato, false, true, true));

					}

					break;
				case "DeJuego":
					dato=in.readLine();
					System.out.println(dato);
					if(MatchMaking.containsKey(dato)){
						MatchMaking.get(dato).remove(0);
					}
					else{


					}
					break;
				default:
					break;
				}
				//				for (Map.Entry<String,ArrayList<Tipo_Amigo>> entry : Amigos_usuarios.entrySet()) {
				//					String key = entry.getKey();
				//					System.out.println("El usuario "+ key+" tiene: "+entry.getValue().toString());
				//				}
				escribeAFichero();
				//				for (Map.Entry<String,ArrayList<Tipo_Amigo>> entry : Amigos_usuarios.entrySet()) {
				//					String key = entry.getKey();
				//					System.out.println("El usuario "+ key+" tiene: "+entry.getValue().toString());
				//				}
				Datos_Usuario.removeAll(Datos_Usuario);
				out.reset();

				accion = in.readLine();

				if (!"END".equals(accion)) throw new IOException( "Conexión errónea: respuesta  inesperada: " + accion );
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

	@SuppressWarnings("unchecked")
	public static void leeDeFichero() {
		try {	//Nombre del archivo
			FileInputStream fin = new FileInputStream("Amigos.amg"); 
			ObjectInputStream ois = new ObjectInputStream(fin);
			Amigos_usuarios = ( TreeMap<String, ArrayList<Tipo_Amigo>> ) ois.readObject();//La extructura a cargar
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void escribeAFichero() {
		try {	//Nombre del archivo
			FileOutputStream fout = new FileOutputStream("Amigos.amg");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject( Amigos_usuarios);//Lo que se va a guardar
			oos.close();
		} catch (IOException e)
		{  
			e.printStackTrace();
			System.out.println("No se han podido cargar los datos");
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

	public static boolean TodasRespondidas(ArrayList<Preguntas> aPreguntas){
		for (Preguntas preguntas : aPreguntas) {

			if(preguntas.respondida==false){
				return false;
			}
		}
		return true;
	}
}
class Tipo_Amigo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1606968345161920544l;
	String Nombre_usuario;
	boolean Solicitud_aceptada;
	boolean Solicitud_enviada;
	boolean Solicitud_pendiente;
	boolean Estado_amigo=false;
	public Tipo_Amigo(String nombre,boolean solicitud_aceptada,boolean solicitud_enviada,boolean Solicitud_pendiente){
		this.Nombre_usuario=nombre;
		this.Solicitud_aceptada=solicitud_aceptada;
		this.Solicitud_enviada=solicitud_enviada;
		this.Solicitud_pendiente=Solicitud_pendiente;
	}
	public void setEstado_amigo(boolean estado_amigo) {
		Estado_amigo = estado_amigo;
	}
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Tipo_Amigo){
			return this.Nombre_usuario.equals(((Tipo_Amigo) arg0).Nombre_usuario);
		}
		else{
			return false;
		}
	}
	@Override
	public String toString() {
		return "Tipo_Amigo [Nombre_usuario=" + Nombre_usuario + ", Solicitud_aceptada=" + Solicitud_aceptada
				+ ", Solicitud_enviada=" + Solicitud_enviada + ", Solicitud_pendiente=" + Solicitud_pendiente
				+ ", Estado_amigo=" + Estado_amigo + "]";
	}

}