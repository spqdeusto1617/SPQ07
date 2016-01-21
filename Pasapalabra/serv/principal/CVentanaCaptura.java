package principal;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
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
import javax.imageio.ImageIO;

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
import javafx.scene.image.PixelReader;
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

@SuppressWarnings("deprecation")
public class CVentanaCaptura extends Control implements Initializable{

	public static ArrayList<Image> aLNoticias;

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

	@FXML
	void anyadir(MouseEvent event) {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Seleccionar imagen para noticia");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
		File selectedFile = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());
		if(selectedFile == null) return;

		BufferedImage img = null;

		try 
		{
			img = ImageIO.read(selectedFile); // eventually C:\\ImageTest\\pic2.jpg
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		if(img.getHeight()>300 || img.getWidth()>300){
			//Alerta mal

			return;
		}

		anyadirAVentana(selectedFile, true);

	}

	@FXML
	void eliminar(MouseEvent event) {
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
							Files.delete(selectedFile.toPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						fNoticias.getChildren().remove(seleccionAnterior);
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
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		aLNoticias = new ArrayList<>();
		Image iGuardar;
		if (cargarFicheros() != null) {
			for (File fImage : cargarFicheros()) {
				try {
					if(Files.isRegularFile(fImage.toPath())){
					
					System.out.println(fImage.toPath());
					iGuardar = new Image(fImage.toURI().toURL().toString());
					aLNoticias.add(iGuardar);
					anyadirAVentana(fImage, false);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Date fComienzo = new Date();
		txtPuertoServidor.setText(String.valueOf(Servidor.PUERTO_DEL_SERVIDOR));
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in;

			in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine();
			txtIpServidor.setText(ip);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		txtEstadoServidor.setText("en línea");


		new Thread(new Runnable() {

			@Override
			public void run() {
				do{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							listaMensajes.setText(Servidor.s2);

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
			
			CopyOption[] options = new CopyOption[]{
				      StandardCopyOption.REPLACE_EXISTING,
				      StandardCopyOption.COPY_ATTRIBUTES
				    }; 
			Path p = null;
			try {
				p = getSystemDefaultPath(extension);
				Files.copy(f.toPath(),p,options);
				System.out.println(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				File file = new File(p.toUri());
				aLNoticias.add(new Image(file.toURI().toURL().toString()));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//		}
	}

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
	}

	public static ArrayList<File> cargarFicheros(){
		ArrayList<File> aLDevolucion = new ArrayList<File>();
		for (File file : getAllFiles()) {
			aLDevolucion.add(file);
		}
		return aLDevolucion;
	}


	public static Path getSystemDefaultPath(String extension){
		String s = "";
		int i = 0;
		if(extension != null){ s = extension; i = getNumberOfFiles(); s = i+s;}
		File f;
		if(System.getProperty("os.name").startsWith("Windows")){
			//WINDOWS
			
				f = new File("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\ServerImgs\\" + s);
			
			f.mkdirs();
			return f.toPath();

		}else if(System.getProperty("os.name").startsWith("Mac")){
			//MAC
			
				f = new File("/Users/"+System.getProperty("user.name")+"/Pasapalabra/ServerImgs/" + s);
	
			f.mkdirs();
			return f.toPath();

		}else{
			//LINUX y si no pues se hará mal y saltará una excepción...
				f = new File("/home/"+System.getProperty("user.name")+"/Pasapalabra/ServerImgs/" + s);

			f.mkdirs();
			return f.toPath();

		}
	}

	public static File[] getAllFiles(){
		return getSystemDefaultPath(null).toFile().listFiles(new FilenameFilter() {
	        @Override
	        public boolean accept(File dir, String name) {
	            return !name.equals(".DS_Store");
	        }
	    });
	}

	public static int getNumberOfFiles(){
		if(cargarFicheros() != null) return cargarFicheros().size();
		else return 0;
	}
}
