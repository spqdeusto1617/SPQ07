package controllers;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objetos.BotonJuego;
import objetos.ObjetoSeleccionPregunta;
import utilidades.Conexion_cliente;
import utilidades.RCarga;
import utilidades.RPanel;
import utilidades.deVentana;

public class EventosJuego extends ClaseExtensora implements Initializable{


	//Logger de la clase

	public static Logger log = utilidades.AppLogger.getWindowLogger(EventosJuego.class.getName());


	//*COMIENZO DE DECLARACIÓN DE ATRIBUTOS*
	//_________________________________________
	public static final int PUNTOSTOTALES = 27;
	public Image imagenDelRival;
	public int vecesHechoX = 0;
	public int vecesHechoY = 0;
	public static boolean juegoEnCurso = false;
	public boolean ventanaMenuDentro = false;
	public ArrayList<ImageView> panelLetrasJugador = new ArrayList<>(); //Panel con todos los labels del jugador
	public ArrayList<ImageView> panelLetrasContrincante = new ArrayList<>(); //Panel con todos los labels del contrincante
	public ArrayList<Node> menuDesplegable; //Colección de todos los elementos del menu desplegable.
	public ArrayList<ObjetoSeleccionPregunta> aLEleccion; //Elección del tipo de juego que se quiere llevar a cabo.
	public ArrayList<BotonJuego> aLBotonesJuego;


	//Declaración del panel
	@FXML public Pane panel;


	@FXML public Text textoESPanel;

	@FXML public Text textoMiPerfil;

	@FXML public ImageView logopsp;

	@FXML public Text textoCerrarSesion;

	@FXML public Text textoPlus;

	@FXML public Rectangle rectanguloPanel;

	@FXML public Circle circuloPlus;

	@FXML public Rectangle rectanguloCerrarSesion;

	@FXML public Rectangle rectanguloAmigos;

	@FXML public Text textoJugar;

	@FXML public Text textoLogeadoComo;

	@FXML public Rectangle rectanguloMiPerfil;

	@FXML public Rectangle rectanguloJugar;

	@FXML public Text textoAmigos;

	@FXML public Circle circuloPanel;

	@FXML public Text textoNombreDeUsuario;

	@FXML public Text textoEstadisticas;

	@FXML public ImageView imagenAvatar;

	@FXML public Rectangle rectanguloEstadisticas;




	//Elementos que desaparecen a la derecha
	//Texto
	@FXML public Text selecMJ;
	@FXML public Text selecTP;

	//Seleccionables
	@FXML public Rectangle vsAleatorio;
	@FXML public Rectangle vsAmigo;
	@FXML public Rectangle rTodos;
	@FXML public Rectangle rGeo;
	@FXML public Rectangle rArte;
	@FXML public Rectangle rHist;
	@FXML public Rectangle rCien;
	@FXML public Rectangle rEntret;
	@FXML public Rectangle rDep;
	@FXML public Rectangle rJug;

	//Textos de los seleccionables
	@FXML public Text tVSAleatorio;
	@FXML public Text tVSAmigo;
	@FXML public Text tTodos;
	@FXML public Text tGeo;
	@FXML public Text tArte;
	@FXML public Text tHist;
	@FXML public Text tCien;
	@FXML public Text tEntret;
	@FXML public Text tDep;
	@FXML public Text textoRJug;

	//Rectangulos de estilo
	@FXML public Rectangle tdpSeleccionado;
	@FXML public Rectangle tdpNSeleccionado;
	@FXML public Rectangle mdjSeleccionado;
	@FXML public Rectangle mdjNSeleccionado;


	//Elementos de la gui de juego no inicializados [Salvo algunos para copiar el StyleSheet ya que en javafx no se pueden clonar los nodos].
	public TextField tfRespuesta;
	@FXML public Rectangle rContestar;
	@FXML public Rectangle rPreguntas;
	@FXML public TextArea taPreguntas;
	@FXML public Rectangle rPasar;
	@FXML public Text textoContestar;
	@FXML public Text textoPasar;
	@FXML public ImageView imgUsuario;
	@FXML public ImageView imgRival;
	@FXML public Text textoTiempoUsuario;
	@FXML public Text textoTiempoRival;

