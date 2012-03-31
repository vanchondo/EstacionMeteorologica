package modelo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import sensores.Espera;

public class Sensor {	
	
	private static String query;
	
	public static String[] getHeaders(String tabla) {
		ResultSet rs = null;
		Conectar con = new Conectar();
		ResultSetMetaData meta = null;
		String[] sensores = null;

		try {
			rs = con.execute("SELECT * FROM " + tabla
					+ " limit 1");
			meta = rs.getMetaData();

			int colmax = meta.getColumnCount();
			sensores = new String[colmax ];

			for (int j = 0; j < colmax ; j++) {
				sensores[j] = (meta.getColumnName(j + 1));
			}

		} catch (SQLException ex1) {
			ex1.printStackTrace();
		} 
		con.cerrar();
		return sensores;
	}
	
	public static void clean(String tabla) {
		Conectar con = new Conectar();
		query="TRUNCATE " + tabla;
		try {
			con.execute(query);
			Logger.add(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
	}	

	public static int count(String query) {
		Conectar con = new Conectar();
		int total = 0;
		ResultSet resultados = null;

		try {
			resultados = con.execute(query);
			if (resultados.next()) {
				total = resultados.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return total;
	}	

	public static void createTables(){
		Precipitacion.crearTabla();
		PeriodoRetorno.crearTabla();
		Intensidad_mm_h.crearTabla();
		CalculoParametros.crearTabla();
		
	}
	
	public static Integer[] getYears(String table) {
		Integer[] years = null;
		int i=0;
		Conectar con= new Conectar();
		String query="SELECT count(distinct anio) as anio FROM "+table;
		try {
			i=Tabla.count(query);
			years= new Integer[i];
			i=0;
			query="SELECT distinct anio as anio FROM "+table;
			ResultSet resultados = con.execute(query);
			while (resultados.next()){
				years[i++]=resultados.getInt("anio");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		con.cerrar();
		return years;
	}
	
	public void setEsperaFinalizado(boolean finalizado) {
		Espera.finalizado=finalizado;
		
	}
	
}
