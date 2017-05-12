package com.pasapalabra.game.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.pasapalabra.game.utilities.WindowUtilities;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**Class that manages events from DeleteAccount.fxml
 * @author alvaro
 *
 */
public class DeleteAccountController extends ExtenderClassController implements Initializable {
	@FXML public Pane panel;

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(ThemeController.class.getName());
	//Declaración del panel
	@FXML public Text textoESPanel;

	//@FXML public Text textoMiPerfil;

	@FXML public ImageView logopsp;

	//@FXML public Text textoCerrarSesion;

	@FXML public Text textoPlus;

	@FXML public Rectangle rectanguloPanel;

	@FXML public Circle circuloPlus;

	//@FXML public Rectangle rectanguloCerrarSesion;

	//@FXML public Rectangle rectanguloAmigos;

	//@FXML public Text textoJugar;

	@FXML public Text textoLogeadoComo;

	//@FXML public Rectangle rectanguloMiPerfil;

	//@FXML public Rectangle rectanguloJugar;

	//@FXML public Text textoAmigos;

	@FXML public Circle circuloPanel;

	@FXML public Text textoNombreDeUsuario;

	//@FXML public Text textoEstadisticas;

	@FXML public ImageView imagenAvatar;

	//@FXML public Rectangle rectanguloEstadisticas;

	//@FXML public Rectangle rectanguloDescartar;

	//FXML public Rectangle rectanguloCambiarCorreo;

	//@FXML public Rectangle rectanguloCambiarContrasenya;

	//@FXML public Text txtDescartar;

	//@FXML public Text txtEliminar;

	@FXML public TextField tfdCorreo;

	@FXML public PasswordField pfdContrasenya;

	@FXML public PasswordField pfdRepetirContrasenya;
	
	//Buttons
	@FXML public Button btnCerrarSesion;
	@FXML public Button btnEstadisticas;
	@FXML public Button btnPerfil;
	@FXML public Button btnAmigos;
	@FXML public Button btnJuego;
	@FXML public Button btnDescartar;
	@FXML public Button btnCambiar;
	@FXML public Button btnCambiarContrasenia;
	@FXML public Button btnEliminar;




	@Override
	public void initialize(URL location, ResourceBundle resources) {

		panel.getStylesheets().add("/css/application.css");
		textoNombreDeUsuario.setText(com.pasapalabra.game.service.ClientConnection.userInfo.getUserName());
		if(LogInController.iAvatar!=null){
			imagenAvatar.setImage(LogInController.iAvatar);
		}else{
			String imagen = "fPerfil";
			Random rand = new Random();
			int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			if(randomNum == 666){
				imagen = "fPerfilPirata";
			}

			Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
			imagenAvatar.setImage(i);
		}
		Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		imagenAvatar.setClip(clip);
		imagenAvatar.setSmooth(true); 
		imagenAvatar.setCache(true); 
	}

	/**After validating the information of the user, this method will
	 * delete the user account from the database.
	 * @param event of the window
	 */
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
			alert2.initModality(Modality.APPLICATION_MODAL);

