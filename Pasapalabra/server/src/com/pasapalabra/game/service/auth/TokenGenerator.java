package com.pasapalabra.game.service.auth;

import java.util.UUID;

public class TokenGenerator {

	public static Token nextUniqueID() {
		return new Token(UUID.randomUUID().toString());
	}
	  
	
}
