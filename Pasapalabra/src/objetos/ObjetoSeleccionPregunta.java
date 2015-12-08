package objetos;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ObjetoSeleccionPregunta {

	//Atributos
	Text texto;
	Rectangle rectangulo;
	boolean elegido;
	boolean modoDeJuego_notTipoPregunta;
	boolean seccion_notSeleccionable;
	//O puede ser sólo texto, en cuyo caso lo llamaremos título.
	
	Text tituloSeccion;
	
	public ObjetoSeleccionPregunta(Text texto, Rectangle rectangulo, boolean modoDeJuego_notTipoPregunta){
		this.texto = texto;
		this.rectangulo = rectangulo;
		this.elegido = false;
		this.modoDeJuego_notTipoPregunta = modoDeJuego_notTipoPregunta;
		this.seccion_notSeleccionable = false;
	}
	
	public ObjetoSeleccionPregunta(Text tituloSeccion){
		this.tituloSeccion = tituloSeccion;
		this.seccion_notSeleccionable = true;
	}

	public boolean isElegido() {
		return elegido;
	}

	public void setElegido(boolean elegido) {
		this.elegido = elegido;
	}

	public boolean isModoDeJuego_notTipoPregunta() {
		return modoDeJuego_notTipoPregunta;
	}

	public void setModoDeJuego_notTipoPregunta(boolean modoDeJuego_notTipoPregunta) {
		this.modoDeJuego_notTipoPregunta = modoDeJuego_notTipoPregunta;
	}

	public Text getTexto() {
		return texto;
	}

	public Rectangle getRectangulo() {
		return rectangulo;
	}

	public Text getTituloSeccion() {
		return tituloSeccion;
	}

	public boolean isSeccion_notSeleccionable() {
		return seccion_notSeleccionable;
	}
	
	
}
