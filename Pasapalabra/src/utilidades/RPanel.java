package utilidades;

import javafx.event.Event;

public class RPanel implements Runnable{
	//Atributos
	public boolean abrir_cerrar_panel; //TRUE = ABRIR - FALSE = CERRAR
	public Event evento;
	
	//Constructor
	public RPanel(boolean acp, Event evento){
		
		this.abrir_cerrar_panel = acp;
		this.evento = evento;
		
	}
	
	//Método de interfaz
	@Override
	public void run() {
		if(abrir_cerrar_panel){
			//ABRIR
			
		}else{
			//CERRAR
			
		}
		
	}

}
