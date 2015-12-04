package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class EventosAmigos extends ClaseExtensora implements Initializable {
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
	    private TextField tfdBuscarAmigos;
	
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
		 
		 utilidades.deVentana.transicionVentana("Juego", event);
	 }
	 
	 void btnPerfil(ActionEvent event){
		 
		 utilidades.deVentana.transicionVentana("Perfil", event);
	 }

	   void btnAmigos(ActionEvent event) {
	    	
	    		Alert alert = new Alert(AlertType.INFORMATION);
	    		alert.setTitle("Información");
	    		alert.setHeaderText(null);
	    		alert.setContentText("Ya estás en la ventana de amigos, selecciona una opción.");
	    		alert.showAndWait();
	    	
	    }


	 @Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Cargar lista de amigos
			//TODO buscar amigos
			//TODO recibir notificaciones de amistad
		 	//TODO: gettear el nombre de perfil
		 	//TODO: gettear la imagen de perfil
		 tblAmigos.setPlaceholder(new Label("No hay amigos disponibles"));
		}

	 void btnBuscarAmigos(ActionEvent event){
		//TODO: buscar amigo
		 //tfdBuscarAmigos.gettext();
	 }
}
