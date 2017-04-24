package com.pasapalabra.game.service.auth;

import java.util.UUID;

public class TokenGenerator {

	/**
	 * Method for generating a random token for each user
	 * @return the new token
	 */
	public static Token nextUniqueID() {
		return new Token(UUID.randomUUID().toString());
	}
	  
	
}
