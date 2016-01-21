package principal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**Clase controladora de la ventana del servidor.
 * @author asierguti
 *
 */
@SuppressWarnings("deprecation")
public class CVentanaCaptura extends Control implements Initializable{

	public static Logger log = utilidades.AppLogger.getWindowLogger(CVentanaCaptura.class.getName());

	//ArrayList interno del programa.
	public static ArrayList<Image> aLNoticias;

	//ArrayList externo del programa.
	public static ArrayList<File> aLNoticiasFicheros;

	/*
	Esta separación es debida a que Image no es serializable y, por tanto,
	no se puede enviar por ningún tipo de conexión.
	 */

	/*
	Representa la selección de la imagen que se ha hecho en la ventana.
	Sirve para recordar lo pulsado la pulsación antes
	 */
	ImageView seleccionAnterior;

	@FXML
	private Pane pGestionarNoticias;

	@FXML
	private Pane pServidor;

	@FXML
	private SplitPane spPane;

	@FXML
	private Text txtHoraDelSistema;

	@FXML
	private TextArea listaMensajes;

	@FXML
	private Pane pPrincipal;

	@FXML
	private Text txtTiempoTranscurrido;

	@FXML
	private FlowPane fNoticias;

	@FXML
	private Text txtEstadoServidor;

	@FXML
	private Button btnSalir;

	@FXML
	private Button btnVolver;

	@FXML
	private Button btnEliminar;

	@FXML
	private Text txtPuertoServidor;

	@FXML
	private Button btnAnyadir;

	@FXML
	private Text txtIpServidor;

	/**
	 * @param event Evento que hace 
	 */
	@FXML
	void anyadir(MouseEvent event) {
		//El usuario elige el fichero
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Seleccionar imagen para noticia");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
		File selectedFile = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

		//Si el fichero es nulo, entonces, no se continua.
		if(selectedFile == null) return;

		//Se añade a la ventana y se guarda (true)
		anyadirAVentana(selectedFile, true);
	}

