package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import sensores.Principal;

public class Precipitacion extends Sensor implements Estaciones {

	public static String tabla = "precipitacion";
	public static final String[] campos = getHeaders(Precipitacion.tabla);
	public static boolean finalizado = false;
	private static int anioIn; // Se usan para obtener la precipitacion maxima
	private static int anioFn; // entre estos anios

	private int anio;
	private String mes;
	private int dia;
	private float min5 = -1;
	private float min10 = -1;
	private float min20 = -1;
	private float min60 = -1;
	private float min90 = -1;
	private float min120 = -1;

	public Precipitacion() {

	}

	public Precipitacion(int pAnio, String pMes, int pDia, float pMin5,
			float pMin10, float pMin20, float pMin60, float pMin90,
			float pMin120) {
		anio = pAnio;
		mes = pMes;
		dia = pDia;
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
		if (find(anio, mes, dia) == null) {
			Conectar con = new Conectar();
			String query = "INSERT INTO " + tabla + "( ";
			for (String campo : campos) {
				query += campo + ",";
			}
			query = query.substring(0, query.length() - 1);
			query += ") VALUES (" + anio + ",'" + mes + "'," + dia + "," + min5
					+ "," + min10 + "," + min20 + "," + min60 + "," + min90
					+ "," + min120 + ")";
			try {
				con.execute(query);
				Logger.add(query);
			} catch (SQLException e) {
				e.printStackTrace();
				correcto = false;
			}
			con.cerrar();
		} else {
			update("min5", min5 + "", tabla);
			update("min10", min10 + "", tabla);
			update("min20", min20 + "", tabla);
			update("min60", min60 + "", tabla);
			update("min90", min90 + "", tabla);
			update("min120", min120 + "", tabla);
		}
		return correcto;

	}

	@Override
	public void createTable() {
		Conectar con = new Conectar();
		String query = "CREATE TABLE IF NOT EXISTS  precipitacion ("
				+ " anio           INT NOT NULL,"
				+ " mes            VARCHAR(20) NOT NULL,"
				+ " dia            INT NOT NULL,"
				+ " min5           FLOAT DEFAULT -1,"
				+ " min10          FLOAT DEFAULT -1,"
				+ " min20          FLOAT DEFAULT -1,"
				+ " min60          FLOAT DEFAULT -1,"
				+ " min90          FLOAT DEFAULT -1,"
				+ " min120         FLOAT DEFAULT -1,"
				+ " UNIQUE (anio, mes, dia)" + ")";
		try {
			con.execute(query);
			Logger.add(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
	}

	public static int contar() {
		return Sensor.count("SELECT count(1) FROM " + tabla);
	}

	@Override
	public void generar() {
		int anioTmp = -1, diaTmp = -1;
		String mesTmp = "";
		float lluviaTmp;

		Conectar con = new Conectar();
		for (int anio = getAnioIn(); anio <= getAnioFn(); anio++) {
			for (int intervalo : Principal.intervalos) {
				Precipitacion precipitacion = new Precipitacion();
				String query = "SELECT getIntervalSum(" + anio + ","
						+ intervalo + ")";
				try {
					ResultSet resultados = con.execute(query);
					if (resultados.next()) {
						String resultado = resultados.getString(1);
						StringTokenizer token = new StringTokenizer(resultado,
								",");
						if (token.hasMoreTokens())
							anioTmp = Integer.parseInt(token.nextToken());
						if (token.hasMoreTokens())
							mesTmp = token.nextToken();
						if (token.hasMoreTokens())
							diaTmp = Integer.parseInt(token.nextToken());
						precipitacion = Precipitacion.find(anioTmp, mesTmp,
								diaTmp);
						if (precipitacion == null) {
							precipitacion = new Precipitacion();
							precipitacion.anio = anioTmp;
							precipitacion.mes = mesTmp;
							precipitacion.dia = diaTmp;
						}
						if (token.hasMoreTokens()) {
							lluviaTmp = Float.parseFloat(token.nextToken());
							switch (intervalo) {
							case 5:
								precipitacion.min5 = lluviaTmp;
								break;

							case 10:
								precipitacion.min10 = lluviaTmp;
								break;

							case 20:
								precipitacion.min20 = lluviaTmp;
								break;

							case 60:
								precipitacion.min60 = lluviaTmp;
								break;

							case 90:
								precipitacion.min90 = lluviaTmp;
								break;

							case 120:
								precipitacion.min120 = lluviaTmp;
								break;
							}
						}

					}
					if (precipitacion.anio != -1)
						precipitacion.save();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		}
		con.cerrar();
		setEsperaFinalizado(true);
	}

	public static void crearTabla() {
		new Precipitacion().createTable();
	}

	public static Precipitacion[] all() {
		int i = 0;
		Precipitacion[] precipitacion = null;
		ResultSet resultados = null;
		String query = "SELECT count(1) FROM " + tabla;
		i = count(query);
		precipitacion = new Precipitacion[i];
		i = 0;
		query = "SELECT * FROM " + tabla;
		Conectar con = new Conectar();
		try {
			resultados = con.execute(query);
			while (resultados.next()) {
				precipitacion[i++] = instanciar(resultados);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
		return precipitacion;
	}

	public static Precipitacion instanciar(ResultSet resultados) {
		Precipitacion periodo = new Precipitacion();
		int i = 0;
		try {
			periodo.anio = resultados.getInt(campos[i++]);
			periodo.mes = resultados.getString(campos[i++]);
			periodo.dia = resultados.getInt(campos[i++]);
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

	public static Precipitacion[] find(String where) {
		Conectar con = new Conectar();
		int i = 0;
		Precipitacion[] precipitaciones = null;
		try {
			i = Tabla
					.count("SELECT count(1) FROM " + tabla + " WHERE " + where);

			precipitaciones = new Precipitacion[i];
			i = 0;
			ResultSet resultados = con.execute("SELECT * FROM " + tabla
					+ " WHERE " + where);
			while (resultados.next())
				precipitaciones[i++] = instanciar(resultados);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return precipitaciones;
	}

	public static void generarDatos() {
		new Precipitacion().generar();
	}

	public static Precipitacion find(int pAnio, String pMes, int pDia) {
		Precipitacion[] precipitaciones = find("anio=" + pAnio + " AND mes='"
				+ pMes + "' AND dia=" + pDia);
		Precipitacion precipitacion = null;
		if (precipitaciones != null && precipitaciones.length > 0)
			precipitacion = precipitaciones[0];
		return precipitacion;
	}

	public void update(String campo, String value, String table) {
		String query = "UPDATE " + table + " SET " + campo + "=" + value
				+ " WHERE anio=" + anio + " AND mes='" + mes + "' AND dia="
				+ dia;
		Conectar con = new Conectar();
		try {
			con.execute(query);
			Logger.add(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
	}

	public static int getAnioIn() {
		return anioIn;
	}

	public static void setAnioIn(int anioIn) {
		Precipitacion.anioIn = anioIn;
	}

	public static int getAnioFn() {
		return anioFn;
	}

	public static void setAnioFn(int anioFn) {
		Precipitacion.anioFn = anioFn;
	}

	public static String getTabla() {
		return tabla;
	}

	public static void setTabla(String tabla) {
		Precipitacion.tabla = tabla;
	}

	public static boolean isFinalizado() {
		return finalizado;
	}

	public static void setFinalizado(boolean finalizado) {
		Precipitacion.finalizado = finalizado;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
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
