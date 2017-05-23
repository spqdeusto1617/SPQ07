package com.pasapalabra.game.service;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.pasapalabra.game.main.TestLauncher;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.service.auth.TokenGenerator;
import static org.junit.Assert.assertEquals;

public class ServiceRegisterTest {
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
	
	
	@BeforeClass
	public static void startClient(){
		
		try {
			String URL = "//" + TestLauncher.SERVER_HOST + ":" + TestLauncher.SERVER_PORT + "/" + TestLauncher.SERVER_SERVICE_NAME;
			ppService = (IPasapalabraService) Naming.lookup(URL);
		} catch (Exception e) {
			throw new RuntimeException("Error connecting to the server", e);
		}
		
	}
	
	
	@Test
	public void register() {
		try {
			registrationSucceded = ppService.registry(userToTest,userPassword);
		} catch (RemoteException e) {
			throw new RuntimeException("Registration failed", e);
		}
		assertEquals(true, registrationSucceded);
	}
}
