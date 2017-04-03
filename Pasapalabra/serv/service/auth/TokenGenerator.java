package service.auth;

import java.util.UUID;

public class TokenGenerator {

	public static Token nextSessionId() {
		return new Token(UUID.randomUUID().toString());
	}
	  
	
}
