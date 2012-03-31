package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Intensidad_mm_h extends Sensor implements Estaciones {

	public static String tabla = "intensidad_mm_h";
	public static final String[] campos = getHeaders(Intensidad_mm_h.tabla);

	private int anio;
	private float min5;
	private float min10;
	private float min20;
	private float min60;
	private float min90;
	private float min120;

	public Intensidad_mm_h() {
	}

	public Intensidad_mm_h(int pAnio, float p5, float p10, float p20,
			float p60, float p90, float p120) {
		anio = pAnio;
		min5 = p5;
		min10 = p10;
		min20 = p20;
		min60 = p60;
		min90 = p90;
		min120 = p120;
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
		query += ") VALUES (" + anio + "," + min5 + "," + min10 + "," + min20
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
		Conectar con = new Conectar();
		String query = "CREATE TABLE IF NOT EXISTS intensidad_mm_h ("
				+ "anio           INT PRIMARY KEY,"
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
		Integer[] years = Sensor.getYears(Precipitacion.tabla);
		for (Integer year : years) {
			Intensidad_mm_h intensidad = new Intensidad_mm_h(year, -1, -1, -1,
					-1, -1, -1);
			Precipitacion[] precipitaciones = Precipitacion
					.find("anio=" + year);
			for (Precipitacion precipitacion : precipitaciones) {
				if (intensidad.min5 == -1 || intensidad.min5 == 0.0)
					intensidad.min5 = precipitacion.getMin5();
				if (intensidad.min10 == -1 || intensidad.min10 == 0.0)
					intensidad.min10 = precipitacion.getMin10();
				if (intensidad.min20 == -1 || intensidad.min20 == 0.0)
					intensidad.min20 = precipitacion.getMin20();
				if (intensidad.min60 == -1 || intensidad.min60 == 0.0)
					intensidad.min60 = precipitacion.getMin60();
				if (intensidad.min90 == -1 || intensidad.min90 == 0.0)
					intensidad.min90 = precipitacion.getMin90();
				if (intensidad.min120 == -1 || intensidad.min120 == 0.0)
					intensidad.min120 = precipitacion.getMin120();

			}
			intensidad.save();
		}
		setEsperaFinalizado(true);
	}

	public static void crearTabla() {
		new Intensidad_mm_h().createTable();
	}

	public static Intensidad_mm_h[] all() {
		int i = 0;
		Intensidad_mm_h[] intensidades = null;
		ResultSet resultados = null;
		String query = "SELECT count(1) FROM " + tabla;
		i = count(query);
		intensidades = new Intensidad_mm_h[i];
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

	public static Intensidad_mm_h instanciar(ResultSet resultados) {
		Intensidad_mm_h intensidad_mm_h = new Intensidad_mm_h();
		int i = 0;
		try {
			intensidad_mm_h.anio = resultados.getInt(campos[i++]);
			intensidad_mm_h.min5 = resultados.getFloat(campos[i++]);
			intensidad_mm_h.min10 = resultados.getFloat(campos[i++]);
			intensidad_mm_h.min20 = resultados.getFloat(campos[i++]);
			intensidad_mm_h.min60 = resultados.getFloat(campos[i++]);
			intensidad_mm_h.min90 = resultados.getFloat(campos[i++]);
			intensidad_mm_h.min120 = resultados.getFloat(campos[i++]);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return intensidad_mm_h;
	}

	public static Intensidad_mm_h[] find(String where) {
		String query = "SELECT * FROM " + tabla + " WHERE " + where;
		int i = Tabla.count(query);
		Conectar con = new Conectar();
		Intensidad_mm_h[] intensidades = null;
		try {
			intensidades = new Intensidad_mm_h[i];
			i = 0;
			ResultSet resultados = con.execute(query);
			intensidades[i++] = instanciar(resultados);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return intensidades;
	}

	public static int contar() {
		return Sensor.count("SELECT count(1) FROM " + tabla);
	}

	public static void generarDatos() {
		new Intensidad_mm_h().generar();
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
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
