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
		if(true){//FIXME: get userdata from DB(if user does not exist, return null)
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
			ArrayList<Question> questions = new ArrayList<>();//FIXME: get questions of that type in arraylist of question( 'ñ' letter must be inserted in the last position of the arraylist) 
			/*questions.add(new Question("works?", "we hope so", 'a', "Unknown"));
			questions.add(new Question("works now?", "we hope so2", 'b', "known")); //for testing purposes*/
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
			
			boolean answered = true;
			Question question = null;
			do{
				currentResult.get(session).nextLetter();
				
				if(currentResult.get(session).getCurrentLetter() == 'ñ'){
					 question = currentQuestions.get(session).get(currentQuestions.get(session).size()-1);
				}
				else{
					 question = currentQuestions.get(session).get(((int)currentResult.get(session).getCurrentLetter())-97);
				}
				if(!question.isAnswered()){
					answered = false;
				}
			}while(answered);
			return QuestionAssembler.getInstance().assembleToDTO(question);
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
				 question = currentQuestions.get(session).get(((int)currentResult.get(session).getCurrentLetter())-97);
				currentQuestions.get(session).get(((int)currentResult.get(session).getCurrentLetter())-97).setAnswered(true);
			}
			
			if(answer.getAnswer().equals(question.getAnswer())){
				currentResult.get(session).increaseRight();
				if(allQuestionsAnsered(currentQuestions.get(session))){System.out.println("Finished");}//TODO: end game
				return true;
			}
			else if(answer.getAnswer().equals("Pasapalabra"))return true;
			else{
				currentResult.get(session).increaseWrong();
				if(allQuestionsAnsered(currentQuestions.get(session))){System.out.println("Finished");}//TODO: end game
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

	/*public static void main(String[] args) {
		PasapalabraService service = new PasapalabraService();
		Token token = service.login("Me", "Test");
		System.out.println(token.getToken());
		UserDTO test = service.getData(token);
		System.out.println(test);
		QuestionDTO test2 = service.play(token, QuestionType.All);
		System.out.println(test2);
		test2.setAnswer("No");
		boolean right = service.answerQuestion(token, test2);
		System.out.println(right);
		test2 = service.getQuestion(token);
		System.out.println("New question: "+test2);
		test2.setAnswer("we hope so");
		right = service.answerQuestion(token, test2);
		System.out.println("Final right: "+right);
	}*/
	
}
