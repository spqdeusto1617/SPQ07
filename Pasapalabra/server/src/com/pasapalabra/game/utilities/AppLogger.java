package com.pasapalabra.game.utilities;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**Clase para de utilidad para crear un archivo log en las demás clases.
 * Parece que está adaptada solamente para ventanas pero está más que preparada para
 * hacer frente a cualquier tipo de código java, sea o no ventana.
 * He usado la palabra window para que sea más sencillo el encontrar el método
 * cuando se programa.
 * @author asier.gutierrez
 *
 */
public class AppLogger {

	//Por defecto en una clase sería algo así.
	public static Logger log = Logger.getLogger(AppLogger.class.getName());

	//Para evitar que en cada clase se tenga que hacer esto desarrollamos este método.
	/**Método para getear el Logger de la clase teniendo el nombre de la propia clase.
	 * El por qué de esto es que así se puede loggear si se ha podido o no obtener el logger.
	 * Es decir, según la aplicación se encienda ya empieza el log muchísimo antes.
	 * @param nombreClase el nombre que tiene la clase en la que buscar el Logger.
	 * @return El Logger de la clase que se ha indicado.
	 */
	public static Logger getWindowLogger(String nombreClase){
		return Logger.getLogger(nombreClase);
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
	public static Boolean crearLogHandler(Logger wLogger, String nombreClase){
		try {
			//Creamos un handler nulo
			FileHandler handler = null;

			//Dependiendo del sistema operativo lo guardaremos en un sitio u otro.
			//Habra una excepcion si no existe la carpeta Pasapalabra y Logs entonces se hace un try catch y en el catch se crea.
			//Para todos los SO igual.
			if(System.getProperty("os.name").startsWith("Windows")){
				//WINDOWS

				try{
					handler = new FileHandler("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\Logs\\"+nombreClase+".xml",true);
				}catch(Exception winEx){
					new File("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\Logs\\").mkdirs();
					handler = new FileHandler("C:\\Users\\"+System.getProperty("user.name")+"\\Pasapalabra\\Logs\\"+nombreClase+".xml",true);
					//Si ya no funciona lo dejamos.
				}


			}else if(System.getProperty("os.name").startsWith("Mac")){
				//MAC

				try{
					handler = new FileHandler("/Users/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+nombreClase+".xml",true);
				}catch(Exception macEx){
					new File("/Users/"+System.getProperty("user.name")+"/Pasapalabra/Logs/").mkdirs();
					handler = new FileHandler("/Users/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+nombreClase+".xml",true);
					//Si ya no funciona lo dejamos.
				}

			}else{
				//LINUX y si no pues se hará mal y saltará una excepción...

				try{
					handler = new FileHandler("/home/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+nombreClase+".xml",true);
				}catch(Exception linEx){
					new File("/home/"+System.getProperty("user.name")+"/Pasapalabra/Logs/").mkdirs();
					handler = new FileHandler("/home/"+System.getProperty("user.name")+"/Pasapalabra/Logs/"+nombreClase+".xml",true);
					//Si ya no funciona lo dejamos.
				}

			}

			//Una vez tenemos ya el handler bien formado le ponemos un formateador XML
			handler.setFormatter(new XMLFormatter());

			//No queremos padres de handlers
			wLogger.setUseParentHandlers(false);

			//Le añadimos al logger su respectivo handler.
			wLogger.addHandler(handler);

		}
		catch ( Exception e ) {
			//Si hay excepción, aunque el handler no funcione loggeamos. Más no podemos hacer. [Debido al presupuesto, claro]
			System.out.println(e);
			wLogger.log(Level.FINEST,"Ya hay un archivo log de Main");

			//Si se ha creado mal return -> false
			return false;
		}
		//Si se ha creado bien return -> true
		return true;
	}
}
