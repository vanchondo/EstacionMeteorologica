package modelo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import sensores.Principal;

public class CalculoParametros extends Sensor implements Estaciones {

	public static String tabla = "calculoParametros";
	public static String[] campos;

	private int id;
	private float x2;
	private float x1;
	private float y;
	private float x1y;
	private float x2y;
	private float x12;
	private float x22;
	private float x1x2;

	public CalculoParametros() {
	}

	public CalculoParametros(int pId, float pX2, float pX1, float pY,
			float pX1y, float pX2y, float pX12, float pX22, float pX1x2) {

		id = pId;
		x2 = pX2;
		x1 = pX1;
		y = pY;
		x1y = pX1y;
		x2y = pX2y;
		x12 = pX12;
		x22 = pX22;
		x1x2 = pX1x2;
	}

	@Override
	public boolean save() {
		campos = getHeaders(CalculoParametros.tabla);
		boolean correcto = true;
		Conectar con = new Conectar();
		String query = "INSERT INTO " + tabla + "( ";
		for (String campo : campos) {
			query += campo + ",";
		}
		query = query.substring(0, query.length() - 1);
		query += ") VALUES (" + id + "," + x2 + "," + x1 + "," + y + "," + x1y
				+ "," + x2y + "," + x12 + "," + x22 + "," + x1x2 + ")";

		try {
			con.execute(query);
			Logger.add(query);
		} catch (SQLException e) {
			e.printStackTrace();
			correcto = false;
		}
		con.cerrar();
		return correcto;
	}

