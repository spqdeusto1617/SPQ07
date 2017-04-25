package com.pasapalabra.game.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pasapalabra.game.utilities.WindowUtilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FriendController extends ExtenderClassController implements Initializable {
	@FXML public Pane panel;

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(ThemeController.class.getName());
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

	@FXML public TableView<Person>tblTablaAmigos;

	@FXML public Button boton;

	@FXML public TextField txtTextoAmigos;
	private final ObservableList<Person> data =
			FXCollections.observableArrayList(new Person("A","K"));
	public static boolean TieneAmigos; 

	//final HBox hb = new HBox();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		TableColumn firstNameCol = new TableColumn("Nombre amigo");
		firstNameCol.setMinWidth(100);
		firstNameCol.setCellValueFactory(
				new PropertyValueFactory<>("Nombre amigo"));

		TableColumn lastNameCol = new TableColumn("Estado amigo");
		lastNameCol.setMinWidth(100);
		lastNameCol.setCellValueFactory(
				new PropertyValueFactory<>("Estado amigo"));

		tblTablaAmigos.setItems(data);
		tblTablaAmigos.getColumns().addAll(firstNameCol, lastNameCol);


		//Intenta obtener los amigos que tiene (si no hay, se indicaría que no tiene)

		try {
				//TODO: get firends

		} catch (Exception e) {
			e.printStackTrace();
			Alert alert3 = new Alert(AlertType.INFORMATION);
			alert3.setTitle("Se produjo un error al tramitar sus datos");
			alert3.setHeaderText("Parece que se ha producido un error al obtener sus amigos");
			alert3.setContentText("Se ha producido un error al intentar obtner sus amigos, por favor, intenteló de nuevo más tarde");
			alert3.initModality(Modality.APPLICATION_MODAL);

			alert3.showAndWait();

		}
		panel.getStylesheets().add("/css/application.css");
		rectanguloAmigos.setOpacity(1f);
		rectanguloEstadisticas.setOpacity(0.3f);
		rectanguloJugar.setOpacity(0.3f);
		rectanguloMiPerfil.setOpacity(0.3f);
		if(TieneAmigos==false){
			tblTablaAmigos.setPlaceholder(new Label("No tienes amigos aun."));
		}
		else{
			//TODO: meter los amigos
		}
		textoNombreDeUsuario.setText(com.pasapalabra.game.utilities.ClientConnexion.userInfo.getUserName());
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
	public void Anyadir(MouseEvent event){
		data.add(new Person("Z","P"));
	}
	/**AccionListener para buscar amigos. Se envía el amigo a buscar al servidor (previamente evaluado que no esté en su lista) y se le da la obción de enviar la 
	 * solicitud o no. Si contesta afirmativo, se añade una solicitud a ambos
	 * @param event
	 */
	public void btnBuscarAmigos(MouseEvent event){

		String[]Dato=new String[1];

		if(txtTextoAmigos.getText().equals(com.pasapalabra.game.utilities.ClientConnexion.userInfo.getUserName())){
			Alert alert3 = new Alert(AlertType.ERROR);
			alert3.setTitle("Error");
			alert3.setHeaderText("Te estás enviando una solicitud de amistad a ti mismo");
			alert3.setContentText("Parece que te estás enviando una solicitud de amistad a ti mismo, seguro que tienes a alguien a quien enviar la solicitud aparte de a ti mismo");
			alert3.initModality(Modality.APPLICATION_MODAL);

			alert3.showAndWait();
		}else{
			Dato[0]=txtTextoAmigos.getText();
			//TODO: set invitation
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Éxito al enviar la solicitud");

			alert.setContentText("La solicitud se envió con éxito a su amigo");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();

		}
	}
	public void btnJugar(MouseEvent event){
		WindowUtilities.windowTransition("Juego", event);
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
		WindowUtilities.windowTransition("Perfil", event);
	}


	public void btnEstadisticas(MouseEvent event){
		WindowUtilities.windowTransition("Estadisticas", event);
	}
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
		com.pasapalabra.game.utilities.WindowUtilities.efectoTransparenciaOnHover(event, this);
	}
	public void esPanel(MouseEvent event){
		//TODO: cerrar panel	
	}
}

/**Clase interna para gestionar los amigos (deprecated)
 * @author Ivan
 *
 */
class Person {

	//    private final SimpleStringProperty nombreUsuario;
	//    private final SimpleBooleanProperty estado;
	//
	//    Person(String fName, boolean lName) {
	//        this.nombreUsuario = new SimpleStringProperty(fName);
	//        this.estado = new SimpleBooleanProperty(lName);
	//    }
	//
	//    public String getFirstName() {
	//        return nombreUsuario.get();
	//    }
	//
	//    public void setFirstName(String fName) {
	//    	nombreUsuario.set(fName);
	//    }
	//
	//	public SimpleBooleanProperty getEstado() {
	//		return estado;
	//	}

	private final SimpleStringProperty firstName;
	private final SimpleStringProperty lastName;

	Person(String fName, String lName) {
		this.firstName = new SimpleStringProperty(fName);
		this.lastName = new SimpleStringProperty(lName);
	}
	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String fName) {
		firstName.set(fName);
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String fName) {
		lastName.set(fName);
	}
}