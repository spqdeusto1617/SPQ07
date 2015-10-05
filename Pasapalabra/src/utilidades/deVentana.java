package utilidades;

import java.io.IOException;

import application.Main;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**Clase de utilidad para ventanas
 * @author asier.gutierrez
 *
 */
public class deVentana {


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
			panel = FXMLLoader.load(Main.class.getResource("../windows/AcercaDe.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app_stage.setScene(new Scene((Parent) panel));
	}
}
