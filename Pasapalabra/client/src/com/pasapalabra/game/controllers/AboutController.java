package com.pasapalabra.game.controllers;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;


/**Class that manages events from AcercaDe.fxml
 * @author asier.gutierrez
 *
 */
public class AboutController extends Control implements Initializable {
	//Se define un logger.
	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(AboutController.class.getName());
	public boolean animacionEnProceso;

	@FXML
	private Text txtEdadIvan;

	@FXML
	private Text txtEdadAsier;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		com.pasapalabra.game.utilities.AppLogger.crearLogHandler(log, AboutController.class.getName());
		log.log(Level.FINEST, "Inicializado de EventosAcercaDe");

		//Comprueba edad de Iván
		//F. Nacimiento de Iván
		Calendar cal = new GregorianCalendar(1996,8,18);
		//Día de hoy
		Calendar now = new GregorianCalendar();

		//Resultado = resta de años
		int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		//Comprobaciones para que no se cuenten mal los años
		if(cal.get(Calendar.MONTH)==now.get(Calendar.MONTH)+1){

			if(cal.get(Calendar.DAY_OF_MONTH)>now.get(Calendar.DAY_OF_MONTH)){

				res=res-1;
			}

		}
		//Seteamos la edad
		txtEdadIvan.setText(""+res+" años");
		log.log(Level.FINEST, "La edad de Iván ha quedado de esta forma: " + txtEdadIvan);

