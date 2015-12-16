package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EventosEliminarCuenta extends ClaseExtensora implements Initializable {
	@FXML public Pane panel;
	
	public static Logger log = utilidades.AppLogger.getWindowLogger(EventosJuego.class.getName());
		//Declaración del panel
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

	    @FXML public Rectangle rectanguloDescartar;
	    
	    @FXML public Rectangle rectanguloCambiarCorreo;
	    
	    @FXML public Rectangle rectanguloCambiarContrasenya;
	    
	    @FXML public Text txtDescartar;
	    
	    @FXML public Text txtEliminar;
	    	    
	    @FXML public TextField tfdCorreo;
	    
	    @FXML public PasswordField pfdContrasenya;
	    
	    @FXML public PasswordField pfdRepetirContrasenya;
	    
	  
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		panel.getStylesheets().add("application/application.css");
	}
	
	public void btnEliminarCuenta(Event event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar cambios");
		alert.setHeaderText("¿Está seguro de que quiere eliminar su cuenta?");
		alert.setContentText("Si elige sí, se perderán todos sus datos, ¿está seguro?");
		alert.initModality(Modality.APPLICATION_MODAL);
		
		//Elijo el dueño de la alerta (o la base) de la misma.
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			Alert alert2 = new Alert(AlertType.CONFIRMATION);
			alert2.setTitle("Confirmar cambios");
			alert2.setHeaderText("¿Realmente está seguro?");
			alert2.setContentText("Lamento ser pesado, pero si elimina su cuenta se perderán todos sus datos, partidas, amigos..., ¿está completamente seguro?");
			alert.initModality(Modality.APPLICATION_MODAL);
			
			//Elijo el dueño de la alerta (o la base) de la misma.
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			Optional<ButtonType> result2 = alert2.showAndWait();
			if (result2.get() == ButtonType.OK){
				//TODO: validar datos
				//TODO: eliminar cuenta
			}
			else{
				
			}
		}
		else{
			//Nada
			
		}
	}
	
	
	public void btnDescartar(Event event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Descartar cambios");
		alert.setHeaderText("¿Está seguro de que quiere descartar los cambios?");
		alert.setContentText("Si sale se perderán todos los cambios que quiera hacer en su cuenta, ¿Está seguro de querer desacer los cambios");
		alert.initModality(Modality.APPLICATION_MODAL);
		
		//Elijo el dueño de la alerta (o la base) de la misma.
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			utilidades.deVentana.transicionVentana("Perfil", event);
		}
		else{
			//Nada
		}
	}
	
	
	
	
	//Transiciones de ventana
	public void btnJugar(MouseEvent event){
		utilidades.deVentana.transicionVentana("Juego", event);
	}
	
	public void btnAmigos(MouseEvent event){
		utilidades.deVentana.transicionVentana("Amigos", event);
	}
	
	public void btnMiPerfil(MouseEvent event){

		utilidades.deVentana.transicionVentana("Perfil", event);
	}
	
	
	public void btnEstadisticas(MouseEvent event){
		utilidades.deVentana.transicionVentana("Estadisticas", event);
	}
	
	public void btnCerrarSesion(MouseEvent event){
		utilidades.deVentana.cerrarSesion(event);
	}
	
	
	
	  //Elimina nivel de transparencia
    @FXML
    void entrado(MouseEvent event) {
    	utilidades.deVentana.efectoTransparenciaOnHover(event, this);
    }
    
    //Añade nivel de transparencia
    @FXML
    void salido(MouseEvent event) {
    	utilidades.deVentana.efectoTransparenciaOnHover(event, this);
    }
	public void esPanel(MouseEvent event){
	//TODO: cerrar panel	
	}
}
