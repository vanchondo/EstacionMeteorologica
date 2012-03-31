package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PeriodoRetorno extends Sensor implements Estaciones {

	public static String tabla = "periodosRetorno";
	public static final String[] campos = getHeaders(PeriodoRetorno.tabla);

	private int orden;
	private float tr;
	private float min5;
	private float min10;
	private float min20;
	private float min60;
	private float min90;
	private float min120;

	public PeriodoRetorno() {

	}

	public PeriodoRetorno(int pOrden, float pTr, float pMin5, float pMin10,
			float pMin20, float pMin60, float pMin90, float pMin120) {
		orden = pOrden;
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
		query += ") VALUES (" + orden + "," + tr + "," + min5 + "," + min10
				+ "," + min20 + "," + min60 + "," + min90 + "," + min120 + ")";
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
		Conectar con = new Conectar();
		String query = "CREATE TABLE IF NOT EXISTS periodosRetorno ("
				+ "orden          INT PRIMARY KEY,"
				+ "tr             FLOAT NOT NULL,"
				+ "min5           FLOAT DEFAULT -1,"
				+ "min10          FLOAT DEFAULT -1,"
				+ "min20          FLOAT DEFAULT -1,"
				+ "min60          FLOAT DEFAULT -1,"
				+ "min90          FLOAT DEFAULT -1,"
				+ "min120         FLOAT DEFAULT -1" + ")";
		try {
			con.execute(query);
			Logger.add(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
	}

	@Override
	public void generar() {
		String query = "";
		int m = 0; // No. de Orden
		int n = 0; // Total de elementos de la tabla intensidad_mm_h
		float tr = 0; // Tiempo de retorno
		query = "SELECT count(1) FROM " + Intensidad_mm_h.tabla
				+ " ORDER BY min120 desc";
		n = count(query);
		Conectar con = new Conectar();
		try {
			for (m = 0; m < n; m++) {
				tr = (float) (n + 1) / (float) (m + 1);
				// No se usa un objeto porque utilizaria mas memoria y mas
				// conexiones a la base de datos.
				query = "INSERT INTO " + tabla + " VALUES(" + (m + 1) + ","
						+ tr;
				query += ",(SELECT min5 FROM " + Intensidad_mm_h.tabla
						+ " ORDER BY min120 desc LIMIT " + m + ",1)";
				query += ",(SELECT min10 FROM " + Intensidad_mm_h.tabla
						+ " ORDER BY min120 desc LIMIT " + m + ",1)";
				query += ",(SELECT min20 FROM " + Intensidad_mm_h.tabla
						+ " ORDER BY min120 desc LIMIT " + m + ",1)";
				query += ",(SELECT min60 FROM " + Intensidad_mm_h.tabla
						+ " ORDER BY min120 desc LIMIT " + m + ",1)";
				query += ",(SELECT min90 FROM " + Intensidad_mm_h.tabla
						+ " ORDER BY min120 desc LIMIT " + m + ",1)";
				query += ",(SELECT min120 FROM " + Intensidad_mm_h.tabla
						+ " ORDER BY min120 desc LIMIT " + m + ",1)";
				query += ")";
				con.execute(query);
				//TODO:Obtener los valores para poder guardarlo en logger 
				Logger.add(query);
			}
			// Precipitacion.agregarIntensidad(year, minutos);
		} catch (SQLException e1) {
			e1.printStackTrace();

		}
		con.cerrar();
		setEsperaFinalizado(true);
	}

	public static void crearTabla() {
		new PeriodoRetorno().createTable();
	}

	public static PeriodoRetorno[] all() {
		int i = 0;
		PeriodoRetorno[] periodos = null;
		ResultSet resultados = null;
		String query = "SELECT count(1) FROM " + tabla;
		i = count(query);
		periodos = new PeriodoRetorno[i];
		i = 0;
		query = "SELECT * FROM " + tabla;
		Conectar con = new Conectar();
		try {
			resultados = con.execute(query);
			while (resultados.next()) {
				periodos[i++] = instanciar(resultados);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
		return periodos;
	}

	public static PeriodoRetorno instanciar(ResultSet resultados) {
		PeriodoRetorno periodo = new PeriodoRetorno();
		int i = 0;
		try {
			periodo.orden = resultados.getInt(campos[i++]);
			periodo.tr = resultados.getFloat(campos[i++]);
			periodo.min5 = resultados.getFloat(campos[i++]);
			periodo.min10 = resultados.getFloat(campos[i++]);
			periodo.min20 = resultados.getFloat(campos[i++]);
			periodo.min60 = resultados.getFloat(campos[i++]);
			periodo.min90 = resultados.getFloat(campos[i++]);
			periodo.min120 = resultados.getFloat(campos[i++]);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return periodo;
	}

	public static PeriodoRetorno[] find(String where) {
		Conectar con = new Conectar();
		int i = 0;
		PeriodoRetorno[] periodos = null;
		try {
			i = Sensor.count("SELECT count(1) FROM " + tabla + " WHERE " + where);

			periodos = new PeriodoRetorno[i];
			i = 0;
			ResultSet resultados = con.execute("SELECT * FROM " + tabla
					+ " WHERE " + where);
			while (resultados.next())
				periodos[i++] = instanciar(resultados);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return periodos;
	}


	public static int contar(){
		return Sensor.count("SELECT count(1) FROM "+ tabla);
	}	
	
	public static void generarDatos() {
		new PeriodoRetorno().generar();
	}

	public static String getTabla() {
		return tabla;
	}

	public static void setTabla(String tabla) {
		PeriodoRetorno.tabla = tabla;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public float getTr() {
		return tr;
	}

	public void setTr(float tr) {
		this.tr = tr;
	}

	public float getMin5() {
		return min5;
	}

	public void setMin5(float min5) {
		this.min5 = min5;
	}

	public float getMin10() {
		return min10;
	}

	public void setMin10(float min10) {
		this.min10 = min10;
	}

	public float getMin20() {
		return min20;
	}

	public void setMin20(float min20) {
		this.min20 = min20;
	}

	public float getMin60() {
		return min60;
	}

	public void setMin60(float min60) {
		this.min60 = min60;
	}

	public float getMin90() {
		return min90;
	}

	public void setMin90(float min90) {
		this.min90 = min90;
	}

	public float getMin120() {
		return min120;
	}

	public void setMin120(float min120) {
		this.min120 = min120;
	}

	public static String[] getCampos() {
		return campos;
	}
}
