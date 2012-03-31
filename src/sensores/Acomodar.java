package sensores;


import java.awt.Window;

public class Acomodar {
	
    public static void acomodar(Window ventana){
    	ventana.setLocationRelativeTo(null);        
        ventana.repaint();
        ventana.setVisible(true);
    }
}
