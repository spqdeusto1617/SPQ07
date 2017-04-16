package com.pasapalabra.game.utilities;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**Clase de utilidad para ventanas.<br>
 * <p>.<i>trasicionVentana</i> - Método para transición de fxml usando FXMLLoader.
 * <p>.<i>centrarVentana</i> - Método para centrar ventanas.
 * @author asier.gutierrez
 */
public class deVentana {
	
	//Loger de la clase deVentana
	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger("Server");
	 
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
			panel = FXMLLoader.load(deVentana.class.getResource("/fxml/"+nombreFichero+".fxml"));
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
}