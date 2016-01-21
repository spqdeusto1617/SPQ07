package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class EventosEstadisticas extends ClaseExtensora implements Initializable {

    @FXML
    private Text textoESPanel;

    @FXML
    private Text txtPartidasJugadas;

    @FXML
    private Text textoMiPerfil;

    @FXML
    private Rectangle rectanguloPanel;

    @FXML
    private Rectangle rectanguloAmigos;

    @FXML
    private Text txtDerrotas;

    @FXML
    private Text txtEmpates;

    @FXML
    private Rectangle rectanguloMiPerfil;

    @FXML
    private Rectangle rectanguloJugar;

    @FXML
    private Text textoAmigos;

    @FXML
    private ImageView imagenAvatar;

    @FXML
    private Pane panel;

    @FXML
    private Circle circuloPlus;

    @FXML
    private Text txtPosicion;

    @FXML
    private Text textoPlus;

    @FXML
    private ImageView logopsp;

    @FXML
    private Text textoLogeadoComo;

    @FXML
    private Text textoCerrarSesion;

    @FXML
    private Text txtRatio;

    @FXML
    private Rectangle rectanguloCerrarSesion;

    @FXML
    private Text textoJugar;

    @FXML
    private Circle circuloPanel;

    @FXML
    private Text textoNombreDeUsuario;

    @FXML
    private Text textoEstadisticas;

    @FXML
    private Rectangle rectanguloEstadisticas;

    @FXML
    private Text txtVictorias;
    
    public static ArrayList<String> Datos_Usuario_Estadisticas=new ArrayList<String>();;
    /*Posiciones:
     * 0: partidas ganadas
     * 1: partidas perdidas
     * 2:partidas empatadas
     * 3:partidas totales jugadas
     * 4: posición en el ranking
     */
    @FXML
    void btnJugar(MouseEvent event) {
    	utilidades.deVentana.transicionVentana("Juego", event);
    }

    @FXML
    void btnMiPerfil(MouseEvent event) {
    	utilidades.deVentana.transicionVentana("Perfil", event);
    }

	@FXML
	void entrado(MouseEvent event) {
		utilidades.deVentana.efectoTransparenciaOnHover(event, this);
	}

	//Añade nivel de transparencia
	@FXML
	void salido(MouseEvent event) {
		utilidades.deVentana.efectoTransparenciaOnHover(event, this);
	}

    @FXML
    void btnEstadisticas(MouseEvent event) {

    }


    @FXML
    void btnAmigos(MouseEvent event) {
    	utilidades.deVentana.transicionVentana("Amigos", event);
    }

    @FXML
    void btnCerrarSesion(MouseEvent event) {
    	utilidades.deVentana.cerrarSesion(event);
    }

    @FXML
    void esPanel(MouseEvent event) {

    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(EventosLogIn.iAvatar!=null){
			imagenAvatar.setImage(EventosLogIn.iAvatar);
		}else{
			String imagen = "fPerfil";
			Random rand = new Random();
			int randomNum = rand.nextInt((1000 - 1) + 1) + 1;
			if(randomNum == 666){
				imagen = "fPerfilPirata";
			}

			Image i = new Image("images/"+ imagen +".png",imagenAvatar.getBoundsInLocal().getWidth(),imagenAvatar.getBoundsInLocal().getHeight(),false,true);
			imagenAvatar.setImage(i);
		}
		Circle clip = new Circle((imagenAvatar.getX()+imagenAvatar.getBoundsInParent().getWidth())/2, (imagenAvatar.getY()+imagenAvatar.getBoundsInParent().getHeight())/2, imagenAvatar.getBoundsInLocal().getHeight()/2);
		imagenAvatar.setClip(clip);
		imagenAvatar.setSmooth(true); 
		imagenAvatar.setCache(true); 
		textoNombreDeUsuario.setText(utilidades.Conexion_cliente.Datos_Usuario.get(0));
		
		
		
		txtDerrotas.setText(Datos_Usuario_Estadisticas.get(1));
		txtEmpates.setText(Datos_Usuario_Estadisticas.get(2));
		txtVictorias.setText(Datos_Usuario_Estadisticas.get(0));
		double d = (Integer.parseInt(Datos_Usuario_Estadisticas.get(0))/Integer.parseInt(Datos_Usuario_Estadisticas.get(1)));
		txtRatio.setText(String.valueOf(d));
		txtPosicion.setText(Datos_Usuario_Estadisticas.get(4));
		txtPartidasJugadas.setText(Datos_Usuario_Estadisticas.get(3));
	}

}
