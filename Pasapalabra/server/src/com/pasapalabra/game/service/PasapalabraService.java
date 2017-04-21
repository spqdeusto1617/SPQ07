package com.pasapalabra.game.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.pasapalabra.game.dao.QuestionDAO;
import com.pasapalabra.game.dao.UserDAO;
import com.pasapalabra.game.dao.mongodb.QuestionMongoDAO;
import com.pasapalabra.game.dao.mongodb.UserMongoDAO;
import com.pasapalabra.game.model.Question;
import com.pasapalabra.game.model.User;
import com.pasapalabra.game.model.UserScore;
import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.model.assembler.QuestionAssembler;
import com.pasapalabra.game.model.assembler.ScoreAssembler;
import com.pasapalabra.game.model.assembler.UserAssembler;
import com.pasapalabra.game.server.Server;
import com.pasapalabra.game.service.auth.SessionManager;
import com.pasapalabra.game.service.auth.Token;

public class PasapalabraService implements IPasapalabraService{
	private static HashMap<String, User> currentUsers = new HashMap<>();

	private static ConcurrentHashMap<String, ArrayList<Question>> currentQuestions = new ConcurrentHashMap<String, ArrayList<Question>>();

	private static ConcurrentHashMap<String, UserScore> currentResult = new ConcurrentHashMap<String, UserScore>();

	@Override
	public boolean registry(UserDTO userData, String pass) throws RemoteException{
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		if(!uDao.checkIfExists(userData.getUserName())){//FIXME: validate if user exist

			try{
				//FIXME: introduce the user into the database
				com.pasapalabra.game.server.Server.mongoConnection.datastore.save(new User(userData.getUserName(), userData.getMail(), pass, userData.getProfileImage(), userData.getDOB(), 0, 0, 0));
				return true;
			}catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else return false;
	}

	public Token login(String userName, String pass) throws RemoteException
	{
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		if(uDao.checkIfExists(userName)){
			User user = uDao.getUserByLogin(userName, pass);
			//UserDTO userDTO = UserAssembler.getInstance().assembleToDTO(user);
			Token token = SessionManager.createSession(userName);
			currentUsers.put(token.getToken(), user);
			return token;
		}
		else return null;
	}

	public UserDTO getData(Token token) throws RemoteException{
		// TODO Auto-generated method stub
		if(currentUsers.containsKey(token.getToken())) return UserAssembler.getInstance().assembleToDTO(currentUsers.get(token.getToken()));
		else return null;
	}

	public QuestionDTO play(Token session, String type) throws RemoteException{
		QuestionDAO qDAO = new QuestionMongoDAO(Server.mongoConnection);
		if(SessionManager.isValidSession(session)){
			ArrayList<Question> questions = new ArrayList<>();//FIXME: get questions of that type in arraylist of question( 'ñ' letter must be inserted in the last position of the arraylist) 
			
			for(char alphabet = 'a'; alphabet <= 'z'; alphabet++){
			questions.add(qDAO.getRandomQuestionByLeter(alphabet));
			} 
			questions.add(qDAO.getRandomQuestionByLeter('ñ'));
			currentQuestions.put(session.getToken(), questions);
			currentResult.put(session.getToken(), new UserScore());
			return QuestionAssembler.getInstance().assembleToDTO(questions.get(0));//We get the first question
		}
		else{
			return null;
		}
	}

	public QuestionDTO getQuestion(Token session) throws RemoteException{
		if(SessionManager.isValidSession(session)){

			boolean answered = true;
			Question question = null;
			do{
				currentResult.get(session.getToken()).nextLetter();
				if(currentResult.get(session.getToken()).getCurrentLetter() == 'ñ'){
					question = currentQuestions.get(session.getToken()).get(currentQuestions.get(session.getToken()).size()-1);
				}
				else{
					question = currentQuestions.get(session.getToken()).get(((int)currentResult.get(session.getToken()).getCurrentLetter())-97);
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

	public boolean answerQuestion(Token session, String answer) throws RemoteException{
		if(SessionManager.isValidSession(session)){
			Question question = null;
			if(currentResult.get(session.getToken()).getCurrentLetter() == 'ñ'){
				question = currentQuestions.get(session.getToken()).get(currentQuestions.get(session.getToken()).size()-1);
			}
			else{
				question = currentQuestions.get(session.getToken()).get(((int)currentResult.get(session.getToken()).getCurrentLetter())-97);
			}

			if(answer.equals(question.getAnswer())){
				currentResult.get(session.getToken()).increaseRight();
				if(currentResult.get(session.getToken()).getCurrentLetter() == 'ñ'){
					currentQuestions.get(session.getToken()).get(currentQuestions.get(session.getToken()).size()-1).setAnswered(true);
				}else{
					currentQuestions.get(session.getToken()).get(((int)currentResult.get(session.getToken()).getCurrentLetter())-97).setAnswered(true);
				}
				return true;
			}
			else if(answer.equals("Pasapalabra")){ System.out.println("Server - Pasamos palabra");return true;}
			else{
				currentResult.get(session.getToken()).increaseWrong();
				if(currentResult.get(session.getToken()).getCurrentLetter() == 'ñ'){
					currentQuestions.get(session.getToken()).get(currentQuestions.get(session.getToken()).size()-1).setAnswered(true);
				}else{
					currentQuestions.get(session.getToken()).get(((int)currentResult.get(session.getToken()).getCurrentLetter())-97).setAnswered(true);
				}
				return false;
			}
		}
		else{
			return false;
		}
	}

	public boolean allQuestionAnswered(Token session) throws RemoteException{
		if(SessionManager.isValidSession(session)){
			return questionsAnswered(currentQuestions.get(session.getToken()));
		}
		else{
			return false;
		}
	}

	public UserScoreDTO getResults(Token session) throws RemoteException{
		if(SessionManager.isValidSession(session)){
			UserScore finalScore = currentResult.get(session.getToken());
			UserDAO uDAO = new UserMongoDAO(Server.mongoConnection);
			uDAO.updateScore(currentUsers.get(session), true);
			currentQuestions.remove(session.getToken());
			currentResult.remove(session.getToken());//TODO: change this for 2 players(next sprint??)
			return ScoreAssembler.getInstance().assembleToDTO(finalScore);
		}
		else{
			return null;
		}
	}

	private static boolean questionsAnswered(ArrayList<Question> aQuestions) throws RemoteException{
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