	@FXML
	void eliminar(MouseEvent event) {
		//Solo se eliminará si la selección existe y el usuario lo confirma.
		if(seleccionAnterior != null){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Borrar noticia");
			alert.setHeaderText("¿Está segur@ de que desea borrar la noticia seleccionada?");
			alert.setContentText("Al borrar esta noticia no volverá a aparecerle a ningún usuario que se conecte.");

			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("AVISO!");
				alert2.setHeaderText(null);
				alert2.setContentText("No somos capaces de encontrar tu imagen en el sistema de archivos. Por favor, elija cual era el archivo que intentó eliminar.");
				alert2.setGraphic(seleccionAnterior);
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();

				//Elige la imagen en el sistema de archivos porque no somos capaces de saber cual era.
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Seleccionar imagen para eliminar");
				fileChooser.setInitialDirectory(getSystemDefaultPath(null).toFile());
				fileChooser.getExtensionFilters().addAll(
						new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
				File selectedFile = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());
				if(selectedFile != null){
					String s = selectedFile.toPath() + "";
					if(System.getProperty("os.name").startsWith("Windows")){
						s = s.substring(0,s.lastIndexOf("\\"));
					}else{
						s = s.substring(0,s.lastIndexOf('/'));
					}
					if(s.equals(getSystemDefaultPath(null).toString())){
						try {
							//Se elimina el archivo 'físico'.
							Files.delete(selectedFile.toPath());
						} catch (IOException e) {
							log.log(Level.WARNING, "No se ha eliminado el archivo con ruta: " + selectedFile.getAbsolutePath(), e);
							e.printStackTrace();
						}
						//Se elimina el archivo de la ventana.
						fNoticias.getChildren().remove(seleccionAnterior);
						//Se deselecciona el archivo.
						seleccionAnterior = null;
					}
				}
			}

		}
	}



	@FXML
	void volver(MouseEvent event) {
		playAnimation(true);
	}

	@FXML
	void gestionarNoticias(MouseEvent event) {
		playAnimation(false);
	}

	void playAnimation(boolean servidor_notNoticias){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if(servidor_notNoticias){
					TranslateTransition translate = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pServidor)
							.toX(0)
							.autoReverse(false)
							.build();

					TranslateTransition translate2 = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pGestionarNoticias)
							.toX(0)
							.autoReverse(false)
							.build();

					translate.play();
					translate2.play();

				}else{
					TranslateTransition translate = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pServidor)
							.toX(-600)
							.autoReverse(false)
							.build();

					TranslateTransition translate2 = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pGestionarNoticias)
							.toX(-600)
							.autoReverse(false)
							.build();

					translate.play();
					translate2.play();
				}


			}
		});


	}

	@FXML
	void salir(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Salir de la aplicación");
		alert.setHeaderText("¿Está segur@ de que desea salir de la aplicación?");
		alert.setContentText("Si sale de la aplicación, todos los usuarios conectados al servidor perderán el progreso que estén haciendo en este momento.");

		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			Servidor.cierre_servidor();
			BaseDeDatosPreguntas.cerrarConexion();
			BaseDeDatosUsuarios.cerrarConexion();
			Platform.exit();
			System.exit(0);
		} else {
			event.consume();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Se crea el log handler
		utilidades.AppLogger.crearLogHandler(log, CVentanaCaptura.class.getName());
		//Se cargan las imágenes de los botones.
		try {
			Image img;
			img = new Image(this.getClass().getResource("res/back.png").toURI().toURL().toString());
			ImageView ivV = new ImageView(img);
			ivV.setFitHeight(10); ivV.setFitWidth(10);
			ivV.setSmooth(true);
			btnVolver.setGraphic(ivV);


			img = new Image(this.getClass().getResource("res/delete.png").toURI().toURL().toString());
			ivV = new ImageView(img);
			ivV.setFitHeight(10); ivV.setFitWidth(10);
			ivV.setSmooth(true);
			btnEliminar.setGraphic(ivV);

			img = new Image(this.getClass().getResource("res/add.png").toURI().toURL().toString());
			ivV = new ImageView(img);
			ivV.setFitHeight(10); ivV.setFitWidth(10);
			ivV.setSmooth(true);
			btnAnyadir.setGraphic(ivV);

		} catch (MalformedURLException e2) {
			log.log(Level.WARNING, "Error al cargar la parte gráfica de los botones de la ventana", e2);
		} catch (URISyntaxException e2) {
			log.log(Level.WARNING, "Error al cargar la parte gráfica de los botones de la ventana", e2);
		}

		//Se crea el arraylist
		aLNoticias = new ArrayList<>();
		Image iGuardar;
		//Se cargan todos los ficheros gráficos que hayan en esa carpeta.
		if (cargarFicheros() != null) {
			for (File fImage : cargarFicheros()) {
				try {
					if(Files.isRegularFile(fImage.toPath())){

						System.out.println(fImage.toPath());
						iGuardar = new Image(fImage.toURI().toURL().toString());
						aLNoticiasFicheros.add(fImage);
						aLNoticias.add(iGuardar);
						anyadirAVentana(fImage, false);
					}
				} catch (Exception e) {
					log.log(Level.WARNING, "Error al cargar los ficheros a la ventana", e);
				}
			}
		}
		//Saca la fecha en la que el servidor se encendió.
		Date fComienzo = new Date();
		//Coge el puerto del servidor
		txtPuertoServidor.setText(String.valueOf(Servidor.PUERTO_DEL_SERVIDOR));
		//Se carga la ip externa del servidor
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in;

			in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine();
			txtIpServidor.setText(ip);
		} catch (IOException e1) {
			log.log(Level.WARNING, "Error al cargar la IP del servidor", e1);
		}

		//Setea el texto
		txtEstadoServidor.setText("en línea");


		new Thread(new Runnable() {

			@Override
			public void run() {
				do{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.log(Level.WARNING, "Error al dormir el hilo", e);
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							//Actualiza los mensajes
							listaMensajes.setText(Servidor.s2);

							//Carga el tiempo transcurrido.
							long diff = new Date().getTime() - fComienzo.getTime();

							long diffSeconds = diff / 1000 % 60;
							long diffMinutes = diff / (60 * 1000) % 60;
							long diffHours = diff / (60 * 60 * 1000) % 24;
							long diffDays = diff / (24 * 60 * 60 * 1000);
							txtTiempoTranscurrido.setText(diffDays + "días " + diffHours + "horas " + diffMinutes + "minutos " + diffSeconds + "segundos");

							DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
							Date today = Calendar.getInstance().getTime();
							String reportDate = df.format(today);
							txtHoraDelSistema.setText(reportDate);

							double [] a = spPane.getDividerPositions();
							if(a[0]<0.5){
								spPane.setDividerPosition(0, 0.5);
							}
						}
					});
				}while(true);

			}
		}).start();

	}

	/**Método para guardar fichero en la carpeta del servidor
	 * @param f Fichero a guardar.
	 */
	public static void guardarFichero(File f){
		//		if(existeRaw(f)){
		//			//Ya existe
		//
		//		}else{
		String extension = "";

		int i = f.getAbsolutePath().lastIndexOf('.');
		if (i > 0) {
			extension = f.getAbsolutePath().substring(i);
		}

		//Opciones de copia
		CopyOption[] options = new CopyOption[]{
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES
		}; 
		Path p = null;
		try {
			p = getSystemDefaultPath(extension);
			//Copia el fichero
			Files.copy(f.toPath(),p,options);
			System.out.println(p);
		} catch (IOException e) {
			log.log(Level.WARNING, "Error al copiar el archivo " + f.getAbsolutePath() + " a " + p  , e);
		}
		try {
			File file = new File(p.toUri());
			aLNoticiasFicheros.add(file);
			aLNoticias.add(new Image(file.toURI().toURL().toString()));
		} catch (MalformedURLException e) {
			log.log(Level.WARNING, "Error al añadir la imagen al array. " + p.toUri().toString(), e);
		}
		//		}
	}

	/**Método para añadir a ventana y guardar (depende del parámetro booleano) un fichero gráfico
	 * @param selectedFile Fichero para añadir a la ventana
	 * @param guardar True: guardar - False: no guardar.
	 */
	public void anyadirAVentana(File selectedFile, boolean guardar){
		//		if(existeRaw(selectedFile)){}
		//		else{
		try {
			ImageView iv = new ImageView(selectedFile.toURI().toURL().toString());
			iv.setFitWidth(100); iv.setFitHeight(100); iv.setSmooth(true);
			iv.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					if(seleccionAnterior != null){
						seleccionAnterior.setEffect(null);
					}
					seleccionAnterior = (ImageView) event.getSource();
					//Se le añade un efecto glow
					DropShadow borderGlow = new DropShadow();
					borderGlow.setOffsetY(0f);
					borderGlow.setOffsetX(0f);
					borderGlow.setColor(Color.AQUA);
					borderGlow.setWidth(20);
					borderGlow.setHeight(20);

					seleccionAnterior.setEffect(borderGlow);
				}
			});
			fNoticias.getChildren().add(iv);
			if(guardar) guardarFichero(selectedFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		}
	}

	/**Consigue todos los ficheros pero en vez de en un array, lo pasa a ArrayList.
	 * @return Todos los ficheros gráficos de la carpeta de imágenes del servidor.
	 */
	public static ArrayList<File> cargarFicheros(){
		ArrayList<File> aLDevolucion = new ArrayList<File>();
		for (File file : getAllFiles()) {
			aLDevolucion.add(file);
		}
		return aLDevolucion;
	}


	/**Método para conseguir la Ruta que se utilizará para guardar las imágenes o
	 * para saber donde están guardadas.
	 * @param extension null si queremos obtener la ruta donde estan guardados los archivos
	 * un string con la extensión si queremos obtener una ruta en la cual guardar un archivo.
	 * @return devuelve la Ruta del archivo a guardar o de la carpeta en la que se encuentra todo.
	 */
	public static Path getSystemDefaultPath(String extension){
		String s = "";
		int i = 0;
		if(extension != null){ s = extension; i = getNumberOfFiles(); s = i+s;}
		File f;
		if(System.getProperty("os.name").startsWith("Windows")){
			//WINDOWS

			f = new File("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\ServerImgs\\" + s);

			f.mkdirs();
			log.log(Level.FINEST, "El archivo creado estará en: " + f.getAbsolutePath());
			return f.toPath();

		}else if(System.getProperty("os.name").startsWith("Mac")){
			//MAC

			f = new File("/Users/"+System.getProperty("user.name")+"/Pasapalabra/ServerImgs/" + s);

			f.mkdirs();
			log.log(Level.FINEST, "El archivo creado estará en: " + f.getAbsolutePath());
			return f.toPath();

		}else{
			//LINUX y si no pues se hará mal y saltará una excepción...
			f = new File("/home/"+System.getProperty("user.name")+"/Pasapalabra/ServerImgs/" + s);

			f.mkdirs();
			log.log(Level.FINEST, "El archivo creado estará en: " + f.getAbsolutePath());
			return f.toPath();
		}
	}

	/**Método para conseguir todos los ficheros de una carpeta.
	 * @return Array de File que contiene todos los ficheros de la carpeta.
	 */
	public static File[] getAllFiles(){
		return getSystemDefaultPath(null).toFile().listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}
		});
	}

	/**Calcula el número de ficheros que existen en la carpeta.
	 * @return Numero de archivos en la carpeta.
	 */
	public static int getNumberOfFiles(){
		if(cargarFicheros() != null) return cargarFicheros().size();
		else return 0;
	}
}