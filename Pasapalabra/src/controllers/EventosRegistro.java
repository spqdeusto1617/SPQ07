
package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

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
	
	@FXML
	private Text texDateIncorrecto;

	@FXML
	private DatePicker fcNacimiento;

	private File file;

	public static boolean Terminos=false;
	
	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		texUsuarioInvalido.setVisible(false);
		texMailIncorrecto.setVisible(false);	
		texContrasenyaCorta.setVisible(false);
		texContrasenyaEncaja.setVisible(false);
		texDateIncorrecto.setVisible(false);
		
		pfdRepetirContrasenya.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			//Focuslost del texto de: repetir contraseña, que comprueba que ambas contraseñas sean iguales
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(pflContrasenya.getText().compareTo(pfdRepetirContrasenya.getText())!=0){
					datoscorrectos=false;
					texContrasenyaEncaja.setVisible(true);

				}
				else{

					datoscorrectos=true;
					texContrasenyaEncaja.setVisible(false);
				}


			}
		} );


	}

	/**Método para comprobar que el usuario ha introducido un nombre que tiene entre 8 y 16 caracteres
	 * @param event el evento de ratón
	 */
	public void comprobarNombreUsuario(Event event){

		if(txtNombreUsuario.getLength()<7||txtNombreUsuario.getLength()>15){
			datoscorrectos=false;
			texUsuarioInvalido.setVisible(true);

		}
		else{
			datoscorrectos=true;
			texUsuarioInvalido.setVisible(false);
		}

	}


	/**Método para comprobar que el usuario ha introducido un email correcto
	 * @param event el evento de ratón
	 */
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

	/**Método para comprobar que la contraseña sea de longitud mínima de 8 caracteres. En caso contrario, no deja crear
	 * el usuario
	 * @param event el evento de ratón
	 */
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

	/**(Método obsoleto) Método para comprobar si las contraseñas de los usuarios coinciden
	 * @param event el evento de ratón
	 */
	public void comprobarContrasenya2(Event event){

		//		if(pflContrasenya.getText().compareTo(pfdRepetirContrasenya.getText())!=0){
		//			datoscorrectos=false;
		//			texContrasenyaEncaja.setVisible(true);
		//			
		//		}
		//		else{
		//			
		//			datoscorrectos=true;
		//			texContrasenyaEncaja.setVisible(false);
		//		}
	}


	/**Botón para crear el usuario. Antes de crearlo, comprueba que los datos estén completos. En caso negativo,
	 * se bloquea el botón, y no avanza 
	 * @param event el evento de ratón
	 */
	public void CrearUsuario(MouseEvent event){
		if(txtNombreUsuario.getText().length()==0||txtCorreoUsuario.getText().length()==0||pflContrasenya.getText().length()==0||pfdRepetirContrasenya.getText().length()==0)
		{
			datoscorrectos=false;
			btnCrear.setDisable(false);

		}
		else{
			utilidades.deVentana.transicionVentana("LogIn", event);
		}

	}


	/**Método que cancela el usuario que se iba a crear, si así lo considera
	 * @param event
	 */
	public void CancelarUsuario(MouseEvent event){

		utilidades.deVentana.transicionVentana("LogIn", event);


	}

	/**FileChooser para la imagen de usuario. Solo acepta imágenes del tipo: jpg, gif, bmp o png
	 * @param event el evento de ratón para activarlo
	 */
	public void cambiarImagen(MouseEvent event){
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
			System.out.println(file.getAbsolutePath());
		}catch(Exception a){
			a.printStackTrace();
		}


	}
	
	/**Método que valida la fecha que mete el usuario, si la fecha de nacimiento que mete es mayor a la actual,
	 * no le deja poner esa fecha.
	 * @param event el evento de ratón
	 */
	public void ComprobarDia(Event event){
		@SuppressWarnings("unused")
		String st = null;
		if(dpFechaNacimiento!=null){
			try {
				st = ft.format(ft.parse(this.dpFechaNacimiento.getValue().toString()));
			} catch (ParseException e) {
				System.out.println("Error");	
			}
		}
		  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
		   //get current date time with Date()
		   Date date = new Date();
		   Date date2 = Date.from(dpFechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		   dateFormat.format(date);
		   if(date.after(date2)){
			   datoscorrectos=true; 
			   texDateIncorrecto.setVisible(false);
		   }
		   else{
			   datoscorrectos=false;  
			   texDateIncorrecto.setVisible(true);
		   }
	}


	/**Método para que el usuario lea los términos y condiciones (Eula)
	 * @param event: el evento de ratón
	 */
	@SuppressWarnings({ "static-access" })
	public void IrATerminos(MouseEvent event){
		//Otro stage para cargar la ventada de términos y condiciones de servivio de la app (EULA)
		//(Mirar eventosregistro.java IrATerminos para más info)
		Stage stageTerminosyCondiciones = new Stage();
		FXMLLoader Loader = new FXMLLoader() ; 
		Parent Root = null;

		try {
			Root = Loader.load(Main.class.getResource("../windows/EULA.fxml"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		Scene Scene = new Scene(Root);
		stageTerminosyCondiciones.setScene(Scene);
		stageTerminosyCondiciones.setResizable(false);
		stageTerminosyCondiciones.show();



	}

	/**Método para comprobar si el usuario ha aceptado el eula o no (en caso de que no los acepte, el botón de crear
	 * no está enable
	 * @param event: el evento del pulsador
	 */
	public void aceptaTerminos(Event event){
		if(txtNombreUsuario.getText().length()==0||txtCorreoUsuario.getText().length()==0||pflContrasenya.getText().length()==0||pfdRepetirContrasenya.getText().length()==0||dpFechaNacimiento==null)
		{
			datoscorrectos=false;

		}

		if(chkTerminos.isSelected()&&datoscorrectos==true){
			btnCrear.setDisable(false);

		}
		else{
			btnCrear.setDisable(true);
		}


	}

}