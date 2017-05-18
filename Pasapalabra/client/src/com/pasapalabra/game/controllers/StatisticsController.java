package com.pasapalabra.game.controllers;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.application.Main;
import com.pasapalabra.game.service.ClientConnection;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**Class that manages events from Statistics.fxml
 * @author alvaro
 *
 */
public class StatisticsController extends ExtenderClassController implements Initializable {

	@FXML
	private Text textoESPanel;

	@FXML
	private Text txtPartidasJugadas;

	//@FXML
	//private Text textoMiPerfil;

	@FXML
	private Rectangle rectanguloPanel;

	//@FXML
	//private Rectangle rectanguloAmigos;

	@FXML
	private Text txtDerrotas;

	@FXML
	private ImageView avatarIMG;

	@FXML
	private Pane panel;

	@FXML
	private Circle circuloPlus;

	@FXML
	private Text textoPlus;

	@FXML
	private ImageView logopsp;

	@FXML
	private Text textoLogeadoComo;

	//@FXML
	//private Text textoCerrarSesion;

	@FXML
	private Text txtRatio;

	//@FXML
	//private Rectangle rectanguloCerrarSesion;

	//@FXML
	//private Text textoJugar;

	@FXML
	private Circle circuloPanel;

	@FXML
	private Text textoNombreDeUsuario;

	@FXML
	private Text userNametxt; 

	//@FXML
	//private Text textoEstadisticas;

	//@FXML
	//private Rectangle rectanguloEstadisticas;

	@FXML
	private Text txtVictorias;

	@FXML public Button btnCerrarSesion;
	@FXML public Button btnEstadisticas;
	@FXML public Button btnPerfil;
	@FXML public Button btnAmigos;
	@FXML public Button btnJuego;

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(StatisticsController.class.getName());

	/*Posiciones:TODO: implement this
	 * 0: partidas ganadas
	 * 1: partidas perdidas
	 * 2:partidas empatadas
	 * 3:partidas totales jugadas
	 * 4: posición en el ranking
	 */

	/**Transition to game window
	 * @param event
	 */
	@FXML
	void btnJugar(MouseEvent event) {
		log.log(Level.FINEST, "Transición a Juego");
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
	}

	/**Transition to profile window
	 * @param event
	 */
	@FXML
	void btnMiPerfil(MouseEvent event) {
//		Alert alert = new Alert(AlertType.INFORMATION);
//
//		alert.setTitle("Function not yet implemented.");
//
//		alert.setHeaderText("Do not use this function");
//
//		alert.setContentText("This Function is not implemented, please, do not use it");
//
//		alert.showAndWait();

		log.log(Level.FINEST, "Transición a Perfil");
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Profile", event);
	}

	@FXML
	void entrado(MouseEvent event) {
		//com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	}

	//Añade nivel de transparencia
	@FXML
	void salido(MouseEvent event) {
		//com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	}

	/**Transition to statistics window
	 * @param event
	 */
	@FXML
	void btnEstadisticas(MouseEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Already in");

		alert.setHeaderText("You are already in statistics");

		alert.setContentText("You are already in your statistics. Select an action");

		alert.showAndWait(); 
		//log.log(Level.FINEST, "Transición a Estadisticas");
		//com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Statistics", event);
	}

	/**Transition to friends window
	 * @param event
	 */
	@FXML
	void btnAmigos(MouseEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Function not yet implemented.");

		alert.setHeaderText("Do not use this function");

		alert.setContentText("This Function is not implemented, please, do not use it");

		alert.showAndWait();
		//log.log(Level.FINEST, "Transición a Amigos");
		//com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Friends", event);
	}

	/**Method to close current session
	 * @param event
	 */
	@FXML
	void btnCerrarSesion(MouseEvent event) {
		log.log(Level.FINEST, "Cerrar sesión");
		com.pasapalabra.game.utilities.WindowUtilities.closeSession(event);
	}

	@FXML
	void esPanel(MouseEvent event) {

	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		com.pasapalabra.game.utilities.AppLogger.crearLogHandler(log, Main.class.getName());
		log.log(Level.FINEST, "Inicializando EventosEstadisticas");
		//Poner la imagen de avatar
		btnAmigos.setOpacity(0.3f);
		btnEstadisticas.setOpacity(1f);
		btnJuego.setOpacity(0.3f);
		btnPerfil.setOpacity(0.3f);
		if(ClientConnection.userIMG != null){
			avatarIMG.setImage(ClientConnection.userIMG);
		}else{
			String imagen = "fPerfil";
			Random rand = new Random();
			int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			if(randomNum == 666){
				imagen = "fPerfilPirata";
			}

			Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),avatarIMG.getBoundsInLocal().getWidth(),avatarIMG.getBoundsInLocal().getHeight(),false,true);
			avatarIMG.setImage(i);
		}

		Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		imagenAvatar.setClip(clip);
		imagenAvatar.setSmooth(true); 
		imagenAvatar.setCache(true); 

		//Se pone el nombre de usuario
		textoNombreDeUsuario.setText(ClientConnection.userInfo.getUserName());

		//Se ponen los datos relacionados con esta ventana. Es decir, estadísticas.

		txtDerrotas.setText(Integer.toString(ClientConnection.userInfo.getGamesLost()));

		txtVictorias.setText(Integer.toString(ClientConnection.userInfo.getGamesWon()));
		double d = 0;
		if(ClientConnection.userInfo.getGamesLost() == 0){
			if(ClientConnection.userInfo.getGamesWon() == 0) d = 0;
			else d = ClientConnection.userInfo.getGamesWon();
		}
		else{
			try{
				d = (ClientConnection.userInfo.getGamesWon())/(ClientConnection.userInfo.getGamesLost());
			}catch (ArithmeticException e) {
				// TODO: handle exception
				log.log(Level.WARNING, "Error occurred during the operation", e);
				d = 0;
			}
		}
		try{
		txtRatio.setText(String.valueOf(d));
		}catch (Exception e) {
		e.printStackTrace();
		}
		int result = ClientConnection.userInfo.getGamesLost() + ClientConnection.userInfo.getGamesWon();
		txtPartidasJugadas.setText(Integer.toString(result));
		log.log(Level.FINEST, "Todos los campos añadidos");
		userNametxt.setText(ClientConnection.userInfo.getUserName());
	}
}