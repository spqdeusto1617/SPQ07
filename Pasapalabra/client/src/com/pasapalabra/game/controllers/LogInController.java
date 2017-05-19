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

import com.pasapalabra.game.service.ClientConnection;

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
	private ImageView imageNews; 

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
		if(!com.pasapalabra.game.service.ClientConnection.serverReady){
			log.log(Level.INFO, "Server offline");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No connection with the server");
			
			alert.setContentText("The server is currenlty offlile, please check your connection and try again");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
			btnLogin.setDisable(true);
			estadoServidor.setImage(new Image("/images/desconectado.png"));
		}
		else btnLogin.setDisable(false);
		try{
			
			log.log(Level.INFO, "Conenction started correctly");
			aLNoticias = new ArrayList<>();
			if((aLNoticiasFicheros != null) || (!aLNoticiasFicheros.isEmpty())){
				for (File archivoGrafico : aLNoticiasFicheros) {
					aLNoticias.add(new Image(archivoGrafico.toURI().toURL().toString()));
				}
				imageNews.setImage(new Image("/images/news.jpg"));
				pagNoticias.setMaxPageIndicatorCount(1);
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
			alert.setTitle("Something went wrong");

			alert.setContentText("Some error occured trying to retreive the news");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
			log.log(Level.SEVERE, "Error trying to retreive data from the server. ",a);
		}catch (Exception e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Something went wrong");

			alert.setContentText("Unespected error ocurred. you can play anyway");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
			log.log(Level.SEVERE, "Error trying to retreive data from the server. ",e);

		}
	}

	/**Método to log in to the game
	 * @param event
	 */
	public void loginSession(MouseEvent event){
		log.log(Level.FINEST, "Login event");
		//Se controlan todos los datos.
		
		if(txtUsuario.getText().length()>0&&txtContra.getText().length()>0){
			//txtIncorrecto.setText("Usuario y/o contraseña incorrecto/s");
			try {	log.log(Level.FINEST, "Checking credentials");
				com.pasapalabra.game.service.ClientConnection.login(txtUsuario.getText(), txtContra.getText());
				
				//com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
			} catch (AccessControlException e) {
				log.log(Level.SEVERE, "LoginError, user does not exist", e);
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
				log.log(Level.SEVERE, "Login error, impossible to connect to the server", e);
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
				log.log(Level.SEVERE, "Login error, duplicated username", e);
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
				log.log(Level.SEVERE, "Login error, unexpected error", e);
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
			try {	log.log(Level.FINEST, "LogIn OK. Retreiving userdata");
			com.pasapalabra.game.service.ClientConnection.retreiveUserData();
			
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
		} catch (SecurityException e) {
			log.log(Level.SEVERE, "Login Error, impossible to retreive your userdata", e);
			//Aviso
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Impossible to retreive your info");

			alert.setContentText("An error occured trying to retrevie your information, please try again");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			txtContra.setText("");
			

		} catch (RemoteException e) {
			log.log(Level.SEVERE, "Error retreiving userdata, connection not ok", e);
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
			log.log(Level.SEVERE, "Error retreiving userdata, unexpected error", e);
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
			log.log(Level.INFO, "Login error - User tried to login without data.");
			//Si el usuario no ha insertado nada en los campos de login se le avisará.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Empty username/password");

			alert.setContentText("No username/password introduced, please introduce userdata and try again");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
		}
	}

	/**Method to go to the register window when the server is operative. 
	 * @param event of the mouse
	 */
	public void signIn(MouseEvent event){
		log.log(Level.FINEST, "Registry method inicialized");
		if( com.pasapalabra.game.service.ClientConnection.serverReady == false ){
			log.log(Level.INFO, "Server not ready");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Server currenly offline");

			alert.setContentText("The server is currenlly offline, please try to reset the program");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
		}
		else{
			log.log(Level.FINEST, "Window transition to singin");
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("SignIn", event);
		}
	}

	/**Method to open the github page of the current project
	 * @param event of the mouse
	 */
	public void goToGitHub(MouseEvent event){
		log.log(Level.FINEST, "Go to github");
		try {
			//Desktop-getea el desktop-navega en-nueva URL-URL-a URI
			Desktop.getDesktop().browse(new URL("https://github.com/spqdeusto1617/SPQ07").toURI());
			log.log(Level.INFO, "Sucessfull acess to gihub");
		} catch (Exception e) {
			log.log(Level.WARNING, "Error trying to acess github", e);
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