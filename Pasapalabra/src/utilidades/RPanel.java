package utilidades;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class RPanel implements Runnable{
	//Atributos
	public boolean abrir_cerrar_panel; //TRUE = ABRIR - FALSE = CERRAR
	public ArrayList<Node> cajaDeControl;
	
	//Constructor
	public RPanel(boolean acp, ArrayList<Node> menuDesplegable){
		this.abrir_cerrar_panel = acp;
		this.cajaDeControl = menuDesplegable;
		
	}
	private int i;
	//Método de interfaz
	@Override
	public void run() {

		for (Node node : cajaDeControl) {
			if(node.getId().equals("textoESPanel")){
				if(abrir_cerrar_panel) ((Text) node).setText("<<");
				else ((Text) node).setText(">>");
			break;
			}
		}
		
		if(!abrir_cerrar_panel){
			for(i = 0; i<200;i++){
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (Node nodo : cajaDeControl) {
//							ObservableMap<Object,Object> map = nodo.getProperties();
//							System.out.println(map.size());
//							for (Object p : map.values()) {
//							        if (p instanceof Property<?>) {
//							                Property<?> pn = (Property<?>) p;
//							                System.out.println( "Propiedad " + pn.getName() + " = " + pn.getValue() );
//							        }
//							}
						
						Class<?> c = nodo.getClass();
						Method ms[] = c.getMethods();
						// System.out.println( c.getName() + " -- " + Arrays.asList( ms ) );
						boolean tieneX = false;
						for (Method m : ms ) {
//							if (m.getName().equals("getX")) {
//								try {
//									Double coordX = (Double) (m.invoke( nodo, null ));
//									System.out.println( coordX );
//								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							}
							if (m.getName().equals("setX")) {
								tieneX  = true;
								try {
									m.invoke( nodo, new Double(-i) );
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else if(m.getName().equals("setCenterX")){
								try {
									m.invoke(nodo, new Double(-i));
								} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
							}
						}
						if (!tieneX) {
//							System.out.println( "No tiene setx: " + c.getName() );
						}
						}
					}
				});
			
//			try {
//				Thread.sleep(5);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			}	
				
			}else{
			//CERRAR
				System.out.println("NADA");
			
		
		}	
	}

}