	@FXML public Text textoPuntuacionU;
	@FXML public Text textoPuntuacionR;

	@FXML public Rectangle rRendirse;
	@FXML public Text textoRendirse;

	@FXML public Text textoUsernameUser;
	@FXML public Text textoUsernameRival;



	//Pantalla de carga
	@FXML public Rectangle fondoCarga;
	@FXML public ProgressBar barraCarga;
	@FXML public Text textoCargaTranquil;
	@FXML public Text textoCargaBuscandoPartida;


	public Thread hiloDeCarga = new Thread(new RCarga(this));
	//_________________________________________
	//*FIN DE DECLARACIÓN DE ATRIBUTOS


	@FXML //TERMINADO
	void btnJugar(MouseEvent event) {
		log.log(Level.FINEST, "Botón de jugar pulsado y JuegoEnCurso = " + juegoEnCurso);
		//Compruebo si se está jugando
		if(juegoEnCurso){
			//Si se está jugando, entonces creo una alerta para decir que está ya el juego en curso
			//Creo una alerta de tipo informativa
			Alert alert = new Alert(AlertType.INFORMATION);
			log.log(Level.FINEST, "Alerta de información creada");
			//Añado título a la alerta
			alert.setTitle("Información");
			log.log(Level.FINEST, "Título añadido a la alerta");
			//Seteo el contenido de la cabecera a nulo
			alert.setHeaderText(null);
			log.log(Level.FINEST, "Cabecera nula añadida a la alerta");
			//Seteo el contenido
			alert.setContentText("Ya estás jugando una partida, si quieres dejarlo\nríndete.");
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
			alert.setTitle("Información");
			log.log(Level.FINEST, "Título añadido a la alerta");
			//Dejamos que la cabecera sea nula
			alert.setHeaderText(null);
			log.log(Level.FINEST, "Cabecera nula añadida a la alerta");
			//Añadimos el contenido que tendrá la alerta
			alert.setContentText("Ya estás en la ventana de juego, selecciona un modo.");
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
	void btnAmigos(MouseEvent event) {
		if(juegoEnCurso){
			//Alerta
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Juego en curso");
			alert2.setHeaderText(null);
			alert2.setContentText("No puedes abandonar la ventana mientras el juego esté en curso."
					+ " Termina la partida para poder avanzar a esa ventana");
			alert2.initModality(Modality.APPLICATION_MODAL);
			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert2.showAndWait();
			log.log(Level.FINE, "El juego está en curso.");
		}else{
			log.log(Level.FINEST, "Transicion de ventana a Amigos");
			utilidades.deVentana.transicionVentana("Amigos", event);
		}
	}

	@FXML
	void btnMiPerfil(MouseEvent event) {
		if(juegoEnCurso){
			//Alerta
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Juego en curso");
			alert2.setHeaderText(null);
			alert2.setContentText("No puedes abandonar la ventana mientras el juego esté en curso."
					+ " Termina la partida para poder avanzar a esa ventana");
			alert2.initModality(Modality.APPLICATION_MODAL);
			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert2.showAndWait();
			log.log(Level.FINE, "El juego está en curso.");
		}else{
			log.log(Level.FINEST, "Transicion de ventana a Perfil");
			utilidades.deVentana.transicionVentana("Perfil", event);
		}
	}

	@FXML
	void btnEstadisticas(MouseEvent event) {
		if(juegoEnCurso){
			//Alerta
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Juego en curso");
			alert2.setHeaderText(null);
			alert2.setContentText("No puedes abandonar la ventana mientras el juego esté en curso."
					+ " Termina la partida para poder avanzar a esa ventana");
			alert2.initModality(Modality.APPLICATION_MODAL);
			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert2.showAndWait();
			log.log(Level.FINE, "El juego está en curso.");
		}else{
			log.log(Level.FINEST, "Transicion de ventana a Estadisticas");
			utilidades.deVentana.transicionVentana("Estadisticas", event);
		}
	}


	//Elimina nivel de transparencia
	@FXML
	void entrado(MouseEvent event) {
		utilidades.deVentana.efectoTransparenciaOnHover(event, this);
	}

	//Añade nivel de transparencia
	@FXML
	void salido(MouseEvent event) {
		utilidades.deVentana.efectoTransparenciaOnHover(event, this);
	}

	@FXML
	void btnCerrarSesion(MouseEvent event) {
		log.log(Level.FINEST, "Cierre de sesión");
		utilidades.deVentana.cerrarSesion(event);
	}

	@FXML
	void aJugar(MouseEvent event){
		if(!preguntasBienSeleccionadas())return;
		if(juegoEnCurso) return;
		/*Inhabilitamos rápidamente que el botón de jugar vuelva a poder ser pulsado.
		 *(Es posible que después de varias comprobaciones necesitemos volverlo a poner a falso)
		 */
		juegoEnCurso = true;
		//Servidor
		//aLEleccion es el arrayList de la elección (Modalidad + Tema) y todos los elementos
		String[]Tipo=new String[2];
		for (ObjetoSeleccionPregunta obsp : aLEleccion) {
			if (obsp.isElegido()) {
				if(obsp.isModoDeJuego_notTipoPregunta()){
					//ESTE ES EL MODO DE JUEGO ELEGIDO
					Tipo[0]=obsp.getTexto().getText();

				}else{
					//ESTE ES EL TEMA DE JUEGO ELEGIDO
					Tipo[1]=obsp.getTexto().getText();
				}
			}
		}

		//
		//Hasta encontrar partida, aquí.
		//Aquí se crearía un hilo que estaría esperando y que no se reanudaría hasta que se encontrase una partida
		//Lanzamos la partida

		try {
			utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Jugar.toString(), Tipo);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error al iniciar la partida");
			alert.setContentText("El tipo de pregunta no encaja, por favor, indique correctamente la próxima vez");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			juegoEnCurso=false;
			utilidades.deVentana.transicionVentana("Juego", event);
			log.log(Level.WARNING, "Error en la partida", e);
			e.printStackTrace();

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Partida no encontrada");
			alert.setContentText("Ha ocurrido un error al encontrar alguna partida, por favor, intenteló de nuevo más tarde");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			juegoEnCurso=false;
			deVentana.transicionVentana("Juego", event);
			log.log(Level.WARNING, "No se ha encontrado partida.", e);
			e.printStackTrace();
		} 
		textoUsernameRival.setText(Conexion_cliente.Nombre_j2);
		textoUsernameUser.setText(Conexion_cliente.Datos_Usuario.get(0));
		if(Conexion_cliente.Mi_Turno){
			Conexion_cliente.Primera_vez=false;
			try{
				utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
			}catch(Exception a){
				log.log(Level.WARNING, "Error al lanzar conexion", a);
				a.printStackTrace();
			}
		}
		if(Conexion_cliente.Mi_Turno){
			taPreguntas.setText(utilidades.Conexion_cliente.Pregunta);
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
						
						if(juegoEnCurso==true){


							utilidades.Conexion_cliente.Respuesta="...........................";


							try{
								utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);

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
									panelLetrasJugador.get(Num_Letra).setImage(new Image("images/letras/verde/"+Conexion_cliente.Letra_Actual+"-green.png"));
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
								panelLetrasJugador.get(Num_Letra).setImage(new Image("images/letras/rojo/"+Conexion_cliente.Letra_Actual+"-red.png"));
							}
							if(juegoEnCurso==true){


								utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
								try{

									utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
									taPreguntas.setText(utilidades.Conexion_cliente.Pregunta);
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
				taPreguntas.setText(utilidades.Conexion_cliente.Pregunta);

			}else{
				taPreguntas.setText("Espere a que su rival termine");
			}
			System.out.println("Al hilo");
			if(!Conexion_cliente.Mi_Turno){


				System.out.println("Entro al bucle bucle en el hilo");


				new Thread(new Runnable() {  
					@Override  
					public void run() {  
						while(!Conexion_cliente.Mi_Turno&&juegoEnCurso){
							System.out.println("Dentro del while");
							if(juegoEnCurso){
								try{
									utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
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
											panelLetrasContrincante.get(Num_Letra).setImage(new Image("images/letras/verde/"+Conexion_cliente.Letra_Actual_Rival+"-green.png"));
										}catch(Exception a){
											a.printStackTrace();
										}
									}else if(!Conexion_cliente.Acierto_rival){
										System.out.println("No acierta el rival");
										System.out.println("Letra actual:"+Conexion_cliente.Letra_Actual);
										int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual_Rival);
										panelLetrasContrincante.get(Num_Letra).setImage(new Image("images/letras/rojo/"+Conexion_cliente.Letra_Actual_Rival+"-red.png"));
									}
								}
							}


						}
						System.out.println("Salgo del while");


						System.out.println("Hola");
						int i=23-22;
						System.out.println("A obtener pregunta en el servidor");
						if(juegoEnCurso){
							try {
								utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
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
									
									if(juegoEnCurso==true){


										utilidades.Conexion_cliente.Respuesta="...........................";


										try{
											utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);

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
												panelLetrasJugador.get(Num_Letra).setImage(new Image("images/letras/verde/"+Conexion_cliente.Letra_Actual+"-green.png"));
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
											panelLetrasJugador.get(Num_Letra).setImage(new Image("images/letras/rojo/"+Conexion_cliente.Letra_Actual+"-red.png"));
										}
										if(juegoEnCurso==true){


											utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
											try{

												utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
												taPreguntas.setText(utilidades.Conexion_cliente.Pregunta);
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

								
									
								}
							}
							}).start(); 
					}
				}).start(); 




			} 
		}




