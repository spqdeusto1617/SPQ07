package com.pasapalabra.game.controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.service.ClientConnection;
import com.pasapalabra.game.service.ClientService;
import com.pasapalabra.game.utilities.AppLogger;
import com.pasapalabra.game.utilities.WindowUtilities;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**Class that manage events from Game.fxml
 * @author alvaro
 *
 */
public class GameController implements Initializable{

	public static Logger log = AppLogger.getWindowLogger(GameController.class.getName());


	//*COMIENZO DE DECLARACIÓN DE ATRIBUTOS*
	//_________________________________________
	public static final int PUNTOSTOTALES = 27;
	public Image imagenDelRival;
	public int vecesHechoX = 0;
	public int vecesHechoY = 0;
	public boolean ventanaMenuDentro = false;
	private static boolean rightAnswered;
	public static QuestionDTO currentQuestion;
	public ArrayList<ImageView> panelLetrasJugador = new ArrayList<>(); //Panel con todos los labels del jugador
	public ArrayList<ImageView> panelLetrasContrincante = new ArrayList<>(); //Panel con todos los labels del contrincante


	@FXML public Pane panel;
	@FXML public Rectangle rPreguntas; 
	@FXML public TextArea taPreguntas;
	@FXML public ImageView iv; 
	@FXML public ImageView userIMG; 
	@FXML public ImageView rivalIMG; 
	@FXML public Text textoTiempoUsuario; 
	@FXML public Text textoTiempoRival; 

	@FXML public Text textoPuntuacionU;
	@FXML public Text textoPuntuacionR;

	@FXML public Text textoUsernameUser;
	@FXML public Text textoUsernameRival;

	@FXML public TextField tfRespuesta; 

	@FXML public Button btnAnswer;
	@FXML public Button btnPass;
	@FXML public Button btnGiveUp;
	@FXML public Button btnReturn;

	

