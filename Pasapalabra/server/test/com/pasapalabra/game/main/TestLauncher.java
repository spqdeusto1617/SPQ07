package com.pasapalabra.game.main;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;

import org.bson.Document;
import org.databene.contiperf.junit.ContiPerfSuiteRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pasapalabra.game.dao.mongodb.QuestionDAOTest;
import com.pasapalabra.game.dao.mongodb.UserDAOTest;
import com.pasapalabra.game.db.MongoTest;
import com.pasapalabra.game.server.Server;
import com.pasapalabra.game.service.ServiceAccountCheckTest;
import com.pasapalabra.game.service.ServiceAccountModifyTest;
import com.pasapalabra.game.service.ServiceGameTest;
import com.pasapalabra.game.service.ServiceLoginTest;
import com.pasapalabra.game.service.ServiceRegisterTest;

@RunWith(ContiPerfSuiteRunner.class)
@SuiteClasses({
	MongoTest.class,
	QuestionDAOTest.class, UserDAOTest.class,
	ServiceRegisterTest.class, ServiceLoginTest.class, ServiceAccountCheckTest.class,
	ServiceAccountModifyTest.class,
	ServiceGameTest.class
})
public class TestLauncher {

	public static final String DB_NAME = "pasapalabra_test";
	
	public static final String SERVER_HOST = "127.0.0.1";
	public static final String SERVER_PORT = "1099";
	public static final String SERVER_SERVICE_NAME = "Pasapalabra";

	private static Thread serverThread;

	private static MongoClient mongoClient = new MongoClient();
	private static MongoDatabase db = mongoClient.getDatabase(DB_NAME);
	private static MongoCollection<Document> questionCollection = db.getCollection("question");

	public static void insertToDB(char letter) {
		questionCollection.insertOne(
				new Document()
				.append("question", "Question")
				.append("answer", "Answer")
				.append("questionType", "All")
				.append("letter", letter)
				.append("creator", "Unknown")
				.append("answered", false)
				); //TODO There are more values from morphia to add.
	}

	@BeforeClass
	public static void prepare() {
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		// Inserts all data
		for(char letter = 'a'; letter <= 'z'; letter++)
			insertToDB(letter);
		insertToDB('ñ');
		
		
		// RMI Server startup
		
		//File projectBase = new File(TestLauncher.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
	
		serverThread = new Thread(new Runnable() {			
			@Override
			public void run() {
				//System.setProperty("java.rmi.server.codebase",  "file:" + projectBase.getAbsolutePath() + "/");
				System.setProperty("java.security.policy", "security/java.policy");
				java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
				try {
					java.rmi.registry.LocateRegistry.createRegistry(1099);
					System.out.println("RMI registry ready.");
				} catch (Exception e) {
					System.out.println("Exception starting RMI registry:");
					e.printStackTrace();
				}
				Server.main(new String[]{SERVER_HOST, SERVER_PORT, SERVER_SERVICE_NAME, DB_NAME});
			}
		});	

		serverThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {

			}
		});

		serverThread.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		
	}


	@AfterClass
	public static void cleanAndStopServer() {
		questionCollection.drop();
		db.getCollection("user").drop();
		
		try{serverThread.interrupt();}catch(Exception e){};
	}
	
}