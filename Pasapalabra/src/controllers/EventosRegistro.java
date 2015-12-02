
package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

	//Otro stage para cargar la ventada de términos y condiciones de servivio de la app (EULA)
	//(Mirar eventosregistro.java IrATerminos para más info)


	@FXML
	private  TextField txtNombreUsuario;


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

	private boolean datosCorrectos=false;

	@FXML
	private DatePicker fcNacimiento;

	private File file;

	private boolean Terminos=false;


	ContextMenu userNameValidator = new ContextMenu();


	ContextMenu userMailValidator = new ContextMenu();

	ContextMenu userPasswordValidator = new ContextMenu();

	ContextMenu userPasswordValidator2 = new ContextMenu();

	ContextMenu userDateValidator = new ContextMenu();

	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");


	@Override
	public void initialize(URL location, ResourceBundle resources) {


		userNameValidator.getItems().add(
				new MenuItem("El nombre de usuario tiene que tener entre 8-16 caracteres"));

		userMailValidator.getItems().add(
				new MenuItem("El correo no es válido"));

		userPasswordValidator.getItems().add(
				new MenuItem("Contraseña corta"));

		userPasswordValidator2.getItems().add(
				new MenuItem("Las contraseñas no coinciden"));

		userDateValidator.getItems().add(
				new MenuItem("No puedes haber nacido en el futuro"));

		pfdRepetirContrasenya.focusedProperty().addListener(new ChangeListener<Boolean>() {

			//Focuslost del texto de: repetir contraseña, que comprueba que ambas contraseñas sean iguales
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(pflContrasenya.getText().compareTo(pfdRepetirContrasenya.getText())!=0){
					datosCorrectos=false;

					userPasswordValidator2.show(pfdRepetirContrasenya, Side.BOTTOM, 0, 0);

				}
				else{
					userPasswordValidator2.hide();
					datosCorrectos=true;
					chkTerminos.setSelected(false);
				}


			}
		} );


	}

	/**Método para comprobar que el usuario ha introducido un nombre que tiene entre 8 y 16 caracteres
	 * @param event el evento de ratón
	 */
	public void comprobarNombreUsuario(Event event){

		if(txtNombreUsuario.getLength()<7||txtNombreUsuario.getLength()>15){
			datosCorrectos=false;
			userNameValidator.show(txtNombreUsuario, Side.BOTTOM, 0, 0);

		}
		else{
			userNameValidator.hide();
			datosCorrectos=true;
			chkTerminos.setSelected(false);

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
			datosCorrectos=true;
			userMailValidator.hide();
			chkTerminos.setSelected(false);
		}
		else{
			userMailValidator.show(txtCorreoUsuario, Side.BOTTOM, 0, 0);
			datosCorrectos=false;
		}
	}

	/**Método para comprobar que la contraseña sea de longitud mínima de 8 caracteres. En caso contrario, no deja crear
	 * el usuario
	 * @param event el evento de ratón
	 */
	public void comprobarContrasenya1(Event event){

		if(pflContrasenya.getText().length()<7){
			datosCorrectos=false;
			userPasswordValidator.show(pflContrasenya, Side.BOTTOM, 0, 0);
		}
		else{
			datosCorrectos=true;
			userPasswordValidator.hide();
			chkTerminos.setSelected(false);
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
	public void crearUsuario(MouseEvent event){
		if(txtNombreUsuario.getText().length()==0||txtCorreoUsuario.getText().length()==0||pflContrasenya.getText().length()==0||pfdRepetirContrasenya.getText().length()==0)
		{
			datosCorrectos=false;
			btnCrear.setDisable(true);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Parece que falla algo");

			alert.setContentText("Parece que hay algún problema con los datos. Por favor, revise los datos antes de registrarse.");

			alert.showAndWait();
			chkTerminos.setSelected(false);

		}
		else{
			utilidades.deVentana.transicionVentana("LogIn", event);
		}

	}


	/**Método que cancela el usuario que se iba a crear, si así lo considera
	 * @param event
	 */
	public void cancelarUsuario(MouseEvent event){

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
			String path="file:///"+file.getAbsolutePath();

			ImgImagenUsuario.setImage(new Image(path));
		}catch(Exception a){
			//TODO: meter un logger con la extepción
			//a.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al leer la imagen");

			alert.setContentText("Se ha producido un error a la hora de leer la imagen. Por favor intenteló otra vez.");

			alert.showAndWait();

		}


	}

	/**Método que valida la fecha que mete el usuario, si la fecha de nacimiento que mete es mayor a la actual,
	 * no le deja poner esa fecha.
	 * @param event el evento de ratón
	 */
	public void comprobarDia(Event event){
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
			datosCorrectos=true; 
			userDateValidator.hide();
			chkTerminos.setSelected(false);
		}
		else{
			datosCorrectos=false;  
			userDateValidator.show(dpFechaNacimiento, Side.BOTTOM, -35, 0);
		}
	}


	/**Método para que el usuario lea los términos y condiciones (Eula)
	 * @param event: el evento de ratón
	 */

	public void irATerminos(MouseEvent event){
		//TODO: meter el pad
		URL res = getClass().getClassLoader().getResource("pdfEULA/EULA.pdf");
		
			File ficheroPDF=new File((res.getPath()));


			//		FXMLLoader Loader = new FXMLLoader() ; 
			//		Parent Root =null;
			//		
			//
			//		try {
			//			Root = (AnchorPane) Loader.load(this.getClass().getResource("../windows/EULA.fxml").openStream());
			//			//Root = Loader.load(Main.class.getResource("../windows/EULA.fxml"));
			//		} catch (IOException e) {
			//
			//			e.printStackTrace();
			//		}
			//		Stage stageTerminosyCondiciones = new Stage();
			//		
			//		Scene Scene = new Scene(Root);
			//		stageTerminosyCondiciones.setScene(Scene);
			//		stageTerminosyCondiciones.setResizable(false);
			//		stageTerminosyCondiciones.show();
			try{	 
				new Thread(new Runnable() {  
					@Override  
					public void run() {  
						try {  
							Desktop.getDesktop().open(ficheroPDF);  
						} catch (IOException e) {  
							// TODO introducir logger de error

						}  
					}  
				}).start();  
			}catch(Exception a){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Parece que hubo un error");
				alert.setContentText("No se pudo abrir el archivo con un visor de PDF, se mostrará a continuación");
				//TODO: texto
				

				// Create expandable Exception.
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				
				String textoEULA =""; 

				Label label = new Label("The exception stacktrace was:");

				TextArea textArea = new TextArea(textoEULA);
				textArea.setEditable(false);
				textArea.setWrapText(true);

				textArea.setMaxWidth(Double.MAX_VALUE);
				textArea.setMaxHeight(Double.MAX_VALUE);
				GridPane.setVgrow(textArea, Priority.ALWAYS);
				GridPane.setHgrow(textArea, Priority.ALWAYS);

				GridPane expContent = new GridPane();
				expContent.setMaxWidth(Double.MAX_VALUE);
				expContent.add(label, 0, 0);
				expContent.add(textArea, 0, 1);

				// Set expandable Exception into the dialog pane.
				alert.getDialogPane().setExpandableContent(expContent);

				alert.showAndWait();	
				
//				Stage anotherStage = new Stage();
//				Pane page ;
//				try {
//					page = FXMLLoader.load(Main.class.getResource("../windows/EULA.fxml"));
//					 Scene anotherScene = new Scene(page);
//			            anotherStage.setScene(anotherScene);
//			            anotherStage.show();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}// FXML for second stage
//	           // Parent anotherRoot = page.load();
//	           

			}
		}

		 
	



	/**Método para comprobar si el usuario ha aceptado el eula o no (en caso de que no los acepte, el botón de crear
	 * no está aactivo)
	 * @param event: el evento del pulsador
	 */
	public void aceptaTerminos(Event event){
		if(txtNombreUsuario.getText().length()==0||txtCorreoUsuario.getText().length()==0||pflContrasenya.getText().length()==0||pfdRepetirContrasenya.getText().length()==0||dpFechaNacimiento==null)
		{
			datosCorrectos=false;

		}

		if(chkTerminos.isSelected()&&datosCorrectos==true){
			btnCrear.setDisable(false);

		}
		else{
			btnCrear.setDisable(true);
		}


	}

}