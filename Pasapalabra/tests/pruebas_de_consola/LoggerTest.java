package pruebas_de_consola;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

import application.Main;

public class LoggerTest {

	public static Logger log = Logger.getLogger(LoggerTest.class.getName());
	
	public static void main(String[] args) {
		logExists();
		log.log(Level.INFO, "Empieza");
		System.out.println("Hola");
		log.log(Level.INFO, "Ha imprimido hola");
		int i = 0;
		log.log(Level.INFO, "i tiene ahora valor 0");
		if(i == 0){
			log.log(Level.INFO, "i es 0");
			
		}else{
			log.log(Level.INFO, "i no es 0");
			
			
		}
		
		System.out.println("Terminado!");
	}
	
	
	private static Boolean logExists(){
		  try {
			  FileHandler handler = null;
			if(System.getProperty("os.name").startsWith("Windows")){
			    handler = new FileHandler("a",true);
			}else if(System.getProperty("os.name").startsWith("Mac")){
				//Habra una excepcion si no existe la carpeta Pasapalabra y Logs entonces se hace un try catch y en el catch se crea.
				//Para windows lo mismo
			    handler = new FileHandler("/Users/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+"LoggerTest"+".xml",true);
			}else{
				
				
				
			}
		    handler.setFormatter(new XMLFormatter());
		    log.setUseParentHandlers(false);
		    log.addHandler(handler);
		    
		  }
		 catch ( Exception e ) {
			 System.out.println(e);
		    log.log(Level.FINEST,"Ya hay un archivo log de " + Main.class.getName());
		    
		    return false;
		  }
		  return true;
		}
}
