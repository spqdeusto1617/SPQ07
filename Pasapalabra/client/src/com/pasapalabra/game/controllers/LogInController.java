package com.pasapalabra.game.controllers;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.AccessControlException;
import java.util.ArrayList;
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
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**Class that manages events from LogIn.fxml
 * @author asier.gutierrez
 *
 */
public class LogInController extends Control implements Initializable {
	//Se define un logger

	public static ArrayList<Image> aLNoticias;

	public static ArrayList<File> aLNoticiasFicheros = new ArrayList<File>();
	
	static boolean serverNotFound;

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(LogInController.class.getName());

	public static Image iAvatar;

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


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Se inicia el handler del logger
		com.pasapalabra.game.utilities.AppLogger.crearLogHandler(log, LogInController.class.getName());
		if(!com.pasapalabra.game.service.ServiceLocator.serverReady){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No hay conexión con el servidor");

			alert.setContentText("Parece que el servidor no está disponible, por favor, inténtelo más tarde");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
			btnLogin.setDisable(true);
			estadoServidor.setImage(new Image("/images/desconectado.png"));
		}
		else btnLogin.setDisable(false);
		try{
			log.log(Level.FINE, "Se ha inicializado EventosLogIn.java. Intento de conexión con servidor...");
			//Empieza conexión
			//com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local, com.pasapalabra.game.utilities.Acciones_servidor.Comprobar.toString(),null);
			log.log(Level.INFO, "Conexión iniciada.");
			aLNoticias = new ArrayList<>();
			if((aLNoticiasFicheros != null) || (!aLNoticiasFicheros.isEmpty())){
				for (File archivoGrafico : aLNoticiasFicheros) {
					aLNoticias.add(new Image(archivoGrafico.toURI().toURL().toString()));
				}
				pagNoticias.setMaxPageIndicatorCount(4);
				pagNoticias.setPageCount(aLNoticias.size());
				pagNoticias.setPageFactory(new Callback<Integer, Node>() {
					@Override
					
					public Node call(Integer pageIndex) {
						return createPage(pageIndex, aLNoticias);
					}
				});
				
			}
		}catch(NullPointerException a){
			a.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Parece que algo ha salido mal");

			alert.setContentText("Parece que las noticias no están disponibles");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
		}catch (Exception e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Parece que algo ha salido mal");

			alert.setContentText("Parece que ha habido un error insesperado, podrá jugar de todos modos");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
			log.log(Level.SEVERE,"Error al iniciar:"+e.getMessage());
			e.printStackTrace();

		}
	}

	/**Método to log in to the game
	 * @param event
	 */
	public void loginSession(MouseEvent event){
		log.log(Level.FINEST, "Evento de LogIn");
		//Se controlan todos los datos.
		
		if(txtUsuario.getText().length()>0&&txtContra.getText().length()>0){
			//txtIncorrecto.setText("Usuario y/o contraseña incorrecto/s");
			try {	log.log(Level.FINEST, "LogIn OK. Comprobamos el login");
				com.pasapalabra.game.service.ServiceLocator.login(txtUsuario.getText(), txtContra.getText());
				
				//com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
			} catch (AccessControlException e) {
				log.log(Level.INFO, "Error de LogIn, user does not exist", e);
				//Aviso
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("User name or password incorrect");

				alert.setContentText("Your username and/or password are incorrect, please check them and try again");
				alert.initModality(Modality.APPLICATION_MODAL);	
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert.showAndWait();
				txtContra.setText("");
				return;
				

			} catch (RemoteException e) {
				log.log(Level.INFO, "Login error, connection not ok", e);
				//Aviso
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error accesing the server");

				alert.setContentText("An error ocurred while trying to connect to the server. Please, check your connection and try again");
				alert.initModality(Modality.APPLICATION_MODAL);	
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert.showAndWait();
				txtContra.setText("");
				e.printStackTrace();
				return;

			}catch (SecurityException e) {
				log.log(Level.INFO, "Login error, user does not exist", e);
				//Aviso
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Duplicated username");

				alert.setContentText("This username is already logged. If this is not correct, try to contact with an admin");
				alert.initModality(Modality.APPLICATION_MODAL);	
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert.showAndWait();
				txtContra.setText("");
				
				e.printStackTrace();
				return;
			}
			catch (Exception e) {
				log.log(Level.INFO, "Login error, unexpected error", e);
				//Aviso
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Unexpected error occured during the login");

				alert.setContentText("An unexpeced error occurred during the login, please try again");
				alert.initModality(Modality.APPLICATION_MODAL);	
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert.showAndWait();
				txtContra.setText("");
				e.printStackTrace();
				return;
			}
			try {	log.log(Level.FINEST, "LogIn OK. Transición de ventana a Juego");
			com.pasapalabra.game.service.ServiceLocator.retreiveUserData();
			
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
		} catch (SecurityException e) {
			log.log(Level.INFO, "Login Error, impossible to retreive your userdata", e);
			//Aviso
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Impossible to retreive your info");

			alert.setContentText("An error occured trying to retrevie your information, please try again");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			txtContra.setText("");
			

		} catch (RemoteException e) {
			log.log(Level.INFO, "Login error, connection not ok", e);
			//Aviso
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error accesing the server");

			alert.setContentText("An error ocurred while trying to download from the server. Please, check your connection and try again");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			txtContra.setText("");
	

		}
		catch (Exception e) {
			log.log(Level.INFO, "Error de LogIn, unexpected error", e);
			//Aviso
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unexpected error occured during the login");

			alert.setContentText("An unexpeced error occurred during the login, please try again");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			txtContra.setText("");
			e.printStackTrace();
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

	/**Method to go to the register window when the server is operative. 
	 * @param event of the mouse
	 */
	public void registro(MouseEvent event){
		log.log(Level.FINEST, "Método de registro iniciado");
		if( com.pasapalabra.game.service.ServiceLocator.serverReady == false ){
			log.log(Level.INFO, "El servidor no está operativo para el registro");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("El servidor no está operativo");

			alert.setContentText("El servidor no está operativo actualmente, no se puede registrar");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
		}
		else{
			log.log(Level.FINEST, "Transición a ventana registro porque el servidor está operativo.");
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("SignIn", event);
		}
	}

	/**Method to open the github page of the current project
	 * @param event of the mouse
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

	public Pane createPage(int pageIndex, ArrayList<Image> aLNoticias) {
		Pane pageBox = new Pane();
		try{
		ImageView iv = new ImageView(aLNoticias.get(pageIndex));
		iv.setX(0); iv.setY(0);
		iv.setFitHeight(300); iv.setFitWidth(300);
		pageBox.getChildren().add(iv);
		return pageBox;
		}catch(Exception a){
			return null;
		}
	}
}