package sensores;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import modelo.Conectar;

public class CrearConexion extends JDialog {

	private static final long serialVersionUID = 1L;

	public CrearConexion(JFrame parent, boolean modal) {
		super(parent, modal);
		setTitle("Crear conexion");
		initComponents();
	}

	private void initComponents() {
		setLayout(new FlowLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		usuarioLbl = new JLabel("User:");
		passLbl = new JLabel("Password:");
		verificarPassLbl = new JLabel("ReType:");
		hostLbl = new JLabel("Host:");
		baseDatosLbl = new JLabel("DataBase:");
		puertoLbl = new JLabel("Port:");

		usuarioTxt = new JTextField("root");
		passTxt = new JPasswordField();
		verificarPassTxt = new JPasswordField();
		hostTxt = new JTextField("localhost");
		//baseDatosTxt = new JTextField("sensor_db");
		baseDatosTxt = new JTextField("gomez_farias_db");
		puertoTxt = new JTextField("3306");

		panelDatos = new JPanel();
		panelBotones = new JPanel();
		panelPrincipal = new JPanel();

		aceptarCmd = new JButton("Aceptar");
		salirCmd = new JButton("Salir");

		panelPrincipal
				.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
		panelDatos.setLayout(new GridLayout(6, 2));
		panelBotones.setLayout(new FlowLayout());

		panelDatos.add(usuarioLbl);
		panelDatos.add(usuarioTxt);
		panelDatos.add(passLbl);
		panelDatos.add(passTxt);
		panelDatos.add(verificarPassLbl);
		panelDatos.add(verificarPassTxt);
		panelDatos.add(baseDatosLbl);
		panelDatos.add(baseDatosTxt);
		panelDatos.add(hostLbl);
		panelDatos.add(hostTxt);
		panelDatos.add(puertoLbl);
		panelDatos.add(puertoTxt);

		panelBotones.add(aceptarCmd);
		panelBotones.add(salirCmd);

		add(panelPrincipal);
		panelPrincipal.add(panelDatos);
		panelPrincipal.add(panelBotones);

		// Eventos
		ManejadorActionListener manejador = new ManejadorActionListener();
		aceptarCmd.addActionListener(manejador);
		salirCmd.addActionListener(manejador);

		pack();

	}

	@SuppressWarnings("deprecation")
	private boolean formLleno() {
		boolean correcto = true;
		USER = usuarioTxt.getText();
		HOST = hostTxt.getText();
		PORT = puertoTxt.getText();
		DB = baseDatosTxt.getText();

		if (passTxt.getText().equals(verificarPassTxt.getText()))
			PASS = new String(passTxt.getPassword());
		else
			correcto = false;

		if (USER.isEmpty() || HOST.isEmpty() || PORT.isEmpty() || DB.isEmpty()
				|| PASS.isEmpty())
			correcto = false;
		else
			correcto = true;

		return correcto;
	}

	private boolean probarConexion() {
		Conectar conexion = new Conectar(USER, PASS, DB, HOST,
				Integer.parseInt(PORT));
		return conexion.isConectado();
	}

	private class ManejadorActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object objeto = e.getSource();

			if (objeto == aceptarCmd) {
				if (formLleno()) {
					if (probarConexion()) {
						String todo = HOST + "," + PORT + "," + DB + "," + USER
								+ "," + PASS;
						FileOutputStream file;

						try {
							file = new FileOutputStream("conexion");
							BufferedOutputStream bufer = new BufferedOutputStream(
									file);
							bufer.write(todo.getBytes());
							bufer.close();
							JOptionPane.showMessageDialog(null,
									"Conexion creada correctamente!");
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						dispose();
					} else
						JOptionPane.showMessageDialog(null, "Datos erroneos");
				}
			} else if (objeto == salirCmd)
				dispose();

		}

	}

	private String USER = "";
	private String PASS = "";
	private String HOST = "";
	private String DB = "";
	private String PORT = "";

	private JLabel usuarioLbl;
	private JLabel passLbl;
	private JLabel verificarPassLbl;
	private JLabel hostLbl;
	private JLabel baseDatosLbl;
	private JLabel puertoLbl;
	private JTextField usuarioTxt;
	private JPasswordField passTxt;
	private JPasswordField verificarPassTxt;
	private JTextField hostTxt;
	private JTextField baseDatosTxt;
	private JTextField puertoTxt;
	private JPanel panelPrincipal;
	private JPanel panelDatos;
	private JPanel panelBotones;
	private JButton aceptarCmd;
	private JButton salirCmd;
}
