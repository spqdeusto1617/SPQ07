package utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import controllers.EventosAmigos;
import controllers.EventosJuego;
import controllers.EventosLogIn;
import controllers.EventosRegistro;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**Clase de utilidad para realizar la conexión del cliente con el servidor
 * @author Iván
 *Tiene un solo método(lanzaConexión) que sirve para realizar la conexión con el servidor, y hacerle consultas sobre lo que el cliente necesite. 
 */
public class Conexion_cliente {
	private static  Socket socket;//Socket para enviar la conexión al servidor
	private static  ObjectInputStream in;//Para recibir los datos del servidor
	private static PrintWriter out;//Para escribir los datos al servidor
	public static final String Ip_Local="127.0.0.1";//La ip local (para pruebas)
	public static ArrayList<String>Datos_Usuario=new ArrayList<>();//Un arraylist con todos los datos del cliente
	public static  ArrayList<Tipo_Amigo>Amigos_usuarios=new ArrayList<Tipo_Amigo>();
	public static String Pregunta="";
	public static String Respuesta="";
	public static boolean Acierto=false;
	public static boolean Acierto_rival=false;
	public static int Correctas=0;
	public static int Incorrectas=0;
	public static char Letra_Actual;
	public static char Letra_Actual_Rival='a';
	public static boolean Mi_Turno;
	public static boolean Ha_Respondido;
	public static String Nombre_j2;
	public static ServerSocket serverSocket;
	static PrintWriter out2 ;
	static	BufferedReader in2 ;
	static PrintWriter out3 ;
	static	BufferedReader in3 ;
	public static boolean Primera_vez=true;

	static int Contestadas_rival=0;

	public static boolean Soy_Servidor;