		/**Evento de ratón. Cuando se ha producido una elección.
		 * @param event
		 */
		@FXML
		void eleccionHecha(MouseEvent event){
			for (ObjetoSeleccionPregunta objetoSeleccionPregunta : aLEleccion) {
				if(event.getSource().equals(objetoSeleccionPregunta.getRectangulo()) 
						|| event.getSource().equals(objetoSeleccionPregunta.getTexto()) 
						|| event.getSource().equals(objetoSeleccionPregunta.getTituloSeccion())){
					if(objetoSeleccionPregunta.isElegido()){
						//No se hace nada porque ya está elegido.
					}else{
						if(objetoSeleccionPregunta.isModoDeJuego_notTipoPregunta()){
							for (ObjetoSeleccionPregunta objetoSeleccionPregunta1 : aLEleccion) {
								if(objetoSeleccionPregunta1.isModoDeJuego_notTipoPregunta()){
									if(objetoSeleccionPregunta == objetoSeleccionPregunta1){
										objetoSeleccionPregunta1.setElegido(true);
										objetoSeleccionPregunta1.getRectangulo().setFill(mdjSeleccionado.getFill());
										objetoSeleccionPregunta1.getRectangulo().setStroke(mdjSeleccionado.getStroke());
									}else{
										objetoSeleccionPregunta1.setElegido(false);
										objetoSeleccionPregunta1.getRectangulo().setFill(mdjNSeleccionado.getFill());
										objetoSeleccionPregunta1.getRectangulo().setStroke(mdjNSeleccionado.getStroke());
									}
								}
							}
						}else if (!objetoSeleccionPregunta.isModoDeJuego_notTipoPregunta()){
							for (ObjetoSeleccionPregunta objetoSeleccionPregunta2 : aLEleccion) {
								if(!objetoSeleccionPregunta2.isModoDeJuego_notTipoPregunta()){
									if(objetoSeleccionPregunta == objetoSeleccionPregunta2){
										objetoSeleccionPregunta2.setElegido(true);
										objetoSeleccionPregunta2.getRectangulo().setFill(tdpSeleccionado.getFill());
										objetoSeleccionPregunta2.getRectangulo().setStroke(tdpSeleccionado.getStroke());
									}else if(!objetoSeleccionPregunta2.isSeccion_notSeleccionable()){
										objetoSeleccionPregunta2.setElegido(false);
										objetoSeleccionPregunta2.getRectangulo().setFill(tdpNSeleccionado.getFill());
										objetoSeleccionPregunta2.getRectangulo().setStroke(tdpNSeleccionado.getStroke());
									}
								}
							}
						}
					}

				}
			}

		}

