package com.pasapalabra.game.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
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

public class ClientConnection {

	public static ClientService cService;

	//public static char currentLetter;

	public static Token sessionAuth;

	public static UserDTO userInfo;
	
	public static UserScoreDTO userScore;

	public static boolean serverReady;

	public static boolean player1 = false;

	public static boolean turn = false;

	public static boolean  playing = false;

	private static boolean reachZ = false;

	public static QuestionType qType;

	@FXML public static Image userIMG;

	//TODO: handle the thrown Exception(remember to differenced)  
	public static void login(String userName, String pass) throws Exception{
		try{
			sessionAuth = ServiceLocator.service.login(userName, pass);
			if(sessionAuth == null)throw new RemoteException("Error");
		}catch(Exception a){
			throw a;
		}

	}

	public static void retreiveUserData() throws Exception{
		try{
			userInfo = ServiceLocator.service.getData(sessionAuth);
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
			return ServiceLocator.service.registry(userData, pass);
		}catch(Exception a){
			throw a;
		}

	}

	public static boolean exitGame() throws Exception{
		try{
			ServiceLocator.service.deLogin(sessionAuth);
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}


	public static UserDTO play(QuestionType type) throws Exception{
		try{
			UserDTO user = ServiceLocator.service.play(sessionAuth, type.toString(), cService);
			if(user == null)return null;
				if(user.getUserName().equals("Wait")){
					qType = type;
					player1 = true;
					turn = false;

				}
				else{
					player1 = false;
					turn = true;
					playing = true;
				}
			return user;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static void exitMatchMaking() throws Exception{
		try{
			ServiceLocator.service.exitMatchMaking(sessionAuth, qType.toString());
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static Boolean changeUserData(String userPass, String userMail, String userIMG, String newUserPass) throws RemoteException, NullPointerException{
		if(userPass == null)return false;
		try{
			Boolean result = false;
			if(newUserPass != null) result = ServiceLocator.service.changeUserPass(sessionAuth, userPass, newUserPass);
			else if(userMail != null) result = ServiceLocator.service.changeUserMail(sessionAuth, userPass, userMail);
			else if(userIMG != null) result = ServiceLocator.service.changeUserIMG(sessionAuth, userPass, userIMG);
			return result;
		}catch(Exception a){
			throw a;
		}

	}

	public static Boolean removeUser(String userPass) throws RemoteException, NullPointerException{
		if(userPass == null)return false;
		try{
			Boolean result = false;
			result = ServiceLocator.service.removeUser(sessionAuth, userPass);
			return result;
		}catch(Exception a){
			throw a;
		}

	}

	public static QuestionDTO getQuestion() throws Exception{
		try{
			QuestionDTO question = ServiceLocator.service.getQuestion(sessionAuth);
			//currentLetter = question.getLeter();
			if(question.getLeter() == 'z')reachZ = true;
			return question;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static boolean delogging() throws Exception{
		try{
			ServiceLocator.service.deLogin(sessionAuth);
			return true;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}


	public static boolean answerQuestion(String answer) throws Exception{
		try{
			return ServiceLocator.service.answerQuestion(sessionAuth, answer);
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	public static boolean endGame() throws Exception{
		if(reachZ){
			try{
				boolean result = ServiceLocator.service.allQuestionAnswered(sessionAuth);
				return result;
			}catch (Exception e) {
				// TODO: handle exception
				throw e;
			}
		}
		else return false;
	}
	
	public static void getResults() throws Exception{
		try{
			userScore = ServiceLocator.service.getResults(sessionAuth);
			if(player1){
				playing = false;
				reachZ = false;
				turn = false;
			}
			else{
				turn = false;
				reachZ = false;
			}
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}


}
