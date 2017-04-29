package com.pasapalabra.game.utilities;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**Utility class to create a log file in the other classes.
 * It seems that it is adapted for the windows only, but it is more than ready for
 * dealing with any type of java code, whether is a window or not.
 * I used the word window to make it easier to find the method when programmed.
 * @author asier.gutierrez
 */
public class AppLogger {

	//By default, it will be like this.
	public static Logger log = Logger.getLogger(AppLogger.class.getName());
	//To not having to create this in all the classes, we create this method.
	/**Method to get the logger for the class, by getting the class name.
	 * This is because we can log if we got or not the logger.
	 * That mean that the logger can start way earlier that the app.
	 * @param className the class name that the logger indicated.
	 * @return The logger of the indicated class.
	 */
	public static Logger getWindowLogger(String className){
		return Logger.getLogger(className);
	}


	/**Método para crear un handler dado un Logger.
	 * 
	 * Se podría usar la propiedad del sistema user.home pero es mejor tenerlo modular.
	 * Cual es el problema? 
	 * 
	 * ->En todos los sistemas operativos puedes cambiar el directorio de usuario,
	 * pero bueno... con el método .mkdirs(); crea ya las carpetas necesarias para
	 * llegar al directorio especificado.
	 * 
	 * @param wLogger el logger de la clase de la cual queremos crear un handler.
	 * @return true/false dependiendo de si se ha podido crear o no. Generalmente siempre se va a poder.
	 */
	public static Boolean crearLogHandler(Logger wLogger, String className){
		try {
			//We create a null handler
			FileHandler handler = null;

			//Depending of the OS, we store the log in diferent places.
			//If the folder: "Pasapalabra" does not exit, the application enters in a catch, where the folder is created.
			//For all the OS is equally done.
			if(System.getProperty("os.name").startsWith("Windows")){
				//WINDOWS

				try{
					handler = new FileHandler("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\Logs\\"+className+".xml",true);
				}catch(Exception winEx){
					new File("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\Logs\\").mkdirs();
					handler = new FileHandler("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\Logs\\"+className+".xml",true);
					//If it does not work, we stop it.
				}


			}else if(System.getProperty("os.name").startsWith("Mac")){
				//MAC

				try{
					handler = new FileHandler("/Users/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+className+".xml",true);
				}catch(Exception macEx){
					new File("/Users/"+System.getProperty("user.name")+"/Pasapalabra/Logs/").mkdirs();
					handler = new FileHandler("/Users/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+className+".xml",true);
					//If it does not work, we stop trying.
				}

			}else{
				//LINUX and if not, an Exception is thrown

				try{
					handler = new FileHandler("/home/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+className+".xml",true);
				}catch(Exception linEx){
					new File("/home/"+System.getProperty("user.name")+"/Pasapalabra/Logs/").mkdirs();
					handler = new FileHandler("/home/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+className+".xml",true);
					//If it does not work, we stop trying.
				}

			}

			//When we have a logger properly done, we create a handler and we set the format.
			handler.setFormatter(new XMLFormatter());

			//We don´t want handler´s parents.
			wLogger.setUseParentHandlers(false);

			//We add the logger to the respective handler.
			wLogger.addHandler(handler);

		}
		catch ( Exception e ) {
			//If there is an exception, even if the handler does not work, we log. We can´t do anymore. [Due to budget, of course]
			System.out.println(e);
			wLogger.log(Level.FINEST,"There is already a log file from the main class");

			//If it was created wrong, return -> false
			return false;
		}
		//If it was created right, return -> true
		return true;
	}
}
