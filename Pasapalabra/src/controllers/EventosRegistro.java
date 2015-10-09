package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**Clase que gestiona los eventos de la clase Registro.fxml
 * @author asier.gutierrez
 *
 */
public class EventosRegistro extends Control implements Initializable {
	//Recuerda usar JavaDoc para cada método
	//Recuerda que tienes que añadir los listener y todo eso
	//Recuerda que tienes que enlazar esta clase con el código fxml
	//(Haz esta clase la clase controladora del código fxml)
	//Si tienes dudas mira en la clase main y eventoslogin
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	
	private TextField txtNombreUsuario;
	
	private TextField txtCorreoUsuario;
	
	private PasswordField pflContrasenya;
	
	private PasswordField pfdRepetirContrasenya;

	private DatePicker dpFechaNacimiento;


	public void CrearUsuario(MouseEvent event){

		utilidades.deVentana.transicionVentana("LogIn", event);


	}


	public void CancelarUsuario(MouseEvent event){

		utilidades.deVentana.transicionVentana("LogIn", event);


	}

}
