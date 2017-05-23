package com.pasapalabra.game.service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ServiceLocator{

	public static IPasapalabraService service;

	
	//public static boolean gameEnd = false;



	public static void startConnection(String[] args)
			throws MalformedURLException, RemoteException, NotBoundException {

		if(args.length == 0){
			System.err.println("No arguments passed");
			//System.exit(0);
		}

		try {
			String URL = "//" + args[0] + ":" + args[1] + "/" + args[2];
			ClientConnection.cService = new ClientService(args[2]);
			service = (IPasapalabraService) Naming.lookup(URL);
			ClientConnection.serverReady = true;
		} catch (Exception e) {
			System.err.println(" *# Error connecting to the server : " + e.getMessage());
			ClientConnection.serverReady = false;
			throw e;
		}

	}
	
}