package com.pasapalabra.game.controllers;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.pasapalabra.game.model.DTO.QuestionType;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.service.ClientService;
import com.pasapalabra.game.service.ClientConnection;
import com.pasapalabra.game.utilities.PanelThread;
import com.pasapalabra.game.utilities.WindowUtilities;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**Class that manages events from ThemeElections.fxml
 * @author alvaro
 *
 */
public class ThemeController extends ExtenderClassController implements Initializable{

	//Logger de la clase

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(ThemeController.class.getName());

	//*START OF THE DECLARATION*
	public static final int PUNTOSTOTALES = 27;
	public Image imagenDelRival;
	public int vecesHechoX = 0;
	public int vecesHechoY = 0;
	public QuestionType type; 
	//public static boolean ClientConnection.playing = false;
	public boolean ventanaMenuDentro = false;
	public ArrayList<Node> menuDesplegable; //Colección de todos los elementos del menu desplegable.
	public ArrayList<Button> aLBotones = new ArrayList<Button>(); 

	//Declaration of the panel
	@FXML public Pane panel;

	@FXML public Text textoESPanel;

	@FXML public Text textoMiPerfil;

	@FXML public ImageView logopsp;

	@FXML public Text textoCerrarSesion;

	@FXML public Text textoPlus;

	@FXML public Rectangle rectanguloPanel;

	@FXML public Circle circuloPlus;

	@FXML public Text textoLogeadoComo;

	@FXML public Circle circuloPanel;

	@FXML public Text textoNombreDeUsuario;

	@FXML public Text searchTXT;

	@FXML public ImageView imagenAvatar;

	//Botones
	@FXML public Button btnJugar;
	@FXML public Button btnCancel;
	@FXML public Button btnCerrarSesion;
	@FXML public Button btnEstadisticas;
	@FXML public Button btnPerfil;
	@FXML public Button btnAmigos;
	@FXML public Button btnJuego;
	@FXML public Button btnAmigo;
	@FXML public Button btnAleatorio;
	@FXML public Button btnGeo;
	@FXML public Button btnArte;
	@FXML public Button btnHistoria;
	@FXML public Button btnCiencia;
	@FXML public Button btnEntretenimiento;
	@FXML public Button btnDeportes;
	@FXML public Button btnTodos;


	//*END OF THE DECLARATION


	/**Method to play the game
	 * @param event
	 */
	@FXML 
	void Play(MouseEvent event) {
		log.log(Level.FINEST, "Botón de jugar pulsado y ClientConnection.playing = " + ClientConnection.playing);
		//Compruebo si se está jugando
		if(ClientConnection.playing){
			//Si se está jugando, entonces creo una alerta para decir que está ya el juego en curso
			//Creo una alerta de tipo informativa
			Alert alert = new Alert(AlertType.INFORMATION);
			log.log(Level.FINEST, "Alerta de información creada");
			//Añado título a la alerta
			alert.setTitle("Information");
			log.log(Level.FINEST, "Título añadido a la alerta");
			//Seteo el contenido de la cabecera a nulo
			alert.setHeaderText(null);
			log.log(Level.FINEST, "Cabecera nula añadida a la alerta");
			//Seteo el contenido
			alert.setContentText("You are already playing, if you want to go\nplease give up.");
			log.log(Level.FINEST, "Contenido de texto añadido a la alerta");
			//Elijo la modalidad de la alerta
			alert.initModality(Modality.APPLICATION_MODAL);
			log.log(Level.FINEST, "Añadida modalidad para la alerta");
			//Elijo el dueño de la alerta (o la base) de la misma.
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			log.log(Level.FINEST, "Añadido dueño sobre el cual se ejecuta la alerta. Se mostrará la alerta...");
			//Muestra la alerta y espera a que el usuario cierre la ventana
			alert.showAndWait();
			log.log(Level.FINEST, "Alerta de información creada, mostrada y cerrada");
		}else{
			//Si el juego no está en curso se saca una alerta explicando que puede elegir modo de juego
			//Creamos alerta de tipo informativa
			Alert alert = new Alert(AlertType.INFORMATION);
			log.log(Level.FINEST, "Alerta de información creada");
			//Añadimos título a la alerta
			alert.setTitle("Information");
			log.log(Level.FINEST, "Título añadido a la alerta");
			//Dejamos que la cabecera sea nula
			alert.setHeaderText(null);
			log.log(Level.FINEST, "Cabecera nula añadida a la alerta");
			//Añadimos el contenido que tendrá la alerta
			alert.setContentText("You are already in theme election window, select a game mode.");
			log.log(Level.FINEST, "Contenido de texto añadido a la alerta");
			//Añadimos modalidad de la alerta
			alert.initModality(Modality.APPLICATION_MODAL);
			log.log(Level.FINEST, "Añadida modalidad para la alerta");
			//Añadimos dueño de la alerta (Ventana sobre la cual se ejecutará)
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			log.log(Level.FINEST, "Añadido dueño sobre el cual se ejecuta la alerta. Se mostrará la alerta...");
			//Muestra la alerta y espera a que el usuario cierre la ventana
			alert.showAndWait();
			log.log(Level.FINEST, "Alerta de información creada, mostrada y cerrada");
		}
	}


	@FXML
	void Friends(MouseEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Function not yet implemented.");

		alert.setHeaderText("Do not use this function");

		alert.setContentText("This Function is not implemented, please, do not use it");

		alert.showAndWait();
		//	
		//
		//		if(ClientConnection.playing){
		//			//Alerta
		//			Alert alert2 = new Alert(AlertType.INFORMATION);
		//			alert2.setTitle("Juego en curso");
		//			alert2.setHeaderText(null);
		//			alert2.setContentText("No puedes abandonar la ventana mientras el juego esté en curso."
		//					+ " Termina la partida para poder avanzar a esa ventana");
		//			alert2.initModality(Modality.APPLICATION_MODAL);
		//			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		//			alert2.showAndWait();
		//			log.log(Level.FINE, "El juego está en curso.");
		//		}else{
		//			log.log(Level.FINEST, "Transicion de ventana a Amigos");
		//			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Friends", event);
		//		}
		//control sift 7
	}

