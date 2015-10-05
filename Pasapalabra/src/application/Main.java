package application;
	
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


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
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override public void handle(WindowEvent t) {
			        System.out.println("...CLOSED");
			    }
			});
			
			//Icono
			primaryStage.getIcons().add(new Image("images/iconopsp.png"));
			//Título de la ventana
			primaryStage.setTitle("Pasapalabra");
			
			//Poner escena
			primaryStage.setScene(scene);
			
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
		launch(args);
	}
}
