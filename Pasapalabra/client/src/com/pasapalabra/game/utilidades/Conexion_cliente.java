package com.pasapalabra.game.utilidades;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.service.auth.Token;


/**Clase de utilidad para realizar la conexión del cliente con el servidor
 * @author Iván
 *Tiene un solo método(lanzaConexión) que sirve para realizar la conexión con el servidor, y hacerle consultas sobre lo que el cliente necesite. 
 */
public class Conexion_cliente {
	
	private static IPasapalabraService service;
	
	public static Token sessionAuth;
	
	public static UserDTO userInfo;
	
	public static void startConnection(String[] args)
			throws MalformedURLException, RemoteException, NotBoundException {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			String URL = "//" + args[0] + ":" + args[1] + "/" + args[2];
			service = (IPasapalabraService) Naming.lookup(URL);
			
		} catch (Exception e) {
			System.err.println(" *# Error connecting to Donation Collector: " + e.getMessage());
			e.printStackTrace();
		}

		}
	
}