package com.pasapalabra.game.service;


import java.rmi.Remote;
import java.rmi.RemoteException;

import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.service.auth.Token;

public interface IPasapalabraService extends Remote {

	public Token login(String userName, String pass) throws RemoteException;
	
	public UserDTO getData(Token token) throws RemoteException;
	
	public QuestionDTO play(Token session, String type) throws RemoteException;
	
	public QuestionDTO getQuestion(Token session) throws RemoteException;
	
	public boolean answerQuestion(Token session, String answer) throws RemoteException;
	
	public boolean allQuestionAnswered(Token session) throws RemoteException;
	
	public UserScoreDTO getResults(Token session) throws RemoteException;

}
