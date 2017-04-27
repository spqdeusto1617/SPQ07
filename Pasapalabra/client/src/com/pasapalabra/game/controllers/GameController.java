package com.pasapalabra.game.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.utilities.WindowUtilities;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**Class that manage events from Game.fxml
 * @author alvaro
 *
 */
public class GameController implements Initializable{
	
	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(ThemeController.class.getName());


	//*COMIENZO DE DECLARACIÓN DE ATRIBUTOS*
	//_________________________________________
	public static final int PUNTOSTOTALES = 27;
	public Image imagenDelRival;
	public int vecesHechoX = 0;
	public int vecesHechoY = 0;
	public boolean ventanaMenuDentro = false;
	private static boolean rightAnswered;
	public static QuestionDTO currentQuestion;
	
	@FXML public Rectangle rPreguntas; 
	@FXML public TextArea taPreguntas;

	@FXML public Text textoTiempoUsuario; 
	@FXML public Text textoTiempoRival; 

	@FXML public Text textoPuntuacionU;
	@FXML public Text textoPuntuacionR;

	@FXML public Text textoUsernameUser;
	@FXML public Text textoUsernameRival;
	
	@FXML public TextField tfRespuesta; 
	
	@FXML public Button btnContestar;
	@FXML public Button btnPasar;
	@FXML public Button btnRendirse;
	
	
	/*TODO: check this@FXML
	void entradoCSS(MouseEvent event){
		GameButton.seleccionar_notDeseleccionar(true,aLBotonesJuego,event);
	}
	@FXML
	void salidoCSS(MouseEvent event){
		GameButton.seleccionar_notDeseleccionar(false,aLBotonesJuego,event);
	}*/
	
	
	/**Method called when Contestar button is pressed in the game. 
	 * If the answer is in blank, error
	 * @param event
	 */
	@FXML
	void btnContestar(MouseEvent event) {
		//SERVIDOR
		//Respuesta
		if(com.pasapalabra.game.utilities.ClientConnexion.gameEnd){
			UserScoreDTO score = null;
			try {
				score = com.pasapalabra.game.utilities.ClientConnexion.getResults();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setTitle("Partida acabada");
			alert.setHeaderText("Ha completado la partida");
			alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+score.getRightAnswered()+" respuestas correctas y: "+score.getWrongAnswered()+" respuestas incorrectas");
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			WindowUtilities.windowTransition("Juego", event);
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
				//if(Conexion_cliente.Mi_Turno){
					if(!com.pasapalabra.game.utilities.ClientConnexion.gameEnd){
						


						try{
							rightAnswered = com.pasapalabra.game.utilities.ClientConnexion.answerQuestion(tfRespuesta.getText());

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
							//				Alert alert = new Alert(AlertType.INFORMATION);
							//				alert.setTitle("Respuesta acertada");
							//				alert.setHeaderText("Ha acertado la respuesta a la pregunta con la letra: "+Conexion_cliente.Letra_Actual);
							//alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
							//				alert.show();
							try{
								int foo =Integer.parseInt(textoPuntuacionU.getText().substring(11));
								foo++;
								textoPuntuacionU.setText("Acertadas: "+Integer.toString(foo));

								int Num_Letra=Pos_Letra(com.pasapalabra.game.utilities.ClientConnexion.currentLetter);System.out.println(Num_Letra+" la posicion de la letra");
								//TODO: check thispanelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/verde/"+com.pasapalabra.game.utilities.ClientConnexion.currentLetter+"-green.png")));
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

							int Num_Letra=Pos_Letra(com.pasapalabra.game.utilities.ClientConnexion.currentLetter);System.out.println(Num_Letra);
							//TODO: check thispanelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/images/letras/rojo/"+com.pasapalabra.game.utilities.ClientConnexion.currentLetter+"-red.png")));
						}
						
						try {
							com.pasapalabra.game.utilities.ClientConnexion.endGame();
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
						if(com.pasapalabra.game.utilities.ClientConnexion.gameEnd){
							UserScoreDTO score = null;
							try {
								score = com.pasapalabra.game.utilities.ClientConnexion.getResults();
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
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Partida acabada");
							alert.setHeaderText("Ha completado la partida");
							alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+score.getRightAnswered()+" respuestas correctas y: "+score.getWrongAnswered()+" respuestas incorrectas");
							alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
							alert.showAndWait();
							WindowUtilities.windowTransition("Juego", event);
						/*
						if(){
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Partida acabada");
							alert.setHeaderText("Ha completado la partida");
							alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
							alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
							alert.showAndWait();
							deVentana.transicionVentana("Juego", event);
						}*/

					}
					else{
					try {
						currentQuestion = com.pasapalabra.game.utilities.ClientConnexion.getQuestion();
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
					//			utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
					//			try{
					//				utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
					//
					//			}catch(Exception a){
					//				a.printStackTrace();
					//			}
					//TODO: indicar si el usuario ha acertado o no

					}/*else{//TODO: make it multiplayer
					taPreguntas.setText("Espere a que el otro usuario acabe");
					new Thread(new Runnable() {  
						@Override  
						public void run() {  
							while(!Conexion_cliente.Mi_Turno&&juegoEnCurso){
								System.out.println("Dentro del while");
								if(juegoEnCurso){
									try{
										com.pasapalabra.game.utilities.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilities.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilities.Acciones_servidor.Obtener_Pregunta.toString(), null);
									}catch(Exception a){
										a.printStackTrace();
										juegoEnCurso=false;
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
							if(juegoEnCurso){
								try{
									com.pasapalabra.game.utilities.ClientConnexion.startConnection(args);
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
								//								deVentana.transicionVentana("Juego", event);
							}
						}
					}).start(); 


				}
			}*/

			}
		}

	}


	
	/**Method called when Pasar button is pressed
	 * It manages everything related to when a user goes to another question. 
	 * It is the user turn and says pasapalabra the letter changes. 
	 * To the contrary, the user can not go to another question. 
	 * @param event
	 */
	@FXML
	void btnPasar(MouseEvent event) {
		//SERVIDOR
		//Algo
		/*if(Conexion_cliente.Mi_Turno){
			if(juegoEnCurso==true){
				com.pasapalabra.game.utilities.ClientConnexion.pasapalabra=true; 
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
				if(com.pasapalabra.game.utilities.ClientConnexion.gameEnd){
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Partida acabada");
					alert.setHeaderText("Ha completado la partida");
					alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
					alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert.showAndWait();
					deVentana.transicionVentana("Juego", event);
				}
			}else{
				//				Alert alert = new Alert(AlertType.INFORMATION);
				//				alert.setTitle("Partida acabada");
				//				alert.setHeaderText("Ha completado la partida");
				//				alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
				//				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				//				alert.showAndWait();
				//				deVentana.transicionVentana("Juego", event);
			}
		}*/
				
		try{
			rightAnswered = com.pasapalabra.game.utilities.ClientConnexion.answerQuestion("Pasapalabra");
			if(!rightAnswered)System.out.println("Ha habido un error al pasar de palabra");
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
			currentQuestion = com.pasapalabra.game.utilities.ClientConnexion.getQuestion();
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
		/*else{
			//TODO: No puedes pasar si no es tu turno
		}*/

	}
	
	/**Method called when Rendirse button is called in the game. 
	 * If the user can (when it is his/her turn) give up. To the contrary, the user
	 * can not give up. 
	 * @param event
	 */
	@FXML
	void btnRendirse(MouseEvent event){
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
			
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		taPreguntas.setText(currentQuestion.getQuestion());
	}
}
