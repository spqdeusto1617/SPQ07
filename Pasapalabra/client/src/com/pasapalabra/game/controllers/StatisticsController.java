package com.pasapalabra.game.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.application.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class StatisticsController extends ExtenderClassController implements Initializable {

	@FXML
	private Text textoESPanel;

	@FXML
	private Text txtPartidasJugadas;

	@FXML
	private Text textoMiPerfil;

	@FXML
	private Rectangle rectanguloPanel;

	@FXML
	private Rectangle rectanguloAmigos;

	@FXML
	private Text txtDerrotas;

	@FXML
	private Text txtEmpates;

	@FXML
	private Rectangle rectanguloMiPerfil;

	@FXML
	private Rectangle rectanguloJugar;

	@FXML
	private Text textoAmigos;

	@FXML
	private ImageView imagenAvatar;

	@FXML
	private Pane panel;

	@FXML
	private Circle circuloPlus;

	@FXML
	private Text txtPosicion;

	@FXML
	private Text textoPlus;

	@FXML
	private ImageView logopsp;

	@FXML
	private Text textoLogeadoComo;

	@FXML
	private Text textoCerrarSesion;

	@FXML
	private Text txtRatio;

	@FXML
	private Rectangle rectanguloCerrarSesion;

	@FXML
	private Text textoJugar;

	@FXML
	private Circle circuloPanel;

	@FXML
	private Text textoNombreDeUsuario;

	@FXML
	private Text textoEstadisticas;

	@FXML
	private Rectangle rectanguloEstadisticas;

	@FXML
	private Text txtVictorias;

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(StatisticsController.class.getName());
	
	/*Posiciones:TODO: implement this
	 * 0: partidas ganadas
	 * 1: partidas perdidas
	 * 2:partidas empatadas
	 * 3:partidas totales jugadas
	 * 4: posición en el ranking
	 */
	 @FXML
	 void btnJugar(MouseEvent event) {
		 log.log(Level.FINEST, "Transición a Juego");
		 com.pasapalabra.game.utilities.WindowUtilities.transicionVentana("Juego", event);
	 }

	 @FXML
	 void btnMiPerfil(MouseEvent event) {
		 log.log(Level.FINEST, "Transición a Perfil");
		 com.pasapalabra.game.utilities.WindowUtilities.transicionVentana("Perfil", event);
	 }

	 @FXML
	 void entrado(MouseEvent event) {
		 com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	 }

	 //Añade nivel de transparencia
	 @FXML
	 void salido(MouseEvent event) {
		 com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	 }

	 @FXML
	 void btnEstadisticas(MouseEvent event) {

	 }


	 @FXML
	 void btnAmigos(MouseEvent event) {
		 log.log(Level.FINEST, "Transición a Amigos");
		 com.pasapalabra.game.utilities.WindowUtilities.transicionVentana("Amigos", event);
	 }

	 @FXML
	 void btnCerrarSesion(MouseEvent event) {
		 log.log(Level.FINEST, "Cerrar sesión");
		 com.pasapalabra.game.utilities.WindowUtilities.cerrarSesion(event);
	 }

	 @FXML
	 void esPanel(MouseEvent event) {

	 }


	 @Override
	 public void initialize(URL location, ResourceBundle resources) {
		 com.pasapalabra.game.utilities.AppLogger.crearLogHandler(log, Main.class.getName());
		 log.log(Level.FINEST, "Inicializando EventosEstadisticas");
		 //Poner la imagen de avatar
		 if(LogInController.iAvatar!=null){
			 //Si no es nula, pone la que hay
			 log.log(Level.FINEST, "El avatar en EventosLogIn.iAvatar no es nulo");
			 imagenAvatar.setImage(LogInController.iAvatar);
		 }else{
			 /*Si es nula significa que el usuario no tiene imagen puesta
			 entonces se pone una por defecto*/
			 log.log(Level.FINEST, "El avatar en EventosLogIn.iAvatar es nulo");
			 String imagen = "fPerfil";
			 Random rand = new Random();
			 int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			 if(randomNum == 666){
				 imagen = "fPerfilPirata";
			 }

			 Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
			 imagenAvatar.setImage(i);
		 }
		 //Se añaden más campos
		 log.log(Level.FINEST, "Añadiendo todos los campos");
		 //Se hace un clip sobre la imagen para que sea circular
		 Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		 imagenAvatar.setClip(clip);
		 imagenAvatar.setSmooth(true); 
		 imagenAvatar.setCache(true); 
		 //Se pone el nombre de usuario
		 textoNombreDeUsuario.setText(com.pasapalabra.game.utilities.ClientConnexion.userInfo.getUserName());

		 //Se ponen los datos relacionados con esta ventana. Es decir, estadísticas.
		
		 /*txtDerrotas.setText(Datos_Usuario_Estadisticas.get(1));
		 txtEmpates.setText(Datos_Usuario_Estadisticas.get(2));
		 txtVictorias.setText(Datos_Usuario_Estadisticas.get(0));
		 double d = (Integer.parseInt(Datos_Usuario_Estadisticas.get(0))/Integer.parseInt(Datos_Usuario_Estadisticas.get(1)));
		 txtRatio.setText(String.valueOf(d));
		 txtPosicion.setText(Datos_Usuario_Estadisticas.get(4));
		 txtPartidasJugadas.setText(Datos_Usuario_Estadisticas.get(3));*/
		 log.log(Level.FINEST, "Todos los campos añadidos");
	 }
}