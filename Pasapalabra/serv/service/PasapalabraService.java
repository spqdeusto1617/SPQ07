package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import model.Question;
import model.QuestionType;
import model.User;
import model.UserScore;
import model.DTO.QuestionDTO;
import model.DTO.UserDTO;
import model.assembler.QuestionAssembler;
import model.assembler.UserAssembler;
import service.auth.SessionManager;
import service.auth.Token;

public class PasapalabraService implements IPasapalabraService{
	private static HashMap<Token, User> currentUsers = new HashMap<>();
	
	private static ConcurrentHashMap<Token, ArrayList<Question>> currentQuestions = new ConcurrentHashMap<Token, ArrayList<Question>>();
	
	private static ConcurrentHashMap<Token, UserScore> currentResult = new ConcurrentHashMap<Token, UserScore>();
	
	@SuppressWarnings("unused")
	public Token login(String userName, String pass)
	{
		if(true){//FIXME: get userdata from DB(if user does not exist, return null
			User user = new User(userName, "", pass, null, new Date(), 0, 0, 0);
			//UserDTO userDTO = UserAssembler.getInstance().assembleToDTO(user);
			Token token = SessionManager.createSession(userName);
			currentUsers.put(token, user);
			return token;
		}
		else return null;
	}
	
	public UserDTO getData(Token token) {
		// TODO Auto-generated method stub
		if(currentUsers.containsKey(token)) return UserAssembler.getInstance().assembleToDTO(currentUsers.get(token));
		else return null;
	}

	public QuestionDTO play(Token session, QuestionType type){
		if(SessionManager.isValidSession(session)){
			ArrayList<Question> questions = null;//FIXME: get questions of that type in arraylist of question( 'ñ' letter must be inserted in the last position of the arraylist) 
			currentQuestions.put(session, questions);
			currentResult.put(session, new UserScore());
			return QuestionAssembler.getInstance().assembleToDTO(questions.get(0));//We get the first question
		}
		else{
			return null;
		}
	}
	
	public QuestionDTO getQuestion(Token session){
		if(SessionManager.isValidSession(session)){
			if(allQuestionsAnsered(currentQuestions.get(session)))return null;//TODO: end game
			boolean answered = true;
			Question question = null;
			do{
				currentResult.get(session).nextLetter();
				
				if(currentResult.get(session).getCurrentLetter() == 'ñ'){
					 question = currentQuestions.get(session).get(currentQuestions.get(session).size()-1);
				}
				else{
					 question = currentQuestions.get(session).get((int)currentResult.get(session).getCurrentLetter());
				}
				if(!question.isAnswered()){
					answered = false;
				}
			}while(answered);
			return QuestionAssembler.getInstance().assembleToDTO(question);
			//We get the first question
		}
		else{
			return null;
		}
	}
	
	public boolean answerQuestion(Token session, QuestionDTO answer){
		if(SessionManager.isValidSession(session)){
			Question question = null;
			if(currentResult.get(session).getCurrentLetter() == 'ñ'){
				 question = currentQuestions.get(session).get(currentQuestions.get(session).size()-1);
				 currentQuestions.get(session).get(currentQuestions.get(session).size()-1).setAnswered(true);
			}
			else{
				 question = currentQuestions.get(session).get((int)currentResult.get(session).getCurrentLetter());
				currentQuestions.get(session).get((int)currentResult.get(session).getCurrentLetter()).setAnswered(true);
			}
			
			if(answer.getAnswer().equals(question.getAnswer())){
				currentResult.get(session).increaseRight();
				return true;
			}
			else if(answer.getAnswer().equals("Pasapalabra"))return true;
			else{
				currentResult.get(session).increaseWrong();
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	
	private static boolean allQuestionsAnsered(ArrayList<Question> aQuestions){
		for (Question question : aQuestions) {

			if(!question.isAnswered()){
				return false;
			}
		}
		return true;
	}

	/*private static int positionOfChar(char leter){
		if(leter == 'ñ'){
			return 14;
		}
		else{
			if(leter >14) return leter + 1;
			else return leter;
		}
	}*/
	
	
}
