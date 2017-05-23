package com.pasapalabra.game.service;

import static org.junit.Assert.assertNotEquals;

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

public class ServiceLoginTest {

	public static IPasapalabraService ppService;
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
			if(!ppService.registry(userToTest,userPassword))
				throw new RuntimeException("Registration failed");
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
	@PerfTest(invocations = 200)
	@Required(max = 2000, average = 350)
	public void login() {
		assertNotEquals(null, userToken);
	}

	@Test
	@PerfTest(invocations = 200)
	@Required(max = 2000, average = 350)
	public void logout() {
		try {
			ppService.deLogin(userToken);
		} catch (RemoteException e) {
			throw new RuntimeException("Logout failed", e);
		}
	}

}
