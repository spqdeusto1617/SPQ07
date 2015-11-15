package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class EventosCambiarMail extends Control implements Initializable {
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
	private TableView<?> tblAmigos;

	@FXML
	private TextField tfdAntiguoMail;
	
	@FXML
	private TextField tfdNuevoMail;
	
	@FXML
	private TextField tfdRepetirMail;
	
	@FXML
	private PasswordField pswContrasenyaUsuario;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

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
			alert2.setTitle("Cerrar sesión");
			alert2.setHeaderText("¿Está seguro?");
			alert2.setContentText("No se podrá volver atrás.");
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
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Descartar cambios");
		alert.setHeaderText("¿Está seguro de que quiere descartar los cambios?");
		alert.setContentText("Si sale se perderán todos los cambios que quiera hacer en su cuenta, ¿Está seguro de querer desacer los cambios");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			utilidades.deVentana.transicionVentana("Juego", event);
		}
		else{
			//Nada
		}

	}

	void btnAmigos(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Descartar cambios");
		alert.setHeaderText("¿Está seguro de que quiere descartar los cambios?");
		alert.setContentText("Si sale se perderán todos los cambios que quiera hacer en su cuenta, ¿Está seguro de querer desacer los cambios");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			utilidades.deVentana.transicionVentana("Amigos", event);
		}
		else{
			//Nada
		}

	}

	void btnPerfil(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Descartar cambios");
		alert.setHeaderText("¿Está seguro de que quiere descartar los cambios?");
		alert.setContentText("Si sale se perderán todos los cambios que quiera hacer en su cuenta, ¿Está seguro de querer desacer los cambios");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			utilidades.deVentana.transicionVentana("Perfil", event);
		}
		else{
			//Nada
		}
	}
	void btnCambiarCorreo(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Aplicar cambios");
		alert.setHeaderText("¿Está seguro de que quiere aplicar los cambios?");
		alert.setContentText("Si se cambian los datos puede que no se puedan volver a recuperar, ¿está completamente seguro?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			
			//TODO: validar datos
			//TODO: aplicar cambios
			utilidades.deVentana.transicionVentana("Perfil", event);
		}
		else{
			//Nada
		}
	}
	void cancelarCambios(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Descartar cambios");
		alert.setHeaderText("¿Está seguro de que quiere descartar los cambios?");
		alert.setContentText("Si sale se perderán todos los cambios que quiera hacer en su cuenta, ¿Está seguro de querer desacer los cambios");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			utilidades.deVentana.transicionVentana("Perfil", event);
		}
		else{
			//Nada
		}
	}
}
