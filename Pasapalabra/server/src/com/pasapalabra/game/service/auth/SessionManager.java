package com.pasapalabra.game.service.auth;

import java.util.HashMap;

public class SessionManager {

	public static HashMap<Token, String> hmSessions = new HashMap<Token, String>();
	
	public static boolean isValidSession(Token token){
		return hmSessions.get(token) != null;
	}
	
	public static String getUser(Token token){
		for(Token tkn : hmSessions.keySet())
			if(tkn.getToken().equals(token.getToken()))
				return hmSessions.get(tkn);
		return null;
	}
	
	public static void addSession(String username, Token token){
		if(hmSessions.get(token) != null)
			hmSessions.remove(token);
		hmSessions.put(token, username);
	}
	
	public static Token createSession(String username){
		Token token = TokenGenerator.nextSessionId();
		hmSessions.put(token, username);
		return token;
	}
	
}
