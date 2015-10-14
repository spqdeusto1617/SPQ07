package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
	//Variable para que se pueda realizar la transición de la info: copiado a portapapeles
	public int alturaDelMensaje=0;
	
	
	//Hilo creado para la animación del portapapeles
	HiloPortapapeles portapapeles=new HiloPortapapeles();
	
	
	
	@FXML
	private ImageView ivTextoCopiadoPortapapeles;
	
	@FXML
	private ImageView ivCierreTextoPortapapeles;

	@FXML
	private AnchorPane panelElPrograma;
	

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
		
		new Thread(portapapeles).start();
		
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
		
		
		new Thread(portapapeles).start();
		
	}

	/**Método para volver a la ventana de login.
	 */
	public void VolveralLog(MouseEvent event){

		utilidades.deVentana.transicionVentana("LogIn", event);



	}


	/**
	 * Método para cerrar el diálogo de: copiado a portapapeles. Resetea ambos componentes a 0. No funciona
	 * a menos que los dos componentes terminen de llegar a su posición.
	 */
	public void Cierrepresionado(MouseEvent event){
		if(alturaDelMensaje==-30){
		alturaDelMensaje=0;
		
		new Thread(new HiloEliminarPortapapeles(alturaDelMensaje, ivCierreTextoPortapapeles, ivTextoCopiadoPortapapeles, panelElPrograma)).run();
//		lblTextocopiadoPortapapeles.setY(alturaDelMensaje);
//		lblCierreTextoPortapapeles.setY(alturaDelMensaje);
		}
	}


	
	
	/**Hilo para hacer la animación del portapapeles
	 * Baja de 0 a -30 los dos elementos a la vez, para que vayan sincronizados
	 *@author Iván
	 */
	class HiloPortapapeles implements Runnable {

		@Override
		public void run() {
		
			while(alturaDelMensaje>-30){
				ivTextoCopiadoPortapapeles.setY(alturaDelMensaje);
				ivCierreTextoPortapapeles.setY(alturaDelMensaje);
				try {
					Thread.sleep(15);
					alturaDelMensaje--;

				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}

			}

		}

	}
	
	/**Hilo para hacer la animación del portapapeles
	 * Hace desaparecer los dos elementos de manera sincronizada
	 *@author Iván
	 */
	class HiloEliminarPortapapeles implements Runnable {
		public int alturaDelMensaje;
		public ImageView equis;
		public ImageView dialogo;
		public AnchorPane panel;
		public HiloEliminarPortapapeles(int alturaDelMensaje, ImageView equis,ImageView dialogo, AnchorPane panel){
			this.alturaDelMensaje = alturaDelMensaje;
			this.equis = equis;
			this.dialogo = dialogo;
			this.panel = panel;
			
		}
		@Override
		public void run() {
		double opacidad= 100;
			while(opacidad>0){
				equis.setOpacity(opacidad);
				dialogo.setOpacity(opacidad);
				panel.requestLayout();
				try {
					Thread.sleep(100);
					opacidad--;
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}

			}
			equis.setY(0);
			dialogo.setY(0);

		}

	}

	

}
