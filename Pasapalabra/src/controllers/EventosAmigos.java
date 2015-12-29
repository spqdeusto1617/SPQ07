package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.TabableView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EventosAmigos extends ClaseExtensora implements Initializable {
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

	@FXML public Rectangle rectanguloBuscarAmigos;

	@FXML public Text texTextoBuscarAmigos;

	@FXML public TableView<?>tblTablaAmigos;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Verificar si el usuario tiene amigos
		panel.getStylesheets().add("application/application.css");
		rectanguloAmigos.setOpacity(1f);
		rectanguloEstadisticas.setOpacity(0.3f);
		rectanguloJugar.setOpacity(0.3f);
		rectanguloMiPerfil.setOpacity(0.3f);
		tblTablaAmigos.setPlaceholder(new Label("No tienes amigos aun."));
		textoNombreDeUsuario.setText(utilidades.Conexion_cliente.Datos_Usuario.get(0));
		if(EventosLogIn.iAvatar!=null){
			imagenAvatar.setImage(EventosLogIn.iAvatar);
		}else{
			String imagen = "fPerfil";
			Random rand = new Random();
			int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			if(randomNum == 666){
				imagen = "fPerfilPirata";
			}
			
			Image i = new Image("images/"+ imagen +".png",imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
			imagenAvatar.setImage(i);
		}
		Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		imagenAvatar.setClip(clip);
		imagenAvatar.setSmooth(true); 
		imagenAvatar.setCache(true); 
	}

	public void btnBuscarAmigos(MouseEvent event){
		//TODO: buscar amigos
	}
	public void btnJugar(MouseEvent event){
		utilidades.deVentana.transicionVentana("Juego", event);
	}

	public void btnAmigos(MouseEvent event){
		Alert alert = new Alert(AlertType.INFORMATION);
		log.log(Level.FINEST, "Alerta de información creada");
		//Añadimos título a la alerta
		alert.setTitle("Información");
		log.log(Level.FINEST, "Título añadido a la alerta");
		//Dejamos que la cabecera sea nula
		alert.setHeaderText(null);
		log.log(Level.FINEST, "Cabecera nula añadida a la alerta");
		//Añadimos el contenido que tendrá la alerta
		alert.setContentText("Ya estás en la ventana de amigos, selecciona algo.");
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
