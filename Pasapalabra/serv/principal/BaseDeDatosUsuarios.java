package principal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseDeDatosUsuarios {
	public static final String nombreBaseDatos = "dbUsuarios";
	public static Connection c = null;
	public static Statement s = null;
	
	public static void iniciarConexion(){
	
		Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:Usuarios.db");
	      s = c.createStatement();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Conexión con base de datos abierta correctamente");
	
	
	}
	
	public static boolean cerrarConexion(){
		try {
			s.close();
			System.out.println(c);
			c.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static void crearTablaBD() {
		Connection c = null;
	    Statement s = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:Usuarios.db");
	      System.out.println("Opened database successfully");

	      s = c.createStatement();
	      String sql =  "CREATE TABLE USUARIOS"
	    	      + "("
	    	      + "NOMBRE_USUARIO VARCHAR(16) NOT NULL PRIMARY KEY,"
	    	      + "ID INTEGER NOT NULL UNIQUE,"
	    	      + "EMAIL VARCHAR(64) NOT NULL UNIQUE,"
	    	      + "PASSWORD VARCHAR(16) NOT NULL,"
	    	      + "FECHA_DE_NACIMIENTO DATE NOT NULL,"
	    	      + "PATH_TO_IMAGE VARCHAR(128),"
	    	      + "PARTIDAS_GANADAS INTEGER DEFAULT 0 CHECK(PARTIDAS_GANADAS>=0),"
	    	      + "PARTIDAS_PERDIDAS INTEGER DEFAULT 0 CHECK(PARTIDAS_PERDIDAS>=0),"
	    	      + "PARTIDAS_EMPATADAS INTEGER DEFAULT 0 CHECK(PARTIDAS_EMPATADAS>=0)"
	    	      + ") "; 
	      
//	      "CREATE TABLE USUARIOS"
//	      + "("
//	      + "NOMBRE_USUARIO VARCHAR(16) NOT NULL PRIMARY KEY,"
//	      + "ID INTEGER NOT NULL UNIQUE,"
//	      + "EMAIL VARCHAR(64) NOT NULL UNIQUE,"
//	      + "PASSWORD VARCHAR(16) NOT NULL,"
//	      + "FECHA_DE_NACIMIENTO DATE NOT NULL,"
//	      + "PATH_TO_IMAGE VARCHAR(128)"
//	      + "PARTIDAS_GANADAS INTEGER DEFAULT 0 CHECK(PARTIDAS_GANADAS>=0),"
//	      + "PARTIDAS_PERDIDAS INTEGER DEFAULT 0 CHECK(PARTIDAS_PERDIDAS>=0),"
//	      + "PARTIDAS_EMPATADAS INTEGER DEFAULT 0 CHECK(PARTIDAS_EMPATADAS>=0)"
//	      + ") "
	      
	      s.executeUpdate(sql);
	      s.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("Tabla no creada");
	      System.exit(0);
	    }
	    System.out.println("Tabla creada correctamente");
	  
	}
	public static boolean nuevoUsuario(String nombreUsuario,
										String email,
										String password,
										Date fechaNacimiento,
										String path_to_image
										){
		try{
			iniciarConexion();
			//TODO VALIDAR DATOS!
			 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 String datestring = dateFormat.format(fechaNacimiento); 
			 String query = "INSERT INTO USUARIOS "
					+ "(NOMBRE_USUARIO, ID, EMAIL, PASSWORD, FECHA_DE_NACIMIENTO, PATH_TO_IMAGE)"
					+ " VALUES"
					+ "('"
					+nombreUsuario
					+ "',"
					+"(SELECT max(ID) FROM USUARIOS)+1"
					+ ",'"
					+email
					+ "','"
					+password
					+ "','"
					+datestring
					+ "','"
					+path_to_image
					+ "')";
			System.out.println(query);
			s.executeUpdate(query);
			
			cerrarConexion();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			
			return false;
		}
		
	}
	
	public static String[] getUsuarioPorNombreOID(String nombre,String id){
		String[] ret = new String[8];
		String query = null;
		if(nombre != null){
			query = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO ='"+nombre+"'";
		}else if(id != null){
			query = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO ='"+id+"'";
		}else return null;
		try {
			
			ResultSet res = s.executeQuery(query);
			ret[0] = res.getString("NOMBRE_USUARIO");
			ret[1] = res.getString("ID");
			ret[2] = res.getString("EMAIL");
			ret[3] = res.getString("PASSWORD");
			ret[4] = res.getString("FECHA_DE_NACIMIENTO");
			ret[5] = res.getString("PATH_TO_IMAGE");
			ret[6] = res.getString("PARTIDAS_GANADAS");
			ret[7] = res.getString("PARTIDAS_PERDIDAS");
			ret[8] = res.getString("PARTIDAS_EMPATADAS");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ret;
	}
	
	
	public static void main(String[] args) throws SQLException {
		nuevoUsuario("Pepelu", "ihoh@a.com", "1234234545234534", new Date(), "C://micara.png");
		
		
	}

}
