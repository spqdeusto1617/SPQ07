package com.pasapalabra.game.controllers;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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

public class EventosCambiarDatos extends ClaseExtensora implements Initializable {
	@FXML public Pane panel;

	public static Logger log = com.pasapalabra.game.utilidades.AppLogger.getWindowLogger(EventosJuego.class.getName());
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

	@FXML public Text txtCambiarCorreo;

	@FXML public Text txtCambiarContrasenya;

	@FXML public TextField tflViejoMail;

	@FXML public TextField tflNuevoMail;

	@FXML public TextField tflNuevoMail2;

	@FXML public PasswordField pflContrasenyaUsuario;

	@FXML public PasswordField pfdAntiguaContrasenya;

	@FXML public PasswordField pfdNuevaContrasenya;

	@FXML public PasswordField pfdNuevaContrasenya1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		panel.getStylesheets().add("application/application.css");
		textoNombreDeUsuario.setText(com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(0));
		if(EventosLogIn.iAvatar!=null){
			imagenAvatar.setImage(EventosLogIn.iAvatar);
		}else{
			String imagen = "fPerfil";
			Random rand = new Random();
			int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			if(randomNum == 666){
				imagen = "fPerfilPirata";
			}

			Image i = new Image(getClass().getResourceAsStream("/com/pasapalabra/game/images/"+ imagen +".png"),imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
			imagenAvatar.setImage(i);
		}
		Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		imagenAvatar.setClip(clip);
		imagenAvatar.setSmooth(true); 
		imagenAvatar.setCache(true); 
	}

