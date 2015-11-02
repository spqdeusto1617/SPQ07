package conexion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class Servidor {

	public static String obtenerIP() throws IOException{
   	 // Make a URL to the web page
       URL url = new URL("http://pastebin.com/2hKudz5e");

       // Get the input stream through URL Connection
       URLConnection con = url.openConnection();
       InputStream is =con.getInputStream();

       // Once you have the Input Stream, it's just plain old Java IO stuff.

       // For this case, since you are interested in getting plain-text web page
       // I'll use a reader and output the text content to System.out.

       // For binary content, it's better to directly read the bytes from stream and write
       // to the target file.


       BufferedReader br = new BufferedReader(new InputStreamReader(is));
       
       String line = null;
       String sToCompare;
       int completed = 0;
       // read each line and write to System.out
       while ((line = br.readLine()) != null) {
       	StringTokenizer st = new StringTokenizer(line, "&gt;");
           while (st.hasMoreTokens()) {
           	if (completed == 3) return null;
           	sToCompare = st.nextToken();
               if(sToCompare.startsWith("VAIP")){
               	sToCompare = sToCompare.substring(4);
               	//System.out.println("Server IP--------------->"+sToCompare);
               	completed++;
               	return sToCompare;
               }else if(sToCompare.startsWith("VAST")){
               	sToCompare = sToCompare.substring(4);
               	System.out.println("Status--------------->"+sToCompare);
               	completed++;
               }else if(sToCompare.startsWith("VARV")){
               	sToCompare = sToCompare.substring(4);
               	System.out.println("Required version--------------->"+sToCompare);
               	completed++;
               }
           }
       	
           
       }
       System.out.println("He terminado");
	return null;
   }
}
