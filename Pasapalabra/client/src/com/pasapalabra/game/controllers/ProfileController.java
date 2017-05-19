package com.pasapalabra.game.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.pasapalabra.game.service.ClientConnection;
import com.pasapalabra.game.utilities.WindowUtilities;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**Class that manages events from Profile.fxml
 * @author alvaro
 *
 */
public class ProfileController extends ExtenderClassController implements Initializable {
	//Se define un logger
	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(ProfileController.class.getName());

	@FXML public Pane panel;

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

	//@FXML public Rectangle rectanguloCambiarDatos;

	//@FXML public Rectangle rectanguloEliminarCuenta;

	@FXML public ImageView imgCambioFotoPerfil;

	//@FXML public Text txtCambiarDatos;

	//@FXML public Text txtEliminarCuenta;

	//Buttons
	@FXML public Button btnCerrarSesion;
	@FXML public Button btnEstadisticas;
	@FXML public Button btnPerfil;
	@FXML public Button btnAmigos;
	@FXML public Button btnJuego;
	@FXML public Button btnChangeInfo; 
	@FXML public Button btnDeleteAccount; 

	// public static ArrayList<String> Datos_Necesarios_Cliente=new ArrayList<>();

	private File file;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//		
		textoNombreDeUsuario.setText(ClientConnection.userInfo.getUserName());

		btnAmigos.setOpacity(0.3f);
		btnEstadisticas.setOpacity(0.3f);
		btnJuego.setOpacity(0.3f);
		btnPerfil.setOpacity(1f);
		if(ClientConnection.userIMG != null){
			imagenAvatar.setImage(ClientConnection.userIMG);
			imgCambioFotoPerfil.setImage(ClientConnection.userIMG);

		}else{
			String imagen = "fPerfil";
			Random rand = new Random();
			int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			if(randomNum == 666){
				imagen = "fPerfilPirata";
			}

			Image i = new Image(getClass().getResourceAsStream("/images/"+ imagen +".png"),imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
			imagenAvatar.setImage(i);
			imgCambioFotoPerfil.setImage(i);
		}

		Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		imagenAvatar.setClip(clip);
		imagenAvatar.setSmooth(true); 
		imagenAvatar.setCache(true); 
	}

	/**Transition to changeInformation window
	 * @param event
	 */
	public void changeInfo (MouseEvent event){
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ChangeInformation", event);
	}

	/**Transition to deleteAccount window
	 * @param event
	 */
	public void DeleteAccount (MouseEvent event){
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("DeleteAccount", event);
	}

