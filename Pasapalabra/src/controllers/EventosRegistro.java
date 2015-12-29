
package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Utilities;

import org.omg.CORBA.UserException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Modality;
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

	//Otro stage para cargar la ventada de términos y condiciones de servivio de la app (EULA)
	//(Mirar eventosregistro.java IrATerminos para más info)


	@FXML public Pane panel;

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
	
	ContextMenu userNameElegido = new ContextMenu();
	
	ContextMenu userMailElegido = new ContextMenu();


	public static String[]Datos_usuario=new String[5];
	
	public static boolean MailExiste=false;

	public static boolean UsuarioExiste=false;
	
	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");


	@Override
	public void initialize(URL location, ResourceBundle resources) {


		//panel.getStylesheets().add("application/application.css");
		userNameValidator.getItems().add(
				new MenuItem("El nombre de usuario tiene que tener entre 8-16 caracteres"));

		userMailValidator.getItems().add(
				new MenuItem("El correo no es válido"));

		userPasswordValidator.getItems().add(
				new MenuItem("Contraseña corta"));

		userPasswordValidator2.getItems().add(
				new MenuItem("Las contraseñas no coinciden"));

		userDateValidator.getItems().add(
				new MenuItem("Tienes que tener al menos 13 años para poder jugar"));

		userNameElegido.getItems().add(
				new MenuItem("El nombre de usuario ya existe"));
		
		userMailElegido.getItems().add(
				new MenuItem("El correo ya existe"));
		
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
		chkTerminos.setDisable(true);

	}

	/**Método para comprobar que el usuario ha introducido un nombre que tiene entre 8 y 16 caracteres
	 * @param event el evento de ratón
	 */
	public void comprobarNombreUsuario(Event event){
		if(userNameElegido.isShowing()){
			userNameElegido.hide();
		}
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
		if(userMailElegido.isShowing()){
			userMailElegido.hide();
		}
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
			alert.initModality(Modality.APPLICATION_MODAL);


			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			alert.showAndWait();
			chkTerminos.setSelected(false);

		}
		else{
			try{
				Datos_usuario[0]=txtNombreUsuario.getText();
				
				Datos_usuario[1]=txtCorreoUsuario.getText();
				
				Datos_usuario[2]=pflContrasenya.getText();
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				
				alert.setTitle("Crear nuevo usuario");
				
				alert.setHeaderText("¿Está seguro?");
				
				alert.setContentText("¿Está seguro de que desea crear el anterior usuario?");

				
				alert.initModality(Modality.APPLICATION_MODAL);
				//Añade 'dueño'. (=La ventana sobre la cual se va a posicionar y la cual bloqueará)
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				
				Optional<ButtonType> result = alert.showAndWait();

				if (result.get() == ButtonType.OK){
					
					utilidades.Conexion_cliente.lanzaConexion(utilidades.Conexion_cliente.Ip_Local, utilidades.Acciones_servidor.Crear_Usuario.toString(), Datos_usuario);
					
					Alert alert2 = new Alert(AlertType.INFORMATION);
					
					alert2.setTitle("Usuario creado con éxito");
					
					alert2.setHeaderText("Éxito en la operación");
					
					alert2.setContentText("Se ha creado el usuario con éxito");

					
					alert2.initModality(Modality.APPLICATION_MODAL);
					//Añade 'dueño'. (=La ventana sobre la cual se va a posicionar y la cual bloqueará)
					alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					
					alert2.showAndWait();
					
					
					utilidades.deVentana.transicionVentana("LogIn", event);
				}
			}catch(Exception a){
				Alert alert2 = new Alert(AlertType.ERROR);
				
				alert2.setTitle("Error al tramitar la creación de usuario");
				
				alert2.setHeaderText("Error cuando se intentó crear el usuario");
				
				alert2.setContentText("Se ha produciod un error cuando intentaba crear su usario, por favor, revise la información y cambielá si es necesario");

				
				alert2.initModality(Modality.APPLICATION_MODAL);
				//Añade 'dueño'. (=La ventana sobre la cual se va a posicionar y la cual bloqueará)
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				
				alert2.showAndWait();
				if(MailExiste){
					userMailElegido.show(txtCorreoUsuario, Side.BOTTOM, 0, 0);
					chkTerminos.setSelected(false);
					btnCrear.setDisable(true);
				}
				else if(UsuarioExiste){
					userNameElegido.show(txtNombreUsuario, Side.BOTTOM, 0, 0);
					chkTerminos.setSelected(false);
					btnCrear.setDisable(true);
				}
				else{
					Alert alert3 = new Alert(AlertType.ERROR);
					
					alert3.setTitle("Error inesperado");
					
					alert3.setHeaderText("Un error inesperado ocurrió");
					
					alert3.setContentText("Parece que ha ocurrido un error inesperado, por favor, intenteló de nuevo más tarde");

					
					alert3.initModality(Modality.APPLICATION_MODAL);
					//Añade 'dueño'. (=La ventana sobre la cual se va a posicionar y la cual bloqueará)
					alert3.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
					
					alert3.showAndWait();	
				}
			}
			
		}

	}


	/**Método que cancela el usuario que se iba a crear, si así lo considera
	 * @param event
	 */
	public void cancelarUsuario(MouseEvent event){
		//Crea alerta de tipo confirmación
		Alert alert = new Alert(AlertType.CONFIRMATION);
		//Pone título
		alert.setTitle("Cancelar el nuevo usuario");
		//Pone cabecera
		alert.setHeaderText("¿Está seguro?");
		//Pone contenido
		alert.setContentText("¿Está seguro de que desea descartar el usuario que está creando?");

		//Añade modalidad
		alert.initModality(Modality.APPLICATION_MODAL);
		//Añade 'dueño'. (=La ventana sobre la cual se va a posicionar y la cual bloqueará)
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		//Mostrar y bloquear ventana padre hasta aceptar o rechazar.
		Optional<ButtonType> result = alert.showAndWait();

		//Ha pulsado ok?
		if (result.get() == ButtonType.OK){
			utilidades.deVentana.transicionVentana("LogIn", event);
		}
		
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
			if(file.length()>5242880){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Tamaño excesivo");
				alert.initModality(Modality.APPLICATION_MODAL);
				
				//Elijo el dueño de la alerta (o la base) de la misma.
				alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert.setContentText("Parece que la imagen es muy grande, por favor, introduzca una imagen más pequeña (tamaño máximo: 5Mb).");

				alert.showAndWait();

			}
			else{
				String path="file:///"+file.getAbsolutePath();
				Datos_usuario[4]=file.getAbsolutePath();
				ImgImagenUsuario.setImage(new Image(path));
			}
		}catch(Exception a){
			
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
	@SuppressWarnings("deprecation")
	public void comprobarDia(Event event){
		@SuppressWarnings("unused")
		String st = null;
		if(dpFechaNacimiento!=null){
			try {
				st = ft.format(ft.parse(this.dpFechaNacimiento.getValue().toString()));
			} catch (ParseException e) {
				
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//get current date time with Date()
		Date date = new Date();
		Date date2 = Date.from(dpFechaNacimiento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		dateFormat.format(date);
		
		if(date.getYear()-date2.getYear()>13){
			datosCorrectos=true; 
			userDateValidator.hide();
			chkTerminos.setSelected(false);
			
			Datos_usuario[3]=dateFormat.format(date2);
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
		//TODO: reparar esto
		URL res = getClass().getClassLoader().getResource("pdfEULA/EULA.pdf");

		File ficheroPDF=new File((res.getPath()));


		try{	 
			new Thread(new Runnable() {  
				@Override  
				public void run() {  
					try {  
						Desktop.getDesktop().open(ficheroPDF);  
					} catch (IOException e) {  
						

					}  
				}  
			}).start(); 
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			chkTerminos.setDisable(false);
		}catch(Exception a){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Parece que hubo un error");
			alert.setContentText("No se pudo abrir el archivo con un visor de PDF. Por favor, comprueba si tiene algún visor de pdf instalado e intenteló de nuevo.");
			alert.initModality(Modality.APPLICATION_MODAL);
			
			//Elijo el dueño de la alerta (o la base) de la misma.
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			

			alert.showAndWait();	

			//				Stage anotherStage = new Stage();
			//				Pane page ;
			//				try {
			//					page = FXMLLoader.load(Main.class.getResource("../windows/EULA.fxml"));
			//					 Scene anotherScene = new Scene(page);
			//			            anotherStage.setScene(anotherScene);
			//			            anotherStage.show();
			//				} catch (IOException e) {
			//					
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
			chkTerminos.setSelected(false);
		}

		if(chkTerminos.isSelected()&&datosCorrectos==true){
			btnCrear.setDisable(false);

		}
		else{
			btnCrear.setDisable(true);
		}


	}

}