package com.pasapalabra.game.dao;

import com.pasapalabra.game.model.User;

public abstract class UserDAO {

	public abstract User getUserByLogin(String username, String password);
	
	public abstract boolean checkIfExists(String username);
	
	public abstract void updateScore(boolean won);
	
	
}
