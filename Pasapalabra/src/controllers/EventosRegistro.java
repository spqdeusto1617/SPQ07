package controllers;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
	

	@FXML
	private TextField txtNombreUsuario;

	@FXML
	private TextField txtCorreoUsuario;

	@FXML
	private PasswordField pflContrasenya;

	@FXML
	private PasswordField pfdRepetirContrasenya;

	@FXML
	private DatePicker dpFechaNacimiento;

	@FXML
	private ImageView ImgImagenUsuario;

	@FXML
	private CheckBox chkTerminos;
	
	@FXML
	private Button btnCrear;
	
	private boolean datoscorrectos=false;
	
	@FXML
	private Text texUsuarioInvalido;
	
	@FXML
	private Text texMailIncorrecto;
	
	@FXML
	private Text texContrasenyaCorta;

	@FXML
	private Text texContrasenyaEncaja;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		texUsuarioInvalido.setVisible(false);
		 texMailIncorrecto.setVisible(false);	
		 texContrasenyaCorta.setVisible(false);
		 texContrasenyaEncaja.setVisible(false);
	}
	
	public void comprobarDatosUsuario(Event event){
		
		if(txtNombreUsuario.getLength()<7||txtNombreUsuario.getLength()>15){
			datoscorrectos=false;
			texUsuarioInvalido.setVisible(true);
			
		}
		else{
			datoscorrectos=true;
			texUsuarioInvalido.setVisible(false);
		}
		
	}
	
	
	public void comprobarMailUsuario(Event event){
		Pattern VALID_EMAIL_ADDRESS_REGEX = 
			    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		
		 Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(txtCorreoUsuario.getText());
		
		 if(matcher.matches()){
			 datoscorrectos=true;
			 texMailIncorrecto.setVisible(false);
		 }
		 else{
			 texMailIncorrecto.setVisible(true);
			 datoscorrectos=false;
		 }
	}
	
	public void comprobarContrasenya1(Event event){
		
		if(pflContrasenya.getText().length()<7){
			datoscorrectos=false;
			texContrasenyaCorta.setVisible(true);
		}
		else{
			datoscorrectos=true;
			texContrasenyaCorta.setVisible(false);
		}
	}
	
	public void comprobarContrasenya2(Event event){
		
		if(pflContrasenya.getText().compareTo(pfdRepetirContrasenya.getText())!=0){
			datoscorrectos=false;
			texContrasenyaEncaja.setVisible(true);
			
		}
		else{
			
			datoscorrectos=true;
			texContrasenyaEncaja.setVisible(false);
		}
	}
	

	public void CrearUsuario(MouseEvent event){

		utilidades.deVentana.transicionVentana("LogIn", event);


	}


	public void CancelarUsuario(MouseEvent event){

		utilidades.deVentana.transicionVentana("LogIn", event);


	}
	//Habría que añadir una clase extra para el filechooser, lo dejo de momento así
	public void cambiarImagen(MouseEvent event){

		//		FileChooser fileChooser = new FileChooser();
		//		fileChooser.setTitle("Open Resource File");
		//		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		//		File selectedFile = fileChooser.getInitialDirectory();
		//		if (selectedFile != null) {
		//			System.out.println(selectedFile.getAbsolutePath());
		//		}
	}
	
	
	/**Método para que el usuario lea los términos y condiciones (Eula)
	 * @param event: el evento de ratón
	 */
	public void IrATerminos(MouseEvent event){

		try {
			Desktop.getDesktop().browse(new URL("http://www.simunova.com/eula").toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}




	}
	
	/**Método para comprobar si el usuario ha aceptado el eula o no (en caso de que no los acepte, el botón de crear
	 * no está enable
	 * @param event: el evento del pulsador
	 */
	public void aceptaTerminos(Event event){
		if(txtNombreUsuario.getText().length()==0||txtCorreoUsuario.getText().length()==0||pflContrasenya.getText().length()==0||pfdRepetirContrasenya.getText().length()==0)
		{
			datoscorrectos=false;
			System.out.println("incorrectos");
		}
	
		if(chkTerminos.isSelected()&&datoscorrectos==true){
			btnCrear.setDisable(false);
			
		}
		else{
			btnCrear.setDisable(true);
		}


	}
	
}
