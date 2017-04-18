package com.pasapalabra.game.dao.mongodb;

import java.util.List;

import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.pasapalabra.game.dao.UserDAO;
import com.pasapalabra.game.model.User;
import com.pasapalabra.game.server.Server;

public class UserMongoDAO extends UserDAO {

	@Override
	public User getUserByLogin(String username, String password) {
		@SuppressWarnings("deprecation")
		List<User> lUser = Server.mongoConnection.datastore.createQuery(User.class)
        .field("userName").equal(username)
        .field("pass").equal(password)
        .limit(1).asList();
		if(lUser.isEmpty())
			return null;
		else
			return lUser.get(0);
	}

	@Override
	public boolean checkIfExists(String username) {
		return !Server.mongoConnection.datastore.createQuery(User.class)
		        .field("userName").equal(username).asList().isEmpty();
	}

	@Override
	public void updateScore(String username, boolean won) {
		Query<User> userToUpdate = Server.mongoConnection.datastore.createQuery(User.class)
                .field("userName").equal(username);
		
		
		UpdateOperations<User> updateOperations;
		if(won)
			updateOperations = Server.mongoConnection.datastore.createUpdateOperations(User.class)
                .inc("GamesWon", 1);
		else
			updateOperations = Server.mongoConnection.datastore.createUpdateOperations(User.class)
                .inc("GamesLost", 1);
		Server.mongoConnection.datastore.update(userToUpdate, updateOperations);
	}

}