	/**Método de utilidad para poder lanzar la conexión con el servidor (réplica de la de Andoni Eguíluz del examen final)
	 * @param ip :la ip del servidor
	 * @param accion :la acción que se quiere realizar (es un ENUM, mirar la clase: Acciones_servidor para más info)
	 * @param datos_cliente : los datos que ell cliente quiere validar, cambiar...
	 * @throws IOException :si hay algún problema con el servidor
	 * @throws SQLException :si el usuario introduce algo mal
	 */
	public static void lanzaConexion( String ip, String accion,String[]datos_cliente) throws IOException, SQLException,SecurityException,SocketException{
		// Pide la dirección del servidor

		if (!ip.equals("")) {
			try {
				Object respuesta =null;

				if(!accion.equals("Obtener_Pregunta")&&!accion.equals("Responder_Pregunta")){
					// Realiza la conexión e inicializa los flujos de comunicación

					socket = new Socket(ip, 9898);
					socket.setSoTimeout(100000);
					in = new ObjectInputStream( socket.getInputStream() );

					out = new PrintWriter(socket.getOutputStream(), true);
					// Consume el Ack (confirmación = acknowledge) del servidor
					respuesta = in.readObject();
					if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
					// Envía el usuario

					out.println( accion );
					// Espera el Ack
					respuesta = in.readObject();
					if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: " + respuesta );
				}

				switch (accion) {
				case "Comprobar":

					do{

						respuesta=in.readObject();

						if(!"FIN".equals(respuesta)){
							EventosLogIn.aLNoticiasFicheros.add((File) respuesta);
						}
						out.println("ACK");

					}while(!"FIN".equals(respuesta));

					break;
				case "Crear_Usuario":



					for (String string : datos_cliente) {

						out.println(string);
						respuesta = in.readObject();
						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: " + respuesta );
					}
					out.println("FIN");
					respuesta = in.readObject();
					if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: " + respuesta );
					respuesta=in.readObject();

					if ("MAIL".equals(respuesta)){
						EventosRegistro.MailExiste=true;
						EventosRegistro.UsuarioExiste=false;	
					}
					else if("USER".equals(respuesta)){
						EventosRegistro.MailExiste=false;
						EventosRegistro.UsuarioExiste=true;
					}
					else if("BIEN".equals(respuesta)){
						EventosRegistro.MailExiste=false;
						EventosRegistro.UsuarioExiste=false;
					}
					else{
						throw new IOException("Error desconocido");
					}
					break;
				case "Log":

					for (String string : datos_cliente) {

						out.println(string);
						respuesta = in.readObject();

						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: " + respuesta );
					}

					out.println("FIN");
					respuesta = in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					respuesta=in.readObject();

					if("ACTIVO".equals(respuesta)){
						out.println( "END" );
						respuesta = in.readObject();
						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
						socket.close();
						throw new SecurityException("Usuario activo");
					}

					if("BIEN".equals(respuesta)){

						do{

							respuesta=in.readObject();

							if(!"FIN".equals(respuesta)){
								Datos_Usuario.add((String) respuesta);
							}
							out.println("ACK");
						}while(!"FIN".equals(respuesta));

					}
					else if("SQL".equals(respuesta)){

						out.println( "END" );
						respuesta = in.readObject();
						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
						socket.close();
						throw new SQLException("Error en el username o contraseña");
					}
					break;
				case "Jugar":

					System.out.println("A jugar");
					out.println(datos_cliente[1]);


					respuesta=in.readObject();
					if(respuesta.equals("Nadie")){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Parece que no hay nadie");
						alert.setHeaderText("Actualmente no hay nadie conectado");
						alert.setContentText("Parece que actualmente no hay nadie conectado, por favor, espere a que reciba alguna respuesta");

						alert.initModality(Modality.APPLICATION_MODAL);

						alert.showAndWait();
						int portNumber = 9899;
						System.out.println("Al try");





						Soy_Servidor=true;
						System.out.println("Vamos a ver qué pasa");
						socket.close();
						@SuppressWarnings("resource")
						ServerSocket serverSocket = new ServerSocket(portNumber);
						serverSocket.setSoTimeout(30000);
						Socket clientSocket = serverSocket.accept();




						out2 =new PrintWriter(clientSocket.getOutputStream(), true);
						in2 = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

						respuesta=in2.readLine();
						if(respuesta.equals("Encontrado")){

							System.out.println("Encontrado");
						}
						else{
							throw new SecurityException("Error al buscar");
						}
						respuesta=in2.readLine();
						System.out.println("Respuesta del servidor "+respuesta);
						if(respuesta.equals("Ok")){


						}
						else if(respuesta.equals("Error")){
							throw new SecurityException("Error al leer el tipo de pregunta");
						}
						respuesta=in2.readLine();
						if(respuesta.equals("Turno")){
							Mi_Turno=true;
							System.out.println("Tengo turno");
						}
						else if(respuesta.equals("No turno")){
							Mi_Turno=false;
							System.out.println("No Tengo turno");
						}
						else{
							throw new SecurityException("Error en el turno");
						}
						out2.println(Datos_Usuario.get(0));

						Nombre_j2= in2.readLine();
						System.out.println("Nombre del 2º jugador"+Nombre_j2);
						System.out.println("Y a jugar");
						return;
					}

					if(respuesta.equals("Encontrado")){

						System.out.println("Encontrado");
					}
					else{
						throw new SecurityException("Error al buscar");
					}
					respuesta=in.readObject();
					if(respuesta.equals("Ok")){


					}
					else if(respuesta.equals("Error")){
						throw new SecurityException("Error al leer el tipo de pregunta");
					}
					respuesta=in.readObject();
					if(respuesta.equals("Turno")){
						Mi_Turno=true;

					}
					else if(respuesta.equals("No turno")){
						Mi_Turno=false;
					}
					else{
						throw new SecurityException("Error en el turno");
					}
					out.println(Datos_Usuario.get(0));
					System.out.println(Datos_Usuario.get(0)+" nombre j1");
					Nombre_j2=(String) in.readObject();
					System.out.println(Nombre_j2+" nombre j2");
					System.out.println("Y a jugar");

					return;


				case "Obtener_Pregunta":
					if(Soy_Servidor){

						if(Mi_Turno){

							if(Primera_vez){
								try{
									@SuppressWarnings("resource")

									ServerSocket serverSocket2 = new ServerSocket(80);
									serverSocket2.setSoTimeout(10000);
									System.out.println("Esperando conexión");
									Socket clientSocket2 = serverSocket2.accept();
									System.out.println("Conexión encontraada");
									out3 =new PrintWriter(clientSocket2.getOutputStream(), true);
									in3 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
									Primera_vez=false;
								}catch(Exception a){
									a.printStackTrace();
								}
							}
							try{
								respuesta= in3.readLine();
								System.out.println(respuesta+" respuesta servidor");
								if(!respuesta.equals("Fin")){

									Pregunta=(String) respuesta;
									respuesta=in3.readLine();
									Letra_Actual= respuesta.toString().charAt(0);
									System.out.println("Letra: "+Letra_Actual);
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Respuesta="...........................";
									//Letra_Actual= respuesta.toString().charAt(0);

									return;
								}


								System.out.println("A acabar en pregunta");
								Correctas=(int) in.readObject();


								Incorrectas=(int) in.readObject();

								EventosJuego.juegoEnCurso=false;
							}catch (SocketException e) {
								System.out.println("Al catch");
								e.printStackTrace();
								EventosJuego.juegoEnCurso=false;
							}

						}else{

							System.out.println("Soy el servidor y espero respuesta del servidor de verdad");
							respuesta=in2.readLine();
							System.out.println("Respuesta del servidor de verdad: "+respuesta);
							out2.println("OK");Contestadas_rival++;
							if(("J1-Mal").equals(respuesta)){

								Ha_Respondido=true;
								Acierto_rival=false;
								out2.println("Ok");
								respuesta=in2.readLine();
								Letra_Actual_Rival=respuesta.toString().charAt(0);

							}
							else if(("Fin").equals(respuesta)){
								EventosJuego.juegoEnCurso=false;
								System.out.println("El juego ya no está en curso");
							}
							else if(("Fin_j1").equals(respuesta)){
								Mi_Turno=true;
								System.out.println("Mi turno");
							}
							else if(("J1-Bien").equals(respuesta)){
								Acierto_rival=true;
								Ha_Respondido=true;
								out2.println("Ok");
								respuesta=in2.readLine();
								System.out.println("Respuesta de la letra:"+respuesta);
								Letra_Actual_Rival= respuesta.toString().charAt(0);

							}

							else if(("Pasa").equals(respuesta)){
								Acierto_rival=true;
								Ha_Respondido=false;
								out2.println("OK");
								respuesta=in2.readLine();
								System.out.println("Respuesta de la letra:"+respuesta);
								Letra_Actual_Rival= respuesta.toString().charAt(0);
								Contestadas_rival--;

							}
						}if(Contestadas_rival==27){Mi_Turno=true;
						System.out.println("Ha respondido 27 y por tanto me toca");}
					}
					else{
						if(Mi_Turno){

							try{
								out.println("OK");//FIXME: ¿por qué da Socket exception?
								Primera_vez=false;
								respuesta= in.readObject();
								System.out.println(respuesta);
								if(!respuesta.equals("Fin")){
									if(respuesta.equals("Fin_J1")){
										Mi_Turno=false;

									}
									else{
										Pregunta=(String) respuesta;
										respuesta=in.readObject();
										Letra_Actual=(char) respuesta;
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										Respuesta="...........................";
									}
									return;
								}


								System.out.println("A acabar en pregunta");
								Correctas=(int) in.readObject();


								Incorrectas=(int) in.readObject();

								EventosJuego.juegoEnCurso=false;
							}catch (SocketException e) {
								System.out.println("Al catch");
								e.printStackTrace();
								EventosJuego.juegoEnCurso=false;
							}
						}else{

							System.out.println("Soy el cliente y espero respuesta del servidor de verdad");
							respuesta=in.readObject();
							System.out.println("Respuesta del servidor de verdad: "+respuesta);
							out.println("OK");Contestadas_rival++;
							if(("J2-Mal").equals(respuesta)){

								Ha_Respondido=true;
								Acierto_rival=false;
								out.println("Ok");
								respuesta=in.readObject();
								Letra_Actual_Rival=respuesta.toString().charAt(0);

							}
							else if(("Fin").equals(respuesta)){
								EventosJuego.juegoEnCurso=false;
								System.out.println("El juego ya no está en curso");
							}

							else if(("J2-Bien").equals(respuesta)){
								Acierto_rival=true;
								Ha_Respondido=true;
								out.println("Ok");
								respuesta=in.readObject();
								System.out.println("Respuesta de la letra:"+respuesta);
								Letra_Actual_Rival= respuesta.toString().charAt(0);

							}

							else if(("Pasa").equals(respuesta)){
								Acierto_rival=true;
								Ha_Respondido=false;

								//respuesta=in.readObject();
								System.out.println("Respuesta de la letra:"+respuesta);
								Letra_Actual_Rival= respuesta.toString().charAt(0);
								Contestadas_rival--;

							}
						}
						//out.println("Ok");
						//FIXME: Software caused connection abort ¿por qué da esto?

					}return;


				case "Responder_Pregunta":
					if(!Mi_Turno){
						System.out.println("No es mi turno");
						return;
					}
					else{
						if(Soy_Servidor){
							out3.println(Respuesta);

							respuesta=in3.readLine();
							System.out.println("La respuesta es: "+respuesta);
							if(!respuesta.equals("Fin")){
								if(respuesta.equals("Bien")){
									Acierto=true;
									Correctas++;
								}
								else if("Mal".equals(respuesta)){
									Acierto=false;
									Incorrectas++;
								}
								else if("Ok".equals(respuesta)){

								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Respuesta="";
								return;
							}
						}
						else{
							out.println(Respuesta);

							respuesta=in.readObject();

							if(!respuesta.equals("Fin")){
								if(respuesta.equals("Bien")){
									Acierto=true;
									Correctas++;
								}
								else if("Mal".equals(respuesta)){
									Acierto=false;
									Incorrectas++;

								}
								else if("Ok".equals(respuesta)){
									System.out.println("Pasapalabra");
								}
								
								return;
							}
							System.out.println("A acabar en respuesta");
							Correctas=(int) in.readObject();
							Incorrectas=(int) in.readObject();
							EventosJuego.juegoEnCurso=false;

							//					System.out.println("Fin");
							//					EventosJuego.juegoEnCurso=false;
							//					respuesta=in.readObject();
							//					if (!"Ok".equals(respuesta)) throw new IOException( "Conexión errónea: " + Respuesta );
							//					Correctas=(int) in.readObject();
							//					Incorrectas=(int) in.readObject();
							//					System.out.println(Correctas+" Correctas "+Incorrectas +" Incorrectas");
							//					out.println("Ok");
							return;
						}

					}
				case "Imagen":


					respuesta=in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					out.println(datos_cliente[0]);
					respuesta=in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					out.println(datos_cliente[3]);
					respuesta=in.readObject();
					if(!"OK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					break;


				case "Pass":	
					respuesta=in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					out.println(datos_cliente[0]);
					respuesta=in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					out.println(datos_cliente[2]);
					respuesta=in.readObject();
					if(!"OK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					break;


				case "Mail":
					respuesta=in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					out.println(datos_cliente[0]);
					respuesta=in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					out.println(datos_cliente[1]);
					respuesta=in.readObject();
					if(!"OK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );



					break;
				case "Amigos":
					out.println(datos_cliente[0]);
					respuesta=in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					respuesta=in.readObject();

					if(respuesta.equals("NADIE")){
						EventosAmigos.TieneAmigos=false;
					}
					else{
						try{
							do{
								respuesta=in.readObject();
								EventosAmigos.TieneAmigos=true;

								System.out.println(respuesta);
								if(!"FIN".equals(respuesta)){
									String nombre_Usuario=(String) respuesta;
									boolean aceptada=(boolean) in.readObject();
									boolean enviada=(boolean) in.readObject();
									boolean pendiente=(boolean) in.readObject();
									boolean estado=(boolean) in.readObject();
									if(aceptada==false&&enviada==false){
										nombre_Usuario=nombre_Usuario+" (Pendiente)";
									}
									else if(aceptada==false&&enviada==true){
										nombre_Usuario=nombre_Usuario+" (Enviada)";
									}
									Tipo_Amigo tipo=new Tipo_Amigo(nombre_Usuario, aceptada, enviada, pendiente,estado);
									Amigos_usuarios.add(tipo);
									out.println("ACK");
								}


							}while(!"FIN".equals(respuesta));
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				case "Estadisticas":

					break;
				case "Eliminar":
					for (String string : datos_cliente) {

						out.println(string);
						respuesta = in.readObject();

						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: " + respuesta );
					}
					out.println("FIN");
					respuesta = in.readObject();
					if(!"ACK".equals(respuesta))throw new IOException( "Conexión errónea: " + respuesta );
					respuesta = in.readObject();
					if("BIEN".equals(respuesta)){

					}
					else if("SQL".equals(respuesta)){
						out.println( "END" );
						respuesta = in.readObject();
						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
						socket.close();
						throw new SQLException("Error en el username o contraseña");
					}
					break;
				case "Delog":
					out.println(datos_cliente[0]);
					Datos_Usuario.removeAll(Datos_Usuario);
					respuesta = in.readObject();
					if (!"BIEN".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
					break;
				case"Add_Amigo":
					out.println(datos_cliente[0]);
					respuesta=in.readObject();
					if(respuesta.equals("OK")){
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Usuario encontrado");
						alert.setHeaderText(datos_cliente[0]+" encontrado");
						alert.setContentText("¿Quieres enviar la solicitud?");

						Optional<ButtonType> result = alert.showAndWait();
						if (result.get() == ButtonType.OK){
							out.println("OK");
							out.println(Datos_Usuario.get(0));
							respuesta=in.readObject();

							if(respuesta.equals("OK")){

							}
							else if(respuesta.equals("REPE")){
								out.println( "END" );
								respuesta = in.readObject();
								if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
								socket.close();
								throw new IOException("Solicitud ya enviada");
							}
						} else {
							out.println("NO_OK");
						}
					}
					else if(respuesta.equals("NO_CONTIENE")){
						out.println( "END" );
						respuesta = in.readObject();
						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
						socket.close();
						throw new SQLException("No existe ese usuario");
					}
					else throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
					break;
				case "Acept_Amigo":
					out.println(datos_cliente[0]);
					respuesta=in.readObject();
					if(respuesta.equals("OK")){


						out.println("OK");
						out.println(Datos_Usuario.get(0));
						respuesta=in.readObject();

						if(!respuesta.equals("OK")){
							out.println( "END" );
							respuesta = in.readObject();
							if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
							socket.close();
							throw new IOException("Error al realizar los cambios");
						}


					}
					else if(respuesta.equals("NO_CONTIENE")){
						out.println( "END" );
						respuesta = in.readObject();
						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
						socket.close();
						throw new SQLException("No existe ese usuario");
					}
					break;
				case "Delete_Amigo":
					out.println(datos_cliente[0]);
					respuesta=in.readObject();
					if(respuesta.equals("OK")){


						out.println("OK");
						out.println(Datos_Usuario.get(0));
						respuesta=in.readObject();

						if(!respuesta.equals("OK")){
							out.println( "END" );
							respuesta = in.readObject();
							if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
							socket.close();
							throw new IOException("Error al realizar los cambios");
						}


					}
					else if(respuesta.equals("NO_CONTIENE")){
						out.println( "END" );
						respuesta = in.readObject();
						if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
						socket.close();
						throw new SQLException("No existe ese usuario");
					}
					break;

				case "DeJuego":
					out.println(datos_cliente[1]);
					break;
				default:
					break;
				}
				//Procedimiento para finalizar la conexión

				out.println( "END" );
				respuesta = in.readObject();
				if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
				socket.close();
				if(EventosRegistro.MailExiste==true||EventosRegistro.UsuarioExiste==true)throw new IOException();

			} catch (ClassNotFoundException e) {  // Error en lectura de objeto
				throw new IOException( "Conexión errónea: lectura de elemento incorrecto desde el servidor" );
			}
		}else{
			throw new IOException("Ip no válida");
		}
	}
}