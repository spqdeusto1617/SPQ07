package com.pasapalabra.game.utilidades;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.QuestionType;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.service.IPasapalabraService;
import com.pasapalabra.game.service.auth.Token;


/**Clase de utilidad para realizar la conexión del cliente con el servidor
 * @author Iván
 *Tiene un solo método(lanzaConexión) que sirve para realizar la conexión con el servidor, y hacerle consultas sobre lo que el cliente necesite. 
 */
public class ClientConnexion {

	private static IPasapalabraService service;
	
	public static char currentLetter;

	public static Token sessionAuth;

	public static UserDTO userInfo;//Para Alvaro: aquí tienes la info del usuario(por si la necesitas)

	public static boolean serverReady;

	public static boolean pasapalabra;//Para Alvaro: este boolean comprueba si el usuario ha hecho pasapalabra (puedes quitarlo y usar tú otro método para comprobarlo si quieres)
	
	private static boolean reachZ = false;

	public static boolean gameEnd;//PARA Alvaro: este boolean comprueba si el juego ha acabado (puedes tambien eliminarlo si queires)

	public static void startConnection(String[] args)
			throws MalformedURLException, RemoteException, NotBoundException {

		if(args.length == 0){
			System.err.println("No arguments passed");
			//System.exit(0);
		}

		try {
			String URL = "//" + args[0] + ":" + args[1] + "/" + args[2];
			service = (IPasapalabraService) Naming.lookup(URL);
			serverReady = true;
		} catch (Exception e) {
			System.err.println(" *# Error connecting to the server : " + e.getMessage());
			serverReady = false;
			throw e;
		}

	}

	public static void login(String userName, String pass) throws Exception{
		try{
			sessionAuth = service.login(userName, pass);
		}catch(Exception a){
			throw a;
		}
		try{
			userInfo = service.getData(sessionAuth);
		}catch(Exception e){
			throw e;
		}
	}
	/*Para Alvaro: este método es para jugar, tienes que pasarle un question type
	 * (que, de momento, no cambia nada) seleccionado por el usuario, y si no hay ningún
	 problema, te devuelve una QuestionDTO (para obtener la pregunta, (nombre varialbe).getQuestion())
	 pero, si hay algun problema, tira una Exception, así que mete la llamada al método en un try/catch
	 Y si entra en el catch, haz que no empiece a jugar
	 * */
	public static QuestionDTO play(QuestionType type) throws Exception{
		try{
			/*QuestionDTO question = service.play(sessionAuth, type.toString());
			reachZ = false;
			gameEnd = false;
			currentLetter = 'a';*/
			return null;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
	/*Para Alvaro: este método es para pedir una pregunta al servidor(tras el usuario responer)
	 * IMPORTANTE: este método se tiene que llamar siempre tras responder una pregunta, pero
	 	se diferencia del anterior en que el primero solo te devuelve la pregunta la 1º vez
	 	PD: tienes acceso tambien a la letra, con ({nombreVariable}.getLetter)
	 */
	public static QuestionDTO getQuestion() throws Exception{
		try{
			QuestionDTO question = service.getQuestion(sessionAuth);
			currentLetter = question.getLeter();
			if(question.getLeter() == 'z')reachZ = true;
			return question;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
	/*
	 * Para Alvaro: este método es para responder a la pregunta. Simplemente pasas
	 * la pregunta y te devuelve true si está bien, y false si está mal.
	 * Importante: si el usuario hace pasapalabra, devuelve true, así que tendras que
	 * controlar cuando el usuario hace pasapalabra(tienes el boolean pasapalabra accesible
	 * para comprobarlo)
	 */
	public static boolean answerQuestion(String answer) throws Exception{
		if(answer.equals("Pasapalabra"))pasapalabra = true;
		else pasapalabra = false;
		try{
			boolean result = service.answerQuestion(sessionAuth, answer);
			return result;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}
	/*
	 * Para Alvaro: con este método compruebas si el juego ha acabado. Devuelve true o false
	 * si el juego ha acabado o no. Puedes llamarlo siempre que quieras, ya que comprueba
	 * antes si el usuario ya ha llegado a la Z, antes de dejar preguntas (si nunca ha llegado,
	 * no puede haber acabado)
	 */
	public static boolean endGame() throws Exception{
		if(reachZ){
			try{
				boolean result = service.allQuestionAnswered(sessionAuth);
				gameEnd = result;
				return result;
			}catch (Exception e) {
				// TODO: handle exception
				throw e;
			}
		}
		else return false;
	}
/*
 * Para Alvaro: por último, una vez que el juego ha acabado, obtenemos la puntuación del usuario
 * (tienes tanto aciertos como fallos)
 */
	public static UserScoreDTO getResults() throws Exception{
		try{
			UserScoreDTO score = service.getResults(sessionAuth);
			return score;
		}catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

}