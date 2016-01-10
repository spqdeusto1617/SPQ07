package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**Clase para generalizar el menú
 * @author asier.gutierrez
 *
 */
public abstract class ClaseExtensora extends Control{
//Declaración de todos los elementos del menú
	   	@FXML public Text textoESPanel;

	    @FXML public Text textoMiPerfil;

	    @FXML public ImageView logopsp;

	    @FXML public Text textoCerrarSesion;
	    
	    @FXML public Text textoPlus;

	    @FXML public Rectangle rectanguloPanel;
	    
	    @FXML public Circle circuloPlus;

	    @FXML public Rectangle rectanguloCerrarSesion;

	    @FXML public Rectangle rectanguloAmigos;

	    @FXML public Text textoJugar;
	    
	    @FXML public Text textoLogeadoComo;

	    @FXML public Rectangle rectanguloMiPerfil;

	    @FXML public Rectangle rectanguloJugar;

	    @FXML public Text textoAmigos;

	    @FXML public Circle circuloPanel;

	    @FXML public Text textoNombreDeUsuario;

	    @FXML public Text textoEstadisticas;

	    @FXML public ImageView imagenAvatar;

	    @FXML public Rectangle rectanguloEstadisticas;

	    @FXML public Pane panel;
}
