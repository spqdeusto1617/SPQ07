package application;
	
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**Clase principal que crea la aplicación y la muestra en pantalla.
 * @author asier.gutierrez
 *
 */
public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Cargar página con el FXML elegido
			Pane page =  FXMLLoader.load(Main.class.getResource("../windows/LogIn.fxml"));
			
			//Añadir la página a la escena
			Scene scene = new Scene(page);
			
			//Añadir a la escena el CSS
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Añadir un escuchador para cuando se cierre la ventana (Evita errores)
//			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//			    @Override public void handle(WindowEvent t) {
//			        System.out.println("...CLOSED");
//			    }
//			});
			
			//Icono
			primaryStage.getIcons().add(new Image("images/iconopsp.png"));
			//Título de la ventana
			primaryStage.setTitle("Pasapalabra");
			
			//Poner escena
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();
			//Mostrar ventana
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**Método main de la clase.
	 * @param args Argumentos de consola (no se utilizan)
	 */
	public static void main(String[] args) {
		if (lockInstance("block.dat"))
			launch(args);
		else{
			//TODO Tiene que funcionar esto
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setTitle("Error Dialog");
//			alert.setHeaderText("Look, an Error Dialog");
//			alert.showAndWait();
			Platform.exit();
		}
	}
	
	private static boolean lockInstance(final String lockFile) {
	    try {
	        final File file = new File(lockFile);
	        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
	        final FileLock fileLock = randomAccessFile.getChannel().tryLock();
	        if (fileLock != null) {
	            Runtime.getRuntime().addShutdownHook(new Thread() {
	                public void run() {
	                    try {
	                        fileLock.release();
	                        randomAccessFile.close();
	                        file.delete();
	                    } catch (Exception e) {
	                       System.out.println("Unable to remove lock file: " + lockFile +  e);
	                    }
	                }
	            });
	            return true;
	        }
	    } catch (Exception e) {
	        System.out.println("Unable to create and/or lock file: " + lockFile + e);
	    }
	    return false;
	}
}
