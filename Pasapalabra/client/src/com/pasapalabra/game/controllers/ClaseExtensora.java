package com.pasapalabra.game.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**Clase para generalizar el menú
 * @author asier.gutierrez
 *
 */
public abstract class ClaseExtensora extends Control{
	//Declaración de todos los elementos del menú
	@FXML public Text textoESPanel;

	@FXML public Button btnPerfil;

	@FXML public ImageView logopsp;

	@FXML public Button btnCerrarSesion;

	@FXML public Text textoPlus;

	@FXML public Rectangle rectanguloPanel;

	@FXML public Circle circuloPlus;

	@FXML public Button btnAmigos;

	@FXML public Button btnJuego;

	@FXML public Text textoLogeadoComo;

	@FXML public Circle circuloPanel;

	@FXML public Text textoNombreDeUsuario;

	@FXML public Button btnEstadisticas;

	@FXML public ImageView imagenAvatar;

	@FXML public Pane panel;
}
