package com.pasapalabra.game.service;

import static org.junit.Assert.assertEquals;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.pasapalabra.game.main.TestLauncher;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.service.auth.Token;
import com.pasapalabra.game.service.auth.TokenGenerator;

public class ServiceAccountModifyTest {

	public static IPasapalabraService ppService;

	private static boolean registrationSucceded = false;
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
	public static void startClient(){

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

		if(!registrationSucceded)
			throw new RuntimeException("Registration failed");

	}


	@Before
	public void login() {
		try {
			userToken = ppService.login(userToTest.getUserName(), userPassword);
		} catch (RemoteException e) {
			throw new RuntimeException("Login failed", e);
		}

		if(userToken == null)
			throw new RuntimeException("Login failed");
	}


	@Test
	@PerfTest(invocations = 200)
	@Required(max = 2000, average = 350)
	public void modifyUserMail() {
		userToTest.setMail(TokenGenerator.nextUniqueID().getToken());
		boolean modificationSucceded = false;
		try {
			modificationSucceded = ppService.changeUserMail(userToken, userPassword, userToTest.getMail());
		} catch (Exception e) {
			throw new RuntimeException("Email modification failed", e);
		}
		assertEquals(true, modificationSucceded);
	}
	
	
	@Test
	public void modifyUserPassword() {
		String previousPassword = userPassword;
		userPassword = TokenGenerator.nextUniqueID().getToken();
		boolean modificationSucceded = false;
		try {
			modificationSucceded = ppService.changeUserPass(userToken, previousPassword, userPassword);
		} catch (Exception e) {
			throw new RuntimeException("Password modification failed", e);
		}
		assertEquals(true, modificationSucceded);
	}
	
	
	@Test
	@PerfTest(invocations = 200)
	@Required(max = 2000, average = 350)
	public void modifyUserImage() {
		userToTest.setProfileImage(TokenGenerator.nextUniqueID().getToken());
		boolean modificationSucceded = false;
		try {
			modificationSucceded = ppService.changeUserIMG(userToken, userPassword, userToTest.getProfileImage());
		} catch (Exception e) {
			throw new RuntimeException("ProfileImage modification failed", e);
		}
		assertEquals(true, modificationSucceded);
	}
	
}
