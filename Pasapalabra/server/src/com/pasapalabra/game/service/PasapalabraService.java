package com.pasapalabra.game.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import com.pasapalabra.game.dao.QuestionDAO;
import com.pasapalabra.game.dao.UserDAO;
import com.pasapalabra.game.dao.mongodb.QuestionMongoDAO;
import com.pasapalabra.game.dao.mongodb.UserMongoDAO;
import com.pasapalabra.game.model.Question;
import com.pasapalabra.game.model.QuestionType;
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

/**
 * Class that implements all the methods available to the server.
 * @author Ivan
 */
public class PasapalabraService implements IPasapalabraService{
	private static ConcurrentHashMap<String, User> currentUsers = new ConcurrentHashMap<>();

	private static ConcurrentHashMap<String, ArrayList<Question>> currentQuestions = new ConcurrentHashMap<String, ArrayList<Question>>();

	private static ConcurrentHashMap<String, UserScore> currentResult = new ConcurrentHashMap<String, UserScore>();

	private static ConcurrentHashMap<String, String> currentMatches = new ConcurrentHashMap<String, String>();

	private static ConcurrentHashMap<String, IClientService> currentClients = new ConcurrentHashMap<String, IClientService>();

	private static ConcurrentHashMap<String, ArrayList<String>> waitingClients = new ConcurrentHashMap<String, ArrayList<String>>();


	@Override
	public boolean registry(UserDTO userData, String pass) throws RemoteException{
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		if(!uDao.checkIfExists(userData.getUserName())){
			try{
				com.pasapalabra.game.server.Server.mongoConnection.datastore.save(new User(userData.getUserName(), userData.getMail(), pass, userData.getProfileImage(), userData.getDOB(), 0, 0, 0));
				return true;
			}catch (Exception e) {
				e.printStackTrace();
				throw new RemoteException();
			}
		}
		else return false;
	}

	public Token login(String userName, String pass) throws RemoteException, SecurityException
	{
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		if(uDao.checkIfExists(userName)){
			User user = uDao.getUserByLogin(userName, pass);
			//User user = new User(userName, "a@a", pass, null, new Date(), 0, 0, 0);
			//UserDTO userDTO = UserAssembler.getInstance().assembleToDTO(user);
			if(currentUsers.containsValue(user)) throw new SecurityException();
			Token token = SessionManager.createSession(userName);
			currentUsers.put(token.getToken(), user);
			return token;
		}
		else return null;
	}

	public UserDTO getData(Token token) throws RemoteException{
		if(currentUsers.containsKey(token.getToken())) return UserAssembler.getInstance().assembleToDTO(currentUsers.get(token.getToken()));
		else return null;
	}

	public UserDTO play(Token session, String type,IClientService service) throws RemoteException, SecurityException{
		if(currentQuestions.containsKey(session.getToken())) throw new SecurityException();
		QuestionDAO qDAO = new QuestionMongoDAO(Server.mongoConnection);
		ArrayList<Question> questions = new ArrayList<>();
		for(char alphabet = 'a'; alphabet <= 'z'; alphabet++){
			questions.add(qDAO.getRandomQuestionByLeter(alphabet));
		} 
		questions.add(qDAO.getRandomQuestionByLeter('ñ'));
		/*ArrayList<Question> questions = new ArrayList<>();
		questions.add(new Question("Test", "Answer", 'a', "Unknown"));
		for(int i = (int)'a'; i<(int)'z'; i++){
			questions.add(new Question("Test", "Answer", (char)(i + 1), "Unknown"));
		} 
		questions.add(new Question("works?", "we hope so", 'ñ', "Unknown")); //for testing purposes*/
		currentQuestions.put(session.getToken(), questions);

		if(SessionManager.isValidSession(session)){
			try{
				waitingClients.get(type).get(0);
			}catch (Exception e) {
				
				waitingClients.put(type, new ArrayList<String>());
				waitingClients.get(type).add(session.getToken());
				currentClients.put(session.getToken(), service);
				currentResult.put(session.getToken(), new UserScore());

				return new UserDTO("Wait");//TODO: revise this
			}
			//If there is that category, but no players
			if(waitingClients.get(type).isEmpty()){
				System.out.println("Waiting clients esta vacio");
				waitingClients.get(type).add(session.getToken());
				currentResult.put(session.getToken(), new UserScore());
				return new UserDTO("Wait");//TODO: revise this
			}
			currentMatches.put(session.getToken(),waitingClients.get(type).get(0));
			waitingClients.get(type).remove(0);
			/*for(char alphabet = 'a'; alphabet <= 'z'; alphabet++){
					questions.add(qDAO.getRandomQuestionByLeter(alphabet));
				} 
				questions.add(qDAO.getRandomQuestionByLeter('ñ'));
				currentQuestions.put(session.getToken(), questions);*/
			currentResult.put(session.getToken(), new UserScore());
			currentClients.put(session.getToken(), service);
			IClientService client= (IClientService)currentClients.get(currentMatches.get(session.getToken()));
			client.getUser(UserAssembler.getInstance().assembleToDTO(currentUsers.get(session.getToken())));
			return UserAssembler.getInstance().assembleToDTO(currentUsers.get(currentMatches.get(session.getToken())));//We get the first question
		}

		else{
			return null;
		}
	}

