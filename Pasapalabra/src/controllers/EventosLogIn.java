package controllers;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.google.api.services.drive.model.File;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidades.Conexion_cliente;

/**Clase que gestiona los eventos de la clase LogIn.fxml
 * @author asier.gutierrez
 *
 */
public class EventosLogIn extends Control implements Initializable {

	public static Image iAvatar;
	
	
	private boolean servidorOperativo=true;
	@FXML
	private Button btnLogin;
	
	@FXML
	private Label txtIncorrecto;
	
	@FXML
	private CheckBox checkRecordar;
	
	@FXML
	public Pagination pagNoticias;
	public void setPagNoticias(Pagination pagNoticias) {
		this.pagNoticias = pagNoticias;
	}
	
	@FXML
	private PasswordField txtContra;
	
	@FXML
	private TextField txtUsuario;
	
	@FXML
	private ImageView estadoServidor;
	
	@FXML
	private Polygon fuPoly;
	
	@FXML
	private Text forkUs;
	
	@FXML
	private Label lblRegistrar;
	
	private String[]Datos_cliente=new String[2];
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try{
			utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local, utilidades.Acciones_servidor.Comprobar.toString(),null);
		}catch(Exception a){
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Parece que algo ha salido mal");

			alert.setContentText("Parece que el servidor no está operativo actualmente, por favor, intenteló de nuevo más tarde");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
			
			servidorOperativo=false;
			btnLogin.setDisable(true);
			estadoServidor.setImage(new Image("images/desconectado.png"));
			
		}
	}
	
	
	
	
	public void loginSession(MouseEvent event){
		if(txtUsuario.getText().length()>0&&txtContra.getText().length()>0){
		Datos_cliente[0]=txtUsuario.getText();
		Datos_cliente[1]=txtContra.getText();
		//txtIncorrecto.setText("Usuario y/o contraseña incorrecto/s");
		try {
			utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local, utilidades.Acciones_servidor.Log.toString(), Datos_cliente);
			if(utilidades.Conexion_cliente.Datos_Usuario.get(5)!=null){
				java.io.File file=new java.io.File(utilidades.Conexion_cliente.Datos_Usuario.get(5));
				if(file.exists()){iAvatar=new Image("file:///"+utilidades.Conexion_cliente.Datos_Usuario.get(5));
			
				}
				else{
					//TODO: hacer algo
				}
				
			}
			utilidades.deVentana.transicionVentana("Juego", event);
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Usuario o contraseña incorrectos");

			alert.setContentText("El usuario y/o la contraseña que has introducido son incorrectas, por favor, revise los datos y vuelva a intentarlo");
			alert.initModality(Modality.APPLICATION_MODAL);		
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			
		}catch (IOException e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al conectarse con el servidor");

			alert.setContentText("Parece que ha habido un problema a la hora de extablecer conexión con el servidor, por favor inténtelo de nuevo más tarde");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
		}
	
		
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Texto vacio");

			alert.setContentText("Parece que no has introducido ningún texto, por favor, intenta introducir su usuario y contraseña antes");
			alert.initModality(Modality.APPLICATION_MODAL);	
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
		}
	}
	
	public void registro(MouseEvent event){
		if(servidorOperativo==false){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("El servidor no está operativo");

			alert.setContentText("El servidor no está operativo actualmente, no se puede registrar");
			alert.initModality(Modality.APPLICATION_MODAL);			
			alert.showAndWait();
		}
		else{
		utilidades.deVentana.transicionVentana("Registro", event);
		}
		
		
	}
	
	public void irAGitHub(MouseEvent event){
		
		try {
	        Desktop.getDesktop().browse(new URL("https://github.com/asier-gutierrez/Pasapalabra").toURI());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	
		
		
		
	}

}
