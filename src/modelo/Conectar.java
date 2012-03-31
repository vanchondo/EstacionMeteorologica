/*
  Ultima modificacion 24/Nov/2011
 */
package modelo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;



/**
 * 
 * @author vedant
 */

public final class Conectar {

	public String BASEDATOS;
	public String USUARIO = "";
	public String CONTRASENA = "";
	public String USUARIOS = "";
	public String IP = "";
	public int PUERTO;

	private Connection conexion = null;
	private Statement instruccion = null;
	private ResultSet resultado=null;
	
	private boolean correcto = false;
	private boolean conectado = false;

	private static int conexionesMax = 30;

	private static DataSource dataSource = null;

	public Conectar() {
		leer();
		conectar();
	}

	public Conectar(String user, String pass, String db, String host, int puerto) {
		IP = host;
		PUERTO = puerto;
		BASEDATOS = db;
		USUARIO = user;
		CONTRASENA = pass;
		correcto = true;
		conectar();
	}

	public void leer() {
		try {
			// Lee el archivo de configuracion para BD
			FileInputStream file = new FileInputStream("conexion");
			BufferedReader bufer = new BufferedReader(new InputStreamReader(
					file));
			String texto = bufer.readLine();
			file.close();

			// Separa la linea leida en palabras por cada coma que encuentre
			StringTokenizer token = new StringTokenizer(texto, ",");
			String[] datos = new String[token.countTokens()];
			int i = 0;
			while (token.hasMoreElements()) {
				datos[i] = token.nextToken();
				i++;
			}

			// IP,PUERTO,BASEDATOS,USUARIO,CONTRASENA
			IP = datos[0];
			PUERTO = Integer.parseInt(datos[1]);
			BASEDATOS = datos[2];
			USUARIO = datos[3];
			CONTRASENA = datos[4];

			correcto = true;
		} catch (IOException ex) {
			correcto = false;
		}
	}

	public void conectar() {
		if (correcto) {				
				MysqlDataSource dataSource = new MysqlDataSource();
				dataSource.setServerName(IP);
				dataSource.setPort(PUERTO);
				dataSource.setDatabaseName(BASEDATOS);
				dataSource.setUser(USUARIO);
				dataSource.setPassword(CONTRASENA);
			try {
				conexion = dataSource.getConnection();
				instruccion = conexion.createStatement();
				resultado = null;
				conectado = true;
			} catch (SQLException e) {
				conectado = false;
				String msj ="Error al conectar, verifique sus datos";
				System.err.println(msj);
				Logger.add(msj);
			}
		}
	}

	public ResultSet execute(String query) throws SQLException{

			instruccion.execute(query);

		try {
			resultado = instruccion.getResultSet();
		} catch (SQLException ex) {
		}
		return resultado;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void cerrar() {
		try {
			//instruccidataSourceon.close();
			conexion.close();
		} catch (SQLException ex) {
		}
	}

	public Connection getConexion() {
		return conexion;
	}

	public static int getConexionesMax() {
		return conexionesMax;
	}

	public static void setConexionesMax(int conexionesMax) {
		Conectar.conexionesMax = conexionesMax;
	}

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static void setDataSource(DataSource dataSource) {
		Conectar.dataSource = dataSource;
	}
}
