package controllers;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EventosPerfil extends ClaseExtensora implements Initializable {
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

	    @FXML public Rectangle rectanguloCambiarDatos;
	    
	    @FXML public Rectangle rectanguloEliminarCuenta;
	    
	    @FXML public ImageView imgCambioFotoPerfil;
	    
	    @FXML public Text txtCambiarDatos;
	    
	    @FXML public Text txtEliminarCuenta;
	    
	    private File file;

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			panel.getStylesheets().add("application/application.css");
			rectanguloAmigos.setOpacity(0.3f);
			rectanguloEstadisticas.setOpacity(0.3f);
			rectanguloJugar.setOpacity(0.3f);
			rectanguloMiPerfil.setOpacity(1f);
		}
		
		public void btnCambiar (MouseEvent event){
			utilidades.deVentana.transicionVentana("CambiarDatos", event);
		}
		
		public void btnEliminar (MouseEvent event){
			utilidades.deVentana.transicionVentana("EliminarCuenta", event);
		}
		
		public void cambioImagen (MouseEvent event){
			//Otro stage para cargar el filechooser
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
				file = fileChooser.showOpenDialog(stageFilechooser);
				if(file.length()>5242880){
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Tamaño excesivo");

					alert.setContentText("Parece que la imagen es muy grande, por favor, introduzca una imagen más pequeña (tamaño máximo: 5Mb).");

					alert.showAndWait();

				}
				else{
				String path="file:///"+file.getAbsolutePath();
				//TODO: cambiar la imagen en la BBDD
				imgCambioFotoPerfil.setImage(new Image(path));
				imagenAvatar.setImage(new Image(path));
				}
			}catch(Exception a){
				//TODO: meter un logger con la extepción
				//a.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error al leer la imagen");

				alert.setContentText("Se ha producido un error a la hora de leer la imagen. Por favor intenteló otra vez.");

				alert.showAndWait();

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
