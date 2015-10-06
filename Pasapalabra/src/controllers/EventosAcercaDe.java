package controllers;

import java.awt.Label;

import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;

/**Clase que gestiona los eventos de la clase AcercaDe.fxml
 * @author asier.gutierrez
 *
 */
public class EventosAcercaDe extends Control {
	//Recuerda usar JavaDoc para cada método
	//Recuerda que tienes que añadir los listener y todo eso
	//Recuerda que tienes que enlazar esta clase con el código fxml
	//(Haz esta clase la clase controladora del código fxml)
	//Si tienes dudas mira en la clase main y eventoslogin
	//Añade un botón para ir atrás
	//Las clases controladoras extienden de Controller

	@FXML
	private Label lblcorreodeivan;

	public void copiarcorreodeivan(MouseEvent event){

		final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(lblcorreodeivan.getText());
        
        clipboard.setContent(content);
		
	}

	@FXML
	private Label lblcorreodeasier;

	public void copiarcorreodeasier(MouseEvent event){

		final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(lblcorreodeasier.getText());
        
        clipboard.setContent(content);
		
	}


}
