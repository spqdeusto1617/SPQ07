package com.pasapalabra.game.service.auth;

import java.util.HashMap;

public class SessionManager {

	public static HashMap<String, String> hmSessions = new HashMap<String, String>();
	
	public static boolean isValidSession(Token token){
		return hmSessions.get(token.getToken()) != null;
	}
	
	public static String getUser(Token token){
		for(String tkn : hmSessions.keySet())
			if(tkn.equals(token.getToken()))
				return hmSessions.get(tkn);
		return null;
	}
	
	public static void addSession(String username, Token token){
		if(hmSessions.get(token.getToken()) != null)
			hmSessions.remove(token.getToken());
		hmSessions.put(token.getToken(), username);
	}
	
	public static Token createSession(String username){
		Token token = TokenGenerator.nextUniqueID();
		hmSessions.put(token.getToken(), username);
		return token;
	}
	
}