	/**AccionListener to change the user image 
	 * @param event of the window
	 */
	public void cambioImagen (MouseEvent event){
		//Otro stage para cargar el filechooser
		Stage stageFilechooser = new Stage();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));

		try{
			//AnotherStage2: para que lance el filechooser
			file = fileChooser.showOpenDialog(stageFilechooser);
			if(file.length()>5242880){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Tamaño excesivo");

				alert.setContentText("Parece que la imagen es muy grande, por favor, introduzca una imagen más pequeña (tamaño máximo: 5Mb).");

				alert.showAndWait();

			}
			else{
				try{
					String path="file:///"+file.getAbsolutePath();

					BufferedImage originalImage = ImageIO.read(file);

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write( originalImage, "jpg", baos );
					baos.flush();
					byte[] imageInByte = baos.toByteArray();
					baos.close();

					String newUserIMG = Base64.encodeBase64URLSafeString(imageInByte);

					TextInputDialog dialog = new TextInputDialog("");
					dialog.setTitle("Password");
					dialog.setHeaderText("Insert your password");
					dialog.setContentText("Please enter your password:");
					Optional<String> result = dialog.showAndWait();
					if (!result.isPresent()){
						return;
					}
					Boolean opResult = ClientConnection.changeUserData(result.get(), null,newUserIMG , null);
					if(opResult == false){
						Alert alert2 = new Alert(AlertType.ERROR);
						alert2.setTitle("Pass incorrect");
						alert2.setHeaderText("The pass is incorrect");
						alert2.setContentText("The password is incorrect, please insert your pass again");
						alert2.initModality(Modality.APPLICATION_MODAL);
						alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert2.showAndWait();

					}
					else if(opResult == true){
						Alert alert2 = new Alert(AlertType.INFORMATION);
						alert2.setTitle("Success with the changes");
						alert2.setHeaderText("The data was updated correctly");
						alert2.setContentText("Your data was updated correctly");
						alert2.initModality(Modality.APPLICATION_MODAL);
						alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
						alert2.showAndWait();
						LogInController.iAvatar = new Image(path);
						ClientConnection.userIMG = new Image(path);
						imgCambioFotoPerfil.setImage(new Image(path));
						imagenAvatar.setImage(new Image(path));
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
					alert2.setTitle("Se produjo un error al tramitar sus datos");
					alert2.setHeaderText("Parece que se ha producido un error al cambiar los datos");
					alert2.setContentText("Se ha producido un error al intentar cambiar los datos, por favor, intenteló de nuevo más tarde");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
			}
		}catch(Exception a){
			//TODO: meter un logger con la extepción
			//a.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al leer la imagen");

			alert.setContentText("Se ha producido un error a la hora de leer la imagen. Por favor intenteló otra vez.");

			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());

			alert.showAndWait();

		}
	}


	/**Transition to game window
	 * @param event
	 */
	public void btnPlay(MouseEvent event){
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("ThemeElection", event);
	}

	/**Transition to friends window
	 * @param event
	 */
	public void btnFriends(MouseEvent event){
		Alert alert = new Alert(AlertType.INFORMATION);

		alert.setTitle("Function not yet implemented.");

		alert.setHeaderText("Do not use this function");

		alert.setContentText("This Function is not implemented, please, do not use it");

		alert.showAndWait();
		//com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Friends", event);
	}

	public void btnMyProfile(MouseEvent event){
//		Alert alert = new Alert(AlertType.INFORMATION);
//
//		alert.setTitle("Function not yet implemented.");
//
//		alert.setHeaderText("Do not use this function");
//
//		alert.setContentText("This Function is not implemented, please, do not use it");
//
//		alert.showAndWait();

				Alert alert = new Alert(AlertType.INFORMATION);
				log.log(Level.FINEST, "Alerta de información creada");
				//Añadimos título a la alerta
				alert.setTitle("Información");
				log.log(Level.FINEST, "Título añadido a la alerta");
				//Dejamos que la cabecera sea nula
				alert.setHeaderText(null);
				log.log(Level.FINEST, "Cabecera nula añadida a la alerta");
				//Añadimos el contenido que tendrá la alerta
				alert.setContentText("Ya estás en la ventana de perfil, selecciona una acción.");
				log.log(Level.FINEST, "Contenido de texto añadido a la alerta");
				//Añadimos modalidad de la alerta
				alert.initModality(Modality.APPLICATION_MODAL);
				log.log(Level.FINEST, "Añadida modalidad para la alerta");
				//Añadimos dueño de la alerta (Ventana sobre la cual se ejecutará)
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				log.log(Level.FINEST, "Añadido dueño sobre el cual se ejecuta la alerta. Se mostrará la alerta...");
				//Muestra la alerta y espera a que el usuario cierre la ventana
				alert.showAndWait();
				log.log(Level.FINEST, "Alerta de información creada, mostrada y cerrada");
	}


	/**Transition to statistics window
	 * @param event
	 */
	public void btnStatistics(MouseEvent event){
//		Alert alert = new Alert(AlertType.INFORMATION);
//
//		alert.setTitle("Function not yet implemented.");
//
//		alert.setHeaderText("Do not use this function");
//
//		alert.setContentText("This Function is not implemented, please, do not use it");
//
//		alert.showAndWait();
		log.log(Level.INFO, "Transition to Stadistics");
		com.pasapalabra.game.utilities.WindowUtilities.windowTransition("Statistics", event);
	}

	/**Method to close current session
	 * @param event
	 */
	public void LogOut(MouseEvent event){
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
		//TODO: cerrar panel EDIT: ABRIR Y CERRAR	
	}
}