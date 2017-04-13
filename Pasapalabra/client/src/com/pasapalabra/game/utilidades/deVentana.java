package com.pasapalabra.game.utilidades;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.application.Main;
import com.pasapalabra.game.controllers.ClaseExtensora;
import com.pasapalabra.game.controllers.EventosAmigos;
import com.pasapalabra.game.controllers.EventosEstadisticas;
import com.pasapalabra.game.controllers.EventosJuego;
import com.pasapalabra.game.controllers.EventosPerfil;

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

/**Clase de utilidad para ventanas.<br>
 * <p>.<i>trasicionVentana</i> - Método para transición de fxml usando FXMLLoader.
 * <p>.<i>centrarVentana</i> - Método para centrar ventanas.
 * @author asier.gutierrez
 */
public class deVentana {
	
	//Loger de la clase deVentana
	public static Logger log = com.pasapalabra.game.utilidades.AppLogger.getWindowLogger(EventosJuego.class.getName());
	 
	/**Método para la transición de una ventana a otra.
	 * @param nombreFichero Nombre sin extensión por la cual se
	 * conoce el fichero en el classpath. Sólo se pide el nombre porque
	 * todas las ventanas que se van a cargar están alojadas en el paquete windows.
	 * Si está fuera del paquete windows se deberá redefinir el método aplicando un path
	 * correcto.
	 * @param event Evento que hace cambiar de una ventana a otra.
	 * El evento debe haber ocurrido en la ventana en la que se quiere
	 * hacer la transición
	 */
	public static void transicionVentana(String nombreFichero, Event event) {
		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Parent panel = null;
		try {
			panel = FXMLLoader.load(Main.class.getResource("/fxml/"+nombreFichero+".fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app_stage.setScene(new Scene((Parent) panel));
		centrarVentana(app_stage);
	}

	/**Método para centrar una ventana. Para centrar la ventana es necesario
	 * que la ventana esté mostrada, si no aparece en la esquina superior izquierda.
	 * @param frame De la clase Stage. Es la ventana que quieres centrar.
	 */
	public static void centrarVentana(Stage frame) {
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
	public static void cerrarSesion(MouseEvent event){
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
				String[] Datos=new String[1];
				//FIXME:Datos[0]=Conexion_cliente.Datos_Usuario.get(0);
				try {
					//FIXME:Conexion_cliente.lanzaConexion(Conexion_cliente.Ip_Local, Acciones_servidor.Delog.toString(),Datos);
					com.pasapalabra.game.utilidades.deVentana.transicionVentana("LogIn", event);
				} catch (Exception e) {
					log.log(Level.WARNING, "Error al cerrar sesion", e);
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
	public static void efectoTransparenciaOnHover(MouseEvent event, ClaseExtensora eventosMenu){
		double op = 0.3;
		if(event.getEventType().toString().equals("MOUSE_ENTERED")) op = 0.8;

		if(event.getSource().getClass().getName()=="javafx.scene.shape.Rectangle"){

			if(((Rectangle) event.getSource()).getId().equals("rectanguloAmigos") && !(eventosMenu instanceof EventosAmigos)){
				//				eventosMenu.textoAmigos.setOpacity(1);
				eventosMenu.rectanguloAmigos.setOpacity(op);
			}else if(((Rectangle) event.getSource()).getId().equals("rectanguloMiPerfil") && !(eventosMenu instanceof EventosPerfil)){
				//				eventosMenu.textoMiPerfil.setOpacity(1);
				eventosMenu.rectanguloMiPerfil.setOpacity(op);
			}else if(((Rectangle) event.getSource()).getId().equals("rectanguloEstadisticas") && !(eventosMenu instanceof EventosEstadisticas)){
				//				eventosMenu.textoEstadisticas.setOpacity(1);
				eventosMenu.rectanguloEstadisticas.setOpacity(op);
			}else if(((Rectangle) event.getSource()).getId().equals("rectanguloJugar") && !(eventosMenu instanceof EventosJuego)){
				//				eventosMenu.textoJugar.setOpacity(1);
				eventosMenu.rectanguloJugar.setOpacity(op);
			}
		}else{
			if(((Text) event.getSource()).getText().equals("Amigos") && !(eventosMenu instanceof EventosAmigos)){
				//				eventosMenu.textoAmigos.setOpacity(1);
				eventosMenu.rectanguloAmigos.setOpacity(op);
			}else if(((Text) event.getSource()).getText().equals("Mi perfil") && !(eventosMenu instanceof EventosPerfil)){
				//				eventosMenu.textoMiPerfil.setOpacity(1);
				eventosMenu.rectanguloMiPerfil.setOpacity(op);
			}else if(((Text) event.getSource()).getText().equals("Estadísticas") && !(eventosMenu instanceof EventosEstadisticas)){
				//				eventosMenu.textoEstadisticas.setOpacity(1);
				eventosMenu.rectanguloEstadisticas.setOpacity(op);
			}else if(((Text) event.getSource()).getText().equals("Jugar") && !(eventosMenu instanceof EventosJuego)){
				//				eventosMenu.textoJugar.setOpacity(1);
				eventosMenu.rectanguloJugar.setOpacity(op);
			}
		}
	}
}