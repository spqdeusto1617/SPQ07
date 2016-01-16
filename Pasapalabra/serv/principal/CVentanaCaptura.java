package principal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CVentanaCaptura extends Control implements Initializable{
		
	
		@FXML
		private SplitPane spPane;
		
	    @FXML
	    private Text txtEstadoServidor;

	    @FXML
	    private Button btnSalir;

	    @FXML
	    private Text txtHoraDelSistema;

	    @FXML
	    public TextArea listaMensajes;

	    @FXML
	    private Text txtIpServidor;
	    
	    @FXML
	    private Text txtPuertoServidor;
	    
	    @FXML
	    private Text txtTiempoTranscurrido;

	    @FXML
	    void salir(MouseEvent event) {
	    	Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setTitle("Salir de la aplicación");
	    	alert.setHeaderText("¿Está segur@ de que desea salir de la aplicación?");
	    	alert.setContentText("Si sale de la aplicación, todos los usuarios conectados al servidor perderán el progreso que estén haciendo en este momento.\n\n¿Está segur@?");

	    	alert.initModality(Modality.APPLICATION_MODAL);
    		alert.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
    		alert.showAndWait();
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
		

	}