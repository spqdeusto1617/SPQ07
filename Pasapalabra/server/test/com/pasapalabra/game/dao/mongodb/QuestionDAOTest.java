package com.pasapalabra.game.dao.mongodb;

import static org.junit.Assert.assertNotEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.pasapalabra.game.dao.QuestionDAO;
import com.pasapalabra.game.main.TestLauncher;
import com.pasapalabra.game.model.Question;

/** Question DAO with MongoDB tests
 * @author Guti
 *
 */
public class QuestionDAOTest {

	static MongoConnection mongoConnection;
	static Question question1 = new Question("A", "A", 'A', "system");
	static Question question2 = new Question("B", "B", 'A', "system");
	static QuestionDAO questionDAO;
	
	@BeforeClass
	public static void startConnection() {
		// Creates connection to DB.
		MongoClientOptions.Builder options = MongoClientOptions.builder();
    	options.socketKeepAlive(true);
    	MongoClient mongoClient = new MongoClient("127.0.0.1:27017", options.build());
    	mongoConnection = new MongoConnection("com.pasapalabra.game.model", mongoClient, TestLauncher.DB_NAME);
    	
    	
    	// Instanciates DAO
    	questionDAO = new QuestionMongoDAO(mongoConnection);
    	
    	
    	// Inserts needed data.
    	mongoConnection.datastore.save(question1);
    	mongoConnection.datastore.save(question2);
	}
	
	@Test
	public void getQuestionByLetter() {
		int size = 1000;
		Question[] questions = new Question[size];
		for(int i = 0; i < size; i++) {
			questions[i] = questionDAO.getRandomQuestionByLeter('A');
		}
		
		int aValues = 0;
		
		for(int i = 0; i < size; i++) {
			if(questions[i].getQuestion().equals("A"))
				aValues++;
		}
		
		// Checks if returns something
		assertNotEquals(null, questionDAO.getRandomQuestionByLeter('A'));
		
		// Checks if it is a random retrieve
		assertNotEquals(0, size-aValues);
	}
	
	
	@AfterClass
	public static void closeConnection() {
		mongoConnection.datastore.delete(question1);
		mongoConnection.datastore.delete(question2);
		mongoConnection.close();
	}
}
