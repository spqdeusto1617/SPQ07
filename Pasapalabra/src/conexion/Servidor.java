package conexion;

import java.net.Socket;

import com.google.api.services.drive.Drive;

public class Servidor {
	public static Socket s = null;
	
	/**
	   * Download a file's content.
	   *
	   * @param service Drive API service instance.
	   * @param file Drive File instance.
	   * @return InputStream containing the file's content if successful,
	   *         {@code null} otherwise.
	   */
//	  private static InputStream downloadFile(Drive service, File file) {
//	    if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
//	      try {
//	        // uses alt=media query parameter to request content
//	        return service.files().get(file.getId()).executeMediaAsInputStream();
//	      } catch (IOException e) {
//	        // An error occurred.
//	        e.printStackTrace();
//	        return null;
//	      }
//	    } else {
//	      // The file doesn't have any content stored on Drive.
//	      return null;
//	    }
//	  }
//
//	  // ...
//	File file = new File();
//	String downloadUrl = file.getExportLinks().get("application/pdf");
	//api key: AIzaSyDn3-j_Tl8KnbLGBWcBTz_zs1qWGhYmCb4
	//request GET https://www.googleapis.com/drive/v2/files/0BxiFI5_jdiUPWW85OUZsTzFfV1k?key={YOUR_API_KEY}
	
//	
//	class CustomProgressListener implements MediaHttpDownloaderProgressListener {
//		  public void progressChanged(MediaHttpDownloader downloader) {
//		    switch (downloader.getDownloadState()) {
//		      case MEDIA_IN_PROGRESS:
//		        System.out.println(downloader.getProgress());
//		        break;
//		      case MEDIA_COMPLETE:
//		        System.out.println("Download is complete!");
//		    }
//		  }
//		}
//
//		OutputStream out = new FileOutputStream("/tmp/driveFile.jpg");
//
//		DriveFiles.Get request = drive.files().get(fileId);
//		request.getMediaHttpDownloader().setProgressListener(new CustomProgressListener());
//		request.executeMediaAndDownloadTo(out);
	
//	public static String obtenerIP() throws IOException{
//   	 // Make a URL to the web page
//       URL url = new URL("http://pastebin.com/2hKudz5e");
//
//       // Get the input stream through URL Connection
//       URLConnection con = url.openConnection();
//       InputStream is =con.getInputStream();
//
//       // Once you have the Input Stream, it's just plain old Java IO stuff.
//
//       // For this case, since you are interested in getting plain-text web page
//       // I'll use a reader and output the text content to System.out.
//
//       // For binary content, it's better to directly read the bytes from stream and write
//       // to the target file.
//
//
//       BufferedReader br = new BufferedReader(new InputStreamReader(is));
//       
//       String line = null;
//       String sToCompare;
//       int completed = 0;
//       // read each line and write to System.out
//       while ((line = br.readLine()) != null) {
//       	StringTokenizer st = new StringTokenizer(line, "&gt;");
//           while (st.hasMoreTokens()) {
//           	if (completed == 3) return null;
//           	sToCompare = st.nextToken();
//               if(sToCompare.startsWith("VAIP")){
//               	sToCompare = sToCompare.substring(4);
//               	//System.out.println("Server IP--------------->"+sToCompare);
//               	completed++;
//               	return sToCompare;
//               }else if(sToCompare.startsWith("VAST")){
//               	sToCompare = sToCompare.substring(4);
//               	System.out.println("Status--------------->"+sToCompare);
//               	completed++;
//               }else if(sToCompare.startsWith("VARV")){
//               	sToCompare = sToCompare.substring(4);
//               	System.out.println("Required version--------------->"+sToCompare);
//               	completed++;
//               }
//           }
//       	
//           
//       }
//       System.out.println("He terminado");
//	return null;
//   }
	
	
	
	
	
	
	
	
	
	
}