	public QuestionDTO getQuestion(Token session) throws RemoteException{
		if(SessionManager.isValidSession(session)){
			if(!currentMatches.containsKey(session.getToken()))return null;//If it´s not playing, nothing is returned
			if(allQuestionAnswered(session))return null;//If the player has finished playing, he can´t answer anymore questions
			boolean answered = false;
			Question question = null;
			do{
				if(currentResult.get(session.getToken()).getCurrentLetter() == 'ñ'){
					question = currentQuestions.get(session.getToken()).get(currentQuestions.get(session.getToken()).size()-1);
				}
				else{
					question = currentQuestions.get(session.getToken()).get(((int)currentResult.get(session.getToken()).getCurrentLetter())-97);
				}
				if(question.isAnswered()){
					answered = true;
					currentResult.get(session.getToken()).nextLetter();
				}
				else answered = false;
			}while(answered);
			return QuestionAssembler.getInstance().assembleToDTO(question);
		}
		else{
			return null;
		}
	}

	public boolean answerQuestion(Token session, String answer) throws RemoteException{
		if(SessionManager.isValidSession(session)){
			if(!currentMatches.containsKey(session.getToken()))return false;
			Question question = null;
			if(!currentQuestions.containsKey(session.getToken()))return false;
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
				IClientService client= (IClientService)currentClients.get(currentMatches.get(session.getToken()));
				client.answered(currentResult.get(session.getToken()).getCurrentLetter(), true);
				currentResult.get(session.getToken()).nextLetter();
				return true;
			}
			else if(answer.equals("Pasapalabra")){ 
				System.out.println("Server - Pasamos palabra");
				currentResult.get(session.getToken()).nextLetter();
				return true;
			}
			else{
				currentResult.get(session.getToken()).increaseWrong();

				if(currentResult.get(session.getToken()).getCurrentLetter() == 'ñ'){
					currentQuestions.get(session.getToken()).get(currentQuestions.get(session.getToken()).size()-1).setAnswered(true);
				}else{
					currentQuestions.get(session.getToken()).get(((int)currentResult.get(session.getToken()).getCurrentLetter())-97).setAnswered(true);
				}
				IClientService client= (IClientService)currentClients.get(currentMatches.get(session.getToken()));
				client.answered(currentResult.get(session.getToken()).getCurrentLetter(), false);
				currentResult.get(session.getToken()).nextLetter();
				return false;
			}
		}
		else{
			return false;
		}
	}

	public boolean allQuestionAnswered(Token session) throws RemoteException{
		if(SessionManager.isValidSession(session)){
			if(currentQuestions.containsKey(session.getToken()))return questionsAnswered(currentQuestions.get(session.getToken()));
		}

		return false;

	}


	public UserScoreDTO getResults(Token session) throws RemoteException{
		if(SessionManager.isValidSession(session)){
			if(!currentMatches.containsKey(session.getToken()))return null;
			if(!allQuestionAnswered(session)) return null;
			UserScore finalScore1 = currentResult.get(session.getToken());
			UserScore finalScore2 = currentResult.get(currentMatches.get(session.getToken()));
			if(finalScore2.getTotalAnswered() == 0){
				String token1 = session.getToken();
				String token2 = currentMatches.get(token1);
				IClientService client= (IClientService)currentClients.get(currentMatches.get(session.getToken()));
				currentMatches.remove(token1);
				currentMatches.put(token2, token1);
				client.changeTurn(ScoreAssembler.getInstance().assembleToDTO(finalScore1));
				return ScoreAssembler.getInstance().assembleToDTO(finalScore1);
			}
			UserDAO uDAO = new UserMongoDAO(Server.mongoConnection);
			if(finalScore1.getRightAnswered() > finalScore2.getRightAnswered()){
				uDAO.updateScore(currentUsers.get(session.getToken()), true);
				uDAO.updateScore(currentUsers.get(currentMatches.get(session.getToken())), false);
				finalScore1.won();
				finalScore2.lost();
			}
			else{
				uDAO.updateScore(currentUsers.get(session), false);
				uDAO.updateScore(currentUsers.get(currentMatches.get(session.getToken())), true);
				finalScore1.lost();
				finalScore2.won();
			}
			IClientService client= (IClientService)currentClients.get(currentMatches.get(session.getToken()));
			client.finalResult(ScoreAssembler.getInstance().assembleToDTO(finalScore2));
			currentQuestions.remove(session.getToken());
			currentResult.remove(session.getToken());
			currentResult.remove(currentMatches.get(session.getToken()));
			currentClients.remove(session.getToken());
			currentClients.remove(currentMatches.get(session.getToken()));
			currentMatches.remove(session.getToken());
			return ScoreAssembler.getInstance().assembleToDTO(finalScore1);
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

	@Override
	public void exitMatchMaking(Token session, String type) throws RemoteException {

		if(SessionManager.isValidSession(session)){

			if(waitingClients.get(type).contains(session.getToken())){
				waitingClients.get(type).remove(session.getToken());
				currentClients.remove(session.getToken());
				currentResult.remove(session.getToken());
				currentQuestions.remove(session.getToken());

			}

		}

	}

	@Override
	public boolean deLogin(Token session) throws RemoteException {
		if(currentUsers.containsKey(session.getToken())){ 
			if(!currentQuestions.containsKey(session.getToken())){
				currentUsers.remove(session.getToken());
				return true;
			}
		}
		return false;

	}


	public static void main(String[] args) {//For testing purposes
		/*PasapalabraService service = new PasapalabraService();
		Token token = null;
		Token token2 = null;
		//Token token3 = null;
		try {
			token = service.login("Me", "Test");
			System.out.println("Token1: "+token.getToken());

			token2 = service.login("You", "Test2");
			System.out.println("Token2: "+token2.getToken());

			//token3 = service.login("He", "Test3");
		} catch (RemoteException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(token.getToken());
		ClientService clientservice1 = new ClientService();
		ClientService clientservice2 = new ClientService();
		//ClientService clientservice3 = new ClientService();
		try {
			UserDTO test = service.play(token, QuestionType.All.toString(),clientservice1);
			//System.out.println("Respuesta del servidor: "+test.getUserName());

			UserDTO test2 = service.play(token2, QuestionType.All.toString(), clientservice2);

			//UserDTO test3 = service.play(token3, QuestionType.All.toString(), clientservice3);
			System.out.println(test2.getUserName());

			//System.out.println("Test:"+test3.getUserName());
			//System.out.println(test.getUserName());
			QuestionDTO question2 = service.getQuestion(token2);
			System.out.println("question"+question2);
			boolean result = service.answerQuestion(token2, "Answer");
			System.out.println(result);
			for(char alphabet = 'a'; alphabet <= 'z'; alphabet++ ){
				question2 = service.getQuestion(token2);
				System.out.println("question"+question2);
				result = service.answerQuestion(token2, Character.toString(alphabet));
				System.out.println("Respuesta servidor: "+result);
			}
			UserScoreDTO score1 = service.getResults(token2);
			System.out.println(score1);

			QuestionDTO question = service.getQuestion(token);
			System.out.println("question"+question);
			result = service.answerQuestion(token, "Answer");
			System.out.println(result);
			for(char alphabet = 'a'; alphabet <= 'z'; alphabet++ ){
				
				question = service.getQuestion(token);
				System.out.println("question"+question);
				result = service.answerQuestion(token, Character.toString(alphabet));
				System.out.println("Respuesta servidor: "+result);
			}
			UserScoreDTO score2 = service.getResults(token);
			System.out.println(score2);
		} catch (RemoteException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
	}



}
