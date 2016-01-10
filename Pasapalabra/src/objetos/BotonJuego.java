package objetos;

import java.util.ArrayList;

import javafx.event.Event;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**Clase de Objeto para poder unir el texto y el rectangulo que conforma un botón en la aplicación.
 * @author asierguti
 *
 */
public class BotonJuego implements Comparable<Event> {

	//Atributos
	Rectangle r;
	Text t;
	
	/**Crea un nuevo Botón de juego
	 * @param r Rectángulo que forma parte del botón
	 * @param t Texto que forma parte del botón
	 */
	public BotonJuego(Rectangle r, Text t){
		this.r = r;
		this.t = t;
	}
	
	/**Dado un booleano, un ArrayList de Botones de Juego y un evento, el botón obtendrá en el rectángulo un ID (CSS)
	 * depende de si está seleccionado/deseleccionado(depende del booleano) y entonces, dependiendo del CSS que se haya diseñado
	 * cambiarán de color, forma...
	 * @param b Selecccionado (true) o deseleccionado (false)
	 * @param aLBJ ArrayList de Botones de Juego
	 * @param event Evento de click hecho sobre un botón (rectángulo o texto)
	 */
	public static void seleccionar_notDeseleccionar(boolean b, ArrayList<BotonJuego> aLBJ, Event event){
		//Recorre ArrayList
		for (BotonJuego botonJuego : aLBJ) {
			//Si el botón de juego es igual al pulsado en el evento
			if(botonJuego.compareTo(event) == 0){
				//Crear una ID de CSS
				String id;
				if(b)id = "seleccionado";else id = "deseleccionado";
				//Se añade la ID al rectángulo
				botonJuego.r.setId(id);
				//Pero también se podría añadir al texto
//				botonJuego.t.setId(id);
				//Termina el método porque sólo hay uno que pueda ser seleccionado en un mismo instante.
				//Mejora de eficiencia
				return;
			}
		}
	}

	/* Método que compara un Botón de Juego con un Evento
	 * Se supone que esta clase está creada para generalizar los toques de botón.
	 * Es decir, creo un ArrayList de Botones de juego. En el FXML a todos los botones les asigno un método igual.
	 * Dependiendo del texto o rectángulo pues será o no el mismo. Este método es útil para cuando los botones se cargan mediante queries SQL
	 * aunque en ese caso le faltaría un ID o algo para hacer la búsqueda en la BD. La utilidad de este botón es añadir o eliminar la opacidad cuando
	 * se les selecciona.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Event o) {
		if(r.equals(o.getSource())) return 0;
		if(t.equals(o.getSource())) return 0;
		return 1;
	}
	
	
	
}
