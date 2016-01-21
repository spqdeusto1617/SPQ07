package utilidades;

import java.io.Serializable;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Tipo_Amigo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1606968345161920544l;
	public SimpleStringProperty  Nombre_usuario;
	public SimpleBooleanProperty Solicitud_aceptada;
	public SimpleBooleanProperty Solicitud_enviada;
	public SimpleBooleanProperty Solicitud_pendiente;
	public SimpleBooleanProperty Estado_amigo;
	public Tipo_Amigo(String nombre,boolean solicitud_aceptada,boolean solicitud_enviada,boolean Solicitud_pendiente,boolean estado){
		this.Nombre_usuario=new SimpleStringProperty(nombre);
		this.Solicitud_aceptada=new SimpleBooleanProperty(solicitud_aceptada);
		this.Solicitud_enviada=new SimpleBooleanProperty(solicitud_aceptada);;
		this.Solicitud_pendiente=new SimpleBooleanProperty(Solicitud_pendiente);;
		this.Estado_amigo=new SimpleBooleanProperty(estado);;
	}

	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Tipo_Amigo){
			return this.Nombre_usuario.equals(((Tipo_Amigo) arg0).Nombre_usuario);
		}
		else{
			return false;
		}
	}
	@Override
	public String toString() {
		return "Tipo_Amigo [Nombre_usuario=" + Nombre_usuario + ", Solicitud_aceptada=" + Solicitud_aceptada
				+ ", Solicitud_enviada=" + Solicitud_enviada + ", Solicitud_pendiente=" + Solicitud_pendiente
				+ ", Estado_amigo=" + Estado_amigo + "]";
	}
}