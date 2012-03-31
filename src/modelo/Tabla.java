package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tabla extends Sensor {

	// Campos de la tabla
	private String fecha;
	private String hora;
	private float idx_tmp_humedad;
	private float temp_ext;
	private float factor_viento;
	private float temp_max;
	private float temp_min;
	private int hum_ext;
	private float rocio;
	private float vel_viento;
	private float max;
	private String direccion;
	private float lluvia = (float) 0.0;
	private float pres_atmos;
	private float hum_int;
	private int var_1;
	private int var_2;

	public int sensor_Id;
	public String sensor_Name;
	public float sensor_Value;
	public String sensor_Flag;
	public static String stats_option = "AVG";

	// Propiedades de la tabla
	/*
	 * Originalmente se utiliza la DB sensor_db y la tabla es sensor_table:
	 * TimeStamp, Sensor_Id, Sensor_Name, Sensor_Value, Sensor_Flag Version 2,
	 * se mueve hacia Gomez_Farias_db->gf_table: fecha, hora, idx_tmp_humedad,
	 * temp_ext, factor_viento, temp_max, temp_min, hum_ext, rocio, vel_viento,
	 * max, direccion, lluvia, pres_atmos, hum_int, var1, var2
	 * 
	 * public static String[] campos_tabla = { "Sensor_Id", "Sensor_Name",
	 * "Sensor_Value", "Sensor_Flag" };
	 */
	// public static String nombre_tabla = "sensor_table";
	public static String tabla = "gf_tabla";

	public static String[] campos_tabla = { "idx_tmp_humedad, temp_ext, factor_viento, temp_max, temp_min, hum_ext, rocio, vel_viento, max, direccion, lluvia, pres_atmos, hum_int, var1, var2" };

	public Tabla() {
	}

	// Modificar parametros
	public Tabla(int pId, String pName, float pValue, String pFlag) {

		sensor_Id = pId;
		sensor_Name = pName;
		sensor_Value = pValue;
		sensor_Flag = pFlag;

	}

	// Modificar parametros
	public Tabla(int pId, float pValue, String pFlag) {

		sensor_Id = pId;
		sensor_Value = pValue;
		sensor_Flag = pFlag;

	}

	public static int obtenerMayor(Tabla[] sensores, String fechaIni,
			String fechaFin, String estados[]) {
		int mayor = 0;
		int i = 0;
		for (Tabla sensor : sensores) {
			if (sensor != null) {
				int cantidad = Tabla.contarSensor(sensor.sensor_Id, fechaIni,
						fechaFin, estados[i]);
				if (mayor < cantidad)
					mayor = cantidad;
				i++;
			}
		}

		return mayor;
	}

	public static float obtenerMayorProm(Tabla[] sensores, String fechaIni,
			String fechaFin, String estados[]) {
		float mayor = 0;
		int i = 0;
		for (Tabla sensor : sensores) {
			if (sensor != null) {
				float cantidad = Tabla.promedioSensor(sensor.sensor_Id,
						fechaIni, fechaFin, estados[i]);
				if (mayor < cantidad)
					mayor = cantidad;
				i++;
			}
		}

		return mayor;
	}

	// count(String where)

	public static int contarSensor(int id, String fechaIni, String fechaFin,
			String estado) {
		int total = 0;
		String query = "SELECT COUNT(Sensor_Id) as total FROM sensor_table WHERE sensor_Id="
				+ id;
		if (fechaIni != null)
			query += " AND TimeStamp>STR_TO_DATE('" + fechaIni
					+ "','%d/%m/%Y')";
		if (fechaFin != null)
			query += " AND TimeStamp<STR_TO_DATE('" + fechaFin
					+ "','%d/%m/%Y')";
		if (estado != null)
			query += " AND Sensor_Flag='" + estado + "'";
		Conectar conexion = new Conectar();
		try {
			ResultSet resultados = conexion.execute(query);
			if (resultados.next())
				total = resultados.getInt("total");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conexion.cerrar();
		return total;
	}

	public static float promedioSensor(int id, String fechaIni,
			String fechaFin, String estado) {
		float total = 0;
		if (fechaIni != null && fechaFin != null) {
			String ini = "STR_TO_DATE('" + fechaIni + "','%d/%m/%Y')";
			String fin = "STR_TO_DATE('" + fechaFin + "','%d/%m/%Y')";
			String query = "SELECT COUNT(Sensor_Id)/DATEDIFF(" + fin + ","
					+ ini + ") as promedio FROM sensor_table WHERE sensor_Id="
					+ id;
			query += " AND TimeStamp>" + ini;
			query += " AND TimeStamp<" + fin;
			if (estado != null)
				query += " AND Sensor_Flag='" + estado + "'";
			Conectar conexion = new Conectar();
			try {
				ResultSet resultados = conexion.execute(query);
				if (resultados.next())
					total = resultados.getFloat("promedio");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conexion.cerrar();
		}
		return total;
	}

	public static Tabla[] buscarTodos() {
		int index = 0;
		int longitud = 0;
		Tabla[] sensores = null;
		Conectar con = new Conectar();
		String query = "SELECT count(Sensor_Id) as numero FROM " + Tabla.tabla;
		ResultSet resultados;
		try {
			resultados = con.execute(query);
			if (resultados.next()) {
				longitud = resultados.getInt("numero");
				sensores = new Tabla[longitud];
				query = "SELECT * FROM " + Tabla.tabla;
				resultados = con.execute(query);
				while (resultados.next()) {
					sensores[index] = (Tabla) Tabla.instanciar(resultados);
					index++;
				}
			}
		} catch (SQLException ex) {
			Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
		}
		con.cerrar();
		return sensores;
	}

	public static Tabla find(int id) {
		Tabla[] sensores = null;
		String query = "SELECT * from " + Tabla.tabla + " WHERE sensor_id="
				+ id + " GROUP BY sensor_name";
		sensores = find(query);
		return sensores[0];
	}

	public static Tabla[] find(String query) {
		Tabla[] sensores = null;
		int i = 0;
		Conectar conexion = new Conectar();
		try {
			ResultSet resultados = conexion.execute(query);
			while (resultados.next())
				i++;
			sensores = new Tabla[i];
			i = 0;
			resultados = conexion.execute(query);
			while (resultados.next())
				sensores[i++] = (Tabla) Tabla.instanciar(resultados);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conexion.cerrar();
		return sensores;
	}

	public static float find(String sensor_name, String fecha_ini,
			String fecha_end) {

		System.out.println(sensor_name + " " + fecha_ini + " " + fecha_end);
		// Precipitacion sensor = null;
		float temp_data = 0.00f;
		// String temp = "AVG";

		String query = "SELECT " + "" + Tabla.stats_option + "" + "("
				+ sensor_name + ") from " + Tabla.tabla
				+ " WHERE fecha between '" + fecha_ini + "' AND '" + fecha_end
				+ "'";
		Conectar conexion = new Conectar();
		// System.out.println("was getting data out from table with date constraints...");
		try {
			ResultSet resultados = conexion.execute(query);
			while (resultados.next())

				temp_data = (Float) resultados.getFloat(1);
			// System.out.println("was gettin...  "+(Float)
			// resultados.getFloat(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conexion.cerrar();
		return temp_data;
	}

	public static Sensor instanciar(ResultSet resultados) {
		Tabla sensor = new Tabla();
		try {
			sensor.setFecha(resultados.getString("fecha"));
			sensor.setHora(resultados.getString("hora"));
			sensor.setIdx_tmp_humedad(resultados.getFloat("idx_temp_hum"));
			sensor.setTemp_ext(resultados.getFloat("temp_ext"));
			sensor.setFactor_viento(resultados.getFloat("factor_viento"));
			sensor.setTemp_max(resultados.getFloat("temp_max"));
			sensor.setTemp_min(resultados.getFloat("temp_min"));
			sensor.setHum_ext(resultados.getInt("hum_ext"));
			sensor.setRocio(resultados.getFloat("rocio"));
			sensor.setVel_viento(resultados.getFloat("vel_viento"));
			sensor.setMax(resultados.getFloat("max"));
			sensor.setDireccion(resultados.getString("direccion"));
			sensor.setLluvia(resultados.getFloat("lluvia"));
			sensor.setPres_atmos(resultados.getFloat("pres_atmos"));
			sensor.setHum_int(resultados.getFloat("hum_int"));
			sensor.setVar_1(resultados.getInt("var_1"));
			sensor.setVar_2(resultados.getInt("var_2"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sensor;

	}

	public static Tabla[] buscarNombres() {
		int index = 0;
		int longitud = 0;
		Tabla[] sensores = null;
		Conectar con = new Conectar();
		String query = "SELECT count(1) FROM " + Tabla.tabla
				+ " GROUP BY (Sensor_Name)";
		longitud=count(query);
		try {
			sensores = new Tabla[longitud];
			query = "SELECT * FROM " + Tabla.tabla + " GROUP BY (Sensor_Name)";
			ResultSet resultados = con.execute(query);
			while (resultados.next()) {
				sensores[index] = (Tabla) Tabla.instanciar(resultados);
				index++;
			}
		} catch (SQLException ex) {
			Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
		}
		con.cerrar();
		return sensores;
	}

	// Obtiene las fechas en las que se registro alguna actividad en un anio
	// especifico
	public static String[] getDates(int pYear) {
		String[] dates = null;
		Conectar con = new Conectar();

		String query = "SELECT  count(DISTINCT fecha) FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')="
				+ pYear + " ORDER BY fecha asc";
		int i = count(query);
		try {

			dates = new String[i];
			i = 0;
			query = "SELECT DISTINCT fecha FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')="
					+ pYear + " ORDER BY fecha asc";
			ResultSet resultados = con.execute(query);
			while (resultados.next()) {
				dates[i++] = resultados.getString("fecha");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return dates;
	}

	// Obtiene las horas en las que se registraron alguna actividad en un dia
	// especifico
	public static String[] getHours(String pDate) {
		String[] hours = null;
		String query = "SELECT count(DISTINCT hora) FROM gf_tabla WHERE fecha='"
				+ pDate + "' ORDER BY hora ASC";

		int i = count(query);
		hours = new String[i];
		Conectar con = new Conectar();
		try {
			query = "SELECT DISTINCT hora FROM gf_tabla WHERE fecha='" + pDate
					+ "' ORDER BY hora ASC";
			i = 0;
			ResultSet resultados = con.execute(query);
			while (resultados.next()) {
				hours[i++] = resultados.getString("hora");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.cerrar();
		return hours;
	}

	// Obtiene la suma maxima de precipitacion en un lapso de tiempo de un dia
	// especifico
	public static String[] getMaxIn(String pDate, String pTime, int pLapse) {

		String[] sum = new String[4];
		boolean correcto = false;
		correcto = false;
		int horas = 0;
		int minutos = pLapse;
		while (!correcto) {
			if (minutos >= 60) {
				horas++;
				minutos -= 60;
			} else
				correcto = true;
		}
		String intervalo = horas + ":" + minutos + ":0";

		String query = "SELECT DATE_FORMAT(fecha,'%Y'), DATE_FORMAT(fecha,'%M'), DATE_FORMAT(fecha,'%d'), SUM(lluvia) FROM gf_tabla"
				+ " WHERE CONCAT(fecha,' ',hora) BETWEEN CONCAT('"
				+ pDate
				+ "',' ','"
				+ pTime
				+ "') AND ADDTIME(CONCAT('"
				+ pDate
				+ "',' ','" + pTime + "'),'" + intervalo + "')";

		// System.out.println(query);
		Conectar con = new Conectar();
		try {
			ResultSet resultados = con.execute(query);
			if (resultados.next()) {
				for (int i = 1; i < 4; i++)
					sum[i] = resultados.getString(i + 1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		con.cerrar();
		return sum;
	}

	public static String[] getDateTime(int pYear, int pMonth){
		//String query ="SELECT COUNT(1) FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')="+pYear;
		String month;
		if (pMonth<10)
			month="0"+pMonth;
		else
			month=pMonth+"";
		String query ="SELECT COUNT(1) FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')="+pYear+" AND DATE_FORMAT(fecha,'%m')="+month + " AND lluvia>0";
		int i = count(query);
		String[] fechas=new String[i];
		//query="SELECT CONCAT(fecha,' ',hora) as fechas FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')="+pYear;
		query="SELECT CONCAT(fecha,' ',hora) as fechas FROM gf_tabla WHERE DATE_FORMAT(fecha,'%Y')="+pYear+" AND DATE_FORMAT(fecha,'%m')="+month+" AND lluvia>0";
		Conectar con = new Conectar();
		try {
			i=0;
			ResultSet resultados = con.execute(query);
			while (resultados.next()){
				fechas[i++]=resultados.getString("fechas");
			}
		} catch (SQLException e) {
			System.err.println("Error al ejecutar: "+query);
		}	
		con.cerrar();
		return fechas;
	}
	
	
	
	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public float getIdx_tmp_humedad() {
		return idx_tmp_humedad;
	}

	public void setIdx_tmp_humedad(float idx_tmp_humedad) {
		this.idx_tmp_humedad = idx_tmp_humedad;
	}

	public float getTemp_ext() {
		return temp_ext;
	}

	public void setTemp_ext(float temp_ext) {
		this.temp_ext = temp_ext;
	}

	public float getFactor_viento() {
		return factor_viento;
	}

	public void setFactor_viento(float factor_viento) {
		this.factor_viento = factor_viento;
	}

	public float getTemp_max() {
		return temp_max;
	}

	public void setTemp_max(float temp_max) {
		this.temp_max = temp_max;
	}

	public float getTemp_min() {
		return temp_min;
	}

	public void setTemp_min(float temp_min) {
		this.temp_min = temp_min;
	}

	public int getHum_ext() {
		return hum_ext;
	}

	public void setHum_ext(int hum_ext) {
		this.hum_ext = hum_ext;
	}

	public float getRocio() {
		return rocio;
	}

	public void setRocio(float rocio) {
		this.rocio = rocio;
	}

	public float getVel_viento() {
		return vel_viento;
	}

	public void setVel_viento(float vel_viento) {
		this.vel_viento = vel_viento;
	}

	public float getMax() {
		return max;
	}

	public void setMax(float max) {
		this.max = max;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public float getLluvia() {
		return lluvia;
	}

	public void setLluvia(float lluvia) {
		this.lluvia = lluvia;
	}

	public float getPres_atmos() {
		return pres_atmos;
	}

	public void setPres_atmos(float pres_atmos) {
		this.pres_atmos = pres_atmos;
	}

	public float getHum_int() {
		return hum_int;
	}

	public void setHum_int(float hum_int) {
		this.hum_int = hum_int;
	}

	public int getVar_1() {
		return var_1;
	}

	public void setVar_1(int var_1) {
		this.var_1 = var_1;
	}

	public int getVar_2() {
		return var_2;
	}

	public void setVar_2(int var_2) {
		this.var_2 = var_2;
	}

	/*
	 * public static boolean agregarTablaResultadosMax(String pYear, String
	 * pMonth, String pDay, String pCampo, String pPrecipitacion) { boolean
	 * correcto = true; Conectar conectar = new Conectar(); String query =
	 * "SELECT * FROM tablaPrecipitacion WHERE anio=" + pYear + " AND mes='" +
	 * pMonth + "' AND dia=" + pDay; try { ResultSet resultados =
	 * conectar.execute(query); if (resultados.next()) { query =
	 * "UPDATE tablaPrecipitacion SET " + pCampo + "=" + pPrecipitacion +
	 * " WHERE anio=" + pYear + " AND mes='" + pMonth + "' AND dia=" + pDay; }
	 * else { query = "INSERT INTO tablaPrecipitacion (anio, mes, dia," + pCampo
	 * + ") VALUES (" + pYear + ",'" + pMonth + "'," + pDay + "," +
	 * pPrecipitacion + ")"; } conectar.execute(query); } catch (SQLException
	 * e1) { e1.printStackTrace(); } conectar.cerrar(); return correcto; }
	 */
}
