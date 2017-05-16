package com.pasapalabra.game.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.pasapalabra.game.main.TestLauncher;
//import com.pasapalabra.game.main.TestLauncher;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.service.auth.Token;
import com.pasapalabra.game.service.auth.TokenGenerator;

public class ServiceBasicTest {
	
	public static IPasapalabraService ppService;
	
	private static boolean registrationSucceded = false;
	private static boolean logoutSucceded = false;
	
	private static UserDTO userToTest = new UserDTO(
			TokenGenerator.nextUniqueID().getToken(),
			TokenGenerator.nextUniqueID().getToken()+"@aa.com",
			null,
			new Date(),
			0,
			0
			);
	private static String userPassword = TokenGenerator.nextUniqueID().getToken();
	private static Token userToken = null;
	
	
	@BeforeClass
	public static void startServer(){
		
		try {
			String URL = "//" + TestLauncher.SERVER_HOST + ":" + TestLauncher.SERVER_PORT + "/" + TestLauncher.SERVER_SERVICE_NAME;
			ppService = (IPasapalabraService) Naming.lookup(URL);
		} catch (Exception e) {
			throw new RuntimeException("Error connecting to the server", e);
		}
		
		try {
			registrationSucceded = ppService.registry(userToTest,userPassword);
		} catch (RemoteException e) {
			throw new RuntimeException("Registration failed", e);
		}
		
	}
	
	
	@Before
	public void loginHook() {
		try {
			userToken = ppService.login(userToTest.getUserName(), userPassword);
		} catch (RemoteException e) {
			throw new RuntimeException("Login failed", e);
		}
	}
	
	
	@Test
	public void register() {
		assertEquals(true, registrationSucceded);
	}
	
	
	@Test
	@Required(max = 100)
	public void login() {
		Token fakeUser = null;
		try {
			fakeUser = ppService.login(TokenGenerator.nextUniqueID().getToken(), TokenGenerator.nextUniqueID().getToken());
		} catch (RemoteException e) {
			throw new RuntimeException("Login failed", e);
		}
		
		assertNotEquals(null, userToken);
		assertEquals(null, fakeUser);
	}
	
	@Test
	@PerfTest(invocations = 200)
	@Required(max = 2000, average = 350)
	public void getData() {
		UserDTO userData = null;
		
		try {
			userData = ppService.getData(userToken);
		} catch(Exception e) {
			throw new RuntimeException("Data retrieving failed", e);
		}
		
		assertNotEquals(null, userData);
		
		boolean isEqual = userData.getDOB().equals(userToTest.getDOB())
				&& userData.getUserName().equals(userToTest.getUserName())
				&& userData.getMail().equals(userToTest.getMail())
				&& userData.getProfileImage() == userToTest.getProfileImage()
				&& userData.getGamesWon() == userToTest.getGamesWon()
				&& userData.getGamesLost() == userToTest.getGamesLost();
		assertEquals(true, isEqual);
	}
	
	
	@After
	public void logoutHook(){
		try{
			ppService.deLogin(userToken);
		}catch(Exception e) {
			throw new RuntimeException("Logout failed", e);
		}
	}
	
	
	@AfterClass
	public static void stopServer(){ }

}
