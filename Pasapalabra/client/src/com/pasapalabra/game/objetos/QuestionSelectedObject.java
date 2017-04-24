package com.pasapalabra.game.objetos;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**Objeto de utilidad para controlar la selección de opciones mediante botones en el juego.
 * @author asier.gutierrez
 *
 */
public class QuestionSelectedObject {
	//Atributos
	//Puede ser un texto + rectángulo + otros datos
	Text texto;
	Rectangle rectangulo;
	boolean elegido;
	boolean modoDeJuego_notTipoPregunta;
	boolean seccion_notSeleccionable;

	//O puede ser sólo texto, en cuyo caso lo llamaremos título. No es seleccionable.
	Text tituloSeccion;

	/**Crea un QuestionSelectedObject (botón) de texto + rectángulo + ¿Es modo de juego o tipo de pregunta?
	 * @see QuestionSelectedObject
	 * @param texto Texto que forma el botón
	 * @param rectangulo Rectángulo que forma el botón
	 * @param modoDeJuego_notTipoPregunta Modo de juego TRUE / Tipo de pregunta FALSE
	 */
	public QuestionSelectedObject(Text texto, Rectangle rectangulo, boolean modoDeJuego_notTipoPregunta){
		this.texto = texto;
		this.rectangulo = rectangulo;
		this.elegido = false; //Por defecto, no está elegido
		this.modoDeJuego_notTipoPregunta = modoDeJuego_notTipoPregunta;
		this.seccion_notSeleccionable = false; //Si se usa este constructor, está claro que no es seleccionable.
	}


	/**Crea un QuestionSelectedObject Este constructor es solo de la cabecera.
	 * Se pone como objeto porque si queremos eliminar toda la parte de selección porque se tiene
	 * que cambiar
	 * @see QuestionSelectedObject
	 * @param tituloSeccion Título sección El título de la sección. (No se seleccionará)
	 */
	public QuestionSelectedObject(Text tituloSeccion){
		this.tituloSeccion = tituloSeccion;
		this.seccion_notSeleccionable = true;
	}


	/*-------------------Geters y Seters-------------------*/
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
	/*----------------Fin de Geters y Seters----------------*/
}