			//Elijo el dueño de la alerta (o la base) de la misma.
			alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			Optional<ButtonType> result2 = alert2.showAndWait();
			if (result2.get() == ButtonType.OK){
				boolean datos_Correctos=true;
				if(!tfdCorreo.getText().equals(com.pasapalabra.game.service.ClientConnection.userInfo.getMail())){
					datos_Correctos=false;
					Alert alert3 = new Alert(AlertType.ERROR);
					alert3.setTitle("Mail no coincide");
					alert3.setHeaderText("El mail no coincide con el suyo");
					alert3.setContentText("Su antiguo mail no coincide, por favor, revíseló de nuevo");
					alert3.initModality(Modality.APPLICATION_MODAL);
					alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert3.showAndWait();
				}
				if(!pfdContrasenya.getText().equals(pfdRepetirContrasenya.getText())){
					datos_Correctos=false;
					Alert alert3 = new Alert(AlertType.ERROR);
					alert3.setTitle("Contraseñas no coinciden");
					alert3.setHeaderText("Las contraseñas no coinciden");
					alert3.setContentText("Las contrasñeas no coinciden, por favor, reviselas y vuelva a intentarlo");
					alert3.initModality(Modality.APPLICATION_MODAL);
					alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert3.showAndWait();
				}
				/*if(!pfdContrasenya.getText().equals(com.pasapalabra.game.utilities.Conexion_cliente.Datos_Usuario.get(3))){
					datos_Correctos=false;
					Alert alert3 = new Alert(AlertType.ERROR);
					alert3.setTitle("Contraseña incorrecta");
					alert3.setHeaderText("Su contraseña es incorrecta");
					alert3.setContentText("Su contraseña no es correcta, revísela de nuevo");
					alert3.initModality(Modality.APPLICATION_MODAL);
					alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert3.showAndWait();
				}*/


				if(datos_Correctos==true){
					try{
						//TODO: Delete account
						Alert alert3 = new Alert(AlertType.INFORMATION);
						alert3.setTitle("Éxito al eliminar su cuenta");
						alert3.setHeaderText("Se ha eliminado su cuenta con éxito");
						alert3.setContentText("Se ha producido la eliminación de su cuenta, procedemos a enviarlo al LogIn");
						alert3.initModality(Modality.APPLICATION_MODAL);
						alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert3.showAndWait();
						WindowUtilities.windowTransition("LogIn", event);
					}catch(Exception a){
						Alert alert3 = new Alert(AlertType.INFORMATION);
						alert3.setTitle("Se produjo un error al tramitar sus datos");
						alert3.setHeaderText("Parece que se ha producido un error al cambiar los datos");
						alert3.setContentText("Se ha producido un error al intentar cambiar los datos, por favor, intenteló de nuevo más tarde");
						alert3.initModality(Modality.APPLICATION_MODAL);
						alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert3.showAndWait();
					}
				}
				else{
					Alert alert3 = new Alert(AlertType.INFORMATION);
					alert3.setTitle("Revise los problemas");
					alert3.setHeaderText("Ha habido problemas al tramitar la solicitud");
					alert3.setContentText("Hay errores con sus datos, por favor, reviselós y vuelva a intentarlo");
					alert3.initModality(Modality.APPLICATION_MODAL);
					alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert3.showAndWait();
				}
			}
			else{

			}
		}
		else{
			//Nada

		}
	}

	/**Method to discard the changes made. If is accepted, you will return to the 
	 * previous window. 
	 * @param event event of the window
	 */
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
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Profile", event);
		}
		else{
			//Nada
		}
	}




	
	/**Transition to Game window
	 * @param event
	 */
	public void btnJugar(MouseEvent event){
		WindowUtilities.windowTransition("ThemeElection", event);
	}
	
	/**Transition to friends window
	 * @param event
	 */
	public void btnAmigos(MouseEvent event){
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Function not yet implemented.");
		
		alert.setHeaderText("Do not use this function");
		 
		alert.setContentText("This Function is not implemented, please, do not use it");

		alert.showAndWait();
		//WindowUtilities.windowTransition("Friends", event);
	}

	/**Transition to profile window
	 * @param event
	 */
	public void btnMiPerfil(MouseEvent event){
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Function not yet implemented.");
		
		alert.setHeaderText("Do not use this function");
		 
		alert.setContentText("This Function is not implemented, please, do not use it");

		alert.showAndWait();
		//WindowUtilities.windowTransition("Profile", event);
	}

	/**Transition to statistics window
	 * @param event
	 */
	public void btnEstadisticas(MouseEvent event){
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Function not yet implemented.");
		
		alert.setHeaderText("Do not use this function");
		 
		alert.setContentText("This Function is not implemented, please, do not use it");

		alert.showAndWait();
		//WindowUtilities.windowTransition("Statistics", event);
	}

	
	/**Method that closes the current session
	 * @param event
	 */
	public void btnCerrarSesion(MouseEvent event){
		WindowUtilities.closeSession(event);
	}



	//Elimina nivel de transparencia
	@FXML
	void entrado(MouseEvent event) {
		WindowUtilities.efectoTransparenciaOnHover(event, this);
	}

	//Añade nivel de transparencia
	@FXML
	void salido(MouseEvent event) {
		WindowUtilities.efectoTransparenciaOnHover(event, this);
	}
	public void esPanel(MouseEvent event){
		//TODO: cerrar panel	
	}
}