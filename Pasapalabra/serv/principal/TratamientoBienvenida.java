package principal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TratamientoBienvenida implements Runnable{
	Socket s;
	public TratamientoBienvenida(Socket s){
		this.s = s;
		
	}
	@Override
	public void run(){
		try{
		if(s == null){
			Thread.currentThread().interrupt();
		}
		OutputStream os = s.getOutputStream();
		PrintWriter pw = new PrintWriter(os, true);
		
		pw.println("Hola");
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		if(br.readLine().equalsIgnoreCase("Hola")){
			pw.println("Usuario");
			String usuario = br.readLine();
			pw.println("Contraseña");
			String contrasenya = br.readLine();
//			
//			Sigue aquí
//			
		}else{
			pw.close();
			s.close();
		}
		}catch(Exception e){
			System.out.println("Excepción de Bienvenida");
		}
		
		
//		Hay que usar esto para cerrar la conexión.
//		pw.close();
//		s.close();

	
	}	
	
}