		//Comprueba edad de Asier
		//F. nacimiento de Asier
		Calendar cal2 = new GregorianCalendar(1996, 10, 14);
		//Día de hoy
		Calendar now2 = new GregorianCalendar();
		//Resultado = resta de años
		int res2 = now2.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		//Comprobaciones para que no se cuenten mal los años
		if(cal2.get(Calendar.MONTH)==now2.get(Calendar.MONTH)+1){

			if(cal2.get(Calendar.DAY_OF_MONTH)>now2.get(Calendar.DAY_OF_MONTH)){

				res2=res2-1;
			}
		}
		//Seteamos la edad
		txtEdadAsier.setText(""+res2+" años");
		log.log(Level.FINEST, "La edad de Asier ha quedado de esta forma: " + txtEdadAsier);

	}
	//Variable para que se pueda realizar la transición de la info: copiado a portapapeles
	public int alturaDelMensaje=0;


	//Hilo creado para la animación del portapapeles
	HiloPortapapeles portapapeles=new HiloPortapapeles();


	//Imágen de que el texto ha sido copiado al portapapeles
	@FXML
	private ImageView ivTextoCopiadoPortapapeles;
	//Imágen de cerrar el cuadro de diálogo (una 'x')
	@FXML
	private ImageView ivCierreTextoPortapapeles;
	//Un pane para el panel del programa
	@FXML
	private AnchorPane panelDelPrograma;
	//Label para el correo de Iván
	@FXML
	private Label lblCorreoDeIvan;

	//Label donde se ubicará el correo de Asier
	@FXML
	private Label lblCorreoDeAsier;

	/**Method that copies to the clipboard selected element
	 * @param event 
	 */
	public void copiarCorreo(MouseEvent event){
		//Getea el portapapeles del sistema
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		//Crea un nuevo contenido
		final ClipboardContent content = new ClipboardContent();
		//Añade al contenido un string dependiendo si era el correo de Asier o de Iván
		if(event.getSource().equals(lblCorreoDeAsier))
			content.putString(lblCorreoDeAsier.getText());
		else content.putString(lblCorreoDeIvan.getText());
		//Setea el contenido al portapapeles
		clipboard.setContent(content);
		//Comienza el hilo de animación
		new Thread(portapapeles).start();
	}

	/**Method to return to the previous window
	 * @param event
	 */
	public void volver(MouseEvent event){
		String pantallaALaQueIr = "ThemeElection";
		log.log(Level.FINEST, "Se ha vuelto a la pantalla de " + pantallaALaQueIr);
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition(pantallaALaQueIr, event);
	}


	/**Method that closes the dialog: copied to the clipboard. Reset the components to 0. 
	 * @param event
	 */
	public void Cierrepresionado(MouseEvent event){
		log.log(Level.FINEST, "Se ha presionado el botón para cerrar el diálogo: altura del mensaje = " + alturaDelMensaje + "/ animación está en proceso = " + animacionEnProceso);
		if(alturaDelMensaje==-30 && !animacionEnProceso){
			alturaDelMensaje=0;
			log.log(Level.FINEST, "La altura del mensaje era igual a -30 y la animación no estaba en proceso => nuevo hilo con runnable HiloEliminarPortapapeles.\n"
					+ "DATOS:\n"
					+ "Altura del mensaje = " + alturaDelMensaje + " (Se ha igualado a 0 pero era igual a -30)\n"
					+ "ImageViewCierreTextoPortapapeles [Objeto] = " + ivCierreTextoPortapapeles + "\n"
					+ "ImageViewCopiadoPortapapeles [Objeto] = " + ivTextoCopiadoPortapapeles + "\n"
					+ "Panel donde está ubicado [Objeto] = " + panelDelPrograma + "\n"
					);
			new Thread(new HiloEliminarPortapapeles(alturaDelMensaje, ivCierreTextoPortapapeles, ivTextoCopiadoPortapapeles, panelDelPrograma)).run();
			//		lblTextocopiadoPortapapeles.setY(alturaDelMensaje);
			//		lblCierreTextoPortapapeles.setY(alturaDelMensaje);
		}
	}

	/**Thread to make the animation of the clipboard
	 * Decreases both elements from 0 to -30 at the same time in order to sincronize them. 
	 * @author Iván
	 *
	 */
	class HiloPortapapeles implements Runnable {

		@Override
		public void run() {

			while(alturaDelMensaje>-30 && !animacionEnProceso){
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
	
	 
	/**Thread to delete both elements from the clipboard at the same time. Synchronized. 
	 * @author Iván
	 *
	 */
	class HiloEliminarPortapapeles implements Runnable {
		//Declaración de atributos
		//Altura que tiene el mensaje
		public int alturaDelMensaje;
		//Imagen de 'x' para cerrar cuadro de diálogo
		public ImageView equis;
		//Imágen de diálogo
		public ImageView dialogo;
		//Panel donde está ubicado lo anterior
		public AnchorPane panel;
		//Constructor
		public HiloEliminarPortapapeles(int alturaDelMensaje, ImageView equis,ImageView dialogo, AnchorPane panel){
			this.alturaDelMensaje = alturaDelMensaje;
			this.equis = equis;
			this.dialogo = dialogo;
			this.panel = panel;

		}
		@Override
		public void run() {
			//Transición
			//Nueva transición de opacidad de 1000 milisegundos de duración y con la imagen de la 'x'
			FadeTransition ft = new FadeTransition(Duration.millis(1000), equis);
			//Desde el valor 1 (opaco)
			ft.setFromValue(1.0);
			//Hasta valor 0 (transparente)
			ft.setToValue(0.0);
			//		ft.setCycleCount(Timeline.INDEFINITE);
			//Que se vuelva a hacer de 1 a 0 y de 0 a 1 = falso > Solo queremos que se haga de 1 a 0
			ft.setAutoReverse(false);
			//Nueva transición de opacidad de 1000 milisegundos de duración y con la imagen de diálogo
			FadeTransition ft2 = new FadeTransition(Duration.millis(1000), dialogo);
			//Desde el valor 1 (opaco)
			ft2.setFromValue(1.0);
			//Hasta valor 0 (transparente)
			ft2.setToValue(0.0);
			//		ft.setCycleCount(Timeline.INDEFINITE);
			//Que se vuelva a hacer de 1 a 0 y de 0 a 1 = falso > Solo queremos que se haga de 1 a 0
			ft2.setAutoReverse(false);

			//Nueva transición paralela con las animaciones creadas antes
			ParallelTransition pt = new ParallelTransition(ft2,ft);
			//Un nuevo evento para cuando finalice la transición
			pt.setOnFinished(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					//Setea los elementos en su sitio
					equis.setY(0);
					dialogo.setY(0);
					//Setea la opacidad de los elementos en 100 (opacos)
					dialogo.setOpacity(100.0);
					equis.setOpacity(100.0);
					//Setea el booleano de que la animación está en proceso a falso [Recordamos que ha terminado]
					animacionEnProceso = false;
				}
			});
			//Ponemos la animación en proceso a verdadero
			animacionEnProceso = true;
			//Empieza la animación
			pt.play();
		}
	}
}