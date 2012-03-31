package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;

import sensores.Principal;

public class IntensidadPrecipitacionTR extends Sensor implements Estaciones {

	public static String tabla = "intensidadPrecipitacionTr";
	public static final String[] campos = getHeaders(tabla);
	public static final int anios[] = { 2, 10, 20, 50, 100, 500, 1000, 10000 };

	private int tr;
	private double min5;
	private double min10;
	private double min20;
	private double min60;
	private double min90;
	private double min120;

	public IntensidadPrecipitacionTR() {
	}

	public IntensidadPrecipitacionTR(int pTr, double pMin5, double pMin10,
			double pMin20, double pMin60, double pMin90, double pMin120) {
		tr = pTr;
		min5 = pMin5;
		min10 = pMin10;
		min20 = pMin20;
		min60 = pMin60;
		min90 = pMin90;
		min120 = pMin120;
	}

	@Override
	public boolean save() {
		boolean correcto = true;
		Conectar con = new Conectar();
		String query = "INSERT INTO " + tabla + "( ";
		for (String campo : campos) {
			query += campo + ",";
		}
		query = query.substring(0, query.length() - 1);
		query += ") VALUES (" + tr + "," + min5 + "," + min10 + "," + min20
				+ "," + min60 + "," + min90 + "," + min120 + ")";
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
		String query = "CREATE TABLE IF NOT EXISTS intensidadPrecipitacionTr("
				+ "tr     INT PRIMARY KEY," + "min5   FLOAT NOT NULL,"
				+ "min10  FLOAT NOT NULL," + "min20  FLOAT NOT NULL,"
				+ "min60  FLOAT NOT NULL," + "min90  FLOAT NOT NULL,"
				+ "min120 FLOAT NOT NULL" + ");";

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
		new IntensidadPrecipitacionTR().createTable();
	}

	public static int contar(){
		return Sensor.count("SELECT count(1) FROM "+ tabla);
	}	
	
	@Override
	public void generar() {
		/*
		 * Obtener i para cada intervalo y para cada anio
		 */
		// i=(K*Tr^m)/(INTERVALOS^n)
		double i;
		for (int anio : anios) {

			IntensidadPrecipitacionTR precipitacionTr = new IntensidadPrecipitacionTR();
			precipitacionTr.tr = anio;

			for (int intervalo : Principal.intervalos) {
				i = (Ecuacion.getK() * Math.pow(anio, Ecuacion.getM()))
						/ Math.pow(intervalo, Ecuacion.getN());

				switch (intervalo) {
				case 5:
					precipitacionTr.min5 = i;
					break;
				case 10:
					precipitacionTr.min10 = i;
					break;
				case 20:
					precipitacionTr.min20 = i;
					break;
				case 60:
					precipitacionTr.min60 = i;
					break;
				case 90:
					precipitacionTr.min90 = i;
					break;
				case 120:
					precipitacionTr.min120 = i;
					break;
				}
			}
			precipitacionTr.save();
		}
		setEsperaFinalizado(true);
	}

	public static IntensidadPrecipitacionTR[] all() {
		int i = 0;
		IntensidadPrecipitacionTR[] intensidades = null;
		ResultSet resultados = null;
		String query = "SELECT count(1) FROM " + tabla;
		i = count(query);
		intensidades = new IntensidadPrecipitacionTR[i];
		i = 0;
		query = "SELECT * FROM " + tabla;
		Conectar con = new Conectar();
		try {
			resultados = con.execute(query);
			while (resultados.next()) {
				intensidades[i++] = instanciar(resultados);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
		return intensidades;
	}

	public static IntensidadPrecipitacionTR instanciar(ResultSet resultados) {
		IntensidadPrecipitacionTR precipitacionTR = new IntensidadPrecipitacionTR();
		int i = 0;
		try {
			precipitacionTR.tr = resultados.getInt(campos[i++]);
			precipitacionTR.min5 = resultados.getFloat(campos[i++]);
			precipitacionTR.min10 = resultados.getFloat(campos[i++]);
			precipitacionTR.min20 = resultados.getFloat(campos[i++]);
			precipitacionTR.min60 = resultados.getFloat(campos[i++]);
			precipitacionTR.min90 = resultados.getFloat(campos[i++]);
			precipitacionTR.min120 = resultados.getFloat(campos[i++]);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return precipitacionTR;
	}

	public static IntensidadPrecipitacionTR[] find(String where) {
		String query = "SELECT * FROM " + tabla + " WHERE " + where;
		int i = Tabla.count(query);
		Conectar con = new Conectar();
		IntensidadPrecipitacionTR[] intensidades = null;
		try {
			intensidades = new IntensidadPrecipitacionTR[i];
			i = 0;
			ResultSet resultados = con.execute(query);
			intensidades[i++] = instanciar(resultados);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return intensidades;
	}

	public static void generarDatos() {
		new IntensidadPrecipitacionTR().generar();
	}

	public int getTr() {
		return tr;
	}

	public void setTr(int tr) {
		this.tr = tr;
	}

	public double getMin5() {
		return min5;
	}

	public void setMin5(float min5) {
		this.min5 = min5;
	}

	public double getMin10() {
		return min10;
	}

	public void setMin10(float min10) {
		this.min10 = min10;
	}

	public double getMin20() {
		return min20;
	}

	public void setMin20(float min20) {
		this.min20 = min20;
	}

	public double getMin60() {
		return min60;
	}

	public void setMin60(float min60) {
		this.min60 = min60;
	}

	public double getMin90() {
		return min90;
	}

	public void setMin90(float min90) {
		this.min90 = min90;
	}

	public double getMin120() {
		return min120;
	}

	public void setMin120(float min120) {
		this.min120 = min120;
	}

	public static String[] getCampos() {
		return campos;
	}

}
