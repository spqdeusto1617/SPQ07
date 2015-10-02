package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

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
		
		
	}
	
	public void registro(MouseEvent event){
		
		
		
		
	}
	
	public void ventanaCreada(WindowEvent event){
		
		System.out.println("Creada!!");
		
		
	}
}
