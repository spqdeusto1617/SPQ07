package com.pasapalabra.game.server;

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

import com.pasapalabra.game.utilities.AppLogger;

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

/**Controller class of the server´s GUI.
 * @author asierguti
 *
 */
@SuppressWarnings("deprecation")
public class CaptureWindow extends Control implements Initializable{

	public static Logger log = AppLogger.getWindowLogger(CaptureWindow.class.getName());

	//Internal array for this class
	public static ArrayList<Image> aLNews;

	//External array for another class
	public static ArrayList<File> aLNewsFile = new ArrayList<File>();

	/*
	This separation is because Image is not serializable, and thus, unable to send it
	through the Internet.
	 */

	/*
	This represent the image´s selection in the window
	This is for remind which image was selected before
	 */
	ImageView previousSelection;

	@FXML
	private Pane pNewsHandler;

	@FXML
	private Pane pServer;

	@FXML
	private SplitPane spPane;

	@FXML
	private Text txtSystemHour;

	@FXML
	private TextArea messagesList;

	@FXML
	private Pane pMain;

	@FXML
	private Text txtElapsedTime;

	@FXML
	private FlowPane fNews;

	@FXML
	private Text txtServerStatus;

	@FXML
	private Button exitBtn;

	@FXML
	private Button returnBtn;

	@FXML
	private Button removeBtn;

	@FXML
	private Text txtServerPort;

	@FXML
	private Button addBtn;

	@FXML
	private Text txtServerIp;
	
	@FXML
	private Text txtServerServiceName;

	/**
	 * @param event Event to load files to send them to the client
	 */
	@FXML
	void add(MouseEvent event) {
		//We chose the file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select new´s image");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
		File selectedFile = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

		//If the file is null, we can´t continue.
		if(selectedFile == null) return;

		//We add the image to the 
		addToWindow(selectedFile, true);
	}

