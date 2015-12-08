package objetos;

import java.util.ArrayList;

import javafx.event.Event;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BotonJuego implements Comparable<Event> {

	//Atributos
	Rectangle r;
	Text t;
	
	public BotonJuego(Rectangle r, Text t){
		this.r = r;
		this.t = t;
	}
	
	public static void seleccionar_notDeseleccionar(boolean b, ArrayList<BotonJuego> aLBJ, Event event){
		for (BotonJuego botonJuego : aLBJ) {
			if(botonJuego.compareTo(event) == 0){
				String id;
				if(b)id = "seleccionado";else id = "deseleccionado";
				botonJuego.r.setId(id);
//				botonJuego.t.setId(id);
				return;
			}
		}
	}

	@Override
	public int compareTo(Event o) {
		if(r.equals(o.getSource())) return 0;
		if(t.equals(o.getSource())) return 0;
		return 1;
	}
	
	
	
}
