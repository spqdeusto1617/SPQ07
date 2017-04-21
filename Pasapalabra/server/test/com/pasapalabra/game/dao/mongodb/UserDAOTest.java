package com.pasapalabra.game.dao.mongodb;

import java.util.Date;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.pasapalabra.game.dao.UserDAO;
import com.pasapalabra.game.model.User;
import com.pasapalabra.game.service.auth.TokenGenerator;

/** User DAO with MongoDB tests.
 * @author Guti
 *
 */
public class UserDAOTest {

	static MongoConnection mongoConnection;
	static User userToTest = new User(TokenGenerator.nextUniqueID().getToken(), "aaaa@aaa.com", "1234", null, new Date(), 0, 0, 0);
	static UserDAO userDAO;
	
	@BeforeClass
	public static void startConnection() {
		// Creates connection to DB.
		MongoClientOptions.Builder options = MongoClientOptions.builder();
    	options.socketKeepAlive(true);
    	MongoClient mongoClient = new MongoClient("127.0.0.1:27017", options.build());
    	mongoConnection = new MongoConnection("com.pasapalabra.game.model", mongoClient, "pasapalabra");
    	
    	
    	// Instanciates DAO
    	userDAO = new UserMongoDAO(mongoConnection);
    	
    	
    	// Inserts needed data.
    	mongoConnection.datastore.save(userToTest);
	}
	
	@Test
	public void getUserByLogin() {
		User user = userDAO.getUserByLogin(userToTest.getUserName(), userToTest.getPass());
		assertNotEquals(null, user);
		assertEquals(userToTest.getUserName(), user.getUserName());
		assertEquals(userToTest.getPass(), user.getPass());
		
		
		// Warning: Take into account that this test could fail in a very low probability.
		assertEquals(null, userDAO.getUserByLogin(TokenGenerator.nextUniqueID().getToken(), userToTest.getPass()));
		
		assertEquals(null, userDAO.getUserByLogin(userToTest.getUserName(), TokenGenerator.nextUniqueID().getToken()));
	}
	
	
	@Test
	public void checkIfExists() {
		// Checks if exist the user saved before.
		assertEquals(true, userDAO.checkIfExists(userToTest.getUserName()));
		
		
		// Checks if exists with random username.
		// Warning: Take into account that this test could fail in a very low probability.
		assertEquals(false, userDAO.checkIfExists(TokenGenerator.nextUniqueID().getToken()));
	}
	
	
	@Test
	public void updateScore() {
		User user = null;
		int won = userToTest.getGamesWon();
		int lost = userToTest.getGamesLost();
		
		// Checking won +1
		userDAO.updateScore(userToTest, true);
		won++;
		user = userDAO.getUserByLogin(userToTest.getUserName(), userToTest.getPass());
		assertEquals(won, user.getGamesWon());
		
		// Checking lost +1
		userDAO.updateScore(userToTest, false);
		lost++;
		user = userDAO.getUserByLogin(userToTest.getUserName(), userToTest.getPass());
		assertEquals(lost, user.getGamesLost());
	}
	
	
	@AfterClass
	public static void closeConnection() {
		mongoConnection.datastore.delete(userToTest);
		mongoConnection.close();
	}
}
