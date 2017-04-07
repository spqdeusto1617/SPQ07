package com.pasapalabra.game.dao.mongodb;

import com.pasapalabra.game.dao.UserDAO;
import com.pasapalabra.game.model.User;

public class UserMongoDAO extends UserDAO {

	@Override
	public User getUserByLogin(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkIfExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateScore(boolean won) {
		// TODO Auto-generated method stub
		
	}

}
