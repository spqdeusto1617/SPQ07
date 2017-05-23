package com.pasapalabra.game.controllers;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pasapalabra.game.service.ClientConnection;
import com.pasapalabra.game.utilities.WindowUtilities;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
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

/**Class to manage evets from ChangeInformation.fxml
 * @author alvaro
 *
 */
public class InformationChangeController extends ExtenderClassController implements Initializable {
	@FXML public Pane panel;

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(ThemeController.class.getName());
	//Declaration of the panel
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

	//@FXML public Rectangle rectanguloCambiarCorreo;

	//@FXML public Rectangle rectanguloCambiarContrasenya;

	//@FXML public Text txtDescartar;

	//@FXML public Text txtCambiarCorreo;

	//@FXML public Text txtCambiarContrasenya;

	@FXML public TextField tflViejoMail;

	@FXML public TextField tflNuevoMail;

	@FXML public TextField tflNuevoMail2;

	@FXML public PasswordField pflContrasenyaUsuario;

	@FXML public PasswordField pfdAntiguaContrasenya;

	@FXML public PasswordField pfdNuevaContrasenya;

	@FXML public PasswordField pfdNuevaContrasenya1;

	//Buttons
	@FXML public Button btnCerrarSesion;
	@FXML public Button btnEstadisticas;
	@FXML public Button btnPerfil;
	@FXML public Button btnAmigos;
	@FXML public Button btnJuego;
	@FXML public Button btnDescartar;
	@FXML public Button btnCambiarCorreo;
	@FXML public Button btnCambiarContrasenia;


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

