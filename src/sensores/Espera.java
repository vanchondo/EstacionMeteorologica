package sensores;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import modelo.CalculoParametros;
import modelo.Ecuacion;
import modelo.IntensidadPrecipitacionTR;
import modelo.Intensidad_mm_h;
import modelo.PeriodoRetorno;
import modelo.Precipitacion;


public class Espera extends JDialog implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int PrecipitacionVal = 10;
	public static final int IntensidadVal =20;
	public static final int PeriodoRetornoVal=30;
	public static final int CalculoParametrosVal = 40;
	public static final int IntensidadTRVal = 50;

	public static int opc;
	GenerarDatos generarDatos = new GenerarDatos();
	public Thread hilo = new Thread(this);
	public Thread hiloDatos;
	public static Espera ventanaEspera;
	public static boolean finalizado = false;
	JFrame papa;

	public Timmer timmer;
	
	
	public Espera(JFrame parent) {
		parent.setEnabled(false);
		papa=parent;
		initComponents();
		Acomodar.acomodar(this);
		hiloDatos = new Thread(generarDatos);
		timmer=new Timmer();
		Thread hiloTimmer = new Thread(timmer);
		hiloDatos.start();
		hiloTimmer.start();
		hilo.start();
		
	}

	private void initComponents() {
		setUndecorated(true);
		mensajeLbl = new JLabel("Espere un momento por favor");
		mensajeLbl.setVisible(true);
		add(mensajeLbl);
		pack();
		
	}

	public static void abrirEspera(JFrame parent, int pOpc) {
		opc = pOpc;
		ventanaEspera = new Espera(parent);
	}

	@Override
	public void run() {
		boolean continuar = false;
		while (!continuar) {
			if (!finalizado)
				continuar = false;
			else
				continuar = true;
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		dispose();
		
		JOptionPane.showMessageDialog(null,
				"Tabla generada en un tiempo de "+timmer.contador+" segundos.");
				
		papa.setEnabled(true);
		
	}
	
	private class Timmer implements Runnable{
		public int contador=0;
		@Override
		public void run() {
			while (true){
				contador++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}	

	private class GenerarDatos implements Runnable {

		@Override
		public void run() {
			switch (opc) {
			case Espera.PrecipitacionVal:
				Precipitacion.generarDatos();
				break;

			case Espera.IntensidadVal:
				Intensidad_mm_h.generarDatos();
				break;
				
			case Espera.PeriodoRetornoVal:
				PeriodoRetorno.generarDatos();
				break;
				
			case Espera.CalculoParametrosVal:
				CalculoParametros.generarDatos();
				break;
				
			case Espera.IntensidadTRVal:
				Ecuacion.generarDatos();
				IntensidadPrecipitacionTR.generarDatos();
				break;
			default:
				break;
			}
		}
	}
	
	private JLabel mensajeLbl;

}
