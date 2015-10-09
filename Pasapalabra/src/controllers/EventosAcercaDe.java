package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;

/**Clase que gestiona los eventos de la clase AcercaDe.fxml
 * @author asier.gutierrez
 *
 */
public class EventosAcercaDe extends Control implements Initializable {
	//Recuerda usar JavaDoc para cada método
	//Recuerda que tienes que añadir los listener y todo eso
	//Recuerda que tienes que enlazar esta clase con el código fxml
	//(Haz esta clase la clase controladora del código fxml)
	//Si tienes dudas mira en la clase main y eventoslogin
	//Añade un botón para ir atrás
	//Las clases controladoras extienden de Controller

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}
	@FXML
	private Label lblcorreodeivan;

	/**Método para copiar al clipboard (o portapapeles) 
	 * el elemento seleccionado (en este caso, el correo).
	 */
	public void copiarcorreodeivan(MouseEvent event){

		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(lblcorreodeivan.getText());
		
		clipboard.setContent(content);

	}

	@FXML
	private Label lblcorreodeasier;

	/**
		Método idéntico al anterior (copiarcorreodeivan) 
	 */
	public void copiarcorreodeasier(MouseEvent event){
		
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(lblcorreodeasier.getText());
		
		clipboard.setContent(content);

	}

	/**Método para volver a la ventana de login.
	 */
	public void VolveralLog(MouseEvent event){

		utilidades.deVentana.transicionVentana("LogIn", event);



	}



}
