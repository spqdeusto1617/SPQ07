package com.pasapalabra.game.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.service.auth.Token;

public interface IPasapalabraService extends Remote {

	/**
	 * Method to registry an user.
	 * @return: true or false if the user is introduced to the DB. 
	 * @param userData: the general info of the new user
	 * @param pass: the password of the user
	 * @throws RemoteException: if some error occurred during the process
	 */
	public boolean registry(UserDTO userData, String pass) throws RemoteException;

	/**
	 * Method for login an user
	 * @param userName: the username of the user
	 * @param pass: the password of the user
	 * @return a session token or null if the user exists
	 * @throws RemoteException: if some error occurred during the process
	 * @throws SecurityException: if the user tries to login more than once
	 */
	public Token login(String userName, String pass) throws RemoteException;
	
	/**
	 * Method for deloging from the applicaciton 
	 * @param session: the session token given after login
	 * @throws RemoteException: if some error occurred during the process
	 */
	public void deLogin(Token session) throws RemoteException;

	/**
	 * Method for retrieving the userdata from a user after performing a correct login.
	 * @param token: the session token given before
	 * @return: the userdata (check UserDTO.java) or null if the session is incorrect
	 * @throws RemoteException: if some error occurred during the process 
	 */
	public UserDTO getData(Token token) throws RemoteException;
	
	/**
	 * Method for updating the user image
	 * @param token: the user session token
	 * @param userPass: his pass (to validate if that is the user
	 * @param userIMG: his new image
	 * @return: true if the changes are a success, false if the user data is incorrect, null if the session is incorrect
	 * @throws RemoteException: if some error occurred during the process , NullPointerException, if there is a problem inserting the userData
	 */
	public Boolean changeUserIMG(Token token, String userPass, String userIMG) throws RemoteException, NullPointerException;
	
	/**
	 * Method for updating the user Mail
	 * @param token: the user session token
	 * @param userPass: his pass (to validate if that is the user
	 * @param userMail: his new mail
	 * @return: true if the changes are a success, false if the user data is incorrect, null if the session is incorrect
	 * @throws RemoteException: if some error occurred during the process , NullPointerException, if there is a problem inserting the userData
	 */
	public Boolean changeUserMail(Token token, String userPass, String userMail) throws RemoteException, NullPointerException;
	
	/**
	 * Method for updating the user password
	 * @param token: the user session token
	 * @param userPass: his pass (to validate if that is the user
	 * @param newUserPass: his new pass
	 * @return: true if the changes are a success, false if the user data is incorrect, null if the session is incorrect
	 * @throws RemoteException: if some error occurred during the process , NullPointerException, if there is a problem inserting the userData
	 */
	public Boolean changeUserPass(Token token, String userPass, String newUserPass) throws RemoteException, NullPointerException;

	/**
	 * Method for removing the user from the database
	 * @param token: the user session token
	 * @param userPass: his pass (to validate if that is the user)
	 * @return: true if the user is eliminated correctly , false if the user data is incorrect, null if the session is incorrect
	 * @throws RemoteException: if some error occurred during the process , NullPointerException, if there is a problem inserting the userData
	 */
	public Boolean removeUser(Token token, String userPass) throws RemoteException, NullPointerException;

	/**
	 * Method for playing, it tries to match the players depending on the category they want to play
	 * @param session: the session token given after login
	 * @param type: the type of question the user wants to play
	 * @param service: its remote interface, for the observer pattern
	 * @return: The rival´s user info., a token (wait) for knowing if you have to wait or null 
	 * @throws RemoteException: if some error occurred during the process
	 * @throws SecurityException: if the user tries to play more than once simultaneously
	 */
	public UserDTO play(Token session, String type, IClientService service) throws RemoteException, SecurityException;

	/**Method for getting a question from the server
	 * @param session: the session token given after login 
	 * @return: the question (check: QuestionDTO:java), or null if it not playing the one asking or the session is invalid
	 * @throws RemoteException: if some error occurred during the process
	 */
	public QuestionDTO getQuestion(Token session) throws RemoteException;

	/**Method for answering a question
	 * @param session: the session token given after login 
	 * @param answer: the answer from the user
	 * @return: true or false, if the answer is right or wrong
	 * @throws RemoteException: if some error occurred during the process
	 */
	public boolean answerQuestion(Token session, String answer) throws RemoteException;

	/**
	 * Method to check if all questions are answered
	 * @param session: the session token given after login
	 * @return: true or false, if all questions are answered, or false if the session is invalid
	 * @throws RemoteException: if some error occurred during the process
	 */
	public boolean allQuestionAnswered(Token session) throws RemoteException;

	/**
	 * Method for getting the final results
	 * @param session: the session token given after login
	 * @return: the userscore of your rival, or null if the session is invalid, or the user is not playing
	 * @throws RemoteException: if some error occurred during the process
	 */
	public UserScoreDTO getResults(Token session) throws RemoteException;
	
	/**
	 * Method for exit the matchmaking process if the player does not want to play
	 * @param session: the session token given after login
	 * @param type: the type of questions
	 * @throws RemoteException:  if some error occurred during the process
	 */
	public void exitMatchMaking(Token session, String type) throws RemoteException;

}