	@FXML
	void remove(MouseEvent event) {
		//Solo se eliminará si la selección existe y el usuario lo confirma.
		if(previousSelection != null){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete new");
			alert.setHeaderText("Do you want to delete this new");
			alert.setContentText("After deleting it, no user can receive it back.");

			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
			Optional<ButtonType> result = alert.showAndWait();
		
			if (result.get() == ButtonType.OK){
				Alert alert2 = new Alert(AlertType.INFORMATION);
				alert2.setTitle("WARNING!");
				alert2.setHeaderText(null);
				alert2.setContentText("We cant find the image. Please select the image in your system.");
				alert2.setGraphic(previousSelection);
				alert2.initModality(Modality.APPLICATION_MODAL);
				alert2.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
				alert2.showAndWait();

				//Elige la imagen en el sistema de archivos porque no somos capaces de saber cual era.
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select image to delete");
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
							log.log(Level.WARNING, "We can´t delete the image with the route: " + selectedFile.getAbsolutePath(), e);
							e.printStackTrace();
						}
						//Se elimina el archivo de la ventana.
						fNews.getChildren().remove(previousSelection);
						//Se deselecciona el archivo.
						previousSelection = null;
					}
				}
			}

		}
	}



	@FXML
	void mReturn(MouseEvent event) {
		playAnimation(true);
	}

	@FXML
	void manageNews(MouseEvent event) {
		playAnimation(false);
	}

	void playAnimation(boolean server_notNews){
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				if(server_notNews){
					TranslateTransition translate = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pServer)
							.toX(0)
							.autoReverse(false)
							.build();

					TranslateTransition translate2 = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pNewsHandler)
							.toX(0)
							.autoReverse(false)
							.build();

					translate.play();
					translate2.play();

				}else{
					TranslateTransition translate = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pServer)
							.toX(-600)
							.autoReverse(false)
							.build();

					TranslateTransition translate2 = TranslateTransitionBuilder
							.create()
							.duration(new Duration(1000))
							.node(pNewsHandler)
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
	void exit(MouseEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit the application");
		alert.setHeaderText("Do you really want to exit the applicaction");
		alert.setContentText("If you exit htis application, all the data will be lost.");

		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			/*Servidor.cierre_servidor();
			BaseDeDatosPreguntas.cerrarConexion();
			BaseDeDatosUsuarios.cerrarConexion();*/
			Platform.exit();
			System.exit(0);
		} else {
			event.consume();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//We create the log
		AppLogger.crearLogHandler(log, CaptureWindow.class.getName());
		//The button´s images are loaded here
		try {
			Image img;
			img = new Image(this.getClass().getResource("/res/back.png").toURI().toURL().toString());
			ImageView ivV = new ImageView(img);
			ivV.setFitHeight(10); ivV.setFitWidth(10);
			ivV.setSmooth(true);
			returnBtn.setGraphic(ivV);
			

			img = new Image(this.getClass().getResource("/res/delete.png").toURI().toURL().toString());
			ivV = new ImageView(img);
			ivV.setFitHeight(10); ivV.setFitWidth(10);
			ivV.setSmooth(true);
			removeBtn.setGraphic(ivV);

			img = new Image(this.getClass().getResource("/res/add.png").toURI().toURL().toString());
			ivV = new ImageView(img);
			ivV.setFitHeight(10); ivV.setFitWidth(10);
			ivV.setSmooth(true);
			addBtn.setGraphic(ivV);

		} catch (MalformedURLException e2) {//graphic
			log.log(Level.WARNING, "Error at loading the button´s graphic part", e2);
		} catch (URISyntaxException e2) {
			log.log(Level.WARNING, "Error at loading the button´s graphic part", e2);
		}

		//We create the arraylist
		aLNews = new ArrayList<>();
		Image iSave;
		//All the graphic part are loaded into a folder
		if (fileLoad() != null) {
			for (File fImage : fileLoad()) {
				try {
					if(Files.isRegularFile(fImage.toPath())){

						System.out.println(fImage.toPath());
						iSave = new Image(fImage.toURI().toURL().toString());
						aLNewsFile.add(fImage);
						aLNews.add(iSave);
						addToWindow(fImage, false);
					}
				} catch (Exception e) {
					log.log(Level.WARNING, "Error while loading files to the window", e);
				}
			}
		}
		//We get the date when the server stared.
		Date startDate = new Date();
		//We get the app port
		//The external ip is loaded
		try {
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in;

			in = new BufferedReader(new InputStreamReader(
					whatismyip.openStream()));
			String ip = in.readLine();
			txtServerIp.setText(ip);
			txtServerPort.setText(Server.serverPort);
			txtServerServiceName.setText(Server.serviceName);
		} catch (IOException e1) {
			log.log(Level.WARNING, "Error while loading the appliction´s IP", e1);
		}

		//Set the server status
		txtServerStatus.setText("online");


		new Thread(new Runnable() {

			@Override
			public void run() {
				do{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.log(Level.WARNING, "Error while sleeping this thread", e);
					}
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							//The messages are updated
						//	listaMensajes.setText(Servidor.s2);

							//The elapsed time is loaded
							long diff = new Date().getTime() - startDate.getTime();

							long diffSeconds = diff / 1000 % 60;
							long diffMinutes = diff / (60 * 1000) % 60;
							long diffHours = diff / (60 * 60 * 1000) % 24;
							long diffDays = diff / (24 * 60 * 60 * 1000);
							txtElapsedTime.setText(diffDays + "days " + diffHours + "hours " + diffMinutes + "minutes " + diffSeconds + "seconds");

							DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
							Date today = Calendar.getInstance().getTime();
							String reportDate = df.format(today);
							txtSystemHour.setText(reportDate);

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

	/**Method to save an image to a folder
	 * @param f file to save.
	 */
	public static void saveToFile(File f){
		//		if(existeRaw(f)){
		//			//It already exits
		//
		//		}else{
		String extension = "";

		int i = f.getAbsolutePath().lastIndexOf('.');
		if (i > 0) {
			extension = f.getAbsolutePath().substring(i);
		}

		//Copy options
		CopyOption[] options = new CopyOption[]{
				StandardCopyOption.REPLACE_EXISTING,
				StandardCopyOption.COPY_ATTRIBUTES
		}; 
		Path p = null;
		try {
			p = getSystemDefaultPath(extension);
			//Copies the file
			Files.copy(f.toPath(),p,options);
			System.out.println(p);
		} catch (IOException e) {
			log.log(Level.WARNING, "Error at coping the file: " + f.getAbsolutePath() + " to " + p  , e);
		}
		try {
			File file = new File(p.toUri());
			aLNewsFile.add(file);
			aLNews.add(new Image(file.toURI().toURL().toString()));
		} catch (MalformedURLException e) {
			log.log(Level.WARNING, "Error at adding the image to the array. " + p.toUri().toString(), e);
		}
		//		}
	}
	
	/**Method to add and save to the windows (if a boolean is true) a graphic image
	 * @param selectedFile the file to add to the window
	 * @param save True: save - False: not save.
	 */
	public void addToWindow(File selectedFile, boolean save){
		//		if(existeRaw(selectedFile)){}
		//		else{
		try {
			ImageView iv = new ImageView(selectedFile.toURI().toURL().toString());
			iv.setFitWidth(100); iv.setFitHeight(100); iv.setSmooth(true);
			iv.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					if(previousSelection != null){
						previousSelection.setEffect(null);
					}
					previousSelection = (ImageView) event.getSource();
					//Se le añade un efecto glow
					DropShadow borderGlow = new DropShadow();
					borderGlow.setOffsetY(0f);
					borderGlow.setOffsetX(0f);
					borderGlow.setColor(Color.AQUA);
					borderGlow.setWidth(20);
					borderGlow.setHeight(20);

					previousSelection.setEffect(borderGlow);
				}
			});
			fNews.getChildren().add(iv);
			if(save) saveToFile(selectedFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		}
	}

	/** We get all the files, but instead of an array, in an arraylist
	 * @return All the graphics files from the server´s images folder
	 */
	public static ArrayList<File> fileLoad(){
		ArrayList<File> aLRetreive = new ArrayList<File>();
		for (File file : getAllFiles()) {
			aLRetreive.add(file);
		}
		return aLRetreive;
	}

	
	/**Method to get the route it will be used to save the images or to know where are saved 
	 * @param extension null if we want to get the path to the image
	 * a string with the extension we want to obtain to save a file
	 * @return we return the path to the file to save, or the folder where everything is saved
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
			log.log(Level.FINEST, "The selected file will be created in: " + f.getAbsolutePath());
			return f.toPath();

		}else if(System.getProperty("os.name").startsWith("Mac")){
			//MAC

			f = new File("/Users/"+System.getProperty("user.name")+"/Pasapalabra/ServerImgs/" + s);

			f.mkdirs();
			log.log(Level.FINEST, "The selected file will be created in: " + f.getAbsolutePath());
			return f.toPath();

		}else{
			//LINUX and if not an exception will be throw...
			f = new File("/home/"+System.getProperty("user.name")+"/Pasapalabra/ServerImgs/" + s);

			f.mkdirs();
			log.log(Level.FINEST, "The selected file will be created in: " + f.getAbsolutePath());
			return f.toPath();
		}
	}

	
	 /** Method to obtain all the files from a folder
	 * @return An array of Files with all the files from the folder
	 */
	public static File[] getAllFiles(){
		return getSystemDefaultPath(null).toFile().listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}
		});
	}

	/**Calculates the number of files in a folder.
	 * @return Number of files in a folder.
	 */
	public static int getNumberOfFiles(){
		if(fileLoad() != null) return fileLoad().size();
		else return 0;
	}
}