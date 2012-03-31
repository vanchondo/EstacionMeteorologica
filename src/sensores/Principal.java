package sensores;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import processing.core.PApplet;

import modelo.CalculoParametros;
import modelo.IntensidadPrecipitacionTR;
import modelo.Intensidad_mm_h;
import modelo.PeriodoRetorno;
import modelo.Precipitacion;
import modelo.Sensor;
import modelo.Tabla;

import animaciones.Graficas;
import animaciones.Snake;

import com.toedter.calendar.JDateChooser;

public class Principal extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<Integer> myselection = new ArrayList<Integer>();
	String[] sensores = null;
	public final static int[] intervalos = { 5, 10, 20, 60, 90, 120 };

	Tabla access_to = new Tabla();
	Principal principal;

	public Principal() {
		principal = this;
		initComponents();
	}

	private void initComponents() {
		// Propiedades del JFrame
		setTitle("Sensores");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ManejadorActionListener manejador = new ManejadorActionListener();
		// Creo los objetos
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		// Panel para Sensores existentes
		sensorPanel = new JPanel();
		// Panel para Fechas
		fechaPanel = new JPanel();
		// Panel para Tipos de grafica
		graficasPanel = new JPanel();
		// Panel para estadistica descriptiva
		statsPanel = new JPanel();
		// Panel en donde se dibujara la grafica
		graficaPanel = new JPanel();
		// Obtengo los nombres de los sensores que se encuentran en la tabla
		// para crear un checkbox por cada sensor
		int i = 0;
		/*
		 * Precipitacion data type is constrained to sensor_table... thus, a
		 * more general approach is required for working with any table instead
		 * a new method was created to lookUpfor table names...
		 */

		precipitacionBtn = new JButton("Crear tabla de precipitacion");
		intensidadBtn = new JButton("Crear tabla de intensidad");
		retornoBtn = new JButton("Crear tabla de periodo de retorno");
		calculoParametrosBtn = new JButton("Calculo de los parámetros");
		intensidadPrecipitacionBtn = new JButton(
				"Intensidad de Precipitacion TR");

		precipitacionBtn.addActionListener(manejador);
		intensidadBtn.addActionListener(manejador);
		retornoBtn.addActionListener(manejador);
		calculoParametrosBtn.addActionListener(manejador);
		intensidadPrecipitacionBtn.addActionListener(manejador);

		sensores = Tabla.getHeaders(Tabla.tabla);

		sensoresChk = new JCheckBox[sensores.length];
		habilitadoChk = new JCheckBox[sensores.length];
		//
		for (int j = 0; j < sensores.length; j++) {
			sensoresChk[j] = new JCheckBox(sensores[j]);
			sensoresChk[j].setActionCommand("" + j);
			sensoresChk[j].setName(j + "");
			sensoresChk[j].addActionListener(manejador);
			habilitadoChk[j] = new JCheckBox("Enable");
			habilitadoChk[j].setEnabled(false);
		}

		// Creo los combos para las fechas
		fechaIni = new JDateChooser();
		fechaFin = new JDateChooser();
		fechaChk = new JCheckBox("Activar fecha");
		fechaIniLbl = new JLabel("Desde:");
		fechaFinLbl = new JLabel("Hasta:");

		// Creo los radioButton para especificar el tipo de grafica a dibujar
		radioGroup = new ButtonGroup();
		barraRadio = new JRadioButton("Barras");
		barraRadio.setActionCommand("1");
		circuloRadio = new JRadioButton("Circular");
		circuloRadio.setActionCommand("2");
		puntoRadio = new JRadioButton("Lineal");
		puntoRadio.setActionCommand("3");

		radioGroup.add(barraRadio);
		radioGroup.add(circuloRadio);
		radioGroup.add(puntoRadio);

		// Creo los radioButton para especificar el tipo de stats a utilizar
		statsGroup = new ButtonGroup();
		avgRadio = new JRadioButton("Average");
		avgRadio.setEnabled(true);
		avgRadio.setActionCommand("1");
		maxRadio = new JRadioButton("Maximum");
		maxRadio.setActionCommand("2");
		minRadio = new JRadioButton("Minimum");
		minRadio.setActionCommand("3");
		stdRadio = new JRadioButton("STD");
		stdRadio.setActionCommand("4");
		varRadio = new JRadioButton("Variance");
		varRadio.setActionCommand("5");

		statsGroup.add(avgRadio);
		statsGroup.add(maxRadio);
		statsGroup.add(minRadio);
		statsGroup.add(varRadio);
		statsGroup.add(stdRadio);

		// Grafica
		grafica = new Snake();
		grafica.init();
		// Personalizo los paneles
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new FlowLayout());
		sensorPanel.setLayout(new GridLayout(6, 2));
		fechaPanel.setLayout(new BoxLayout(fechaPanel, BoxLayout.Y_AXIS));
		graficasPanel.setLayout(new BoxLayout(graficasPanel, BoxLayout.Y_AXIS));
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));

		sensorPanel.setBorder(BorderFactory.createTitledBorder("Sensores"));
		graficasPanel.setBorder(BorderFactory.createTitledBorder("Graficas"));
		statsPanel.setBorder(BorderFactory.createTitledBorder("Stats"));
		fechaPanel.setBorder(BorderFactory.createTitledBorder(""));

		// Eventos de los objetos

		fechaChk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ocultarMostrarFechas(fechaChk.isSelected());

			}
		});

		barraRadio.addActionListener(manejador);
		circuloRadio.addActionListener(manejador);
		puntoRadio.addActionListener(manejador);

		avgRadio.addActionListener(manejador);
		maxRadio.addActionListener(manejador);
		minRadio.addActionListener(manejador);
		varRadio.addActionListener(manejador);
		stdRadio.addActionListener(manejador);

		// Agrego y posiciono los objetos en el formulario
		for (i = 0; i < sensores.length; i++) {
			sensorPanel.add(sensoresChk[i]);
			sensorPanel.add(habilitadoChk[i]);
		}

		add(panel1);
		add(panel2);

		panel1.add(panel3);
		panel1.add(fechaPanel);

		panel2.add(graficaPanel);

		panel3.add(sensorPanel);
		panel3.add(graficasPanel);
		panel3.add(statsPanel);

		fechaPanel.add(fechaChk);
		fechaPanel.add(fechaIniLbl);
		fechaPanel.add(fechaIni);
		fechaPanel.add(fechaFinLbl);
		fechaPanel.add(fechaFin);
		fechaPanel.add(precipitacionBtn);
		fechaPanel.add(intensidadBtn);
		fechaPanel.add(retornoBtn);
		fechaPanel.add(calculoParametrosBtn);
		fechaPanel.add(intensidadPrecipitacionBtn);

		ocultarMostrarFechas(false);

		graficasPanel.add(barraRadio);
		graficasPanel.add(circuloRadio);
		graficasPanel.add(puntoRadio);
		// graficasPanel.add(precipitacionBtn);
		statsPanel.add(avgRadio);
		statsPanel.add(maxRadio);
		statsPanel.add(minRadio);
		statsPanel.add(varRadio);
		statsPanel.add(stdRadio);

		graficaPanel.add(grafica);

		pack();

	}

	public class ManejadorActionListener implements ActionListener {
		String tabla = null;

		@SuppressWarnings("static-access")
		@Override
		public void actionPerformed(ActionEvent e) {
			Object fuente = e.getSource();
			Espera.finalizado = false;

			if (fuente == avgRadio)
				access_to.stats_option = "AVG";
			else if (fuente == maxRadio)
				access_to.stats_option = "MAX";
			else if (fuente == minRadio)
				access_to.stats_option = "MIN";
			else if (fuente == varRadio)
				access_to.stats_option = "VARIANCE";
			else if (fuente == stdRadio)
				access_to.stats_option = "STD";

			else if (fuente == barraRadio) {

				dibujar();
			} else if (fuente == circuloRadio)
				dibujar();
			else if (fuente == puntoRadio)
				dibujar();

			else if (fuente == precipitacionBtn) {
				
				if (Precipitacion.contar() > 0) {
					if (pregunta(
							"La tabla de precipitacion ya contiene datos.\n ¿Desea borrarla?",
							"¿Desea borrarla?")) {
						Sensor.clean(Precipitacion.tabla);
						Sensor.clean(Intensidad_mm_h.tabla);
						Sensor.clean(PeriodoRetorno.tabla);
						Sensor.clean(CalculoParametros.tabla);
						Sensor.clean(IntensidadPrecipitacionTR.tabla);
						System.out.println("Tablas borradas");
						precipitacionBtn.doClick();
					}
				} else {
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

					if (fechaIni.getDate() != null
							&& fechaFin.getDate() != null) {
						int anioIn = Integer.parseInt(sdf.format(fechaIni
								.getDate()));
						int anioFn = Integer.parseInt(sdf.format(fechaFin
								.getDate()));

						if ((anioFn - anioIn) < 0)
							JOptionPane
									.showMessageDialog(null,
											"Ingrese una fecha valida",
											"Fecha invalida",
											JOptionPane.ERROR_MESSAGE);
						else {
							Precipitacion.setAnioIn(anioIn);
							Precipitacion.setAnioFn(anioFn);
							Espera.abrirEspera(principal,
									Espera.PrecipitacionVal);
						}

					} else {
						JOptionPane.showMessageDialog(null,
								"Seleccione una fecha valida.",
								"Fecha invalida", JOptionPane.ERROR_MESSAGE);

					}
				}

			} else if (fuente == intensidadBtn) {
				if (Precipitacion.contar() > 0) {
					if (Intensidad_mm_h.contar() > 0) {
						if (pregunta(
								"La tabla de intensidad ya contiene datos.\n ¿Desea borrarla?",
								"¿Desea borrarla?")) {
							Sensor.clean(Intensidad_mm_h.tabla);
							Sensor.clean(PeriodoRetorno.tabla);
							Sensor.clean(CalculoParametros.tabla);
							Sensor.clean(IntensidadPrecipitacionTR.tabla);
							System.out.println("Tablas borradas");
							intensidadBtn.doClick();
						}
					} else {
						Espera.abrirEspera(principal, Espera.IntensidadVal);
					}
				} else
					JOptionPane.showMessageDialog(null,
							"La tabla de precipitacion no contiene datos");

			} else if (fuente == retornoBtn) {
				if (Intensidad_mm_h.contar() > 0) {
					if (PeriodoRetorno.contar() > 0) {
						if (pregunta(
								"La tabla de Periodos de retorno ya contiene datos.\n ¿Desea borrarla?",
								"¿Desea borrarla?")) {
							Sensor.clean(PeriodoRetorno.tabla);
							Sensor.clean(CalculoParametros.tabla);
							Sensor.clean(IntensidadPrecipitacionTR.tabla);
							System.out.println("Tablas borradas");
							retornoBtn.doClick();
						}
					} else {
						Espera.abrirEspera(principal, Espera.PeriodoRetornoVal);
					}
				} else
					JOptionPane.showMessageDialog(null,
							"La tabla de intensidad mm/h no contiene datos");

			} else if (fuente == calculoParametrosBtn) {
				if (PeriodoRetorno.contar() > 0) {
					if (CalculoParametros.contar() > 0) {
						if (pregunta(
								"La tabla de calculo de parametros ya contiene datos.\n ¿Desea borrarla?",
								"¿Desea borrarla?")) {
							Sensor.clean(CalculoParametros.tabla);
							Sensor.clean(IntensidadPrecipitacionTR.tabla);
							System.out.println("Tablas borradas");
							calculoParametrosBtn.doClick();
						}
					} else {
						Espera.abrirEspera(principal, Espera.CalculoParametrosVal);
					}
				} else
					JOptionPane.showMessageDialog(null,
							"La tabla de periodosRetorno no contiene datos");

			} else if (fuente == intensidadPrecipitacionBtn) {
				if (CalculoParametros.contar() > 0) {
					if (IntensidadPrecipitacionTR.contar() > 0) {
						if (pregunta("La tabla de "
								+ IntensidadPrecipitacionTR.tabla
								+ " ya contiene datos.\n ¿Desea borrarla?",
								"¿Desea borrarla?")) {
							Sensor.clean(IntensidadPrecipitacionTR.tabla);
							System.out.println("Tablas borradas");
							calculoParametrosBtn.doClick();
						}
					} else {
						Espera.abrirEspera(principal, Espera.IntensidadTRVal);
					}
				}
			} else {
				JCheckBox check = (JCheckBox) e.getSource();
				int opc = Integer.parseInt(check.getName());
				boolean enabled = check.isSelected();
				habilitadoChk[opc].setEnabled(enabled);
				if (enabled)
					myselection.add(opc);
				else
					myselection.remove(myselection.indexOf(opc));
			}
		}
	}

	private void ocultarMostrarFechas(boolean mostrar) {
		fechaIni.setEnabled(mostrar);
		fechaFin.setEnabled(mostrar);
		precipitacionBtn.setEnabled(mostrar);
		retornoBtn.setEnabled(mostrar);
		intensidadBtn.setEnabled(mostrar);
		calculoParametrosBtn.setEnabled(mostrar);
		intensidadPrecipitacionBtn.setEnabled(mostrar);

	}

	private void dibujar() {

		// Obtenemos el numero de sensores que se desean buscar
		int s = 0;
		String[] selected_sensorNames = new String[myselection.size()];

		for (int i = 0; i < myselection.size(); i++) {
			selected_sensorNames[i] = (sensores[myselection.get(i)]);
		}

		for (int i = 0; i < sensoresChk.length; i++) {
			if (sensoresChk[i].isSelected())
				s++;
		}
		// Creo un arreglo de enteros del tama�o de los sensores a usar
		/*
		 * Not sure if this is necessary, as sensores got sensor's names and
		 * habilitadoChk got those enabled
		 */
		Integer[] sensores = new Integer[s];
		String[] estados = new String[s];

		// Obtengo el id de los sensores a usar
		s = 0;
		for (int i = 0; i < sensoresChk.length; i++) {
			if (sensoresChk[i].isSelected()) {
				sensores[s] = Integer.parseInt(sensoresChk[i]
						.getActionCommand());
				String estado = "false";
				if (habilitadoChk[i].isEnabled()) {
					if (habilitadoChk[i].isSelected())
						estado = "true";
				}
				estados[s] = estado;
				s++;
			}
		}

		boolean need_date = false;

		// Obtengo la fecha seleccionada... remember to constraint graphics
		// until a date was indicated
		String fechaIn = null;
		String fechaFn = null;
		if (fechaChk.isSelected()) {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			fechaIn = sdf.format(fechaIni.getDate());
			fechaFn = sdf.format(fechaFin.getDate());

		}
		int opc = 0;
		if (barraRadio.isSelected() && fechaChk.isSelected()) {
			opc = Integer.parseInt(barraRadio.getActionCommand());
		} else if (circuloRadio.isSelected() && fechaChk.isSelected()) {
			opc = Integer.parseInt(circuloRadio.getActionCommand());
		} else if (puntoRadio.isSelected() && fechaChk.isSelected()) {
			opc = Integer.parseInt(puntoRadio.getActionCommand());
		} else {
			JOptionPane.showMessageDialog(panel1,
					"Needs to select a window date");
			need_date = true;
		}
		pack();
		graficaPanel.remove(grafica);
		grafica.dispose();
		if (!need_date)
			grafica = new Graficas(opc, selected_sensorNames, fechaIn, fechaFn);
		need_date = false;
		graficaPanel.add(grafica);
		grafica.init();
	}

	public void call_graficar() {

	}

	private boolean pregunta(String pregunta, String titulo) {
		int response = JOptionPane.showOptionDialog(null, pregunta, titulo,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, "Question");
		if (response == JOptionPane.YES_OPTION)
			return true;
		else
			return false;

	}

	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel statsPanel;
	private JPanel sensorPanel;
	private JPanel fechaPanel;
	private JPanel graficasPanel;
	private JPanel graficaPanel;
	private JCheckBox[] sensoresChk;
	private JCheckBox[] habilitadoChk;
	private JDateChooser fechaIni;
	private JDateChooser fechaFin;
	private JLabel fechaIniLbl;
	private JLabel fechaFinLbl;
	private JCheckBox fechaChk;
	private JRadioButton barraRadio;
	private JRadioButton circuloRadio;
	private JRadioButton puntoRadio;
	private ButtonGroup radioGroup;
	private ButtonGroup statsGroup;
	private JRadioButton avgRadio;
	private JRadioButton maxRadio;
	private JRadioButton minRadio;
	private JRadioButton stdRadio;
	private JRadioButton varRadio;
	private PApplet grafica;
	private JButton precipitacionBtn;
	private JButton intensidadBtn;
	private JButton retornoBtn;
	private JButton calculoParametrosBtn;
	private JButton intensidadPrecipitacionBtn;

	@Override
	public void run() {
		initComponents();

	}

	@Override
	public void dispose() {
		System.exit(0);
	}
}
