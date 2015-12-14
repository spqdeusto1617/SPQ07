package principal;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
	public final Connection conexionBDUsuarios = null;
	public final Connection conexionBDPreguntas = null;
	
	@SuppressWarnings("resource")
	public static void main(String args[]) {
		final int portNumber = 1025; //TODO Revisar si existen apps importantes que puedan utilizar este puerto
		System.out.println("Creando servidor en el puerto:" + portNumber);
		ServerSocket serverSocket = null;
		try{
		serverSocket = new ServerSocket(portNumber);
//		serverSocket.setSoTimeout(120000);//NO ES PARA LO QUE SE CREÍA INICIALMENTE QUE SERVÍA. ESTO SIRVE PARA
		//QUE SI NO LLEGA UNA SOLICITUD EN ESTE TIEMPO, EL SERVIDOR SE APAGA. LO QUE SE CREÍA ERA QUE EL TIMEOUT
		//ERA POR SI UN SOCKET DEJABA DE CONTESTAR. SE TENDRÁ QUE VER CÓMO HACER ESO ENTONCES.
		}catch(Exception e){
			e.printStackTrace();
		}
		
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Thread(new TratamientoBienvenida(socket)).run();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
	}
	
}