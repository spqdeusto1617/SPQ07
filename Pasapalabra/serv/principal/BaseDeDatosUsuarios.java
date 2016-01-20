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
	public static Connection c ;
	public static Statement s ;

	/**
	 * Método para iniciar la conexión con la BBDD (se ha implementado al iniciar el servidor, para no tener que estar abriendo la conexión todo el rato) 
	 */
	public static void iniciarConexion(){


		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Usuarios.db");
			s = c.createStatement();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//System.out.println("Conexión con base de datos abierta correctamente");


	}

	/**Método para cerrar la conexión con la base de datos
	 * @return
	 */
	public static boolean cerrarConexion(){
		try {
			s.close();

			c.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static void crearTablaBD() {

		try {

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
			//	System.out.println("Tabla no creada");
			System.exit(0);
		}
		//System.out.println("Tabla creada correctamente");

	}
	/**Método para crear un nuevo usuario
	 * @param nombreUsuario el nombre de usuario
	 * @param email su Mail
	 * @param password su contraseña
	 * @param fechaNacimiento su fecha de nacimiento
	 * @param path_to_image la ruta de suu imagen
	 * @throws SQLException si hay algún error (generalmente, si el Nombre de usuario existe, o el Mail)
	 */
	public static void nuevoUsuario(String nombreUsuario,
			String email,
			String password,
			Date fechaNacimiento,
			String path_to_image) throws SQLException{

		try{
			String query;
			String num_id;

			//iniciarConexion(c,s);
			//TODO VALIDAR DATOS!
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String datestring = dateFormat.format(fechaNacimiento); 
			query = "SELECT count(*) FROM USUARIOS";

			ResultSet rs= s.executeQuery(query);
			int	resultado=rs.getInt(1);

			if(resultado==0){

				num_id="0";

			}
			else{
				//TODO: ¿parsear el String y añadirle +1?
				num_id="SELECT MAX(ID) FROM USUARIOS";
				rs=s.executeQuery(num_id);
				resultado=rs.getInt(1);
				resultado=resultado+1;
			}



			query = "INSERT INTO USUARIOS "
					+ "(NOMBRE_USUARIO, ID, EMAIL, PASSWORD, FECHA_DE_NACIMIENTO, PATH_TO_IMAGE)"
					+ " VALUES"
					+ "('"
					+nombreUsuario
					+ "',"
					+resultado
					+ ",'"
					+email
					+ "','"
					+password
					+ "','"
					+datestring
					+ "','"
					+path_to_image
					+ "')";
			//System.out.println(query);
			s.executeUpdate(query);



		}catch(Exception e){

			try {
				if(e.getMessage().endsWith("USUARIOS.NOMBRE_USUARIO")){

					throw new SQLException("Usuario repetido");

				}
				else if (e.getMessage().endsWith("USUARIOS.EMAIL")){
					throw new SQLException("Email repetido");
				}
				else{
					throw new SQLException(e.getMessage());
				}
			} catch(Error a){

			}
		}

	}

	/** Método para conseguir toda la información de un usario
	 * @param nombre su nombre de usuario
	 * @param id su id
	 * @param contrasenya la contraseña
	 * @return un array de Strings con toda la información
	 * @throws SQLException si hay algún problema (generalmente, que no exista)
	 */
	public static String[] getUsuarioPorNombreOID(String nombre,String id,String contrasenya) throws SQLException{
		String[] ret = new String[10];
		String query = null;
		if(nombre != null){
			query = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO ='"+nombre+"'AND PASSWORD='"+contrasenya+"'";
		}else if(id != null){
			query = "SELECT * FROM USUARIOS WHERE NOMBRE_USUARIO ='"+id+"'AND PASSWORD='"+contrasenya+"'";
		}else throw new SQLException("Nombre e ID vacios");
		try {

			ResultSet res = s.executeQuery(query);
			ret[0] = res.getString("NOMBRE_USUARIO");
			ret[1] = res.getString("ID");
			ret[2] = res.getString("EMAIL");
			ret[3] = res.getString("PASSWORD");
			ret[4] = res.getString("FECHA_DE_NACIMIENTO");
			ret[5] = res.getString("PATH_TO_IMAGE");
			int Partidas_Ganadas=res.getInt("PARTIDAS_GANADAS");
			ret[6] = res.getString("PARTIDAS_GANADAS");
			ret[7] = res.getString("PARTIDAS_PERDIDAS");
			ret[8] = res.getString("PARTIDAS_EMPATADAS");
			ret[9] = Pos_Usuario_Ranking(Partidas_Ganadas);
		} catch (SQLException e) {
			if(e.getMessage().endsWith("USUARIOS.NOMBRE_USUARIO")){
				throw new SQLException("Ese usuario no existe");}
			else{
				throw new SQLException(e.getMessage());
			}
		}

		return ret;
	}

	/**Método para cambiar los datos del usuario, bien el mail, bien la contraña, o bien el path
	 * Para que funcione, se ha de pasar lo que uno quiere cambiar (EJ: pass), y el resto pasar nulls(path y Mail)
	 * @param nombreUsuario:el nombre del usuario a cambiar
	 * @param email: el Email a cambiar
	 * @param password:la contraseña nueva
	 * @param path_to_image: la ruta de la imagens
	 * @throws SQLException: en caso de que algo termine mal
	 */
	public static void cambiarDatosUsuario(String nombreUsuario,
			String email,
			String password,
			String path_to_image) throws SQLException{

		try{
			String query = "";
			

			//iniciarConexion(c,s);
			//TODO VALIDAR DATOS!

			if(path_to_image!=null){
				query="UPDATE USUARIOS SET PATH_TO_IMAGE='"+path_to_image+"' WHERE NOMBRE_USUARIO='"+nombreUsuario+"'";
				
				s.executeUpdate(query);
			}
			else if(password!=null){
				query="UPDATE USUARIOS SET PASSWORD='"+password+"' WHERE NOMBRE_USUARIO='"+nombreUsuario+"'";
				
				s.executeUpdate(query);
			}

			else if(email!=null){
				query="UPDATE USUARIOS SET EMAIL='"+email+"' WHERE NOMBRE_USUARIO='"+nombreUsuario+"'";
				
				s.executeUpdate(query);
			}
			else{
				throw new SQLException("Error con los null");
			}

			
			

		}catch(Exception e){
			e.printStackTrace();
			throw new SQLException("Error con los datos");
		}

	}

	/**Método para eliminar el usuario
	 * @param nombre_usuario el nombre de usuario
	 * @param contrasenya la contraseña (por mayor seguridad)
	 * @throws SQLException si hay algún error
	 */
	public static void eliminar_Usuario(String nombre_usuario,String contrasenya) throws SQLException{
		String query = "DELETE FROM USUARIOS WHERE NOMBRE_USUARIO='"+nombre_usuario+"' AND PASSWORD='"+contrasenya+"'";
		try {
			s.executeUpdate(query);
			
		} catch (SQLException e) {
		
			e.printStackTrace();
			throw new SQLException("Error al eliminar");
		}
	}
	/**Método para obtener la posición en el ranking que ocupa un usuario (relativo a partidas ganadas)
	 * @param Partidas_Ganadas la cantidad de partidas ganadas
	 * @return devuelve los gugadores que tienen más victorias que el usuario +1
	 */
	public static String Pos_Usuario_Ranking(int Partidas_Ganadas){
		String query = "SELECT COUNT(*)+1 FROM USUARIOS WHERE PARTIDAS_GANADAS>"+Partidas_Ganadas+"";
		try {
			ResultSet res = s.executeQuery(query);
			return  res.getString(1);
		} catch (SQLException e) {
		
			e.printStackTrace();
			return null;
		}
	}
	/**Método para guardar el resultado de una partida
	 * @param Usuario_1 el primer usuario 
	 * @param Usuario_2 el segundo usuario
	 * @param Resultado el resultado (relativo al jugador 1)
	 */
	public static void Anyadir_registro_partida(String Usuario_1, String Usuario_2, String Resultado){
		String query ="";
		
		if(Resultado.equalsIgnoreCase("Victoria")){
			 query = "UPDATE USUARIOS SET PARTIDAS_GANADAS=PARTIDAS_GANADAS+1 WHERE NOMBRE_USUARIO='"+Usuario_1+"'";
		}
		else if(Resultado.equalsIgnoreCase("Empate")){
			query = "UPDATE USUARIOS SET PARTIDAS_EMPATADAS=PARTIDAS_EMPATADAS+1 WHERE NOMBRE_USUARIO='"+Usuario_1+"'";
		}
		else if(Resultado.equalsIgnoreCase("Derrota")){
			query = "UPDATE USUARIOS SET PARTIDAS_PERDIDAS=PARTIDAS_PERDIDAS+1 WHERE NOMBRE_USUARIO='"+Usuario_1+"'";
		}
		try {
			s.executeUpdate(query);
			
		} catch (SQLException e) {
		
			e.printStackTrace();
			
		}
		if(Resultado.equalsIgnoreCase("Victoria")){
			 query = "UPDATE USUARIOS SET PARTIDAS_PERDIDAS=PARTIDAS_PERDIDAS+1 WHERE NOMBRE_USUARIO='"+Usuario_2+"'";
		}
		else if(Resultado.equalsIgnoreCase("Empate")){
			query = "UPDATE USUARIOS SET PARTIDAS_EMPATADAS=PARTIDAS_EMPATADAS+1 WHERE NOMBRE_USUARIO='"+Usuario_2+"'";
		}
		else if(Resultado.equalsIgnoreCase("Derrota")){
			query = "UPDATE USUARIOS SET PARTIDAS_GANADAS=PARTIDAS_GANADAS+1 WHERE NOMBRE_USUARIO='"+Usuario_2+"'";
		}
		try {
			s.executeUpdate(query);
			
		} catch (SQLException e) {
		
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) throws SQLException {
		iniciarConexion();


				try{
					Anyadir_registro_partida("12345678", "123456789", "victoria");
				}catch(Exception a){
					a.printStackTrace();
				}
		
		cerrarConexion();
	}

}
