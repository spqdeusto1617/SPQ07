package com.pasapalabra.game.dao.mongodb;

import java.util.Date;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.pasapalabra.game.dao.UserDAO;
import com.pasapalabra.game.model.User;
import com.pasapalabra.game.service.auth.TokenGenerator;

/** Defines all database User related needed methods.
 * @author Guti
 *
 */
public class UserMongoDAO extends UserDAO {

	private MongoConnection connection;

	public UserMongoDAO(MongoConnection mongoConnection) {
		this.connection = mongoConnection;
	}


	@Override
	public User getUserByLogin(String username, String password) {
		@SuppressWarnings("deprecation")
		List<User> lUser = this.connection.datastore.createQuery(User.class)
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
		return !this.connection.datastore.createQuery(User.class)
				.field("userName").equal(username).asList().isEmpty();
	}


	@Override
	public void updateScore(User user, boolean won) {
		if(won)
			user.setGamesWon(user.getGamesWon()+1);
		else
			user.setGamesLost(user.getGamesLost()+1);
		this.connection.datastore.save(user);
	}

	public static void main(String[] args) {
		User userToTest = new User(TokenGenerator.nextUniqueID().getToken(), "aaaa@aaa.com", "1234", null, new Date(), 0, 0, 0);
		MongoClientOptions.Builder options = MongoClientOptions.builder();
		options.socketKeepAlive(true);
		MongoClient mongoClient = new MongoClient("127.0.0.1:27017", options.build());
		MongoConnection mongoConnection = new MongoConnection("com.pasapalabra.game.model", mongoClient, "pasapalabra");


		// Instanciates DAO
		UserDAO userDAO = new UserMongoDAO(mongoConnection);


		// Inserts needed data.
		mongoConnection.datastore.save(userToTest);
		System.out.println(userToTest);
		userDAO.updateScore(userToTest, true);
		
	}

}
