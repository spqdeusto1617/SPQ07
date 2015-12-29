package principal;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**Clase del servidor (réplica de la de Andoni Eguíluz del examen final)
 * @author Iván
 *Aquí se crea el servidor, y una interfaz para gestionarlo
 */
public class Servidor {
	private static ServerSocket servidor = null;//El socket del servidor
	static boolean funcionando;//Si está el servidor funcionando
	private static JTextArea ListaMensajes = null;//Donde sale la info
	static ArrayList<String> Datos_Usuario=new ArrayList<>();//Los datos del cliente que solicita algo (habría que cambiarlo, porque si no, puede que accedan varios a la vez y fastidiarla
	static ArrayList<String>Clientes_conectados=new ArrayList<>();//Los clientes conectados
	static String[]Datos_Enviar_Usuario;//Los datos a enviar al usuario (si son varios)
	/**
	 * Hilo para iniciar el servidor
	 */
	public void inicio_servidor() {
		(new Thread() {
			@Override
			public void run() {
				try {
					funcionando = true;
					servidor = new ServerSocket(9898);

					while (funcionando) {



						Conexion_Servidor conexion=new Conexion_Servidor(servidor.accept());  

						ListaMensajes.append("Ha entrado un usuario nuevo\n");
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

				ListaMensajes.append("Quiere: "+accion+"\n");
				out.writeObject( "ACK" );
				switch (accion) {
				case "Comprobar":
					//TODO: terminar esto(faltan noticias)
					ListaMensajes.append("Comprobación realizada\n");
					break;
				case "Crear_Usuario":


					ListaMensajes.append("Empiezo a recibir la información\n");
					do{

						dato=in.readLine();
						ListaMensajes.append("Ha introducido: "+dato+"\n");
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

					ListaMensajes.append("Empiezo a recibir la información\n");

					do{

						dato=in.readLine();
						ListaMensajes.append("Ha introducido: "+dato+"\n");
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
							ListaMensajes.append("Le envío: "+string+"\n");
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

					ListaMensajes.append("Empiezo a recibir la información\n");


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

					ListaMensajes.append("Empiezo a recibir la información\n");
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
					ListaMensajes.append("Empiezo a recibir la información\n");
					do{

						dato=in.readLine();
						ListaMensajes.append("Ha introducido: "+dato+"\n");
						if(!"FIN".equals(dato)){
							Datos_Usuario.add(dato);
						}
						out.writeObject("ACK");

					}while(!"FIN".equals(dato));
					try{
						BaseDeDatosUsuarios.eliminar_Usuario(Datos_Usuario.get(0), Datos_Usuario.get(2));
						Clientes_conectados.remove(Datos_Usuario.get(0));
						out.writeObject("BIEN");
						ListaMensajes.append("Eliminado con éxito\n");
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

				ListaMensajes.append("Conexión finalizada\n");
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
	@SuppressWarnings("serial")
	private static class VentanaCaptura extends JFrame {
		public VentanaCaptura() {
			setTitle( "Pasapalabra - Servidor" );
			setSize( 600, 400 );
			setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			// Componentes
			ListaMensajes = new JTextArea( 8, 10 );
			ListaMensajes.setEditable(false);
			getContentPane().add( new JScrollPane(ListaMensajes), BorderLayout.SOUTH );
			// Escuchadores
			addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					Servidor.cierre_servidor();
					BaseDeDatosPreguntas.cerrarConexion();
					BaseDeDatosUsuarios.cerrarConexion();
				}
			} );

		}
	}

	public static void main(String[] args) {
		BaseDeDatosPreguntas.iniciarConexion();
		BaseDeDatosUsuarios.iniciarConexion();
		VentanaCaptura v=new VentanaCaptura();
		v.setVisible(true);
		Servidor s=new Servidor();
		s.inicio_servidor();
		ListaMensajes.append("Esperando conexión con algún cliente...\n");
		try { Thread.sleep(10); } catch (InterruptedException e) {}
	}
}
