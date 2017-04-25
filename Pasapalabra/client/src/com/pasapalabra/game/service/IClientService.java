package com.pasapalabra.game.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;

/**
 * @author ivan
 * Class for the server to send information to the desired client
 */
public interface IClientService extends Remote{
	
	/**
	 * Method for sending to the client the information of the other player
	 * @param user: the user´s information
	 * @throws RemoteException: if some error occurred during the process
	 */
	public void getUser(UserDTO user)throws RemoteException ;
	
	/**
	 * Method for indicating to the client if the other player has answered a letter right or wrong
	 * @param letter: the letter answered
	 * @param result: if the other player has answered right or wrong
	 * @throws RemoteException: if some error occurred during the process
	 */
	public void answered(char letter,boolean result) throws RemoteException;
	
	/**
	 * Method to get the final result of the other player
	 * @param score: the score of the other player
	 * @throws RemoteException: if some error occurred during the process
	 */
	public void finalResult(UserScoreDTO score) throws RemoteException;
	
	/**
	 * Method to indicate to the client that is his turn
	 * @param score: the score of the other player
	 * @throws RemoteException: if some error occurred during the process
	 */
	public void changeTurn(UserScoreDTO score) throws RemoteException;
	
}
