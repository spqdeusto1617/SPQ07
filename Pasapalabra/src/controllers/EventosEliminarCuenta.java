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

/**Clase que gestiona los eventos de la clase EventosEliminarCuenta.fxml
 * @author asier.gutierrez
 *
 */
public class EventosEliminarCuenta extends Control implements Initializable{

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
	private TextField tfdMailUsuario;
	
	@FXML
	private PasswordField psfContrasenyaUsuario;
	
	@FXML
	private PasswordField psfContrasenyaEncaja;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Meter estadísticas

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
	void eliminarCuenta(ActionEvent event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminar cuenta");
		alert.setHeaderText("¿Está seguro de que desea eliminar su cuenta?");
		alert.setContentText("Si se elimina su cuenta, se perderá para siempre, junto con su información, ¿está completamente seguro?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			//VUELTA A CONFIRMAR
			Alert alert2 = new Alert(AlertType.CONFIRMATION);
			alert2.setTitle("Eliminar cuenta");
			alert2.setHeaderText("¿Está completamente seguro?");
			alert2.setContentText("No hay vuelta atras.");
			Optional<ButtonType> result2 = alert2.showAndWait();
			if (result2.get() == ButtonType.OK){
				//TODO:Comprobar datos
				//TODO:Eliminar cuenta
				Alert alert3 = new Alert(AlertType.INFORMATION);
				alert3.setTitle("Cuenta eliminada");
				alert3.setHeaderText("Cuenta eliminada con éxito");
				alert3.setContentText("Se ha eliminado su cuenta con éxito.Volverá a la ventana de login");
				alert3.showAndWait();
				utilidades.deVentana.transicionVentana("LogIn", event);
			} else {
				//NADA
			}
		} else {
			//NADA
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
