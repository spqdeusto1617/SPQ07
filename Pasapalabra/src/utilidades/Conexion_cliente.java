package utilidades;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import controllers.EventosLogIn;
import controllers.EventosRegistro;

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

	/**Método de utilidad para poder lanzar la conexión con el servidor
	 * @param ip :la ip del servidor
	 * @param accion :la acción que se quiere realizar (es un ENUM, mirar la clase: Acciones_servidor para más info)
	 * @param datos_cliente : los datos que ell cliente quiere validar, cambiar...
	 * @throws IOException :si hay algún problema con el servidor
	 * @throws SQLException :si el usuario introduce algo mal
	 */
	public static void lanzaConexion( String ip, String accion,String[]datos_cliente) throws IOException, SQLException{
		// Pide la dirección del servidor

		if (!ip.equals("")) {
			try {

				// Realiza la conexión e inicializa los flujos de comunicación
				socket = new Socket(ip, 9898);

				in = new ObjectInputStream( socket.getInputStream() );

				out = new PrintWriter(socket.getOutputStream(), true);
				// Consume el Ack (confirmación = acknowledge) del servidor
				Object respuesta = in.readObject();
				if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
				// Envía el usuario

				out.println( accion );
				// Espera el Ack
				respuesta = in.readObject();
				if (!"ACK".equals(respuesta)) throw new IOException( "Conexión errónea: " + respuesta );
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
					respuesta = in.readObject();
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
					//TODO: terminar esto

					break;
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
				case "Estadisticas":
					//TODO: terminar esto
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
					respuesta = in.readObject();
					if (!"BIEN".equals(respuesta)) throw new IOException( "Conexión errónea: respuesta del servidor inesperada: " + respuesta );
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
