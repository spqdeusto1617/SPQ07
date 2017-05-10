package com.pasapalabra.game.service;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;

public class ClientService extends UnicastRemoteObject implements IClientService, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;

	public ClientService(String name) throws RemoteException {
		super();
		this.name = name;
	}
	//TODO: implement this
	@Override
	public void getUser(UserDTO user) throws RemoteException {
		System.err.println("Nombre de usuario: "+user.getUserName());
		ServiceLocator.playing = true;
	}
	//TODO: implement this
	@Override
	public void answered(char letter, boolean result) throws RemoteException {
		System.err.println("Respuesta del otro usuario a la letra: "+letter+", "+result);

	}
	//TODO: implement this
	@Override
	public void finalResult(UserScoreDTO score) throws RemoteException {
		System.err.println("La puntuación final del otro usuario es: "+score);
		ServiceLocator.playing = false;//TODO: transicion de vuelta
		ServiceLocator.turn = false;
		ServiceLocator.player1 = false;
		
	}
	//TODO: implement this (change to observer mode)
	@Override
	public void changeTurn(UserScoreDTO score) throws RemoteException {
		System.err.println("La puntuación de su rival es: "+score+" y ahora le toca jugar a usted");
		 ServiceLocator.turn = true;
		
		 //TODO: play now
		
	}
	@Override
	public void otherDisconnected() throws RemoteException {
		System.err.println("Su rival se ha desconectado, saliendo del juego");
		//TODO: exit the game(Use windowUtilities.forcedCloseSession
	}

}
