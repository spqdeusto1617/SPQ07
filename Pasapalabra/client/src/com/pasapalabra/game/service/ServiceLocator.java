package com.pasapalabra.game.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.QuestionType;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.service.auth.Token;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;


public class ServiceLocator{

	private static IPasapalabraService service;

	public static ClientService cService;

	public static char currentLetter;

	public static Token sessionAuth;

	public static UserDTO userInfo;

	public static boolean serverReady;

	public static boolean player1 = false;

	public static boolean turn = false;

	public static boolean playing = false;

	private static boolean reachZ = false;

	public static QuestionType type;

	@FXML public static Image userIMG;

	//public static boolean gameEnd = false;



	public static void startConnection(String[] args)
			throws MalformedURLException, RemoteException, NotBoundException {

		if(args.length == 0){
			System.err.println("No arguments passed");
			//System.exit(0);
		}

		try {
			String URL = "//" + args[0] + ":" + args[1] + "/" + args[2];
			cService = new ClientService(args[2]);
			service = (IPasapalabraService) Naming.lookup(URL);
			serverReady = true;
		} catch (Exception e) {
			System.err.println(" *# Error connecting to the server : " + e.getMessage());
			serverReady = false;
			throw e;
		}

	}
	//TODO: handle the thrown Exception(remember to differenced)  
	public static void login(String userName, String pass) throws Exception{
		try{
			sessionAuth = service.login(userName, pass);
			if(sessionAuth == null)throw new RemoteException("Error");
		}catch(Exception a){
			throw a;
		}

	}

	public static void retreiveUserData() throws Exception{
		try{
			userInfo = service.getData(sessionAuth);
			if(userInfo.getProfileImage() != null){
				byte[] imageByteArray = Base64.decodeBase64(userInfo.getProfileImage());
				try {
					BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imageByteArray));

					if (imag != null) {
						userIMG = SwingFXUtils.toFXImage(imag, null);

					}else{
						userIMG = null;
					}
				}catch (Exception e) {
					userIMG = null;
				}
			}
		}catch(Exception e){
			delogging();
			throw e;
		}
	}

	public static boolean createUser(UserDTO userData, String pass) throws Exception{
		try{
			return service.registry(userData, pass);
		}catch(Exception a){
			throw a;
		}

	}

	public static boolean exitGame() throws Exception{
		try{
			return service.deLogin(sessionAuth);
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}


	public static UserDTO play(QuestionType type) throws Exception{
		try{
			UserDTO user = service.play(sessionAuth, type.toString(), cService);
			if(user == null)//TODO: delog this user
				if(user.getUserName().equals("Wait")){
					ServiceLocator.type = type;
					player1 = true;
					turn = false;//TODO: keep the game waiting

				}
			player1 = false;
			turn = true;
			playing = true;
			return user;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static void exitMatchMaking() throws Exception{
		try{
			service.exitMatchMaking(sessionAuth, ServiceLocator.type.toString());
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static QuestionDTO getQuestion() throws Exception{
		try{
			QuestionDTO question = service.getQuestion(sessionAuth);
			if(question == null)//TODO: delog game
				currentLetter = question.getLeter();
			if(question.getLeter() == 'z')reachZ = true;
			return question;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static boolean delogging() throws Exception{
		try{
			return service.deLogin(sessionAuth);

		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}


	public static boolean answerQuestion(String answer) throws Exception{
		try{
			return service.answerQuestion(sessionAuth, answer);
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static boolean endGame() throws Exception{
		if(reachZ){
			try{
				boolean result = service.allQuestionAnswered(sessionAuth);
				return result;
			}catch (Exception e) {
				// TODO: handle exception
				throw e;
			}
		}
		else return false;
	}
	//TODO: if player1 == false, end game, else, pass to observer mode
	public static UserScoreDTO getResults() throws Exception{
		try{
			UserScoreDTO score = service.getResults(sessionAuth);
			if(player1){//TODO: acabar
				playing = false;
				player1 = false;
				reachZ = false;
				turn = false;
			}
			else{//TODO: Te toca espectar
				turn = false;
				reachZ = false;
			}
			return score;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

}