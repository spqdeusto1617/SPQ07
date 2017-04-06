package service;


import java.rmi.Remote;

import model.QuestionType;
import model.DTO.QuestionDTO;
import model.DTO.UserDTO;
import service.auth.Token;

public interface IPasapalabraService extends Remote {

	public Token login(String userName, String pass);
	
	public UserDTO getData(Token token);
	
	public QuestionDTO play(Token session, QuestionType type);
	
	public QuestionDTO getQuestion(Token session);
	
	public boolean answerQuestion(Token session, String answer);
	
	public boolean allQuestionAnswered(Token session);

}
