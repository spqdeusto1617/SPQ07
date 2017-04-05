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
import model.DTO.UserScoreDTO;
import model.assembler.QuestionAssembler;
import model.assembler.ScoreAssembler;
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
			questions.add(new Question("works?", "we hope so", 'a', "Unknown"));
			/*for(int i = (int)'a'; i<(int)'z'; i++){
			questions.add(new Question("works?", "we hope so", (char)(i + 1), "Unknown"));
			} 
			System.out.println("Last question introduced: "+questions.get(questions.size()-1).getLeter());
			questions.add(new Question("works?", "we hope so", 'ñ', "Unknown")); //for testing purposes*/
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
			}
			else{
				 question = currentQuestions.get(session).get(((int)currentResult.get(session).getCurrentLetter())-97);
			}
			
			if(answer.getAnswer().equals(question.getAnswer())){
				currentResult.get(session).increaseRight();
				if(currentResult.get(session).getCurrentLetter() == 'ñ'){
					currentQuestions.get(session).get(currentQuestions.get(session).size()-1).setAnswered(true);
					}else{
					currentQuestions.get(session).get(((int)currentResult.get(session).getCurrentLetter())-97).setAnswered(true);
					}
				return true;
			}
			else if(answer.getAnswer().equals("Pasapalabra")){ System.out.println("Server - Pasamos palabra");return true;}
			else{
				currentResult.get(session).increaseWrong();
				if(currentResult.get(session).getCurrentLetter() == 'ñ'){
				currentQuestions.get(session).get(currentQuestions.get(session).size()-1).setAnswered(true);
				}else{
				currentQuestions.get(session).get(((int)currentResult.get(session).getCurrentLetter())-97).setAnswered(true);
				}
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public boolean allQuestionAnswered(Token session){
		if(SessionManager.isValidSession(session)){
			return questionsAnswered(currentQuestions.get(session));
		}
		else{
			return false;
		}
	}
	
	public UserScoreDTO getResults(Token session){
		if(SessionManager.isValidSession(session)){
			//FIXME: save UserScore to DB
			UserScore finalScore = currentResult.get(session);
			currentQuestions.remove(session);
			currentResult.remove(session);//TODO: change this for 2 players(next sprint??)
			return ScoreAssembler.getInstance().assembleToDTO(finalScore);
		}
		else{
			return null;
		}
	}
	
	private static boolean questionsAnswered(ArrayList<Question> aQuestions){
		for (Question question : aQuestions) {

			if(!question.isAnswered()){
				return false;
			}
		}
		return true;
	}

	/*public static void main(String[] args) {//For testing purposes
		PasapalabraService service = new PasapalabraService();
		Token token = service.login("Me", "Test");
		System.out.println(token.getToken());
		QuestionDTO test2 = service.play(token, QuestionType.All);
		test2.setAnswer("Pasapalabra");
		System.out.println("First Question: "+test2.getQuestion() +"\n letter: "+test2.getLeter());
		System.out.println("\n");
		service.answerQuestion(token, test2);
		for(int i = 'a'; i<'z'+1;i++){
			test2 = service.getQuestion(token);
			System.out.println("Question number: "+(i-96)+" Question: "+test2.getQuestion() +"\n letter: "+test2.getLeter());
			
			test2.setAnswer("No");
			System.out.println("result: "+service.answerQuestion(token, test2));
			System.out.println("\n");
		}
		if(service.allQuestionAnswered(token)){
			System.err.println("Finnished");
		}
		else{
			System.err.println("No finised");
			test2 = service.getQuestion(token);
			System.out.println("Last question: "+test2.getLeter());
			test2.setAnswer("we hope so");
			System.out.println("result: "+service.answerQuestion(token, test2));
			if(service.allQuestionAnswered(token)){
				System.err.println("Finnished");
				UserScoreDTO score = service.getResults(token);
				System.err.println("Final results: right awnwered: "+score.getRightAnswered()+" wrong answered: "+score.getWrongAnswered());
			}
		}
	}*/
	
}
