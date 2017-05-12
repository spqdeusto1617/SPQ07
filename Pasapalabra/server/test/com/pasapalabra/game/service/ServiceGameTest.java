package com.pasapalabra.game.service;

import static org.junit.Assert.assertEquals;

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
import com.pasapalabra.game.model.QuestionType;
import com.pasapalabra.game.model.DTO.QuestionDTO;
import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.service.auth.Token;
import com.pasapalabra.game.service.auth.TokenGenerator;

import helpers.ClientService;

public class ServiceGameTest {
	private static IPasapalabraService ppService;
	private static boolean registrationSucceded = false;
	private static boolean deLoginSucceded = false;
	private static UserDTO user1 = new UserDTO(
			TokenGenerator.nextUniqueID().getToken(),
			TokenGenerator.nextUniqueID().getToken()+"@aa.com",
			null,
			new Date(),
			0,
			0
			);
	private static UserDTO user2 = new UserDTO(
			TokenGenerator.nextUniqueID().getToken(),
			TokenGenerator.nextUniqueID().getToken()+"@aa.com",
			null,
			new Date(),
			0,
			0
			);
	private static String user1Password = TokenGenerator.nextUniqueID().getToken();
	private static String user2Password = TokenGenerator.nextUniqueID().getToken();
	private static Token user1Token = null;
	private static Token user2Token = null;
	private static IClientService service1;
	private static IClientService service2;
	private static UserScoreDTO userScore = null;

	@BeforeClass
	public static void startServer(){
		
		try {
			String URL = "//" + TestLauncher.SERVER_HOST + ":" + TestLauncher.SERVER_PORT + "/" + TestLauncher.SERVER_SERVICE_NAME;
			ppService = (IPasapalabraService) Naming.lookup(URL);
		} catch (Exception e) {
			throw new RuntimeException("Error connecting to the server", e);
		}
		
		try {
			service1 = new ClientService();
		}catch(Exception e){
			throw new RuntimeException("Error instanciating ClientService", e);
		};

		try {
			service2 = new ClientService();
		}catch(Exception e){
			throw new RuntimeException("Error instanciating ClientService", e);
		};

		try {
			registrationSucceded = ppService.registry(user1,user1Password);
			assertEquals(true, registrationSucceded);
			registrationSucceded = ppService.registry(user2,user2Password);
			assertEquals(true, registrationSucceded);
		} catch (RemoteException e) {
			throw new RuntimeException("Registration failed", e);
		}

	}

	@Before
	@Required(max = 120, average = 30)
	public void login() {
		try {
			user1Token = ppService.login(user1.getUserName(), user1Password);
			user2Token = ppService.login(user2.getUserName(), user2Password);
		} catch (RemoteException e) {
			throw new RuntimeException("Login failed", e);
		}
	}

	@Test
	@Required(max = 100, average = 20)
	public void exitMatchMaking(){
		UserDTO result = null;
		try{
			result = ppService.play(user1Token, QuestionType.All.toString(), service1);
		}catch (RemoteException e) {
			throw new RuntimeException("Registry failed", e);
		}catch (IllegalArgumentException e) {
			// TODO: handle exception
			throw new RuntimeException("Registry failed", e);
		}
		assertEquals("Wait", result.getUserName());
		try{
			ppService.exitMatchMaking(user1Token, QuestionType.All.toString());
		}catch (RemoteException e) {
			throw new RuntimeException("Login failed", e);
		}
	}

	@Test
	@PerfTest(duration = 2000)
	@Required(throughput = 2)
	public void play(){
		UserDTO result1 = null;
		UserDTO result2 = null;
		QuestionDTO question = null;
		boolean result = false;
		try{
			result1 = ppService.play(user1Token, QuestionType.All.toString(), service1);
		}catch (RemoteException e) {
			throw new RuntimeException("Registry failed", e);
		}catch (IllegalArgumentException e) {
			throw new RuntimeException("Registry failed", e);
		}
		assertEquals("Wait", result1.getUserName());
		try{
			result2 = ppService.play(user2Token, QuestionType.All.toString(), service2);
			assertEquals(user1.getUserName(), result2.getUserName());
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}

		try{
			question = ppService.getQuestion(user2Token);
			assertEquals("Test", question.getQuestion());
			assertEquals('a', question.getLeter());
			assertEquals("Unknown", question.getCreator());
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}

		try{
			result = ppService.answerQuestion(user2Token, "Pasapalabra");
			assertEquals(true, result);
			result = ppService.answerQuestion(user1Token, "Answer");
			assertEquals(false, result);
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}
		try{
			for(char alphabet = 'a'; alphabet <= 'z'; alphabet++ ){
				question = ppService.getQuestion(user2Token);
				result = ppService.answerQuestion(user2Token, Character.toString(alphabet));
			}
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}
		try{
			question = ppService.getQuestion(user2Token);
			assertEquals('a', question.getLeter());
			result = ppService.answerQuestion(user2Token, "Answer");
			assertEquals(true, result);
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}

		try{
			userScore = ppService.getResults(user2Token);
			assertEquals(1, userScore.getRightAnswered());
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}
		try{
			result = ppService.answerQuestion(user1Token, "Answer");
			assertEquals(true, result);
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}
		try{
			for(char alphabet = 'a'; alphabet <= 'z'; alphabet++ ){
				question = ppService.getQuestion(user1Token);
				result = ppService.answerQuestion(user1Token, "Answer");
			}
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}
		try{
			userScore = ppService.getResults(user1Token);
			assertEquals(true, userScore.isVictory());
		}catch (RemoteException e) {
			throw new RuntimeException("Playing failed", e);
		}
	}

	@After
	public void logout(){
		try{
			deLoginSucceded = ppService.deLogin(user1Token);
			deLoginSucceded = ppService.deLogin(user2Token);
		}catch(Exception e) {
			throw new RuntimeException("Data retrieving failed", e);
		}
	}

	@AfterClass
	public static void stopServer(){ }

}
