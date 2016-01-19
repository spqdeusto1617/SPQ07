package utilidades;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;

import controllers.EventosAmigos;
import controllers.EventosJuego;
import controllers.EventosLogIn;
import controllers.EventosRegistro;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;


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
	public static int Correctas=0;
	public static int Incorrectas=0;
	public static char Letra_Actual;

	/**Método de utilidad para poder lanzar la conexión con el servidor
	 * @param ip :la ip del servidor
	 * @param accion :la acción que se quiere realizar (es un ENUM, mirar la clase: Acciones_servidor para más info)
	 * @param datos_cliente : los datos que ell cliente quiere validar, cambiar...
	 * @throws IOException :si hay algún problema con el servidor
	 * @throws SQLException :si el usuario introduce algo mal
	 */
	public static void lanzaConexion( String ip, String accion,String[]datos_cliente) throws IOException, SQLException,SecurityException,Error{
		// Pide la dirección del servidor

		if (!ip.equals("")) {
			try {
				Object respuesta =null;

				if(!accion.equals("Obtener_Pregunta")&&!accion.equals("Responder_Pregunta")){
					// Realiza la conexión e inicializa los flujos de comunicación
					System.out.println();
					socket = new Socket(ip, 9898);

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
					//TODO: terminar esto(faltan noticias)

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
					out.println(datos_cliente[1]);
					respuesta=in.readObject();
					if(respuesta.equals("Ok")){

						return;
					}
					else if(respuesta.equals("Error")){
						throw new SecurityException("Error al leer el tipo de pregunta");
					}
					break;
				case "Obtener_Pregunta":

					try{
						out.println("OK");//FIXME: ¿por qué da Socket exception?
						
						respuesta= in.readObject();
						
						if(!respuesta.equals("Fin")){

							Pregunta=(String) respuesta;
							respuesta=in.readObject();
							Letra_Actual=(char) respuesta;

							return;
						}


						System.out.println("A acabar en pregunta");
						Correctas=(int) in.readObject();


						Incorrectas=(int) in.readObject();

						EventosJuego.juegoEnCurso=false;
					}catch (SocketException e) {
						System.out.println("Al catch");
						
						EventosJuego.juegoEnCurso=false;
					}
					//out.println("Ok");
					//FIXME: Software caused connection abort ¿por qué da esto?
					return;
				case "Responder_Pregunta":

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


