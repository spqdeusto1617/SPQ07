package principal;
//TODO: geteaar preguntas por tipo
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class BaseDeDatosPreguntas {
	//	private static Connection C ;
	//	private static Statement S ;


	//	public static void iniciarConexion(){
	//
	//
	//		try {
	//			Class.forName("org.sqlite.JDBC");
	//			C = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
	//			S = C.createStatement();
	//		} catch ( Exception e ) {
	//			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	//			System.exit(0);
	//		}
	//
	//
	//
	//	}

	public static boolean cerrarConexion(Connection c,Statement s){
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
		Connection c = null ;
		Statement s = null ;
		int id = 0;
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
			s = c.createStatement();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		try {
			c = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			s = c.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			s.executeUpdate("create table Preguntas " +
					"(Pregunta string NOT NULL UNIQUE,Respuesta String NOT NULL,Id int NOT NULL PRIMARY KEY" +
					",  Evaluada boolean DEFAULT false, Creador String default 'Admin',Letra String,Valoracion int DEFAULT 0,Tipo String default 'Todos')");
			System.out.println("Tabla creada");
		} catch (SQLException e) {
			// Si hay excepción es que la tabla ya existía (lo cual es correcto)
			// e.printStackTrace();  

		}
		cerrarConexion(c,s);
	}
	public static void meterPreguntas(String pregunta,String respuesta,String Creador,char Letra,Tipo_pregunta tipo_de_pregunta){
		Connection c = null ;
		Statement s = null ;
		int id = 0;
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
			s = c.createStatement();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//	iniciarConexion();
		try {
			c = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			s = c.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try{
			try{
				String query = "SELECT MAX(ID) FROM Preguntas";
				ResultSet rs= s.executeQuery(query);
				id=rs.getInt(1);

				id++;

			}catch(Exception a){
				a.printStackTrace();
				id=0;
			}
			//TODO: cambiar el true por false al terminar de meter preguntas, para que no se autorizen todas las preguntas enviadas
			s.executeUpdate("INSERT INTO Preguntas VALUES('"+pregunta+"','"+respuesta+"',"+id+",'"+true+"','"+Creador+"','"+new StringBuilder().append(Letra).toString()+"',"+0+",'"+tipo_de_pregunta.toString() +"')");
		}catch(Exception a){
			a.printStackTrace();
			System.out.println("Ya existe esos valores");
		}
		cerrarConexion(c,s);
	}
	public static void obtenerpreguntasportipo(Tipo_pregunta tipo,char letra_de_la_pregunta){
		Connection c = null ;
		Statement s = null ;

		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
			s = c.createStatement();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			//System.exit(0);
		}
		
		try {
			c = DriverManager.getConnection("jdbc:sqlite:Preguntas.db");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			s = c.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			String query = "SELECT COUNT(*) FROM Preguntas WHERE Evaluada='"+true+"'AND Letra='"+letra_de_la_pregunta+"'AND tipo='"+tipo.toString()+"'";

			ResultSet rs= s.executeQuery(query);

			int total=rs.getInt(1);
		
			Random rand=new Random();
			int randomNum = rand.nextInt(total) ;
			ResultSet rs2= s.executeQuery("SELECT * FROM Preguntas WHERE Evaluada='"+true+"'AND Letra='"+letra_de_la_pregunta+"'AND tipo='"+tipo.toString()+"'");
			int contador=0;
			
			
			while(rs2.next()){
				
				if(contador==randomNum){
					String Pregunta = rs2.getString("Pregunta");
					String Respuesta = rs2.getString("Respuesta");
					String creador=rs2.getString("Creador");
					String letra=rs2.getString("Letra");
					System.out.println(Pregunta + "\t" + Respuesta +
							"\t" + letra +
							"\t" + creador);
					break;
				}
				contador++;
			}
			//TODO: devolver los resultados
		} catch (SQLException e ) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		

		//meterPreguntas("Con la b", "a",  "Paco", 'b',Tipo_pregunta.Historia);

		//obtenerpreguntasportipo(Tipo_pregunta.Historia, 'b');
	}



}