	/**AccionListener para Cambiar tu correo, te pide validar datos, y si son correctos, intenta cambiarlo en el servidor 
	 * @param event el evento de acción
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
			if(!tflViejoMail.getText().equals(com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(2))){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Mail no coincide");
				alert2.setHeaderText("El mail no coincide con el suyo");
				alert2.setContentText("Su antiguo mail no coincide, por favor, revíseló de nuevo");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(!tflNuevoMail.getText().equals(tflNuevoMail2.getText())){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Mail no coincide");
				alert2.setHeaderText("Los nuevos Mails no coinciden");
				alert2.setContentText("Los mails no coinciden, por favor, reviselos y vuelva a intentarlo");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(!pflContrasenyaUsuario.getText().equals(com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(3))){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Contraseña incorrecta");
				alert2.setHeaderText("Su contraseña es incorrecta");
				alert2.setContentText("Su contraseña no es correcta, revísela de nuevo");
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
				alert2.setTitle("Mail no válido");
				alert2.setHeaderText("El mail que ha introducido no es válido");
				alert2.setContentText("El mail que se ha introducido no es válido, intente introducir uno válido la próxima vez");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}

			if(datos_Correctos==true){
				try{
					String[]Datos=new String[4];

					Datos[0]=com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(0);
					Datos[1]=tflNuevoMail.getText();
					Datos[2]=com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(3);	
					Datos[3]=com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(5);
					com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Mail.toString(), Datos);
					com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.add(2, tflNuevoMail.getText());
					Alert alert2 = new Alert(AlertType.INFORMATION);
					alert2.setTitle("Éxito al cambiar sus datos");
					alert2.setHeaderText("Se han cambiado los datos con éxito");
					alert2.setContentText("Sus datos se han alterado con éxito");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
					com.pasapalabra.game.utilidades.deVentana.transicionVentana("Perfil", event);
				}catch(Exception a){
					Alert alert2 = new Alert(AlertType.INFORMATION);
					alert2.setTitle("Se produjo un error al tramitar sus datos");
					alert2.setHeaderText("Parece que se ha producido un error al cambiar los datos");
					alert2.setContentText("Se ha producido un error al intentar cambiar los datos, por favor, intenteló de nuevo más tarde");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
			}
			else{
				Alert alert2 = new Alert(AlertType.CONFIRMATION);
				alert2.setTitle("Revise los problemas");
				alert2.setHeaderText("Ha habido problemas al tramitar la solicitud");
				alert2.setContentText("Hay errores con sus datos, por favor, reviselós y vuelva a intentarlo");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
		}
		else{
			//Nada
		}
	}

	/**Idéntico que el correo, pero con la contraseña
	 * @param event el evento
	 */
	public void btnCambiarContrasenya(Event event){
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
			if(!pfdAntiguaContrasenya.getText().equals(com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(3))){
				datos_Correctos=false;
				//System.out.println("Contraseña vieja: "+utilidades.Conexion_cliente.Datos_Usuario.get(3)+" nueva contraseña: "+pfdAntiguaContrasenya.getText());
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Contraseña incorrecta");
				alert2.setHeaderText("La contraseña no coincide");
				alert2.setContentText("Su contraseña no coincide con su antigua contraseña, por favor, revíseló de nuevo");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(!pfdNuevaContrasenya.getText().equals(pfdNuevaContrasenya1.getText())){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Contraseña no coincide");
				alert2.setHeaderText("Las nuevas contraseñas no coinciden");
				alert2.setContentText("Las nuevas contraseñas no coinciden, por favor, reviselas y vuelva a intentarlo");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(pfdNuevaContrasenya.getText().equals(pfdAntiguaContrasenya.getText())){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Las contraseñas son iguales");
				alert2.setHeaderText("La nueva contraseña es igual");
				alert2.setContentText("La nuevas contraseña es igual que la anterior, por favor, intente introducir una contraseña nueva");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(pfdNuevaContrasenya.getText().length()<7){
				datos_Correctos=false;
				Alert alert2 = new Alert(AlertType.ERROR);
				alert2.setTitle("Nueva contraseña corta");
				alert2.setHeaderText("La nueva contraseña es muy corta");
				alert2.setContentText("La nuevas contraseña es demasiado corta, intente introducir una más lafga(8 caracteres mínimo)");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}
			if(datos_Correctos==true){
				try{
					String[]Datos=new String[4];

					Datos[0]=com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(0);
					Datos[1]=com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(2);
					Datos[2]=pfdNuevaContrasenya.getText();
					Datos[3]=com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.get(5);
					com.pasapalabra.game.utilidades.Conexion_cliente.lanzaConexion(com.pasapalabra.game.utilidades.Conexion_cliente.Ip_Local,com.pasapalabra.game.utilidades.Acciones_servidor.Pass.toString(), Datos);
					com.pasapalabra.game.utilidades.Conexion_cliente.Datos_Usuario.add(3, pfdNuevaContrasenya.getText());
					Alert alert2 = new Alert(AlertType.INFORMATION);
					alert2.setTitle("Éxito al cambiar sus datos");
					alert2.setHeaderText("Se han cambiado los datos con éxito");
					alert2.setContentText("Sus datos se han alterado con éxito");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
					com.pasapalabra.game.utilidades.deVentana.transicionVentana("Perfil", event);
				}catch(Exception a){

					Alert alert2 = new Alert(AlertType.ERROR);
					alert2.setTitle("Se produjo un error al tramitar sus datos");
					alert2.setHeaderText("Parece que se ha producido un error al cambiar los datos");
					alert2.setContentText("Se ha producido un error al intentar cambiar los datos, por favor, intenteló de nuevo más tarde");
					alert2.initModality(Modality.APPLICATION_MODAL);
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					alert2.showAndWait();
				}
			}
			else{
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("Revise los problemas");
				alert2.setHeaderText("Ha habido problemas al tramitar la solicitud");
				alert2.setContentText("Hay errores con sus datos, por favor, reviselós y vuelva a intentarlo");
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();
			}

		}
		else{
			//Nada
		}
	}

	/**Botón para descartar los cambios realizados. Si se acepta rechazarlos, se vuelve a la ventana anterior
	 * @param event el evneto de ventana
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
			com.pasapalabra.game.utilidades.deVentana.transicionVentana("Perfil", event);
		}
		else{
			//Nada
		}
	}




	//Transiciones de ventana
	public void btnJugar(MouseEvent event){
		com.pasapalabra.game.utilidades.deVentana.transicionVentana("Juego", event);
	}

	public void btnAmigos(MouseEvent event){
		com.pasapalabra.game.utilidades.deVentana.transicionVentana("Amigos", event);
	}

	public void btnMiPerfil(MouseEvent event){

		com.pasapalabra.game.utilidades.deVentana.transicionVentana("Perfil", event);
	}


	public void btnEstadisticas(MouseEvent event){
		com.pasapalabra.game.utilidades.deVentana.transicionVentana("Estadisticas", event);
	}

	public void btnCerrarSesion(MouseEvent event){
		com.pasapalabra.game.utilidades.deVentana.cerrarSesion(event);
	}

	//Elimina nivel de transparencia
	@FXML
	void entrado(MouseEvent event) {
		com.pasapalabra.game.utilidades.deVentana.efectoTransparenciaOnHover(event, this);
	}

	//Añade nivel de transparencia
	@FXML
	void salido(MouseEvent event) {
		com.pasapalabra.game.utilidades.deVentana.efectoTransparenciaOnHover(event, this);
	}
	public void esPanel(MouseEvent event){
		//TODO: cerrar panel	
	}
}