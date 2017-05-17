package com.pasapalabra.game.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.WriteResult;
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

	public static Logger log = com.pasapalabra.game.utilities.AppLogger.getWindowLogger(PasapalabraService.class.getName());

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
				log.log(Level.SEVERE, "Error occurred while trying to registry a new user", e);
				throw new RemoteException();
			}
		}
		else return false;
	}

	public Token login(String userName, String pass) throws RemoteException
	{
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		if(uDao.checkIfExists(userName)){
			User user = uDao.getUserByLogin(userName, pass);
			//User user = new User(userName, "a@a", pass, null, new Date(), 0, 0, 0);
			if(SessionManager.userExist(userName)) {
				deLogin(new Token(getUserToken(userName)));
				log.log(Level.INFO, "user "+userName+" exists");
			}
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
		if(!SessionManager.isValidSession(session)) return null;
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



		if(!waitingClients.containsKey(type)){
			waitingClients.put(type, new ArrayList<String>());
			waitingClients.get(type).add(session.getToken());
			currentClients.put(session.getToken(), service);
			currentResult.put(session.getToken(), new UserScore());

			return new UserDTO("Wait");
		}

		//If there is that category, but no players
		if(waitingClients.get(type).isEmpty()){
			System.out.println("Waiting clients esta vacio");
			waitingClients.get(type).add(session.getToken());
			currentClients.put(session.getToken(), service);
			currentResult.put(session.getToken(), new UserScore());
			return new UserDTO("Wait");
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
				log.log(Level.INFO, "Server - Pasapalabra");
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
		log.log(Level.INFO, "Not valid session "+session.getToken());
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
				log.log(Level.INFO, "Changed players");
				return ScoreAssembler.getInstance().assembleToDTO(finalScore1);
			}
			UserDAO uDAO = new UserMongoDAO(Server.mongoConnection);
			if(finalScore1.getRightAnswered() > finalScore2.getRightAnswered()){
				//FIXME: Revise this
				uDAO.updateScore(currentUsers.get(session.getToken()), true);
				uDAO.updateScore(currentUsers.get(currentMatches.get(session.getToken())), false);
				finalScore1.won();
				finalScore2.lost();
			}
			else{
				//FIXME: Revise this
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
			log.log(Level.INFO, "Game ended");
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

	private static String getUserToken(String userName){
		Map<String, User> map = currentUsers;
		for (Map.Entry<String, User> entry : map.entrySet()) {
			if(entry.getValue().getUserName().equals(userName)){
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public Boolean changeUserIMG(Token token, String userPass, String userIMG) throws RemoteException, NullPointerException {
		if(!SessionManager.isValidSession(token)) return null;
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		User user = uDao.getUserByLogin(currentUsers.get(token.getToken()).getUserName(), userPass);
		if(user == null) return false;
		user.setProfileImage(userIMG);
		if(Server.mongoConnection.datastore.save(user) != null)return true;
		else throw new NullPointerException();
	}

	@Override
	public Boolean changeUserMail(Token token, String userPass, String userMail) throws RemoteException, NullPointerException{
		if(!SessionManager.isValidSession(token)) return null;
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		User user = uDao.getUserByLogin(currentUsers.get(token.getToken()).getUserName(), userPass);
		if(user == null) return false;
		user.setMail(userMail);
		if(Server.mongoConnection.datastore.save(user) != null)return true;
		else throw new NullPointerException();
	}

	@Override
	public Boolean changeUserPass(Token token, String userPass, String newUserPass) throws RemoteException, NullPointerException{
		if(!SessionManager.isValidSession(token)) return null;
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		User user = uDao.getUserByLogin(currentUsers.get(token.getToken()).getUserName(), userPass);
		if(user == null) return false;
		user.setPass(newUserPass);
		if(Server.mongoConnection.datastore.save(user) != null)return true;
		else throw new NullPointerException();
	}

	@Override
	public Boolean removeUser(Token token, String userPass) throws RemoteException, NullPointerException{
		if(!SessionManager.isValidSession(token)) return null;
		UserDAO uDao = new UserMongoDAO(Server.mongoConnection);
		User user = uDao.getUserByLogin(currentUsers.get(token.getToken()).getUserName(), userPass);
		if(user == null) return false;
		WriteResult deleteWriteResult = null;
		deleteWriteResult = Server.mongoConnection.datastore.delete(user);
		if(deleteWriteResult.getN() == 1)return true;
		else throw new NullPointerException();
	}

	private static String getRivalToken(String token){
		Map<String, String> map = currentMatches;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if(entry.getValue().equals(token)){
				return entry.getKey();
			}
		}
		return null;
	}
	@Override
	public void exitMatchMaking(Token session, String type) throws RemoteException {

		if(SessionManager.isValidSession(session)){

			if(waitingClients.get(type).contains(session.getToken())){
				waitingClients.get(type).remove(session.getToken());
				currentClients.remove(session.getToken());
				currentResult.remove(session.getToken());
				currentQuestions.remove(session.getToken());
				log.log(Level.INFO, "User exit matchmaking");
			}

		}

	}

	@Override
	public void deLogin(Token session) throws RemoteException {
		if(SessionManager.isValidSession(session)){
			if(currentUsers.containsKey(session.getToken())){ 
				if(!currentQuestions.containsKey(session.getToken())){//The player is not playing, no problem
					currentUsers.remove(session.getToken());
					SessionManager.removeUser(session);
					log.log(Level.INFO, "User delog");

				}
				else{//The player is playing, we try to exit 
					if(currentMatches.contains(session.getToken())){//The user is not currently playing
						String rivalToken = getRivalToken(session.getToken());
						IClientService client = (IClientService) currentClients.get(rivalToken);
						currentClients.remove(session.getToken());
						currentQuestions.remove(session.getToken());
						currentQuestions.remove(rivalToken);
						currentResult.remove(session.getToken());
						currentResult.remove(rivalToken);
						currentMatches.remove(rivalToken);
						currentUsers.remove(session.getToken());
						SessionManager.removeUser(session);
						try{
							client.otherDisconnected();//We notify his rival that he has exited
						}catch (RemoteException e) {
							log.log(Level.SEVERE, "Error trying to contact to the other user");
						}
						log.log(Level.INFO, "User delog while not playing");
					}
					else if(currentMatches.containsKey(session.getToken())){//the user is playing
						String rivalToken = currentMatches.get(session.getToken());
						IClientService client= (IClientService) currentClients.get(rivalToken);
						currentClients.remove(session.getToken());
						currentQuestions.remove(session.getToken());
						currentQuestions.remove(rivalToken);
						currentResult.remove(session.getToken());
						currentResult.remove(rivalToken);
						currentMatches.remove(session.getToken());
						currentUsers.remove(session.getToken());
						SessionManager.removeUser(session);
						try{
							client.otherDisconnected();//We notify his rival that he has exited
						}catch (RemoteException e) {
							log.log(Level.SEVERE, "Error trying to contact to the other user");
						}
						log.log(Level.INFO, "User delog while playing");


					}
					else if(waitingClients.containsKey(session.getToken())){//The player is waiting to play
						waitingClients.get(QuestionType.All.toString()).remove(session.getToken());
						currentQuestions.remove(session.getToken());
						currentClients.remove(session.getToken());
						currentResult.remove(session.getToken());
						currentUsers.remove(session.getToken());
						SessionManager.removeUser(session);
						log.log(Level.INFO, "User delog while waiting");

					}
					else{
						currentUsers.remove(session.getToken());
						currentQuestions.remove(session.getToken());
						SessionManager.removeUser(session);
					}
				}
			}
		}
		else throw new RemoteException("Error delogin");

	}


	public static void main(String[] args) {//For testing purposes
		/*PasapalabraService service = new PasapalabraService();
		Token token = null;
		ClientService clientservice1 = new ClientService();
		try {
			token = service.login("12345678", "12345678");
			System.out.println("Token1: "+token.getToken());
			UserDTO userDTO = service.play(token, QuestionType.All.toString(), clientservice1);
			service.exitMatchMaking(token, QuestionType.All.toString());
			service.deLogin(token);
		}catch (Exception e) {
		
			e.printStackTrace();
		}*/
		/*PasapalabraService service = new PasapalabraService();
		Token token = null;
		Token token2 = null;
		Token token3 = null;
		Token token4 = null;
		ClientService clientservice1 = new ClientService();
		ClientService clientservice2 = new ClientService();
		ClientService clientservice3 = new ClientService();
		ClientService clientservice4 = new ClientService();
		try {
			token = service.login("12345678", "12345678");
			System.out.println("Token1: "+token.getToken());

			token2 = service.login("12345678", "12345678");

			UserDTO userDTO = service.play(token, QuestionType.All.toString(), clientservice1);
			if(userDTO == null)System.out.println("Null");
			//System.out.println("Token2: "+token2.getToken());

			token3 = service.login("Jugador 3", "Test3");
			//System.out.println("Token3: "+token3.getToken());

			token4 = service.login("Jugador 4", "Test3");

		} catch (RemoteException | SecurityException e) {
			
			e.printStackTrace();
		}


		//System.out.println(token.getToken());
		ClientService clientservice1 = new ClientService();
		ClientService clientservice2 = new ClientService();
		ClientService clientservice3 = new ClientService();
		ClientService clientservice4 = new ClientService();
		try {
			UserDTO test = service.play(token, QuestionType.All.toString(),clientservice1);
			//System.out.println("Respuesta del servidor: "+test.getUserName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			service.exitMatchMaking(token, QuestionType.All.toString());
			System.out.println("Salimos del matchmaking");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			test = service.play(token, QuestionType.All.toString(),clientservice1);
			System.out.println("Respuesta del servidor: "+test.getUserName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			test = service.play(token, QuestionType.All.toString(),clientservice1);
			UserDTO test2 = service.play(token2, QuestionType.All.toString(), clientservice2);
			System.out.println("Juego 1");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}

			UserDTO test3 = service.play(token3, QuestionType.All.toString(), clientservice3);
			System.out.println(test3.getUserName());

			//System.out.println("Test:"+test3.getUserName());
			//System.out.println(test.getUserName());
			QuestionDTO question2 = service.getQuestion(token2);
			System.out.println("question"+question2);
			boolean result = service.answerQuestion(token2, "Answer");
			System.out.println("Respuesta servidor:"+result);
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
			System.out.println("--------------------------------------------------------");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			System.out.println("Siguiente partida");
			System.out.println(test3.getUserName());
			UserDTO test4 = service.play(token4, QuestionType.All.toString(),clientservice4);
			System.out.println(test4.getUserName());
		} catch (RemoteException | SecurityException e) {
		
			e.printStackTrace();
		}

		 */}





}
