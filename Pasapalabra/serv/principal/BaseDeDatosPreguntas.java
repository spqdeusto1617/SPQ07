package principal;
//TODO: geteaar preguntas por tipo
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class BaseDeDatosPreguntas {
		private static Connection C ;
		private static Statement S ;


		public static void iniciarConexion(){
	
	
			try {
				Class.forName("org.sqlite.JDBC");
				C = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
				S = C.createStatement();
			} catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
	
	
	
		}

	public static boolean cerrarConexion(){
		try {
			S.close();

			C.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
	public static void crearTablaBD() {
		

		try {
			S.executeUpdate("create table Preguntas " +
					"(Pregunta string NOT NULL UNIQUE,Respuesta String NOT NULL,Id int NOT NULL PRIMARY KEY" +
					",  Evaluada boolean DEFAULT false, Creador String default 'Admin',Letra String,Valoracion int DEFAULT 0,Tipo String default 'Todos')");
			System.out.println("Tabla creada");
		} catch (SQLException e) {
			// Si hay excepción es que la tabla ya existía (lo cual es correcto)
			// e.printStackTrace();  

		}
		
	}
	/**Método para introducir preguntas a la base de datos de preguntas
	 * @param pregunta la pregunta
	 * @param respuesta la respuesta
	 * @param Creador el creador
	 * @param Letra la letra que sería
	 * @param tipo_de_pregunta el tipo de pregunta (revisar Tipo_pregunta.java)
	 */
	public static void meterPreguntas(String pregunta,String respuesta,String Creador,char Letra,Tipo_pregunta tipo_de_pregunta){
		int id = 0;
		try{
			try{
				String query = "SELECT MAX(ID) FROM Preguntas";
				ResultSet rs= S.executeQuery(query);
				id=rs.getInt(1);

				id++;

			}catch(Exception a){
				a.printStackTrace();
				id=0;
			}
			//TODO: cambiar el true por false al terminar de meter preguntas, para que no se autorizen todas las preguntas enviadas
			S.executeUpdate("INSERT INTO Preguntas VALUES('"+pregunta+"','"+respuesta+"',"+id+",'"+true+"','"+Creador+"','"+new StringBuilder().append(Letra).toString()+"',"+0+",'"+tipo_de_pregunta.toString() +"')");
		}catch(Exception a){
			a.printStackTrace();
			System.out.println("Ya existe esos valores");
		}
		
	}
	/**Método que obtiene preguntas aleatoriamente de las preguntas disponibles dependiendo de la letra que se que quiera y del tipo de pregunta
	 * @param tipo el tipo de pregunta
	 * @param letra_de_la_pregunta  la letra
	 * @return la pregunta (tipo pregunta)
	 */
	public static Preguntas obtenerpreguntasportipo(Tipo_pregunta tipo,char letra_de_la_pregunta){
		

		
		try{
			String query = "SELECT COUNT(*) FROM Preguntas WHERE Evaluada='"+true+"'AND Letra='"+letra_de_la_pregunta+"'AND tipo='"+tipo.toString()+"'";

			ResultSet rs= S.executeQuery(query);

			int total=rs.getInt(1);
		
			Random rand=new Random();
			int randomNum = rand.nextInt(total) ;
			ResultSet rs2= S.executeQuery("SELECT * FROM Preguntas WHERE Evaluada='"+true+"'AND Letra='"+letra_de_la_pregunta+"'AND tipo='"+tipo.toString()+"'");
			int contador=0;
			
			
			while(rs2.next()){
				
				if(contador==randomNum){
					String Pregunta = rs2.getString("Pregunta");
					String Respuesta = rs2.getString("Respuesta");
					String creador=rs2.getString("Creador");
					String letra=rs2.getString("Letra");
//					System.out.println(Pregunta + "\t" + Respuesta +
//							"\t" + letra +
//							"\t" + creador);
				
					return new Preguntas(Pregunta, Respuesta, letra.charAt(0), creador);
				}
				contador++;
			}
			//TODO: devolver los resultados
		} catch (SQLException e ) {
			e.printStackTrace();
		}
		return null;
		
	}


	public static void main(String[] args) {
		iniciarConexion();

		meterPreguntas("U: elemento químico conocido popularmente por su naturaleza radioactiva", "Uranio",  "Admin", 'u',Tipo_pregunta.Todos);
		cerrarConexion();
		//obtenerpreguntasportipo(Tipo_pregunta.Historia, 'b');
	}



}

