package principal;

public class Preguntas{
	String Pregunta;
	String Respuesta;
	char letra;
	String Creador;
	boolean respondida=false;
	public Preguntas(String pregunta, String respuesta, char letra, String creador) {
		super();
		Pregunta = pregunta;
		Respuesta = respuesta;
		this.letra = letra;
		Creador = creador;
	}
	@Override
	public String toString() {
		return "Preguntas [Pregunta=" + Pregunta + ", Respuesta=" + Respuesta + ", letra=" + letra + ", Creador="
				+ Creador + "]";
	}
	public String getPregunta() {
		return Pregunta;
	}
	public boolean isRespondida() {
		return respondida;
	}
	public String getRespuesta() {
		return Respuesta;
	}
	public char getLetra() {
		return letra;
	}
	public String getCreador() {
		return Creador;
	}
	public void setRespondida(boolean respondida) {
		this.respondida = respondida;
	}


	
}