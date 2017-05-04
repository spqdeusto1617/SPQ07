package com.pasapalabra.game.service.auth;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for managing all the current sessions 
 * @author ivan
 */
public class SessionManager {

	/**
	 * Map for managing all the current sessions
	 */
	public static ConcurrentHashMap<String, String> hmSessions = new ConcurrentHashMap<String, String>();
	
	/**
	 * Method for checking if a session is correct
	 * @param token: the session token
	 * @return true or false if the session is correct or not
	 */
	public static boolean isValidSession(Token token){
		return hmSessions.get(token.getToken()) != null;
	}
	
	/**
	 * Method for get a concrete user
	 * @param token: the session token
	 * @return: the token in String format, or null if the token does not exist
	 */
	public static String getUser(Token token){
		for(String tkn : hmSessions.keySet())
			if(tkn.equals(token.getToken()))
				return hmSessions.get(tkn);
		return null;
	}
	
	/**
	 * Method for adding a new session
	 * @param username: the username of the user
	 * @param token: the session token
	 */
	public static void addSession(String username, Token token){
		if(hmSessions.get(token.getToken()) != null)
			hmSessions.remove(token.getToken());
		hmSessions.put(token.getToken(), username);
	}
	
	/**
	 * Method for creating a new session
	 * @param username: the username of the user
	 * @return: the new session token 
	 */
	public static Token createSession(String username){
		Token token = TokenGenerator.nextUniqueID();
		hmSessions.put(token.getToken(), username);
		return token;
	}
	
	/**
	 * Method to validate if an user exist
	 * @param username: the username to validate
	 * @return: true or false if the user exist
	 */
	public static boolean userExist(String username){
		return hmSessions.containsValue(username);
	}

	/**
	 * Method to remove an user from the userlist(the user has disconnected)
	 * @param session: his session token
	 */
	public static void removeUser(Token session){
		hmSessions.remove(session.getToken());
	}

}