	/**AccionListener to change the email after validating that the information is correct.  
	 * @param event of the action
	 */
	public void btnCambiarCorreo(Event event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar cambios");
		alert.setHeaderText("¿Está seguro de que quiere realizar estos cambios?");
		alert.setContentText("Si elige sí, se perderán todos los datos anteriores, ¿está seguro?");
		alert.initModality(Modality.APPLICATION_MODAL);

		//Elijo el dueño de la alerta (o la base) de la misma.
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			boolean datos_Correctos=true;
			if(!tflViejoMail.getText().equals(ClientConnection.userInfo.getMail())){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Mail doesn´t match");
				alert2.setHeaderText("This email does not match your previous mail");
				alert2.setContentText("Your introduced mail is incorrect. Please, reintroduce your old email and try again");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(!tflNuevoMail.getText().equals(tflNuevoMail2.getText())){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Mail don´t match");
				alert2.setHeaderText("The new mails don´t match");
				alert2.setContentText("The new introduced mail don´t match. Please check those and try again");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			Pattern VALID_EMAIL_ADDRESS_REGEX = 
					Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(tflNuevoMail.getText());

			if(!matcher.matches()){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Email not acceptable");
				alert2.setHeaderText("The introduced mail is not acceptable");
				alert2.setContentText("Please, introduce a valid mail and try again");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}

			if(datos_Correctos==true){
				try{
					//TODO: change mail (validate pass)tflNuevoMail.getText()

					Boolean opResult = ClientConnection.changeUserData(pflContrasenyaUsuario.getText(), tflNuevoMail.getText(), null, null);
					if(opResult == false){
						Alert alert2 = new Alert(AlertType.ERROR);
						alert2.setTitle("Pass incorrect");
						alert2.setHeaderText("The pass is incorrect");
						alert2.setContentText("The password is incorrect, please insert your pass again");
						alert2.initModality(Modality.APPLICATION_MODAL);
						alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert2.showAndWait();
						pflContrasenyaUsuario.setText("");
					}
					else if(opResult == true){
						Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setTitle("Success with the changes");
						alert2.setHeaderText("The data was updated correctly");
						alert2.setContentText("Your data was updated correctly");
						alert2.initModality(Modality.APPLICATION_MODAL);
						alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert2.showAndWait();
						com.pasapalabra.game.service.ClientConnection.userInfo.setMail(tflNuevoMail.getText());
						com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Profile", event);
					}else{
						WindowUtilities.forcedCloseSession(event);
					}
				}catch(NullPointerException a){
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Error trying to save your new data");
					alert2.setHeaderText("There was an error trying to update your data");
					alert2.setContentText("An error occurred trying to update your data, please try again");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
				catch(RemoteException a){
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Error trying to connect to the server");
					alert2.setHeaderText("An error ocurred with the connection");
					alert2.setContentText("An error occurred trying to access to the server, please check your connection and try again");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
				catch(Exception a){
					Alert alert2 = new Alert(AlertType.INFORMATION);
					alert2.setTitle("Unexpected error occurred");
					alert2.setHeaderText("An unexpected error occurred");
					alert2.setContentText("Please, check your connection and try again.");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
			}
			else{
				Alert alert2 = new Alert(AlertType.CONFIRMATION);
				alert2.setTitle("Revise the previous problems");
				alert2.setHeaderText("An error occurred trying to change your data");
				alert2.setContentText("There are errors in your data, please check those and try again");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
		}
		else{
			//Nada
		}
	}

	/**Method to change the password after validating that the information is correct. 
	 * @param event of the action
	 */
	public void btnCambiarContrasenya(Event event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm changes");
		alert.setHeaderText("Are you sure you want those changes?");
		alert.setContentText("All the previous data will be lost, are you sure?");
		alert.initModality(Modality.APPLICATION_MODAL);

		//Elijo el dueño de la alerta (o la base) de la misma.
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){

			boolean datos_Correctos=true;
			if(!pfdNuevaContrasenya.getText().equals(pfdNuevaContrasenya1.getText())){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("New passwords don´t match");
				alert2.setHeaderText("The new passwords don´t match");
				alert2.setContentText("The new passwords don´t match, please, check the passwords and try again");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}

			if(pfdNuevaContrasenya.getText().length()<7){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("New password sort");
				alert2.setHeaderText("The new password is too sort");
				alert2.setContentText("The new password is too sort, please introduce a pass of at least 8 characters");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(datos_Correctos==true){
				try{

					Boolean opResult = ClientConnection.changeUserData(pfdAntiguaContrasenya.getText(), null, null, pfdNuevaContrasenya.getText());
					if(opResult == false){
						Alert alert2 = new Alert(AlertType.ERROR);
						alert2.setTitle("Pass incorrect");
						alert2.setHeaderText("The pass is incorrect");
						alert2.setContentText("The password is incorrect, please insert your pass again");
						alert2.initModality(Modality.APPLICATION_MODAL);
						alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert2.showAndWait();
						pfdAntiguaContrasenya.setText("");
					}
					else if(opResult == true){
						Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setTitle("Success with the changes");
						alert2.setHeaderText("The data was updated correctly");
						alert2.setContentText("Your data was updated correctly");
						alert2.initModality(Modality.APPLICATION_MODAL);
						alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert2.showAndWait();
						com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Profile", event);
					}else{
						WindowUtilities.forcedCloseSession(event);
					}
				}catch(NullPointerException a){
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Error trying to save your new data");
					alert2.setHeaderText("There was an error trying to update your data");
					alert2.setContentText("An error occurred trying to update your data, please try again");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
				catch(RemoteException a){
					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Error trying to connect to the server");
					alert2.setHeaderText("An error ocurred with the connection");
					alert2.setContentText("An error occurred trying to access to the server, please check your connection and try again");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
				catch(Exception a){
					Alert alert2 = new Alert(AlertType.INFORMATION);
					alert2.setTitle("Unexpected error occurred");
					alert2.setHeaderText("An unexpected error occurred");
					alert2.setContentText("Please, check your connection and try again.");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
			}
			else{
				Alert alert2 = new Alert(AlertType.CONFIRMATION);
				alert2.setTitle("Revise the previous problems");
				alert2.setHeaderText("Errors occurred trying to change your data");
				alert2.setContentText("There are errors in your data, please check those and try again");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}

		}
		else{
			//Nada
		}
	}

	/**Button to discard the changes done. If he/she accepts you will return to the previous window
	 * @param event of the window
	 */
	public void btnDescartar(Event event){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		//Pone título
		alert.setTitle("Discart the changes");
		//Pone cabecera
		alert.setHeaderText("Are you sure?");
		//Pone contenido
		alert.setContentText("Are you sure you want to discard the changes?");

		//Añade modalidad
		alert.initModality(Modality.APPLICATION_MODAL);
		//Añade 'dueño'. (=La ventana sobre la cual se va a posicionar y la cual bloqueará)
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK){
			com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Profile", event);
		}
		else{
			//Nada
		}
	}





	/**Transition to game window
	 * @param event
	 */
	public void btnJugar(MouseEvent event){
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
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
		//com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Friends", event);
	}

	/**Transition to profile window
	 * @param event
	 */
	public void btnMiPerfil(MouseEvent event){
		//		Alert alert = new Alert(AlertType.INFORMATION);
		//
		//		alert.setTitle("Function not yet implemented.");
		//
		//		alert.setHeaderText("Do not use this function");
		//
		//		alert.setContentText("This Function is not implemented, please, do not use it");
		//
		//		alert.showAndWait();
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Profile", event);
	}

	/**Transition to statistics window
	 * @param event
	 */
	public void btnEstadisticas(MouseEvent event){
		//		Alert alert = new Alert(AlertType.INFORMATION);
		//
		//		alert.setTitle("Function not yet implemented.");
		//
		//		alert.setHeaderText("Do not use this function");
		//
		//		alert.setContentText("This Function is not implemented, please, do not use it");
		//
		//		alert.showAndWait();
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Statistics", event);
	}

	/**Method to close current session
	 * @param event
	 */
	public void btnCerrarSesion(MouseEvent event){
		com.pasapalabra.game.utilities.WindowUtilities.closeSession(event);
	}

	//Elimina nivel de transparencia
	@FXML
	void entrado(MouseEvent event) {
		//com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	}

	//Añade nivel de transparencia
	@FXML
	void salido(MouseEvent event) {
		//com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	}
	public void esPanel(MouseEvent event){
		//TODO: cerrar panel	
	}
}