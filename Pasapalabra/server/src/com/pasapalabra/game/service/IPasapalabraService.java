package com.pasapalabra.game.service;


import java.rmi.Remote;

import com.pasapalabra.game.model.QuestionType;
import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.service.auth.Token;

public interface IPasapalabraService extends Remote {

	public Token login(String userName, String pass);
	
	public UserDTO getData(Token token);
	
	public QuestionDTO play(Token session, QuestionType type);
	
	public QuestionDTO getQuestion(Token session);
	
	public boolean answerQuestion(Token session, String answer);
	
	public boolean allQuestionAnswered(Token session);

}
