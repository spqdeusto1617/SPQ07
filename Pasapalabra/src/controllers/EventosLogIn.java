package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**Clase que gestiona los eventos de la clase LogIn.fxml
 * @author asier.gutierrez
 *
 */
public class EventosLogIn extends Control implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
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
	private Label lblRegistrar;
	
	public void loginSession(MouseEvent event){
		
		txtIncorrecto.setText("Usuario y/o contraseña incorrecto/s");
		//Si todo va bien:
//		Transición de ventana
		utilidades.deVentana.transicionVentana("AcercaDe", event);
	}
	
	public void registro(MouseEvent event){
		
		
		
		
	}

}
