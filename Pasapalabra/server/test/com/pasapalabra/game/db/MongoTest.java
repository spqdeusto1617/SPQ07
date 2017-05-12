package com.pasapalabra.game.db;

import java.util.Date;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteResult;
import com.pasapalabra.game.dao.mongodb.MongoConnection;
import com.pasapalabra.game.model.User;
import com.pasapalabra.game.service.auth.TokenGenerator;

public class MongoTest {
	
	static MongoConnection mongoConnection;
	User userToTest = new User(TokenGenerator.nextUniqueID().getToken(), "aaaa@aaa.com", "1234", null, new Date(), 0, 0, 0);
	static User userToTestDeleteOp = new User(TokenGenerator.nextUniqueID().getToken(), "aaaa@aaa.com", "1234", null, new Date(), 0, 0, 0);
	static WriteResult deleteWriteResult = null;
	
	@BeforeClass
	public static void startConnection() {
		MongoClientOptions.Builder options = MongoClientOptions.builder();
    	options.socketKeepAlive(true);
    	MongoClient mongoClient = new MongoClient("127.0.0.1:27017", options.build());
    	mongoConnection = new MongoConnection("com.pasapalabra.game.model", mongoClient, "pasapalabra"); 
    	
    	mongoConnection.datastore.save(userToTestDeleteOp);
    	deleteWriteResult = mongoConnection.datastore.delete(userToTestDeleteOp);
	}
	
	
	@Test
	public void insert() {
		mongoConnection.datastore.save(userToTest);
	}
	
	
	@Test
	public void update() {
		userToTest.setPass(TokenGenerator.nextUniqueID().getToken());
		assertNotEquals(null, mongoConnection.datastore.save(userToTest));
	}
	
	
	@Test
	public void delete() {
		assertEquals(1, deleteWriteResult.getN());
	}
	
	
	@AfterClass
	public static void closeConnection() {
		mongoConnection.close();
	}
	
}