	/**Method to go to the profile window when the game is not in progress
	 * @param event
	 */
	@FXML
	void MyProfile(MouseEvent event) {
		//		Alert alert = new Alert(AlertType.INFORMATION);
		//
		//		alert.setTitle("Function not yet implemented.");
		//		
		//		alert.setHeaderText("Do not use this function");
		//		 
		//		alert.setContentText("This Function is not implemented, please, do not use it");
		//
		//		alert.showAndWait();
		
		

		if(ClientConnection.playing){
			//Alerta
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Game in progress");
			alert2.setHeaderText(null);
			alert2.setContentText("You can not go out of the game while the game is in progress."
					+ " Finish the game in order to go to your profile window");
			alert2.initModality(Modality.APPLICATION_MODAL);
			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert2.showAndWait();
			log.log(Level.FINE, "El juego está en curso.");
		}else{
			log.log(Level.FINEST, "Transicion de ventana a Perfil");
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Profile", event);
		}
	}

	@FXML
	void cancelMatch(MouseEvent event){
		try {
			ClientConnection.exitMatchMaking();
			ClientConnection.playing = false;
			btnCancel.setVisible(false);
			searchTXT.setVisible(false);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**Method to go to the statistics window when the game is not in progress
	 * @param event
	 */
	@FXML
	void Statistics(MouseEvent event) {

		if(com.pasapalabra.game.service.ClientConnection.playing){
			//Alerta
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Game in progress");
			alert2.setHeaderText(null);
			alert2.setContentText("You can not go out of the window while the game is in progress."
					+ " Finish the game in order to go to statistics window");
			alert2.initModality(Modality.APPLICATION_MODAL);
			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert2.showAndWait();
			log.log(Level.FINE, "El juego está en curso.");
		}else{
			log.log(Level.FINEST, "Transicion de ventana a Estadisticas");
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Statistics", event);
		}
	}

	//Elimina nivel de transparencia
	@FXML
	void entrado(MouseEvent event) {
		//com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	}

	//Añade nivel de transparencia
	@FXML
	void salido(MouseEvent event) {
		//com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	}

	/**Method to close current session
	 * @param event
	 */
	@FXML
	void LogOut(MouseEvent event) {
		log.log(Level.FINEST, "Cierre de sesión");
		com.pasapalabra.game.utilities.WindowUtilities.closeSession(event);
	}

	//TODO
	@FXML 
	void btnAll(MouseEvent event){
		ClientConnection.qType = QuestionType.All; 
	}

	@FXML 
	void btnSports(MouseEvent event){
		ClientConnection.qType = QuestionType.Sport; 
	}


	/**Method that check if question type button and VS random button is clicked. 
	 *  @return false if both buttons are not pressed
	 *  @return true if both buttons are pressed
	 */
	boolean bothClicked(){
		if (btnAleatorio.isPressed() || btnAmigo.isPressed()){
			//for(Button aLBotones: boton){
			//if(boton.isPressed(){
			//return true;
			if(btnCiencia.isPressed() || btnDeportes.isPressed() 
					|| btnArte.isPressed() || btnEntretenimiento.isPressed()
					|| btnGeo.isPressed() || btnHistoria.isPressed() 
					|| btnTodos.isPressed()){
				return true; 
			}else{
				return false; 
			}
		}else{
			return false; 
		}
	}

	//boolean true al hacer click en los botones y si los dos son true hace latransición a game sino no. 
	//Deportes... hacen lo mismo que el all
	@FXML
	void aPlay(MouseEvent event){
		//if(!bothClicked())return;
		if(ClientConnection.playing) return;
		/*Inhabilitamos rápidamente que el botón de jugar vuelva a poder ser pulsado.
		 *(Es posible que después de varias comprobaciones necesitemos volverlo a poner a falso)
		 */
		ClientConnection.playing = true;
		//Servidor
		//aLEleccion es el arrayList de la elección (Modalidad + Tema) y todos los elementos
		//String[]Tipo=new String[2];
		QuestionType type = QuestionType.All; 
		ClientConnection.qType = type;
		//		for (QuestionSelectedObject obsp : aLEleccion) {
		//			if (obsp.isElegido()) {
		//				if(obsp.isModoDeJuego_notTipoPregunta()){
		//					//ESTE ES EL MODO DE JUEGO ELEGIDO
		//					//tipo[1]
		//					type=obsp.getTexto().getText();
		//
		//				}else{
		//					//ESTE ES EL TEMA DE JUEGO ELEGIDO
		//					//tipo[2]
		//					type=obsp.getTexto().getText();
		//				}
		//			}
		//		}
		/*for(Button botonTema: aLBotones){
			if(botonTema.isPressed()){
				//FALTARIA RECORRER LOS QUESTION TYPES, NO TENGO NI IDEA
				if(botonTema.getText().equals(type.toString())){
					//type.toString().equals(botonTema.getText());
					log.log(Level.INFO, "Button selected");
				}
			}else{
				log.log(Level.WARNING, "Error, there is not any theme selected. Please, select one");
			}
		}*/

		//
		//Hasta encontrar partida, aquí.
		//Aquí se crearía un hilo que estaría esperando y que no se reanudaría hasta que se encontrase una partida
		//Lanzamos la partida

		try {
			//TODO: cambiar
			// ANTES utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Jugar.toString(), Tipo);
			UserDTO user= com.pasapalabra.game.service.ClientConnection.play(type); 
			if(user == null) WindowUtilities.forcedCloseSession(event);
			if(user.getUserName().equals("Wait")){
				ClientConnection.player1 = true;
				ClientConnection.turn = false;
				searchTXT.setVisible(true);
				btnCancel.setVisible(true);

				try {  
				//	while(ClientConnection.playing && !ClientService.found){
						for (int i = 0; i < 40; i++) {
							Thread.sleep(500);
							
							if(ClientService.found) {i = 60;
							}
						}
				} catch (InterruptedException e) {  }
			
				
				if(ClientService.found){
					ClientService.found = false;

					WindowUtilities.windowTransition("Game", event);
				}
				else{
					cancelMatch(event);
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("No match found");
					alert.setContentText("Impssible to find a match, please try later");
					alert.initModality(Modality.APPLICATION_MODAL);	
					alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert.showAndWait();
				}
			}   
			else{
				ClientService.rivalData = user;
				ClientConnection.player1 = false;
				ClientConnection.turn = true;
				if(user.getProfileImage() != null){
					byte[] imageByteArray = Base64.decodeBase64(user.getProfileImage());
					try {
						BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imageByteArray));

						if (imag != null) {
							ClientService.rivalIMG = SwingFXUtils.toFXImage(imag, null);

						}else{
							ClientService.rivalIMG = null;
						}
					}catch (Exception e) {
						ClientService.rivalIMG = null;
					}
				}
				com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Game", event);
			} 
		}catch (SecurityException e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error while starting the game");
			alert.setContentText("Question type doesn't match, please, indicate correctly for the next time");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			ClientConnection.playing=false;
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
			log.log(Level.WARNING, "Error en la partida", e);
			e.printStackTrace();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Game not found");
			alert.setContentText("An error ocurred while searching a game, please, try again later ");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			ClientConnection.playing=false;
			WindowUtilities.windowTransition("ThemeElection", event);
			log.log(Level.WARNING, "No se ha encontrado partida.", e);
			e.printStackTrace();
		} 
		/*textoUsernameRival.setText(Conexion_cliente.Nombre_j2);
		textoUsernameUser.setText(Conexion_cliente.Datos_Usuario.get(0));
		if(Conexion_cliente.Mi_Turno){
			Conexion_cliente.Primera_vez=false;
			try{
				com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
			}catch(Exception a){
				log.log(Level.WARNING, "Error al lanzar conexion", a);
				a.printStackTrace();
			}
		}
		if(Conexion_cliente.Mi_Turno){
			taPreguntas.setText(com.pasapalabra.game.utilities.Conexion_cliente.Pregunta);
			Conexion_cliente.Respuesta="...........................";
			new Thread(new Runnable() {  
				@Override  
				public void run() {  
					while(Conexion_cliente.Mi_Turno){
						for (int i = 20; i >0; i--) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							textoTiempoUsuario.setText(Integer.toString(i));
							if(!Conexion_cliente.Respuesta.equals("...........................")){
								i=20;
								System.out.println(Conexion_cliente.Respuesta+" respuesta");
							}
						}

						if(ClientConnection.playing==true){


							com.pasapalabra.game.utilities.Conexion_cliente.Respuesta="...........................";


							try{
								com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Responder_Pregunta.toString(), null);

							}catch(Exception a){
								a.printStackTrace();
							} 




							if(Conexion_cliente.Acierto==true){
								//				Alert alert = new Alert(AlertType.INFORMATION);
								//				alert.setTitle("Respuesta acertada");
								//				alert.setHeaderText("Ha acertado la respuesta a la pregunta con la letra: "+Conexion_cliente.Letra_Actual);
								//alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								//				alert.show();
								try{
									int foo =Integer.parseInt(textoPuntuacionU.getText().substring(11));
									foo++;
									textoPuntuacionU.setText("Acertadas: "+Integer.toString(foo));

									int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual);System.out.println(Num_Letra+" la posicion de la letra");
									panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+Conexion_cliente.Letra_Actual+"-green.png")));
								}catch(Exception a){
									a.printStackTrace();
								}
							}
							else{
								//				Alert alert = new Alert(AlertType.INFORMATION);
								//				alert.setTitle("Respuesta fallada");
								//				alert.setHeaderText("Ha fallado la respuesta a la pregunta con la letra: "+Conexion_cliente.Letra_Actual);
								//alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								//				alert.show();

								int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual);System.out.println(Num_Letra);
								panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+Conexion_cliente.Letra_Actual+"-red.png")));
							}
							if(ClientConnection.playing==true){


								com.pasapalabra.game.utilities.Conexion_cliente.Respuesta=tfRespuesta.getText();
								try{

									com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
									taPreguntas.setText(com.pasapalabra.game.utilities.Conexion_cliente.Pregunta);
									tfRespuesta.setText("");
								}catch(Exception a){
									a.printStackTrace();
								}


							}  
							else{
								//TODO: terminar
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Partida acabada");
								alert.setHeaderText("Ha completado la partida");
								alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
								alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								alert.showAndWait();
								deVentana.windowTransition("Juego", event);
							}
							if(!ClientConnection.playing){
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Partida acabada");
								alert.setHeaderText("Ha completado la partida");
								alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
								alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								alert.showAndWait();
								deVentana.windowTransition("Juego", event);
							}

						}
						else{
							//TODO: terminar
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Partida acabada");
							alert.setHeaderText("Ha completado la partida");
							alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
							alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
							alert.showAndWait();
							deVentana.windowTransition("Juego", event);
						}
						//			utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
						//			try{
						//				utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
						//
						//			}catch(Exception a){
						//				a.printStackTrace();
						//			}
						//TODO: indicar si el usuario ha acertado o no



					}
				}
				}).start(); 
			}else{
				taPreguntas.setText("Espere a que su rival termine");
			}

			//Cerrar panel
			//    	new Thread(new RPanel( ventanaMenuDentro, menuDesplegable )).start();
			Platform.runLater(new RPanel(ventanaMenuDentro, menuDesplegable));
			//Eliminar selección de modo de juego y preguntas
			eliminarOpcionesPartida(true);

			//Dibujar letras
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					crearRosco(true,panelLetrasJugador);
					crearRosco(false,panelLetrasContrincante);
					anyadirGUI();
				}
			});
			if(Conexion_cliente.Mi_Turno){
				taPreguntas.setText(com.pasapalabra.game.utilities.Conexion_cliente.Pregunta);

			}else{
				taPreguntas.setText("Espere a que su rival termine");
			}
			System.out.println("Al hilo");
			if(!Conexion_cliente.Mi_Turno){


				System.out.println("Entro al bucle bucle en el hilo");


				new Thread(new Runnable() {  
					@Override  
					public void run() {  
						while(!Conexion_cliente.Mi_Turno&&ClientConnection.playing){
							System.out.println("Dentro del while");
							if(ClientConnection.playing){
								try{
									com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
								}catch(Exception a){
									a.printStackTrace();
									ClientConnection.playing=false;
								}	
								System.out.println("A comprobar si el rival ha hacertado");
								if(Conexion_cliente.Ha_Respondido){
									if(!Conexion_cliente.Mi_Turno&&Conexion_cliente.Acierto_rival){
										System.out.println("Ha acertado");
										int foo =Integer.parseInt(textoPuntuacionR.getText().substring(11));
										foo++;
										textoPuntuacionR.setText("Acertadas: "+Integer.toString(foo));
										int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual_Rival);
										try{
											panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+Conexion_cliente.Letra_Actual_Rival+"-green.png")));
										}catch(Exception a){
											a.printStackTrace();
										}
									}else if(!Conexion_cliente.Acierto_rival){
										System.out.println("No acierta el rival");
										System.out.println("Letra actual:"+Conexion_cliente.Letra_Actual);
										int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual_Rival);
										panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+Conexion_cliente.Letra_Actual_Rival+"-red.png")));
									}
								}
							}


						}
						System.out.println("Salgo del while");


						System.out.println("Hola");
						int i=23-22;
						System.out.println("A obtener pregunta en el servidor");
						if(ClientConnection.playing){
							try {
								com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
								taPreguntas.setText(Conexion_cliente.Pregunta);
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}			} 
						Conexion_cliente.Respuesta="...........................";
						new Thread(new Runnable() {  
							@Override  
							public void run() {  
								while(Conexion_cliente.Mi_Turno){
									for (int i = 20; i >0; i--) {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										textoTiempoUsuario.setText(Integer.toString(i));
										if(!Conexion_cliente.Respuesta.equals("...........................")){
											i=20;

										}
									}

									if(ClientConnection.playing==true){


										com.pasapalabra.game.utilities.Conexion_cliente.Respuesta="...........................";


										try{
											com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Responder_Pregunta.toString(), null);

										}catch(Exception a){
											a.printStackTrace();
										} 




										if(Conexion_cliente.Acierto==true){
											//				Alert alert = new Alert(AlertType.INFORMATION);
											//				alert.setTitle("Respuesta acertada");
											//				alert.setHeaderText("Ha acertado la respuesta a la pregunta con la letra: "+Conexion_cliente.Letra_Actual);
											//alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
											//				alert.show();
											try{
												int foo =Integer.parseInt(textoPuntuacionU.getText().substring(11));
												foo++;
												textoPuntuacionU.setText("Acertadas: "+Integer.toString(foo));

												int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual);System.out.println(Num_Letra+" la posicion de la letra");
												panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+Conexion_cliente.Letra_Actual+"-green.png")));
											}catch(Exception a){
												a.printStackTrace();
											}
										}
										else{
											//				Alert alert = new Alert(AlertType.INFORMATION);
											//				alert.setTitle("Respuesta fallada");
											//				alert.setHeaderText("Ha fallado la respuesta a la pregunta con la letra: "+Conexion_cliente.Letra_Actual);
											//alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
											//				alert.show();

											int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual);System.out.println(Num_Letra);
											panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+Conexion_cliente.Letra_Actual+"-red.png")));
										}
										if(ClientConnection.playing==true){


											com.pasapalabra.game.utilities.Conexion_cliente.Respuesta=tfRespuesta.getText();
											try{

												com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
												taPreguntas.setText(com.pasapalabra.game.utilities.Conexion_cliente.Pregunta);
												tfRespuesta.setText("");
											}catch(Exception a){
												a.printStackTrace();
											}


										}  
										else{
											//TODO: terminar
											Alert alert = new Alert(AlertType.INFORMATION);
											alert.setTitle("Partida acabada");
											alert.setHeaderText("Ha completado la partida");
											alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
											alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
											alert.showAndWait();
											deVentana.windowTransition("Juego", event);
										}
										if(!ClientConnection.playing){
											Alert alert = new Alert(AlertType.INFORMATION);
											alert.setTitle("Partida acabada");
											alert.setHeaderText("Ha completado la partida");
											alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
											alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
											alert.showAndWait();
											deVentana.windowTransition("Juego", event);
										}

									}
									else{
										//TODO: terminar
										Alert alert = new Alert(AlertType.INFORMATION);
										alert.setTitle("Partida acabada");
										alert.setHeaderText("Ha completado la partida");
										alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
										alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
										alert.showAndWait();
										deVentana.windowTransition("Juego", event);
									}
									//			utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
									//			try{
									//				utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
									//
									//			}catch(Exception a){
									//				a.printStackTrace();
									//			}
									//TODO: indicar si el usuario ha acertado o no



								}
							}
							}).start(); 
					}
				}).start(); 


		 */

	} 


	//TODO: Cambiar el método
	/**Evento de ratón. Cuando se ha producido una elección.
	 * @param event
	 */

	@FXML
	void eleccionHecha(MouseEvent event){
		//			for (QuestionSelectedObject objetoSeleccionPregunta : aLEleccion) {
		//				if(event.getSource().equals(objetoSeleccionPregunta.getRectangulo()) 
		//						|| event.getSource().equals(objetoSeleccionPregunta.getTexto()) 
		//						|| event.getSource().equals(objetoSeleccionPregunta.getTituloSeccion())){
		//					if(objetoSeleccionPregunta.isElegido()){
		//						//No se hace nada porque ya está elegido.
		//					}else{
		//						if(objetoSeleccionPregunta.isModoDeJuego_notTipoPregunta()){
		//							for (QuestionSelectedObject objetoSeleccionPregunta1 : aLEleccion) {
		//								if(objetoSeleccionPregunta1.isModoDeJuego_notTipoPregunta()){
		//									if(objetoSeleccionPregunta == objetoSeleccionPregunta1){
		//										objetoSeleccionPregunta1.setElegido(true);
		//										objetoSeleccionPregunta1.getRectangulo().setFill(mdjSeleccionado.getFill());
		//										objetoSeleccionPregunta1.getRectangulo().setStroke(mdjSeleccionado.getStroke());
		//									}else{
		//										objetoSeleccionPregunta1.setElegido(false);
		//										objetoSeleccionPregunta1.getRectangulo().setFill(mdjNSeleccionado.getFill());
		//										objetoSeleccionPregunta1.getRectangulo().setStroke(mdjNSeleccionado.getStroke());
		//									}
		//								}
		//							}
		//						}else if (!objetoSeleccionPregunta.isModoDeJuego_notTipoPregunta()){
		//							for (QuestionSelectedObject objetoSeleccionPregunta2 : aLEleccion) {
		//								if(!objetoSeleccionPregunta2.isModoDeJuego_notTipoPregunta()){
		//									if(objetoSeleccionPregunta == objetoSeleccionPregunta2){
		//										objetoSeleccionPregunta2.setElegido(true);
		//										objetoSeleccionPregunta2.getRectangulo().setFill(tdpSeleccionado.getFill());
		//										objetoSeleccionPregunta2.getRectangulo().setStroke(tdpSeleccionado.getStroke());
		//									}else if(!objetoSeleccionPregunta2.isSeccion_notSeleccionable()){
		//										objetoSeleccionPregunta2.setElegido(false);
		//										objetoSeleccionPregunta2.getRectangulo().setFill(tdpNSeleccionado.getFill());
		//										objetoSeleccionPregunta2.getRectangulo().setStroke(tdpNSeleccionado.getStroke());
		//									}
		//								}
		//							}
		//						}
		//					}
		//
		//				}
		//			}
		//
	}

	//Servidor - Así se han seteado los textos del tiempo y la puntuación.
	//Se deben modificar con el paso del tiempo.
	/*


    @FXML public Text textoTiempoUsuario;
    @FXML public Text textoTiempoRival;

    @FXML public Text textoPuntuacionU;
    @FXML public Text textoPuntuacionR;
	 */
	/*
		@FXML
		void btnContestar(MouseEvent event) {
			//SERVIDOR
			//Respuesta
			if(!ClientConnection.playing){
				Alert alert=new Alert(AlertType.INFORMATION);
				alert.setTitle("Partida acabada");
				alert.setHeaderText("Ha completado la partida");
				alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert.showAndWait();
				deVentana.windowTransition("Juego", event);
			}
			else{	

				if(tfRespuesta.getText().length()==0){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Respuesta en blanco");
					alert.setHeaderText("No puedes responder en blanco");
					alert.setContentText("No se puede responder vacío,si no se te ocurre la respuesta, dale a pasar");
					alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert.showAndWait();
				}
				else{
					if(Conexion_cliente.Mi_Turno){
						if(ClientConnection.playing==true){


							com.pasapalabra.game.utilities.Conexion_cliente.Respuesta=tfRespuesta.getText();


							try{
								com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Responder_Pregunta.toString(), null);

							}catch(Exception a){
								a.printStackTrace();
							} 




							if(Conexion_cliente.Acierto==true){
								//				Alert alert = new Alert(AlertType.INFORMATION);
								//				alert.setTitle("Respuesta acertada");
								//				alert.setHeaderText("Ha acertado la respuesta a la pregunta con la letra: "+Conexion_cliente.Letra_Actual);
								//alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								//				alert.show();
								try{
									int foo =Integer.parseInt(textoPuntuacionU.getText().substring(11));
									foo++;
									textoPuntuacionU.setText("Acertadas: "+Integer.toString(foo));

									int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual);System.out.println(Num_Letra+" la posicion de la letra");
									panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+Conexion_cliente.Letra_Actual+"-green.png")));
								}catch(Exception a){
									a.printStackTrace();
								}
							}
							else{
								//				Alert alert = new Alert(AlertType.INFORMATION);
								//				alert.setTitle("Respuesta fallada");
								//				alert.setHeaderText("Ha fallado la respuesta a la pregunta con la letra: "+Conexion_cliente.Letra_Actual);
								//alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								//				alert.show();

								int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual);System.out.println(Num_Letra);
								panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+Conexion_cliente.Letra_Actual+"-red.png")));
							}
							if(ClientConnection.playing==true){


								com.pasapalabra.game.utilities.Conexion_cliente.Respuesta=tfRespuesta.getText();
								try{

									com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
									taPreguntas.setText(com.pasapalabra.game.utilities.Conexion_cliente.Pregunta);
									tfRespuesta.setText("");
								}catch(Exception a){
									a.printStackTrace();
								}


							}  
							else{
								//TODO: terminar
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Partida acabada");
								alert.setHeaderText("Ha completado la partida");
								alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
								alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								alert.showAndWait();
								deVentana.windowTransition("Juego", event);
							}
							if(!ClientConnection.playing){
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Partida acabada");
								alert.setHeaderText("Ha completado la partida");
								alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
								alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
								alert.showAndWait();
								deVentana.windowTransition("Juego", event);
							}

						}
						else{
							//TODO: terminar
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Partida acabada");
							alert.setHeaderText("Ha completado la partida");
							alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
							alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
							alert.showAndWait();
							deVentana.windowTransition("Juego", event);
						}
						//			utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
						//			try{
						//				utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
						//
						//			}catch(Exception a){
						//				a.printStackTrace();
						//			}
						//TODO: indicar si el usuario ha acertado o no

					}else{
						taPreguntas.setText("Espere a que el otro usuario acabe");
						new Thread(new Runnable() {  
							@Override  
							public void run() {  
								while(!Conexion_cliente.Mi_Turno&&ClientConnection.playing){
									System.out.println("Dentro del while");
									if(ClientConnection.playing){
										try{
											com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
										}catch(Exception a){
											a.printStackTrace();
											ClientConnection.playing=false;
										}	
										System.out.println("A comprobar si el rival ha hacertado");
										if(Conexion_cliente.Ha_Respondido){
											if(!Conexion_cliente.Mi_Turno&&Conexion_cliente.Acierto_rival){
												System.out.println("Ha acertado");
												int foo =Integer.parseInt(textoPuntuacionR.getText().substring(11));
												foo++;
												textoPuntuacionR.setText("Acertadas: "+Integer.toString(foo));
												int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual_Rival);
												try{
													panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+Conexion_cliente.Letra_Actual_Rival+"-green.png")));
												}catch(Exception a){
													a.printStackTrace();
												}
											}else if(!Conexion_cliente.Acierto_rival){
												System.out.println("No acierta el rival");
												System.out.println("Letra actual:"+Conexion_cliente.Letra_Actual);
												int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual_Rival);
												panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+Conexion_cliente.Letra_Actual_Rival+"-red.png")));
											}
										}
									}


								}
								System.out.println("Salgo del while");


								System.out.println("Hola");
								int i=23-22;
								System.out.println("A obtener pregunta en el servidor");
								if(ClientConnection.playing){
									try{
										com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);

										taPreguntas.setText(Conexion_cliente.Pregunta);

									}catch(Exception a){
										a.printStackTrace();
									}
								}
								else{
									//								Alert alert = new Alert(AlertType.INFORMATION);
									//								alert.setTitle("Partida acabada");
									//								alert.setHeaderText("Ha completado la partida");
									//								alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
									//								alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
									//								alert.showAndWait();
									//								deVentana.windowTransition("Juego", event);
								}
							}
						}).start(); 


					}
				}

			}

		}


		@FXML
		void btnPasar(MouseEvent event) {
			//SERVIDOR
			//Algo
			if(Conexion_cliente.Mi_Turno){
				if(ClientConnection.playing==true){
					com.pasapalabra.game.utilities.Conexion_cliente.Respuesta="Pasapalabra";
					try{
						com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Responder_Pregunta.toString(), null);
					}catch(Exception a){
						a.printStackTrace();
					}

					try{
						com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
						taPreguntas.setText(com.pasapalabra.game.utilities.Conexion_cliente.Pregunta);
						tfRespuesta.setText("");
					}catch(Exception a){
						a.printStackTrace();
					}
					if(!ClientConnection.playing){
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Partida acabada");
						alert.setHeaderText("Ha completado la partida");
						alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
						alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert.showAndWait();
						deVentana.windowTransition("Juego", event);
					}
				}else{
					//				Alert alert = new Alert(AlertType.INFORMATION);
					//				alert.setTitle("Partida acabada");
					//				alert.setHeaderText("Ha completado la partida");
					//				alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
					//				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					//				alert.showAndWait();
					//				deVentana.windowTransition("Juego", event);
				}
			}else{
				//TODO: No puedes pasar si no es tu turno
			}

		}
		@FXML
		void btnRendirse(MouseEvent event){
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
					if(ClientConnection.playing==true){
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
					deVentana.windowTransition("Juego", event);
				}
			}

		}
		//Para usar el textBox 
		private static int Pos_Letra(char letra_Actual) {



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
	 */
	@FXML
	void masInfo(MouseEvent event){
		if(!ClientConnection.playing){
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("AcercaDe", event);
		}
	}

	/*
		@FXML
		void entradoCSS(MouseEvent event){
			GameButton.seleccionar_notDeseleccionar(true,aLBotones,event);

		}
	 */

	@FXML //MIRAR
	void selected(MouseEvent event){
		for(Button boton: aLBotones){
			if(boton.isPressed()){
				boton.setOnAction((ActionEvent e) ->{
					boton.getStyleClass().add("addBobOk"); //Añadir por ejemplo AddBoOk y en el css poner .addBoOk 
				});
			}
			else{
				//System.out.println("Boton no seleccionado");
				log.log(Level.WARNING, "Error, boton no seleccionado");
			}
		}

	}

	@FXML //MIRAR
	void notSelected(MouseEvent event){
		for(Button boton: aLBotones){
			if(!boton.isPressed()){
				boton.setOnAction((ActionEvent e) ->{
					boton.getStyleClass().add("addBobOk"); //Añadir por ejemplo AddBoOk y en el css poner .addBoOk 
				});
			}
			else{
				//System.out.println("Boton si está seleccionado");
				log.log(Level.FINEST, "Boton si está seleccionado");
			}
		}

	}

	/*
		@FXML
		void salidoCSS(MouseEvent event){
			GameButton.seleccionar_notDeseleccionar(false,aLBotones,event);
		}
	 */

	//		public void eliminarOpcionesPartida(boolean eliminar_notAnyadir){
	//			panel.getChildren().remove(btnJugar);
	//			if(eliminar_notAnyadir){
	//				for (QuestionSelectedObject osp : aLEleccion) {
	//					if(osp.isSeccion_notSeleccionable()){
	//						panel.getChildren().remove(osp.getTituloSeccion());
	//					}else{
	//						//panel.getChildren().remove(osp.getRectangulo());
	//						//panel.getChildren().remove(osp.getTexto());
	//						//panel.getChildren().remove(Button); 
	//					}
	//				}
	//			}else{
	//				for (QuestionSelectedObject osp : aLEleccion) {
	//					if(osp.isSeccion_notSeleccionable()){
	//						panel.getChildren().add(osp.getTituloSeccion());
	//					}else{
	//						panel.getChildren().add(osp.getRectangulo());
	//						panel.getChildren().add(osp.getTexto());
	//					}
	//				}
	//			}
	//	}

	/**Method to create the rosco of the game with all the letters. 
	 * @param amigo_notEnemigo
	 * @param aLImgV
	 */


	/**Method that adds the styles designed in the juego.css
	 */
	public void anyadirGUI(){
		panel.getStylesheets().add("/css/juego.css");
		panel.getStylesheets().remove("/css/application.css");
		BackgroundImage myBII= new BackgroundImage(new Image(getClass().getResourceAsStream("/images/pspgamebg.png"),panel.getWidth(),panel.getHeight(),false,true),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		//then you set to your node
		panel.setBackground(new Background(myBII));}
	/*
			//Cuadro de texto
			tfRespuesta = new TextField();
			tfRespuesta.setLayoutX(250);
			tfRespuesta.setLayoutY(500);
			tfRespuesta.setPrefWidth(300);
			tfRespuesta.setAlignment(Pos.CENTER);
			tfRespuesta.setBackground(null);
			tfRespuesta.setStyle("-fx-border-color: gray;\n"
					+ "-fx-border-insets: 5;\n"
					+ "-fx-border-width: 0 0 1 0;\n"
					+ "-fx-background-color: transparent ;");



			imgUsuario.setLayoutX(150);
			imgUsuario.setLayoutY(100);
			imgRival.setLayoutX(525);
			imgRival.setLayoutY(100);

			textoTiempoUsuario.setLayoutX(20);
			textoTiempoUsuario.setLayoutY(350);
			textoTiempoRival.setLayoutX(720);
			textoTiempoRival.setLayoutY(350);

			textoPuntuacionU.setLayoutX(160);
			textoPuntuacionU.setLayoutY(30);
			textoPuntuacionR.setLayoutX(540);
			textoPuntuacionR.setLayoutY(30);

			rRendirse.setLayoutX(620);
			rRendirse.setLayoutY(560);
			rRendirse.setOpacity(0.3);
			textoRendirse.setLayoutX(670);
			textoRendirse.setLayoutY(585);

			textoUsernameRival.setLayoutX(525);
			textoUsernameRival.setLayoutY(300);

			textoUsernameUser.setLayoutX(150);
			textoUsernameUser.setLayoutY(300);

			aLBotonesJuego = new ArrayList<>();
			aLBotonesJuego.add(new GameButton(rRendirse, textoRendirse));
			aLBotonesJuego.add(new GameButton(rPasar, textoPasar));
			aLBotonesJuego.add(new GameButton(rContestar, textoContestar));
			//Servidor
			//Imagen del usuario y del rival
			//Usuario

			if(EventosLogIn.iAvatar!=null){
				imgUsuario.setImage(EventosLogIn.iAvatar);
			}else{
				String imagen = "fPerfil";
				Random rand = new Random();
				int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
				if(randomNum == 666){
					imagen = "fPerfilPirata";
				}

				Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),imgUsuario.getBoundsInLocal().getWidth(),imgUsuario.getBoundsInLocal().getHeight(),false,true);
				imgUsuario.setImage(i);
			}
			Circle clip = new Circle((imgUsuario.getX()+imgUsuario.getBoundsInParent().getWidth())/2, (imgUsuario.getY()+imgUsuario.getBoundsInParent().getHeight())/2, imgUsuario.getBoundsInLocal().getHeight()/2);
			imgUsuario.setClip(clip);
			imgUsuario.setSmooth(true); 
			imgUsuario.setCache(true); 

			//Rival
			if(imagenDelRival!=null){
				imgRival.setImage(imagenDelRival);
			}else{
				String imagen = "fPerfil";
				Random rand = new Random();
				int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
				if(randomNum == 666){
					imagen = "fPerfilPirata";
				}

				Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),imgRival.getBoundsInLocal().getWidth(),imgRival.getBoundsInLocal().getHeight(),false,true);
				imgRival.setImage(i);
			}
			Circle clip2 = new Circle((imgRival.getX()+imgRival.getBoundsInParent().getWidth())/2, (imgRival.getY()+imgRival.getBoundsInParent().getHeight())/2, imgUsuario.getBoundsInLocal().getHeight()/2);
			imgRival.setClip(clip2);
			imgRival.setSmooth(true); 
			imgRival.setCache(true); 

			//Servidor - Así se han declarado los textos de los nombres de usuario
			//Hay que setearlos
			/*
	    @FXML public Text textoUsernameUser;
	    @FXML public Text textoUsernameRival;
	 */

	//Efectos
	/*DropShadow dropShadow = new DropShadow();
			dropShadow.setColor(Color.DODGERBLUE);
			dropShadow.setRadius(25);
			dropShadow.setSpread(0.5);
			dropShadow.setBlurType(BlurType.GAUSSIAN);
			textoPuntuacionU.setEffect(dropShadow);
			textoTiempoUsuario.setEffect(dropShadow);
			textoTiempoRival.setEffect(dropShadow);
			textoPuntuacionR.setEffect(dropShadow);
			textoUsernameRival.setEffect(dropShadow);
			textoUsernameUser.setEffect(dropShadow);


			//		rContestar.setY(60);
			//		System.out.println(rContestar.getBoundsInParent());

			//Cuadro de preguntas
			//Parte rectágulo
			rPreguntas.setLayoutX(125);
			rPreguntas.setLayoutY(385);
			//		//Parte text área
			taPreguntas.setLayoutX(130);
			taPreguntas.setLayoutY(390);	
			ScrollBar scrollBarv = (ScrollBar)taPreguntas.lookup(".scroll-bar:vertical");
			scrollBarv.setDisable(true);

			//Añadir elementos
			panel.getChildren().add(tfRespuesta);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//TODO gui cargando
			pqGuiCargando(true);

			//		panel.getStylesheets().add("/css/juego.css");
			//		panel.getStylesheets().remove("/css/application.css");
		}

	 */


	//		/**Method that check that the theme is well selected
	//		 * @return true if yes
	//		 * @return false if the theme is not well selected
	//		 */
	//		boolean preguntasBienSeleccionadas(){
	//			/*
	//			int count = 0;
	//			for (QuestionSelectedObject objetoSeleccionPregunta : aLEleccion) {
	//				if(objetoSeleccionPregunta.isElegido()) count++;
	//				if(!objetoSeleccionPregunta.isSeccion_notSeleccionable()){
	//					if(objetoSeleccionPregunta.getTexto().getText().equals("VS Amigo")){
	//						if(objetoSeleccionPregunta.isElegido()){
	//							//Dialogo VS Amigo no disponible en versión 1.0;
	//							return false;
	//						}
	//					}
	//				}
	//			}
	//			if(count == 2) return true; 
	//			else{
	//				//Dialogo elige modo y el tema
	//				return false;
	//			}
	//			*/
	//			int count = 0;
	//			for (QuestionSelectedObject objetoSeleccionPregunta : aLEleccion) {
	//				if(objetoSeleccionPregunta.isElegido()) count++;
	//				if(!objetoSeleccionPregunta.isSeccion_notSeleccionable()){
	//					if(btnAmigo.isPressed()){
	//						if(objetoSeleccionPregunta.isElegido()){
	//							//Dialogo VS Amigo no disponible en versión 1.0;
	//							return false;
	//						}
	//					}
	//				}
	//			}
	//			if(count == 2) return true; 
	//			else{
	//				//Dialogo elige modo y el tema
	//				return false;
	//			}
	//		}
	//		
	/**Method that manages the E S of the panel
	 * @param event 
	 */
	@FXML
	void esPanel(MouseEvent event) {
		if(!ClientConnection.playing){
			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("Información");
			a.setHeaderText("Prueba a empezar juego para cerrar el panel");
			a.initModality(Modality.APPLICATION_MODAL);
			a.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			a.showAndWait();
		}else{
			Platform.runLater(new PanelThread( ventanaMenuDentro, menuDesplegable ));
			ventanaMenuDentro = !ventanaMenuDentro;
		}
	}

	public void BusquedaPartida(MouseEvent event){

		try {
			QuestionType type = QuestionType.All; 
			UserDTO udto= com.pasapalabra.game.service.ClientConnection.play(type);
			if(udto.getUserName().equals("Wait")){
				Alert alert = new Alert(AlertType.INFORMATION);

				alert.setTitle("Wait");

				alert.setHeaderText("Searching game");

				alert.setContentText("Wait, searching game...");

				alert.show();
			}else{
				log.log(Level.FINEST, "Transicion de ventana a Juego");
				com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Game", event);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.log(Level.WARNING, "Error, no se ha podido conectar");
			e.printStackTrace();
		} 
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*TODO: revise thisConexion_cliente.Correctas=0;
			Conexion_cliente.Incorrectas=0;
			panel.getChildren().remove(fondoCarga);
			panel.getChildren().remove(barraCarga);
			panel.getChildren().remove(textoCargaTranquil);
			panel.getChildren().remove(textoCargaBuscandoPartida);
			panel.applyCss();*/
		//SERVIDOR
		btnAmigos.setOpacity(0.3f);
		btnEstadisticas.setOpacity(0.3f);
		btnJuego.setOpacity(1f);
		btnPerfil.setOpacity(0.3f);
		btnCancel.setVisible(false);
		searchTXT.setVisible(false);
		if(ClientConnection.userIMG != null){
			imagenAvatar.setImage(ClientConnection.userIMG);
		}else{
			String imagen = "fPerfil";
			Random rand = new Random();
			int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			if(randomNum == 666){
				imagen = "fPerfilPirata";
			}

			Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
			imagenAvatar.setImage(i);
		}
		Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		imagenAvatar.setClip(clip);
		imagenAvatar.setSmooth(true); 
		imagenAvatar.setCache(true); 

		//Rellenar ArrayList menu desplegable
		this.menuDesplegable = new ArrayList<Node>();
		//Rectángulos y círculos
		menuDesplegable.add(rectanguloPanel);
		menuDesplegable.add(btnAmigos);
		menuDesplegable.add(btnCerrarSesion);
		menuDesplegable.add(btnEstadisticas);
		menuDesplegable.add(btnJuego);
		menuDesplegable.add(btnPerfil);
		menuDesplegable.add(circuloPanel);
		menuDesplegable.add(circuloPlus);
		menuDesplegable.add(logopsp);
		menuDesplegable.add(textoLogeadoComo);
		menuDesplegable.add(textoESPanel);
		menuDesplegable.add(textoLogeadoComo);
		menuDesplegable.add(textoPlus);

		//Variables de usuario
		menuDesplegable.add(textoNombreDeUsuario);
		textoNombreDeUsuario.setText(com.pasapalabra.game.service.ClientConnection.userInfo.getUserName());
		menuDesplegable.add(imagenAvatar);

		/* ya no hay rectangulos y textos. No se necesita objeto seleccion pregunta
			//Rellenar elementos.
			this.aLEleccion = new ArrayList<QuestionSelectedObject>();
			//Elementos título
			aLEleccion.add(new QuestionSelectedObject(selecMJ));
			aLEleccion.add(new QuestionSelectedObject(selecTP));

			//Elementos elegibles (No hay interfaz, no confundirse).
			aLEleccion.add(new QuestionSelectedObject(btnAleatorio, btnAmigo, true));
			aLEleccion.add(new QuestionSelectedObject(tVSAmigo, vsAmigo, true));
			aLEleccion.add(new QuestionSelectedObject(tTodos, rTodos, false));
			aLEleccion.add(new QuestionSelectedObject(tGeo, rGeo, false));
			aLEleccion.add(new QuestionSelectedObject(tArte, rArte, false));
			aLEleccion.add(new QuestionSelectedObject(tHist, rHist, false));
			aLEleccion.add(new QuestionSelectedObject(tCien, rCien, false));
			aLEleccion.add(new QuestionSelectedObject(tEntret, rEntret, false));
			aLEleccion.add(new QuestionSelectedObject(tDep, rDep, false));
		 */
		aLBotones.add(btnArte); 
		aLBotones.add(btnCiencia); 
		aLBotones.add(btnDeportes); 
		aLBotones.add(btnEntretenimiento); 
		aLBotones.add(btnGeo); 
		aLBotones.add(btnHistoria); 

		panel.getStylesheets().add("/css/application.css");
	}
}