	/**Method called when Contestar button is pressed in the game. 
	 * If the answer is in blank, error
	 * @param event
	 */
	@FXML
	void Answer(MouseEvent event) {
		boolean gameEnd = false;
		if(ClientService.rivalDisconnected)return;
		if(!ClientConnection.playing)return;
		if(!ClientConnection.turn)return;
		if(tfRespuesta.getText().length()==0){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No answer");
			alert.setHeaderText("");
			alert.setContentText("You can´t leave the answer empty");
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.show();
		}
		try {
			if(!com.pasapalabra.game.service.ClientConnection.endGame()){
				try{
					rightAnswered = com.pasapalabra.game.service.ClientConnection.answerQuestion(tfRespuesta.getText());
					//tfRespuesta.setText("");	
				}catch(Exception a){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Hubo un error");
					alert.setHeaderText("Parece que se ha producido un error");
					alert.setContentText("No se ha podido contestar la pregunta, por favor, vuelva a intentarlo");
					alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert.showAndWait();
					a.printStackTrace();
					return;
				} 
				if(rightAnswered){

					int foo =Integer.parseInt(textoPuntuacionU.getText().substring(7));
					foo++;
					textoPuntuacionU.setText("Right: "+Integer.toString(foo));

					int Num_Letra=Pos_Letter(currentQuestion.getLeter());
					panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+currentQuestion.getLeter()+"-green.png")));
				}
				else{
					int Num_Letra=Pos_Letter(currentQuestion.getLeter());
					panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+currentQuestion.getLeter()+"-red.png")));
				}
			}
			try {
				gameEnd = ClientConnection.endGame();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Hubo un error");
				alert.setHeaderText("Parece que se ha producido un error");
				alert.setContentText("No se ha podido comprobar si todas las preguntas están respondidas");
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert.showAndWait();
				e1.printStackTrace();
				return;
			}
			try {
				if(gameEnd){
					try {
						ClientConnection.getResults();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Hubo un error");
						alert.setHeaderText("Parece que se ha producido un error");
						alert.setContentText("No se ha podido recuperar la puntuación");
						alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert.showAndWait();
						e.printStackTrace();
						return;
					}
					//TODO: terminar
					if(ClientConnection.player1){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Game ended");
						alert.setHeaderText("The game has ended");
						alert.setContentText("The game has ended. Press the return button to see the final results");
						alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert.showAndWait();
					}
					else{
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Game ended");
						alert.setHeaderText("The game has ended");
						alert.setContentText("The gam has ended. Please, wait until your rival ends");
						alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert.show();
						taPreguntas.setText("Wait until your rival, "+ClientService.rivalData.getUserName()+" finish playing");
						spectate();
					}

				}
				else{
					try {
						currentQuestion = com.pasapalabra.game.service.ClientConnection.getQuestion();
						taPreguntas.setText(currentQuestion.getQuestion());
					} catch (Exception e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Hubo un error");
						alert.setHeaderText("Parece que se ha producido un error");
						alert.setContentText("No se ha podido recuperar la siguiente pregunta");
						alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert.showAndWait();
						e.printStackTrace();
						return;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}





	/**Method called when Pasar button is pressed
	 * It manages everything related to when a user goes to another question. 
	 * It is the user turn and says pasapalabra the letter changes. 
	 * To the contrary, the user can not go to another question. 
	 * @param event
	 */
	@FXML
	void Pass(MouseEvent event) {
		if(ClientService.rivalDisconnected)return;
		if(!ClientConnection.playing)return;
		if(!ClientConnection.turn)return;
		try{
			rightAnswered = com.pasapalabra.game.service.ClientConnection.answerQuestion("Pasapalabra");
			if(!rightAnswered){
				log.log(Level.WARNING, "Error al pasar de palabra");
			}
		}catch(Exception a){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Hubo un error");
			alert.setHeaderText("Parece que se ha producido un error");
			alert.setContentText("No se ha podido contestar la pregunta, por favor, vuelva a intentarlo");
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			a.printStackTrace();
			return;
		} 
		try {
			currentQuestion = com.pasapalabra.game.service.ClientConnection.getQuestion();
			taPreguntas.setText(currentQuestion.getQuestion());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Hubo un error");
			alert.setHeaderText("Parece que se ha producido un error");
			alert.setContentText("No se ha podido recuperar la siguiente pregunta");
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			e.printStackTrace();
			return;
		}
	}

	/**Method called when Rendirse button is called in the game. 
	 * If the user can (when it is his/her turn) give up. To the contrary, the user
	 * can not give up. 
	 * @param event
	 */
	@FXML
	void GiveUp(MouseEvent event){
		/*
		if(!Conexion_cliente.Mi_Turno){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No te puedes rendir");
			alert.setContentText("No te puedes rendir a menos que sea tu turno");
			alert.showAndWait();
		}
		else{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("¿Está seguro?");
			alert.setHeaderText("¿Está seguro de que quiere rendirse");
			alert.setContentText("Si se rinde, perderá la partida, ¿está completamente seguro?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				if(juegoEnCurso==true){
					try{	
						com.pasapalabra.game.utilities.Conexion_cliente.Respuesta="Rendirse";
						com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local, com.pasapalabra.game.utilities.Acciones_servidor.Responder_Pregunta.toString(), null);
						tfRespuesta.setText("");
					}catch(Exception a){
						a.printStackTrace();
					}
				}

				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Partida acabada");
				alert2.setHeaderText("Ha completado la partida");
				alert2.setContentText("Se ha terminado la partida con su rendición, por tanto, se concidera la partida acabada");
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
				deVentana.transicionVentana("Juego", event);
			}
		}

		 */
	}

	//Para usar el textBox 
	/**Method that returns the position of the current letter
	 * @param letra_Actual
	 * @return
	 */
	private static int Pos_Letter(char letra_Actual) {
		if(letra_Actual=='ñ'){
			return 14;
		}
		else if(letra_Actual-'a'>13){
			return letra_Actual-'a'+1;
		}
		else{
			return letra_Actual-'a';
		}
	}

	public void returnTheme(Event event){
		if(ClientConnection.playing)return;
		if(ClientService.rivalDisconnected){
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setTitle("Game ended");
			alert.setHeaderText("The game ended because your rival disconnected");
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
		}
		else{
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setTitle("Game ended");
			alert.setHeaderText("The game ended has ended");
			alert.setContentText("The result are:\nYou answered "+ClientConnection.userScore.getRightAnswered()
			+ " questions right and "+ClientConnection.userScore.getWrongAnswered()+" questions wrong. \nYour "
			+ "rival has answered "+ClientService.rivalScore.getRightAnswered()+" questions "
			+ "right and "+ClientService.rivalScore.getWrongAnswered()+" questions wrong.");
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();

			if(ClientConnection.userScore.getRightAnswered() > ClientService.rivalScore.getRightAnswered()){
				Alert alert2=new Alert(AlertType.INFORMATION);
				alert2.setTitle("YOU WON");
				alert2.setHeaderText("Congratulations, you won against your rival, "+textoUsernameRival.getText());
				alert2.showAndWait();
			}
			else if(ClientConnection.userScore.getRightAnswered() < ClientService.rivalScore.getRightAnswered()){
				Alert alert2=new Alert(AlertType.INFORMATION);
				alert2.setTitle("You lost");
				alert2.setHeaderText("Unfortunately, you lost against: "+textoUsernameRival.getText());
				alert2.showAndWait();
			}else{
				Alert alert2=new Alert(AlertType.INFORMATION);
				alert2.setTitle("There is a tie");
				alert2.setHeaderText("There is a tie between you and your rival, "+textoUsernameRival.getText());
				alert2.showAndWait();
			}
		}
		ClientConnection.player1 = false;
		WindowUtilities.windowTransition("ThemeElection", event);
	}

	public void createRosco(boolean amigo_notEnemigo, ArrayList<ImageView> aLImgV){
		ImageView iv;
		char letraABC = 'a';

		for (int i = 0; i < PUNTOSTOTALES; i++) {
			//VARIABLE ABCD...


			//CREAMOS ImageV	
			iv = new ImageView();

			iv.setLayoutX(coordX(amigo_notEnemigo));
			iv.setLayoutY(coordY());
			iv.setFitHeight(25);
			iv.setFitWidth(25);
			//CARGAMOS LA IMAGEN
			if(i == 14){
				letraABC--;
				iv.setImage(new Image(getClass().getResourceAsStream("/images/letras/azul/ñ-blue.png")));
			}else{

				iv.setImage(new Image(getClass().getResourceAsStream("/images/letras/azul/"+letraABC+"-blue.png")));
			}

			try{
				panel.getChildren().add(iv);
			}catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			//A—ADIMOS EL LABEL AL ARRAYLIST DE LABELS
			aLImgV.add(iv);

			//SUMAMOS 1 A LA LETRA
			letraABC++;

		}
	}

	/**
	 * @param amigo_notEnemigo 
	 * @return  the coordinate X to make the rosco
	 */
	public int coordX(boolean amigo_notEnemigo){
		int resultado;
		//double divisionCircunferencia = 360/27;
		//resultado = (int)(200* (Math.cos(200+divisionCircunferencia*vecesHechoX/2)))+300;

		//(Radio * Coseno de ((360grados partido por número de elementos a repartir * número de elementos creados * pi partido por 180) - pi partido por 2) + posición del rádio en X
		//Nota: El radio ha de ser igual al de Y, si no, no se dibujará un rosco redondo.
		if(amigo_notEnemigo) resultado = (int)(150 * Math.cos((360/27.0)*vecesHechoX*(Math.PI/180)-Math.PI/2)) + 200;
		else resultado = (int)(150 * Math.cos((360/27.0)*vecesHechoX*(Math.PI/180)-Math.PI/2)) + 575;

		vecesHechoX++;
		//		System.out.println(resultado);
		return resultado;
	}


	/**
	 * @return the coordinate Y to make the rosco
	 */
	public int coordY(){
		int resultado;
		//double divisionCircunferencia = 360/27;

		//resultado = (int)(200* (Math.sin(200+divisionCircunferencia*vecesHechoY/2))) +300;

		//(Radio * Seno de ((360grados partido por número de elementos a repartir * número de elementos creados * pi partido por 180) - pi partido por 2) + posición del rádio en Y
		//Nota: El radio ha de ser igual al de Y, si no, no se dibujará un rosco redondo.
		resultado = (int)(150 * Math.sin((360/27.0)*vecesHechoY*(Math.PI/180)-Math.PI/2)) +160;

		vecesHechoY++;
		//		System.out.println(resultado);
		return resultado;
	}

	public void spectate(){
		new Thread(new Runnable() {  
			@Override  
			public void run() {   
				while (!ClientConnection.turn && !ClientService.rivalDisconnected && ClientConnection.playing) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(ClientService.rivalAnswered){
						ClientService.rivalAnswered = false;
						if(ClientService.rivalAnswer){
							int foo = Integer.parseInt(textoPuntuacionR.getText().substring(7));
							foo++;
							textoPuntuacionR.setText("Right: "+Integer.toString(foo));
							int Num_Letra = Pos_Letter(ClientService.leterAnswered);
							panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+ClientService.leterAnswered+"-green.png")));
						}
						else{
							int Num_Letra=Pos_Letter(ClientService.leterAnswered);
							panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+ClientService.leterAnswered+"-red.png")));
						}
					}
				}
				if(ClientService.rivalDisconnected){
					taPreguntas.setText("Your rival disconnected, press the return button to exit");
				}
				else{
					if(ClientConnection.player1){
						if(ClientConnection.turn){
							taPreguntas.setText("Your rival ended and now is your turn");
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								currentQuestion = ClientConnection.getQuestion();
								taPreguntas.setText(currentQuestion.getQuestion());
							} catch (Exception e) {

								e.printStackTrace();
							}
						}
					}
					else{
						taPreguntas.setText("The game has ended. Press return button to exit the game");
					}
				}
			}  
		}).start(); 
	}

	public void checkOtherDisconnect(){
		new Thread(new Runnable() {  
			@Override  
			public void run() {  
				while(ClientConnection.playing && !ClientService.rivalDisconnected){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(ClientService.rivalDisconnected){
					try{
						Thread.sleep(1000);
					}catch (Exception e) {
						// TODO: handle exception
					}
					taPreguntas.setText("Your rival disconnected. Please press return to end.");
				}
			}  
		}).start(); 
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		try {
			//currentQuestion = ClientConnection.getQuestion(); 
			createRosco(true, panelLetrasJugador);
			createRosco(false, panelLetrasContrincante);

			//image of the user
			if(ClientConnection.userIMG != null){
				userIMG.setImage(ClientConnection.userIMG);
			}else{
				String imagen = "fPerfil";
				Random rand = new Random();
				int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
				if(randomNum == 666){
					imagen = "fPerfilPirata";
				}

				Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),userIMG.getBoundsInLocal().getWidth(),userIMG.getBoundsInLocal().getHeight(),false,true);
				userIMG.setImage(i);
			}

			//image of the rival
			if(ClientService.rivalIMG != null){
				rivalIMG.setImage(ClientService.rivalIMG);
			}else{
				String imagen = "fPerfil";
				Random rand = new Random();
				int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
				if(randomNum == 666){
					imagen = "fPerfilPirata";
				}

				Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),rivalIMG.getBoundsInLocal().getWidth(),rivalIMG.getBoundsInLocal().getHeight(),false,true);
				rivalIMG.setImage(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "Error trying to set the user/rival image", e);
		}
		textoUsernameRival.setText(com.pasapalabra.game.service.ClientService.rivalData.getUserName());
		textoUsernameUser.setText(ClientConnection.userInfo.getUserName());
		if(!ClientConnection.player1){
			try {
				currentQuestion = ClientConnection.getQuestion();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taPreguntas.setText(currentQuestion.getQuestion());
		}
		else{
			taPreguntas.setText("Please, wait until your rival, "+textoUsernameRival.getText()+" finish his turn");
			spectate();
		}
		checkOtherDisconnect();
	}
}
