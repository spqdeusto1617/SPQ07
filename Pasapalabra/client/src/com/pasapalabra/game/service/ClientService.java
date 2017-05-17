package com.pasapalabra.game.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.pasapalabra.game.controllers.GameController;
import com.pasapalabra.game.controllers.ThemeController;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class ClientService extends UnicastRemoteObject implements IClientService, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;

	@FXML public static Image rivalIMG;

	public static UserDTO rivalData;

	public static UserScoreDTO rivalScore;

	public static volatile boolean found = false;

	public static volatile boolean rivalDisconnected = false;

	public static volatile boolean rivalAnswered = false;

	public static volatile char leterAnswered = 'a';

	public static volatile boolean rivalAnswer = false;

	public ClientService(String name) throws RemoteException {
		super();
		this.name = name;
	}
	//TODO: implement this
	@Override
	public void getUser(UserDTO user) throws RemoteException {
		rivalData = user;
		ClientConnection.playing = true;
		if(rivalData.getProfileImage() != null){
			byte[] imageByteArray = Base64.decodeBase64(rivalData.getProfileImage());
			try {
				BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imageByteArray));

				if (imag != null) {
					rivalIMG = SwingFXUtils.toFXImage(imag, null);

				}else{
					rivalIMG = null;
				}
			}catch (Exception e) {
				rivalIMG = null;
			}
			
		}
		found = true;
	}
	//TODO: implement this
	@Override
	public void answered(char letter, boolean result) throws RemoteException {
		leterAnswered = letter;
		rivalAnswer = result;
		rivalAnswered = true;
	}
	//TODO: implement this
	@Override
	public void finalResult(UserScoreDTO score) throws RemoteException {
		ClientConnection.playing = false;//TODO: transicion de vuelta
		ClientConnection.turn = false;
		ClientConnection.player1 = false;
		rivalData = null;
		rivalScore = score;
	}
	//TODO: implement this (change to observer mode)
	@Override
	public void changeTurn(UserScoreDTO score) throws RemoteException {
		ClientConnection.turn = true;
		rivalScore = score;
		//TODO: play now

	}
	@Override
	public void otherDisconnected() throws RemoteException {	
		ClientConnection.turn = false;
		ClientConnection.playing = false;
		ClientConnection.player1 = false;
		rivalData = null;
		rivalDisconnected = true;
		//TODO: exit the game(Use windowUtilities.forcedCloseSession
	}

}