	@Override
	public void createTable() {
		String query = "CREATE TABLE IF NOT EXISTS " + tabla + " ("
				+ "id INT PRIMARY KEY AUTO_INCREMENT," + "x2 FLOAT,"
				+ "x1 FLOAT," + "y FLOAT," + "x1y FLOAT," + "x2y FLOAT,"
				+ "x12 FLOAT," + "x22 FLOAT," + "x1x2 FLOAT)";
		Conectar con = new Conectar();
		try {
			con.execute(query);
			Logger.add(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();

	}

	public static void crearTabla() {
		new CalculoParametros().createTable();
	}

	@Override
	public void generar() {
		String query = "SELECT count(1) as total FROM periodosRetorno";
		int noOrden = Sensor.count(query);
		for (int intervalo : Principal.intervalos) {
			for (int i = 1; i <= noOrden; i++) {

				PeriodoRetorno periodos = PeriodoRetorno.find("orden=" + i)[0];
				CalculoParametros parametros = new CalculoParametros();

				switch (intervalo) {
				case 5:
					parametros.y = (float) Math.log10(periodos.getMin5());
					break;

				case 10:
					parametros.y = (float) Math.log10(periodos.getMin10());
					break;

				case 20:
					parametros.y = (float) Math.log10(periodos.getMin20());
					break;

				case 60:
					parametros.y = (float) Math.log10(periodos.getMin60());
					break;

				case 90:
					parametros.y = (float) Math.log10(periodos.getMin90());
					break;

				case 120:
					parametros.y = (float) Math.log10(periodos.getMin120());
					break;

				}

				parametros.x2 = (float) Math.log10(intervalo);
				parametros.x1 = (float) Math.log10(periodos.getTr());
				parametros.x1y = parametros.x1 * parametros.y;
				parametros.x2y = parametros.x2 * parametros.y;
				parametros.x12 = (float) Math.pow(parametros.x1, 2);
				parametros.x22 = (float) Math.pow(parametros.x2, 2);
				parametros.x1x2 = parametros.x1 * parametros.x2;

				parametros.save();

			}

		}
		setEsperaFinalizado(true);
	}

	public static CalculoParametros[] all() {
		int i = 0;
		CalculoParametros[] parametros = null;
		ResultSet resultados = null;
		String query = "SELECT count(1) FROM " + tabla;
		i = count(query);
		parametros = new CalculoParametros[i];
		i = 0;
		query = "SELECT * FROM " + tabla;
		Conectar con = new Conectar();
		try {
			resultados = con.execute(query);
			while (resultados.next()) {
				parametros[i++] = instanciar(resultados);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
		return parametros;
	}

	public static CalculoParametros instanciar(ResultSet resultados) {
		CalculoParametros parametros = new CalculoParametros();
		int i = 0;
		try {
			parametros.id = resultados.getInt(campos[i++]);
			parametros.x2 = resultados.getFloat(campos[i++]);
			parametros.x1 = resultados.getFloat(campos[i++]);
			parametros.y = resultados.getFloat(campos[i++]);
			parametros.x1y = resultados.getFloat(campos[i++]);
			parametros.x2y = resultados.getFloat(campos[i++]);
			parametros.x12 = resultados.getFloat(campos[i++]);
			parametros.x22 = resultados.getFloat(campos[i++]);
			parametros.x1x2 = resultados.getFloat(campos[i++]);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parametros;
	}

	public static CalculoParametros[] find(String where) {
		String query = "SELECT * FROM " + tabla + " WHERE " + where;
		int i = Tabla.count(query);
		Conectar con = new Conectar();
		CalculoParametros[] parametros = null;
		try {
			parametros = new CalculoParametros[i];
			i = 0;
			ResultSet resultados = con.execute(query);
			parametros[i++] = instanciar(resultados);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return parametros;
	}

	public static CalculoParametros sum() {
		CalculoParametros parametros = null;
		String query = "SELECT -1 id, sum(x2) x2,sum(x1) x1,sum(y) y,sum(x1y) x1y,sum(x2y) x2y,sum(x12) x12,sum(x22) x22,sum(x1x2) x1x2 FROM calculoParametros";
		Conectar con = new Conectar();
		try {
			ResultSet resultados = con.execute(query);
			if (resultados.next())
				parametros = instanciar(resultados);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return parametros;
	}

	public static void guardarSumatoria(int pN, double pX2, double pX1, double pY,
			double pX1y, double pX2y, double pX12, double pX22, double pX1x2) {

		FileOutputStream file;
		try {
			file = new FileOutputStream("sumatoria.txt");
			BufferedOutputStream bufer = new BufferedOutputStream(file);
			
			bufer.write(("N:" + pN + '\n').getBytes());
			bufer.write(("x2:" + pX2 + '\n').getBytes());
			bufer.write(("x1:" + pX1 + '\n').getBytes());
			bufer.write(("y:" + pY + '\n').getBytes());
			bufer.write(("x1y:" + pX1y + '\n').getBytes());
			bufer.write(("x2y:" + pX2y + '\n').getBytes());
			bufer.write(("x1^2:" + pX12 + '\n').getBytes());
			bufer.write(("x2^2:" + pX22 + '\n').getBytes());
			bufer.write(("x1x2:" + pX1x2 + '\n').getBytes());
			
			bufer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int contar(){
		return Sensor.count("SELECT count(1) FROM "+ tabla);
	}

	public static void generarDatos() {
		new CalculoParametros().generar();
	}

	public static String getTabla() {
		return tabla;
	}

	public static void setTabla(String tabla) {
		CalculoParametros.tabla = tabla;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX1y() {
		return x1y;
	}

	public void setX1y(float x1y) {
		this.x1y = x1y;
	}

	public float getX2y() {
		return x2y;
	}

	public void setX2y(float x2y) {
		this.x2y = x2y;
	}

	public float getX12() {
		return x12;
	}

	public void setX12(float x12) {
		this.x12 = x12;
	}

	public float getX22() {
		return x22;
	}

	public void setX22(float x22) {
		this.x22 = x22;
	}

	public float getX1x2() {
		return x1x2;
	}

	public void setX1x2(float x1x2) {
		this.x1x2 = x1x2;
	}

	public static String[] getCampos() {
		return campos;
	}
}
