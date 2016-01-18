package controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidades.Conexion_cliente;

/**Clase que gestiona los eventos de la clase LogIn.fxml
 * @author asier.gutierrez
 *
 */
public class EventosLogIn extends Control implements Initializable {
	//Se define un logger
	public static Logger log = utilidades.AppLogger.getWindowLogger(EventosLogIn.class.getName());
	
	public static Image iAvatar;
	
	private boolean servidorOperativo = true;
	
	@FXML
	private Button btnLogin;
	
	@FXML
	private Label txtIncorrecto;
	
	@FXML
	private CheckBox checkRecordar;
	
	@FXML
	public Pagination pagNoticias;
	public void setPagNoticias(Pagination pagNoticias) {
		this.pagNoticias = pagNoticias;
	}
	
	@FXML
	private PasswordField txtContra;
	
	@FXML
	private TextField txtUsuario;
	
	@FXML
	private ImageView estadoServidor;
	
	@FXML
	private Polygon fuPoly;
	
	@FXML
	private Text forkUs;
	
	@FXML
	private Label lblRegistrar;
	
	private String[]Datos_cliente=new String[2];
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Se inicia el handler del logger
		utilidades.AppLogger.crearLogHandler(log, EventosLogIn.class.getName());
		try{
			log.log(Level.FINE, "Se ha inicializado EventosLogIn.java. Intento de conexión con servidor...");
			//Empieza conexión
			utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local, utilidades.Acciones_servidor.Comprobar.toString(),null);
			log.log(Level.INFO, "Conexión iniciada.");
		}catch(Exception a){
			log.log(Level.WARNING, "No se ha podido establecer conexión con el servidor", a);
			//Alertamos al usuario de que no hay conexión con servidor
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Parece que algo ha salido mal");

			alert.setContentText("Parece que el servidor no está operativo actualmente, por favor, inténtelo de nuevo más tarde");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
			
			//El servidor no está operativo.
			servidorOperativo = false;
			//No se puede hacer login.
			btnLogin.setDisable(true);
			//El estado del servidor se define gráficamente con una imagen.
			estadoServidor.setImage(new Image("images/desconectado.png"));
		}
	}
	
	/**Método para hacer login
	 * @param event Evento que hace ejecutar el método.
	 */
	public void loginSession(MouseEvent event){
		log.log(Level.FINEST, "Evento de LogIn");
		//Se controlan todos los datos.
		if(txtUsuario.getText().length()>0&&txtContra.getText().length()>0){
		Datos_cliente[0]=txtUsuario.getText();
		Datos_cliente[1]=txtContra.getText();
		//txtIncorrecto.setText("Usuario y/o contraseña incorrecto/s");
		try {
			utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local, utilidades.Acciones_servidor.Log.toString(), Datos_cliente);
			if(utilidades.Conexion_cliente.Datos_Usuario.get(5)!=null){
				java.io.File file = new java.io.File(utilidades.Conexion_cliente.Datos_Usuario.get(5));
				if(file.exists()){
					iAvatar=new Image("file:///"+utilidades.Conexion_cliente.Datos_Usuario.get(5));
				}
				else{
					//TODO: hacer algo //EDIT: PONER LA IMAGEN POR DEFECTO
				}
				
			}
			
				String Partidas_Ganadas=Conexion_cliente.Datos_Usuario.get(6);
				String Partidas_Empatadas=Conexion_cliente.Datos_Usuario.get(8);
				String Partidas_Perdidas=Conexion_cliente.Datos_Usuario.get(7);
				
				int Partidas_Jugadas_Totales=Integer.parseInt(Partidas_Empatadas.substring(0))+Integer.parseInt(Partidas_Ganadas.substring(0))+Integer.parseInt(Partidas_Perdidas.substring(0));
				String Partidas_totates=Integer.toString(Partidas_Jugadas_Totales);
				String Pos_Ranking=Conexion_cliente.Datos_Usuario.get(9);
				EventosEstadisticas.Datos_Usuario_Estadisticas.add(Partidas_Ganadas);
				EventosEstadisticas.Datos_Usuario_Estadisticas.add(Partidas_Perdidas);
				EventosEstadisticas.Datos_Usuario_Estadisticas.add(Partidas_Empatadas);
				EventosEstadisticas.Datos_Usuario_Estadisticas.add(Partidas_totates);
				EventosEstadisticas.Datos_Usuario_Estadisticas.add(Pos_Ranking);
			log.log(Level.FINEST, "LogIn OK. Transición de ventana a Juego");
			utilidades.deVentana.transicionVentana("Juego", event);
		} catch (SQLException e) {
			log.log(Level.INFO, "Error de LogIn", e);
			//Aviso
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Usuario o contraseña incorrectos");

			alert.setContentText("El usuario y/o la contraseña que has introducido son incorrectos, por favor, revise los datos y vuelva a intentarlo");
			alert.initModality(Modality.APPLICATION_MODAL);		
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			txtContra.setText("");
		}catch (IOException e) {
			log.log(Level.INFO, "Error de LogIn", e);
			//Aviso
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al conectarse con el servidor");

			alert.setContentText("Parece que ha habido un problema a la hora de extablecer conexión con el servidor, por favor inténtelo de nuevo más tarde");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			txtContra.setText("");
		}catch (SecurityException e) {
			log.log(Level.INFO, "Error de LogIn", e);
			//Aviso
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Usuario conectado actualmente");

			alert.setContentText("Parece que su usuario ya está conectado en otro dispositivo. Si cree que esto es un error, o desconoce por qué está pasando, contacte con los administradores lo antes posible");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			txtContra.setText("");
	
		
		}
		}
		else{
			log.log(Level.INFO, "Error de LogIn - El usuario no ha insertado nada.");
			//Si el usuario no ha insertado nada en los campos de login se le avisará.
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Texto vacio");

			alert.setContentText("Parece que no has introducido ningún texto, por favor, intente introducir su usuario y contraseña antes");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
		}
	}
	
	/**Método para ir a la pantalla de registro siempre y cuando el servidor esté operativo.
	 * @param event Evento de ratón que hace llamar al método
	 */
	public void registro(MouseEvent event){
		log.log(Level.FINEST, "Método de registro iniciado");
		if( servidorOperativo == false ){
			log.log(Level.INFO, "El servidor no está operativo para el registro");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("El servidor no está operativo");

			alert.setContentText("El servidor no está operativo actualmente, no se puede registrar");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
		}
		else{
			log.log(Level.FINEST, "Transición a ventana registro porque el servidor está operativo.");
			utilidades.deVentana.transicionVentana("Registro", event);
		}
	}
	
	/**Método para abrir el navegador en la dirección de GitHub del proyecto
	 * @param event Evento de ratón que hace llamar al método.
	 */
	public void irAGitHub(MouseEvent event){
		log.log(Level.FINEST, "Ir a GitHub");
		try {
			//Desktop-getea el desktop-navega en-nueva URL-URL-a URI
	        Desktop.getDesktop().browse(new URL("https://github.com/asier-gutierrez/Pasapalabra").toURI());
	        log.log(Level.INFO, "Se ha accedido sin problema a GitHub");
	    } catch (Exception e) {
	    	log.log(Level.WARNING, "Error en el método irAGitHub", e);
	        e.printStackTrace();
	    }
	}
}
