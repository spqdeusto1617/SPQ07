package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import utilidades.RPanel;

public class EventosJuego extends Control implements Initializable{

	public boolean juegoEnCurso = false;
	public boolean ventanaMenuDentro = false;
	private ArrayList<Control> panelLetrasJugador; //Panel con todos los labels del jugador
	private ArrayList<Control> panelLetrasContrincante; //Panel con todos los labels del contrincante
	private ArrayList<Control> menuDesplegable; //Colección de todos los elementos del menu desplegable.
	private ArrayList<Control> eleccion; //Elección del tipo de juego que se quiere llevar a cabo.
	
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
    void btnJugar(ActionEvent event) {
    	if(juegoEnCurso){
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Información");
    		alert.setHeaderText(null);
    		alert.setContentText("Ya estás jugando una partida, si quieres dejarlo\nríndete.");
    		alert.showAndWait();
    	}else{
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Información");
    		alert.setHeaderText(null);
    		alert.setContentText("Ya estás en la ventana de juego, selecciona un modo.");
    		alert.showAndWait();
    	}
    }
    
    @FXML
    void btnAmigos(ActionEvent event) {
    	//Abrir agenda de amigos, contactos, etc.
    }
    
    @FXML
    void btnMiPerfil(ActionEvent event) {
    	//Abrir ventana de perfil
    }
    
    @FXML
    void btnEstadisticas(ActionEvent event) {
    	//Abrir ventana de estadísticas
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

    /**Método que gestiona la E/S del panel
     * @param event Evento sucedido
     */
    @FXML
    void esPanel(ActionEvent event) {
    	
    		new Thread(new RPanel( ventanaMenuDentro, event )).run();
    		
    }

	/* (non-Javadoc) Clase de la interfaz para que al cargarse el FXML ejecute tareas.
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO rellenar arraylist menu desplegable y elementos
		
		//TODO Cargar imagen personal
		
		//TODO Setear "estoy conectado" o ya lo setea login¿? o como¿?
		
		//TODO Setear chat¿?
		
		//TODO Setear username
		
	}


}
