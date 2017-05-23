package helpers;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.pasapalabra.game.model.DTO.UserDTO;
import com.pasapalabra.game.model.DTO.UserScoreDTO;
import com.pasapalabra.game.service.IClientService;

public class ClientService extends UnicastRemoteObject implements IClientService, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientService() throws RemoteException { }
	
	@Override
	public void getUser(UserDTO user) throws RemoteException { }
	
	@Override
	public void answered(char letter, boolean result) throws RemoteException { }
	
	@Override
	public void finalResult(UserScoreDTO score) throws RemoteException { }
	
	@Override
	public void changeTurn(UserScoreDTO score) throws RemoteException { }
	
	@Override
	public void otherDisconnected() throws RemoteException { }

}
