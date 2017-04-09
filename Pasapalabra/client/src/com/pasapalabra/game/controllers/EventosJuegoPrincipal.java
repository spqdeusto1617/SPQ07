package com.pasapalabra.game.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.pasapalabra.game.objetos.BotonJuego;
import com.pasapalabra.game.utilidades.deVentana;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.objetos.ObjetoSeleccionPregunta;
import com.pasapalabra.game.utilidades.RCarga;
import com.pasapalabra.game.utilidades.RPanel;
import com.pasapalabra.game.utilidades.ClientConnexion;

public class EventosJuegoPrincipal implements Initializable{
	
	public static Logger log = com.pasapalabra.game.utilidades.AppLogger.getWindowLogger(EventosJuego.class.getName());


	//*COMIENZO DE DECLARACIÓN DE ATRIBUTOS*
	//_________________________________________
	public static final int PUNTOSTOTALES = 27;
	public Image imagenDelRival;
	public int vecesHechoX = 0;
	public int vecesHechoY = 0;
	public static boolean juegoEnCurso = false;
	public boolean ventanaMenuDentro = false;


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
	
	
	@FXML
	void entradoCSS(MouseEvent event){
		BotonJuego.seleccionar_notDeseleccionar(true,aLBotonesJuego,event);
	}
	@FXML
	void salidoCSS(MouseEvent event){
		BotonJuego.seleccionar_notDeseleccionar(false,aLBotonesJuego,event);
	}
	
	
	@FXML
	void btnContestar(MouseEvent event) {
		//SERVIDOR
		//Respuesta
		if(!juegoEnCurso){
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setTitle("Partida acabada");
			alert.setHeaderText("Ha completado la partida");
			alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			deVentana.transicionVentana("Juego", event);
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
					if(juegoEnCurso==true){


						com.pasapalabra.game.utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();


						try{
							com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);

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
								panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/com/pasapalabra/game/images/letras/verde/"+Conexion_cliente.Letra_Actual+"-green.png")));
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
							panelLetrasJugador.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/com/pasapalabra/game/images/letras/rojo/"+Conexion_cliente.Letra_Actual+"-red.png")));
						}
						if(juegoEnCurso==true){


							com.pasapalabra.game.utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
							try{

								com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
								taPreguntas.setText(com.pasapalabra.game.utilidades.Conexion_cliente.Pregunta);
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
							deVentana.transicionVentana("Juego", event);
						}
						if(!juegoEnCurso){
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Partida acabada");
							alert.setHeaderText("Ha completado la partida");
							alert.setContentText("Se ha terminado la partida, y su resultado ha sido: "+Conexion_cliente.Correctas+" respuestas correctas y: "+Conexion_cliente.Incorrectas+" respuestas incorrectas");
							alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
							alert.showAndWait();
							deVentana.transicionVentana("Juego", event);
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
						deVentana.transicionVentana("Juego", event);
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
							while(!Conexion_cliente.Mi_Turno&&juegoEnCurso){
								System.out.println("Dentro del while");
								if(juegoEnCurso){
									try{
										com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
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
												panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/com/pasapalabra/game/images/letras/verde/"+Conexion_cliente.Letra_Actual_Rival+"-green.png")));
											}catch(Exception a){
												a.printStackTrace();
											}
										}else if(!Conexion_cliente.Acierto_rival){
											System.out.println("No acierta el rival");
											System.out.println("Letra actual:"+Conexion_cliente.Letra_Actual);
											int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual_Rival);
											panelLetrasContrincante.get(Num_Letra).setImage(new Image(getClass().getResourceAsStream("/com/pasapalabra/game/images/letras/rojo/"+Conexion_cliente.Letra_Actual_Rival+"-red.png")));
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
									com.pasapalabra.game.utilidades.ClientConnexion.startConnection(args);
									com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);

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
			}

		}

	}


	
	@FXML
	void btnPasar(MouseEvent event) {
		//SERVIDOR
		//Algo
		if(Conexion_cliente.Mi_Turno){
			if(juegoEnCurso==true){
				com.pasapalabra.game.utilidades.ClientConnexion.pasapalabra=true; 
				try{
					com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
				}catch(Exception a){
					a.printStackTrace();
				}

				try{
					com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
					taPreguntas.setText(com.pasapalabra.game.utilidades.Conexion_cliente.Pregunta);
					tfRespuesta.setText("");
				}catch(Exception a){
					a.printStackTrace();
				}
				if(!juegoEnCurso){
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
				if(juegoEnCurso==true){
					try{	
						com.pasapalabra.game.utilidades.Conexion_cliente.Respuesta="Rendirse";
						com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local, com.pasapalabra.game.utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
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

	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
