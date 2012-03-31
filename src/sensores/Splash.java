package sensores;

import java.awt.FlowLayout;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import modelo.Conectar;
import modelo.Sensor;

import animaciones.BouncyBubbles;
import animaciones.Koch;
import animaciones.Reach3;
import animaciones.Reflection1;
import animaciones.SimpleParticleSystem;

import processing.core.PApplet;

public class Splash extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	public Splash() {
		initComponents();

		hilo = new Thread(this);
		hilo.start();
	}

	private void initComponents() {
		setUndecorated(true);
		setLayout(new FlowLayout());

		panel1 = new JPanel();
		panel2 = new JPanel();
		cargandoLbl = new JLabel("Cargando");
		cargandoLbl.setHorizontalTextPosition(JLabel.CENTER);

		panel2.setLayout(new FlowLayout());
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		progreso = new JProgressBar();
		progreso.setMaximum(100);
		grafico();

		add(panel1);
		panel1.add(grafico);
		panel1.add(progreso);
		panel1.add(panel2);
		panel2.add(cargandoLbl);
		pack();
		Acomodar.acomodar(this);
	}

	private void grafico() {

		Random r = new Random();
		int valorDado = r.nextInt(5) + 1;

		switch (valorDado) {
		case 1:
			grafico = new Reach3();
			break;

		case 2:
			grafico = new Reflection1();
			break;

		case 3:
			grafico = new SimpleParticleSystem();
			break;

		case 4:
			grafico = new Koch();
			break;

		case 5:
			grafico = new BouncyBubbles();
			break;
		default:
			break;
		}

		grafico.init();
	}

	@Override
	public void run() {
		String msj = "Iniciando aplicacion";
		Object ventana = null;
		try {
			for (int i = 0; i <= 10; i++) {
				switch (i) {
				case 1:
					msj = "Verificando por archivos necesarios";
					Conectar conexion = new Conectar();
					if (!conexion.isConectado()) {
						ventana = new CrearConexion(this, true);
						Acomodar.acomodar((JDialog) ventana);
						conexion = new Conectar();
						if (!conexion.isConectado())
							System.exit(-1);
					}
					conexion.cerrar();
					break;

				case 3:
					msj = "Verificando conexion con BD";
					Sensor.createTables();
					break;

				case 6:
					msj = "Configurando interfaz de usuario";
					break;

				case 9:
					msj = "Todo listo abriendo aplicacion";
					ventana = new Principal();
					break;
				default:
					break;
				}
				progreso.setValue(i * 10);
				progreso.repaint();
				cargandoLbl.setText(msj);
				cargandoLbl.repaint();
				Thread.sleep(TIEMPOESPERA);
			}

			// grafico.dispose();
			dispose();
			Acomodar.acomodar((JFrame) ventana);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private int TIEMPOESPERA = 100;
	private Thread hilo;
	private JPanel panel1;
	private JPanel panel2;
	private JProgressBar progreso;
	private JLabel cargandoLbl;
	private PApplet grafico;

}
