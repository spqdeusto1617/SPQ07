package com.pasapalabra.game.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;

public interface IClientService extends Remote{
	
	public void getUser(UserDTO user)throws RemoteException ;
	
	public void answered(char letter,boolean result) throws RemoteException;
	
	public void result(UserScoreDTO score) throws RemoteException;
	
	public void changeTurn(UserScoreDTO score) throws RemoteException;
	
}
