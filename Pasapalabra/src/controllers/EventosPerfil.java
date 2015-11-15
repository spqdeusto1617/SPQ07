package controllers;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EventosPerfil extends Control implements Initializable {
	
	 @FXML
	    private Text textoESPanel;

	    @FXML
	    private Text textoMiPerfil;

	    @FXML
	    private ImageView logopsp;

	    @FXML
	    private Text textoCerrarSesion;

	    @FXML
	    private Rectangle rectanguloPanel;

	    @FXML
	    private Rectangle rectanguloCerrarSesion;

	    @FXML
	    private Rectangle rectanguloAmigos;

	    @FXML
	    private Text textoJugar;

	    @FXML
	    private Rectangle rectanguloMiPerfil;

	    @FXML
	    private Rectangle rectanguloJugar;

	    @FXML
	    private Text textoAmigos;

	    @FXML
	    private Circle circuloPanel;

	    @FXML
	    private Text textoNombreDeUsuario;

	    @FXML
	    private Text textoEstadisticas;

	    @FXML
	    private ImageView imagenAvatar;

	    @FXML
	    private Rectangle rectanguloEstadisticas;

	    @FXML
	    private Pane panel;
	    
	    @FXML
	    private ImageView imgAvatar2;
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO: gettear el nombre de usuario
		//TODO: gettear la imagen de usuario
		
	}
		public void btnPerfil(ActionEvent event){
			Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Información");
    		alert.setHeaderText(null);
    		alert.setContentText("Ya estás en la ventana de perfil, selecciona una opción.");
    		alert.showAndWait();
		}
	 @FXML
	    void btnCerrarSesion(ActionEvent event) {
	    	Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Cerrar sesión");
			alert.setHeaderText("¿Está seguro de querer cerrar sesión?");
			alert.setContentText("Si cierra perderá la partida en curso que\n"
					+ "pueda estar jugando o los cambios que esté"
					+ "haciendo en su cuenta si no los ha guardado aún.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK){
				//VUELTA A CONFIRMAR
				Alert alert2 = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Cerrar sesión");
				alert.setHeaderText("¿Está seguro?");
				alert.setContentText("No se podrá volver atrás.");
				Optional<ButtonType> result2 = alert2.showAndWait();
				if (result2.get() == ButtonType.OK){
				    //CERRAR SESIÓN
					
				} else {
				    //NADA
				}
			} else {
			    //NADA
			}
	    }
	 void btnJugar(ActionEvent event){
		 utilidades.deVentana.transicionVentana("Juego", event);
	 }
	
	 void btnAmigos(ActionEvent event){
		 
		 utilidades.deVentana.transicionVentana("Perfil", event);
	 }
	 void cambioMail(ActionEvent event){
		 utilidades.deVentana.transicionVentana("CambiarMail", event);
	 }
	 void cambiarContrasenya(ActionEvent event){
		 utilidades.deVentana.transicionVentana("CambiarContrasenya", event);
	 }
	 void eliminarCuenta(ActionEvent event){
		 utilidades.deVentana.transicionVentana("EliminarCuenta", event);
	 }
	 
	 void cambiaImagen (ActionEvent event){
		 Stage stageFilechooser = new Stage();
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("JPG", "*.jpg"),
					new FileChooser.ExtensionFilter("GIF", "*.gif"),
					new FileChooser.ExtensionFilter("BMP", "*.bmp"),
					new FileChooser.ExtensionFilter("PNG", "*.png")
					);
			try{
				//AnotherStage2: para que lance el filechooser
				File file = fileChooser.showOpenDialog(stageFilechooser);
				String path="file:///"+file.getAbsolutePath();
			
				imgAvatar2.setImage(new Image(path));
			}catch(Exception a){
				//TODO: meter un logger con la extepción
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error al leer la imagen");
				
				alert.setContentText("Se ha producido un error a la hora de leer la imagen. Por favor intenteló otra vez.");

				alert.showAndWait();
			}
	 }
}