		//Servidor - Así se han seteado los textos del tiempo y la puntuación.
		//Se deben modificar con el paso del tiempo.
		/*


    @FXML public Text textoTiempoUsuario;
    @FXML public Text textoTiempoRival;

    @FXML public Text textoPuntuacionU;
    @FXML public Text textoPuntuacionR;
		 */

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


							utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();


							try{
								utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);

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
									panelLetrasJugador.get(Num_Letra).setImage(new Image("images/letras/verde/"+Conexion_cliente.Letra_Actual+"-green.png"));
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
								panelLetrasJugador.get(Num_Letra).setImage(new Image("images/letras/rojo/"+Conexion_cliente.Letra_Actual+"-red.png"));
							}
							if(juegoEnCurso==true){


								utilidades.Conexion_cliente.Respuesta=tfRespuesta.getText();
								try{

									utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
									taPreguntas.setText(utilidades.Conexion_cliente.Pregunta);
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
											utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
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
													panelLetrasContrincante.get(Num_Letra).setImage(new Image("images/letras/verde/"+Conexion_cliente.Letra_Actual_Rival+"-green.png"));
												}catch(Exception a){
													a.printStackTrace();
												}
											}else if(!Conexion_cliente.Acierto_rival){
												System.out.println("No acierta el rival");
												System.out.println("Letra actual:"+Conexion_cliente.Letra_Actual);
												int Num_Letra=Pos_Letra(Conexion_cliente.Letra_Actual_Rival);
												panelLetrasContrincante.get(Num_Letra).setImage(new Image("images/letras/rojo/"+Conexion_cliente.Letra_Actual_Rival+"-red.png"));
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
										utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);

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
					utilidades.Conexion_cliente.Respuesta="Pasapalabra";
					try{
						utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
					}catch(Exception a){
						a.printStackTrace();
					}

					try{
						utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local,utilidades.Acciones_servidor.Obtener_Pregunta.toString(), null);
						taPreguntas.setText(utilidades.Conexion_cliente.Pregunta);
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
							utilidades.Conexion_cliente.Respuesta="Rendirse";
							utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local, utilidades.Acciones_servidor.Responder_Pregunta.toString(), null);
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
		@FXML
		void masInfo(MouseEvent event){
			if(!juegoEnCurso){
				utilidades.deVentana.transicionVentana("AcercaDe", event);
			}
		}

		@FXML
		void entradoCSS(MouseEvent event){
			BotonJuego.seleccionar_notDeseleccionar(true,aLBotonesJuego,event);
		}
		@FXML
		void salidoCSS(MouseEvent event){
			BotonJuego.seleccionar_notDeseleccionar(false,aLBotonesJuego,event);
		}

		public void eliminarOpcionesPartida(boolean eliminar_notAnyadir){
			panel.getChildren().remove(textoRJug);
			panel.getChildren().remove(rJug);
			if(eliminar_notAnyadir){
				for (ObjetoSeleccionPregunta osp : aLEleccion) {
					if(osp.isSeccion_notSeleccionable()){
						panel.getChildren().remove(osp.getTituloSeccion());
					}else{
						panel.getChildren().remove(osp.getRectangulo());
						panel.getChildren().remove(osp.getTexto());
					}
				}
			}else{
				for (ObjetoSeleccionPregunta osp : aLEleccion) {
					if(osp.isSeccion_notSeleccionable()){
						panel.getChildren().add(osp.getTituloSeccion());
					}else{
						panel.getChildren().add(osp.getRectangulo());
						panel.getChildren().add(osp.getTexto());
					}
				}
			}

		}

		public void crearRosco(boolean amigo_notEnemigo, ArrayList<ImageView> aLImgV){
			ImageView iv;
			char letraABC = 'a';

			for (int i = 0; i < PUNTOSTOTALES; i++) {
				//VARIABLE ABCD...


				//CREAMOS ImageView
				iv = new ImageView();

				iv.setLayoutX(coordX(amigo_notEnemigo));
				iv.setLayoutY(coordY());
				iv.setFitHeight(25);
				iv.setFitWidth(25);

				//CARGAMOS LA IMAGEN
				if(i == 14){
					letraABC--;
					iv.setImage(new Image("images/letras/azul/ñ-blue.png"));
				}else{

					iv.setImage(new Image("images/letras/azul/"+letraABC+"-blue.png"));
				} 

				panel.getChildren().add(iv);
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
		 * @param amigo_notEnemigo Booleano que depende de 
		 * @return Devuelve la coordenada X necesaria para dibujar el rosco
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

		//Método que calcula la coordenada de Y del rosco.
		public int coordY(){
			int resultado;
			//double divisionCircunferencia = 360/27;

			//resultado = (int)(200* (Math.sin(200+divisionCircunferencia*vecesHechoY/2))) +300;

			//(Radio * Seno de ((360grados partido por número de elementos a repartir * número de elementos creados * pi partido por 180) - pi partido por 2) + posición del rádio en Y
			//Nota: El radio ha de ser igual al de Y, si no, no se dibujará un rosco redondo.
			resultado = (int)(150 * Math.sin((360/27.0)*vecesHechoY*(Math.PI/180)-Math.PI/2)) +200;

			vecesHechoY++;
			//		System.out.println(resultado);
			return resultado;
		}

		public void anyadirGUI(){
			panel.getStylesheets().add("application/juego.css");
			panel.getStylesheets().remove("application/application.css");
			BackgroundImage myBII= new BackgroundImage(new Image("images/pspgamebg.png",panel.getWidth(),panel.getHeight(),false,true),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
			//then you set to your node
			panel.setBackground(new Background(myBII));

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

			//Botón contestar
			//Parte rectángulo
			rContestar.setLayoutX(205);
			rContestar.setLayoutY(540);
			rContestar.setOpacity(0.3);
			rPasar.setLayoutX(420);
			rPasar.setLayoutY(540);
			rPasar.setOpacity(0.3);
			textoContestar.setLayoutX(245);
			textoContestar.setLayoutY(565);
			textoPasar.setLayoutX(478);
			textoPasar.setLayoutY(565);

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
			aLBotonesJuego.add(new BotonJuego(rRendirse, textoRendirse));
			aLBotonesJuego.add(new BotonJuego(rPasar, textoPasar));
			aLBotonesJuego.add(new BotonJuego(rContestar, textoContestar));
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

				Image i = new Image("images/"+ imagen +".png",imgUsuario.getBoundsInLocal().getWidth(),imgUsuario.getBoundsInLocal().getHeight(),false,true);
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

				Image i = new Image("images/"+ imagen +".png",imgRival.getBoundsInLocal().getWidth(),imgRival.getBoundsInLocal().getHeight(),false,true);
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
			DropShadow dropShadow = new DropShadow();
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

			//		panel.getStylesheets().add("application/juego.css");
			//		panel.getStylesheets().remove("application/application.css");
		}

		void pqGuiCargando(boolean poner_notQuitar){
			if(poner_notQuitar){
				fondoCarga.setX(0.0);
				fondoCarga.setY(0.0);
				fondoCarga.setTranslateZ(999);

				barraCarga.setLayoutX(185.0);
				barraCarga.setLayoutY(455.0);

				textoCargaTranquil.setX(185.0);
				textoCargaTranquil.setY(110.0);

				textoCargaBuscandoPartida.setX(225.0);
				textoCargaBuscandoPartida.setY(550.0);
			}else{

			}
		}

		boolean preguntasBienSeleccionadas(){
			int count = 0;
			for (ObjetoSeleccionPregunta objetoSeleccionPregunta : aLEleccion) {
				if(objetoSeleccionPregunta.isElegido()) count++;
				if(!objetoSeleccionPregunta.isSeccion_notSeleccionable()){
					if(objetoSeleccionPregunta.getTexto().getText().equals("VS Amigo")){
						if(objetoSeleccionPregunta.isElegido()){
							//Dialogo VS Amigo no disponible en versión 1.0;
							return false;
						}
					}
				}
			}
			if(count == 2) return true; 
			else{
				//Dialogo elige modo y el tema
				return false;
			}
		}

		/**Método que gestiona la E/S del panel
		 * @param event Evento sucedido
		 */
		@FXML
		void esPanel(MouseEvent event) {
			if(!juegoEnCurso){
				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("Información");
				a.setHeaderText("Prueba a empezar juego para cerrar el panel");
				a.initModality(Modality.APPLICATION_MODAL);
				a.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				a.showAndWait();
			}else{
				Platform.runLater(new RPanel( ventanaMenuDentro, menuDesplegable ));
				ventanaMenuDentro = !ventanaMenuDentro;
			}
		}

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			Conexion_cliente.Correctas=0;
			Conexion_cliente.Incorrectas=0;
			panel.getChildren().remove(fondoCarga);
			panel.getChildren().remove(barraCarga);
			panel.getChildren().remove(textoCargaTranquil);
			panel.getChildren().remove(textoCargaBuscandoPartida);
			panel.applyCss();
			//SERVIDOR
			//TODO Cargar imagen personal
			if(EventosLogIn.iAvatar!=null){
				imagenAvatar.setImage(EventosLogIn.iAvatar);
			}else{
				String imagen = "fPerfil";
				Random rand = new Random();
				int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
				if(randomNum == 666){
					imagen = "fPerfilPirata";
				}

				Image i = new Image("images/"+ imagen +".png",imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
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
			menuDesplegable.add(rectanguloAmigos);
			menuDesplegable.add(rectanguloCerrarSesion);
			menuDesplegable.add(rectanguloEstadisticas);
			menuDesplegable.add(rectanguloJugar);
			menuDesplegable.add(rectanguloMiPerfil);
			menuDesplegable.add(circuloPanel);
			menuDesplegable.add(circuloPlus);
			menuDesplegable.add(logopsp);

			//Textos
			menuDesplegable.add(textoAmigos);
			menuDesplegable.add(textoCerrarSesion);
			menuDesplegable.add(textoESPanel);
			menuDesplegable.add(textoEstadisticas);
			menuDesplegable.add(textoJugar);
			menuDesplegable.add(textoMiPerfil);
			menuDesplegable.add(textoLogeadoComo);
			menuDesplegable.add(textoPlus);

			//Variables de usuario
			menuDesplegable.add(textoNombreDeUsuario);
			textoNombreDeUsuario.setText(utilidades.Conexion_cliente.Datos_Usuario.get(0));
			menuDesplegable.add(imagenAvatar);

			//Rellenar elementos.
			this.aLEleccion = new ArrayList<ObjetoSeleccionPregunta>();
			//Elementos título
			aLEleccion.add(new ObjetoSeleccionPregunta(selecMJ));
			aLEleccion.add(new ObjetoSeleccionPregunta(selecTP));

			//Elementos elegibles (No hay interfaz, no confundirse).
			aLEleccion.add(new ObjetoSeleccionPregunta(tVSAleatorio, vsAleatorio, true));
			aLEleccion.add(new ObjetoSeleccionPregunta(tVSAmigo, vsAmigo, true));
			aLEleccion.add(new ObjetoSeleccionPregunta(tTodos, rTodos, false));
			aLEleccion.add(new ObjetoSeleccionPregunta(tGeo, rGeo, false));
			aLEleccion.add(new ObjetoSeleccionPregunta(tArte, rArte, false));
			aLEleccion.add(new ObjetoSeleccionPregunta(tHist, rHist, false));
			aLEleccion.add(new ObjetoSeleccionPregunta(tCien, rCien, false));
			aLEleccion.add(new ObjetoSeleccionPregunta(tEntret, rEntret, false));
			aLEleccion.add(new ObjetoSeleccionPregunta(tDep, rDep, false));

			panel.getStylesheets().add("application/application.css");
		}
	}