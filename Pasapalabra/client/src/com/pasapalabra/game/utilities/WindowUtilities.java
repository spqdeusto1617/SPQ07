package com.pasapalabra.game.utilities;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.application.Main;
import com.pasapalabra.game.controllers.ExtenderClassController;
import com.pasapalabra.game.controllers.FriendController;
import com.pasapalabra.game.controllers.StatisticsController;
import com.pasapalabra.game.controllers.ThemeController;
import com.pasapalabra.game.service.ClientConnection;
import com.pasapalabra.game.controllers.ProfileController;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**Utilities class for windows.<br>
 * <p>.<i>windowTransition</i> - Transition method for the fxml using FXMLLoader.
 * <p>.<i>centerWindow</i> - method for center the windows.
 * @author asier.gutierrez
 */
public class WindowUtilities {

	//Class´s logger
	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger("Server");

	/**Method for transist from one window to the other.
	 * @param fileName: Name without extension by which
 	 * the file is Known in the classpath. Only the name is requested because 
	 * all the windows to be loaded are located in the windows package.
 	 * If it is outside the windows package, the method must be redefined 
	 * by applying a correct path.
	 * @param event: Event that makes the transition from one window to other.
	 * The event must have occurred in the window in which you want
	 * make the transition
	 */
	public static void windowTransition(String fileName, Event event) {
		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent panel = null;
		try {
			panel = FXMLLoader.load(WindowUtilities.class.getResource("/fxml/"+fileName+".fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app_stage.setScene(new Scene((Parent) panel));
		centerWindow(app_stage);
	}

	/**Method for closing a window. To center the window is necessary
	 * that the window is displayed, if not, it will appear in the upper left corner.
	 * @param frame: form the Stage class. This is the window that you want to center.
	 */
	public static void centerWindow(Stage frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setX(x);
		frame.setY(y);
	}

	/**<h1>Clase para cerrar sesión en la aplicación.</h1>
	 * </br>
	 * <i>*Necesario evento de ratón y conexión en curso.</i>
	 * 
	 * @param event Evento de ratón.
	 */
	public static void closeSession(MouseEvent event){
		//Crea alerta de tipo confirmación
		Alert alert = new Alert(AlertType.CONFIRMATION);
		//Pone título
		alert.setTitle("Cerrar sesión");
		//Pone cabecera
		alert.setHeaderText("¿Está seguro de querer cerrar sesión?");
		//Pone contenido
		alert.setContentText("Si cierra perderá la partida en curso que\n"
				+ "pueda estar jugando o los cambios que esté"
				+ "haciendo en su cuenta si no los ha guardado aún.");

		//Añade modalidad
		alert.initModality(Modality.APPLICATION_MODAL);
		//Añade 'dueño'. (=La ventana sobre la cual se va a posicionar y la cual bloqueará)
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		//Mostrar y bloquear ventana padre hasta aceptar o rechazar.
		Optional<ButtonType> result = alert.showAndWait();

		//Ha pulsado ok?
		if (result.get() == ButtonType.OK){
			//Si
			//VUELTA A CONFIRMAR
			//Crear alerta de tipo confirmación
			Alert alert2 = new Alert(AlertType.CONFIRMATION);
			//Pone título
			alert2.setTitle("Cerrar sesión");
			//Pone cabecera
			alert2.setHeaderText("¿Está seguro?");
			//Pone contenido
			alert2.setContentText("No se podrá volver atrás.");
			//Añade modalidad
			alert2.initModality(Modality.APPLICATION_MODAL);
			//Añade 'dueño'
			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			//Muestra y espera hasta resultado
			Optional<ButtonType> result2 = alert2.showAndWait();

			//Ha pulsado ok?
			if (result2.get() == ButtonType.OK){
				//Si

				//En la clase del servidor habrá una rutina para cerrar sesión.
				try {
					if(ClientConnection.delogging()){
						com.pasapalabra.game.service.ClientConnection.sessionAuth = null;

						com.pasapalabra.game.service.ClientConnection.userInfo = null;

						windowTransition("LogIn", event);

					}
					else{
						/*Alert alert3 = new Alert(AlertType.ERROR);
						//Pone título
						alert3.setTitle("Error");
						//Pone cabecera
						alert3.setHeaderText("No puedes salir ahora");
						//Pone contenido
						alert3.setContentText("No puedes delogearte ahora, pruebalo mas tarde.");
						//Añade modalidad
						alert3.initModality(Modality.APPLICATION_MODAL);
						//Añade 'dueño'
						alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());

						alert3.showAndWait();*/
						com.pasapalabra.game.service.ClientConnection.sessionAuth = null;

						com.pasapalabra.game.service.ClientConnection.userInfo = null;

						log.log(Level.INFO, "Delog while result == false");
						windowTransition("LogIn", event);
					}
				} catch (Exception e) {
					log.log(Level.WARNING, "Error al cerrar sesion", e);
					Alert alert3 = new Alert(AlertType.ERROR);
					//Pone título
					alert3.setTitle("Error al delogearte");
					//Pone cabecera
					alert3.setHeaderText("Ha habido un error al delogearte");
					//Pone contenido
					alert3.setContentText("Ha habido un error insesperado a la hora de salir de la aplicación, por favor, intenteló de nuevo.");
					//Añade modalidad
					alert3.initModality(Modality.APPLICATION_MODAL);
					//Añade 'dueño'
					alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());

					alert3.showAndWait();
				} 
				//

			} else {
				//No
				//NADA
			}
		} else {
			//No
			//NADA
		}
	}
	//Eventos generalizados para plantillas descendientes [SIN REDEFINICIÓN DE ELEMENTOS] de VENTANA_BASE_DE_MENU_V2.fxml

	//

	public static void forcedCloseSession(Event event){


		ClientConnection.sessionAuth = null;

		ClientConnection.userInfo = null;
		
		ClientConnection.userIMG = null;

		windowTransition("LogIn", event);


		//


	}

	/**Método para dado un evento de hover sube el nivel de transparencia (de 0.2 a 0.8).
	 * Cuando pierde el el hover baja el nivel de transparencia a 0.2 otra vez.
	 * </br>
	 **Por hover nos referimos al selector hover de CSS
	 *</br></br>
	 *<i>Evento generalizado para plantilla descendiente [SIN REDEFINICIÓN DE ELEMENTOS] de VENTANA_BASE_DE_MENU_V2.fxml</i>
	 *
	 * @param event Evento de onMouseEntered/onMouseExited
	 * @param eventosMenu Clase controladora de eventos (Ver texto en itálica) en la que se produce el evento y desde la cual se llama a este.
	 */
	public static void efectoTransparenciaOnHover(MouseEvent event, ExtenderClassController eventosMenu){
		double op = 0.3;
		if(event.getEventType().toString().equals("MOUSE_ENTERED")) op = 0.8;

		if(event.getSource().getClass().getName()=="javafx.scene.shape.Rectangle"){

			if(((Rectangle) event.getSource()).getId().equals("rectanguloAmigos") && !(eventosMenu instanceof FriendController)){
				//				eventosMenu.textoAmigos.setOpacity(1);
				eventosMenu.btnAmigos.setOpacity(op);
			}else if(((Rectangle) event.getSource()).getId().equals("rectanguloMiPerfil") && !(eventosMenu instanceof ProfileController)){
				//				eventosMenu.textoMiPerfil.setOpacity(1);
				eventosMenu.btnPerfil.setOpacity(op);
			}else if(((Rectangle) event.getSource()).getId().equals("rectanguloEstadisticas") && !(eventosMenu instanceof StatisticsController)){
				//				eventosMenu.textoEstadisticas.setOpacity(1);
				eventosMenu.btnEstadisticas.setOpacity(op);
			}else if(((Rectangle) event.getSource()).getId().equals("rectanguloJugar") && !(eventosMenu instanceof ThemeController)){
				//				eventosMenu.textoJugar.setOpacity(1);
				eventosMenu.btnJuego.setOpacity(op);
			}
		}else{
			if(((Text) event.getSource()).getText().equals("Amigos") && !(eventosMenu instanceof FriendController)){
				//				eventosMenu.textoAmigos.setOpacity(1);
				eventosMenu.btnAmigos.setOpacity(op);
			}else if(((Text) event.getSource()).getText().equals("Mi perfil") && !(eventosMenu instanceof ProfileController)){
				//				eventosMenu.textoMiPerfil.setOpacity(1);
				eventosMenu.btnPerfil.setOpacity(op);
			}else if(((Text) event.getSource()).getText().equals("Estadísticas") && !(eventosMenu instanceof StatisticsController)){
				//				eventosMenu.textoEstadisticas.setOpacity(1);
				eventosMenu.btnEstadisticas.setOpacity(op);
			}else if(((Text) event.getSource()).getText().equals("Jugar") && !(eventosMenu instanceof ThemeController)){
				//				eventosMenu.textoJugar.setOpacity(1);
				eventosMenu.btnJuego.setOpacity(op);
			}
		}
	}
}