package animaciones;

import java.util.Arrays;

import modelo.Tabla;
import processing.core.PApplet;
import processing.core.PImage;

public class Graficas extends PApplet {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	int x = 0;
	int y = 250;
	int tamano = 300;
	int ancho = 20;
	private int tipoGrafica = 0;
	float d = 0;
	// 1 Barras
	// 2 Circular
	// 3 Lineal
	private Integer sensores[];
	private String [] sensor_names;
	private String fechaIni;
	private String fechaFin;
	private String estados[];
	private boolean promedio = false;

	float[] z = new float[20];
	float[] w = new float[20];
	float segLength = 10;
	PImage a;

	public static int colores[][] = { { 255, 255, 0 }, { 255, 0, 0 },
			{ 0, 255, 255 }, { 0, 255, 0 }, { 0, 0, 255 } };

	public Graficas() {

	}

	public Graficas(int tipoGraf, String sensor_names[], String fechaIn,
			String fechaFn) {
		tipoGrafica = tipoGraf;
		this.sensor_names = sensor_names;
		fechaIni = fechaIn;
		fechaFin = fechaFn;		

	}
	
	public Graficas(int tipoGraf, Integer sen[], String fechaIn,
			String fechaFn, String states[]) {
		tipoGrafica = tipoGraf;
		setSensores(sen);
		fechaIni = fechaIn;
		fechaFin = fechaFn;
		estados = states;

	}

	public void setup() {
		size(tamano, tamano);
		background(0);
		fill(0, 255, 0);
		if (fechaIni != null && fechaFin != null)
			promedio = true;
		switch (tipoGrafica) {
		case 1:
			dibujarBarras(sensor_names, fechaIni, fechaFin, estados);
			break;

		case 2:
//			dibujarPie(sensor_names, fechaIni, fechaFin, estados);
			break;

		case 3:
	//		dibujarPuntos(sensor_names, fechaIni, fechaFin, estados);
			break;
		default:
			snake();
			break;
		}

	}

	private void dibujarBarras(String sensor_names[], String fechaIni, String fechaFin, String estados[]) {
		
		float [] tmp_arrayData=new float[sensor_names.length];
		float [] tmp_sortedarrayData;
		int sensor_names_length = sensor_names.length;
		
	//	System.out.println(sensor_names[0]+"  "+fechaIni+" "+ fechaFin );
		
		x = 0;
	//	float[] cantidad = new float[sensor_names.length];
		int j = sensor_names_length;
		//Precipitacion[] sensores = new Precipitacion[sensor_names.length];
		
		for (int i=0; i<sensor_names_length ; i++) {
			tmp_arrayData[i]= Tabla.find(sensor_names[i],fechaIni, fechaFin);
			//System.out.println( Precipitacion.find(sensor_names[i],fechaIni, fechaFin));
			//i++;
		}
		//tmp_arrayData[0]= Precipitacion.find(sensor_names[0],fechaIni, fechaFin);
		//float tmp_data= Precipitacion.find(sensor_names[0],fechaIni, fechaFin);
		tmp_sortedarrayData=tmp_arrayData;
		Arrays.sort(tmp_sortedarrayData);
	//	System.out.println(tmp_arrayData[tmp_arrayData.length-1]);
		
		//if(sensor_names.length>1)
			plano(tmp_arrayData[tmp_sortedarrayData.length-1]);
	
	for (int i=0; i<sensor_names_length ; i++) {
		rectangulo(sensor_names[i], tmp_arrayData[i], Graficas.colores[i], j);
		j--;
	}
	
/*		
		float max = 0;
		if (promedio)
			max = Precipitacion
					.obtenerMayorProm(sensores, fechaIni, fechaFin, estados);
		else
			max = Precipitacion.obtenerMayor(sensores, fechaIni, fechaFin, estados);
		plano(max);
		i = 0;
		
		for (Precipitacion sensor : sensores) {
			if (promedio)
				cantidad[i] = Precipitacion.promedioSensor(sensor.sensor_Id, fechaIni,
						fechaFin, estados[i]);
			else
				cantidad[i] = Precipitacion.contarSensor(sensor.sensor_Id, fechaIni,
						fechaFin, estados[i]);
			rectangulo(sensor.sensor_Name, cantidad[i], Graficas.colores[i]);
			i++;

		}
*/
	}

	private void plano(float mx) {
		stroke(255);
		d = mx / 10;
		// Eje Y
		line(x + 40, 10, x + 40, y + 10);
		// Eje X
		line(x + 30, y, x + 275, y);
		int i = 1;
		float rango = 0;
		// for (int v = y - 10; v >= 10; v -= 10) {
		for (int v = y - 10; v >= 10; v -= y / 10) {
			line(x + 35, v, x + 45, v);
			rango += d;
			// System.out.println(v);
			if (i % 2 == 0) {
				if (promedio)
					text(rango, x + 5, v);
				else
					text((int) rango, x + 5, v);
				i = 0;
			}

			i++;
		}
		stroke(0);
	}

	private void rectangulo(String name, float alto, int[] colores, int ncharts) {
		//int numbersof_bars = ncharts;
		x += 50;
		fill(colores[0], colores[1], colores[2]);
		rect(x, y, ancho, -(alto / d) * (y / 10));
		fill(255, 255, 255);
		if (promedio)
			text(alto, x, y + 10);
		else{
			int numero =(int)alto;
			text(numero, x, y + 10);
		}		
		text(name, x - 5, y + 20);
	}

	public void dibujarPie(Integer sensoresInt[], String fechaIni,
			String fechaFin, String estados[]) {
		float total = 0;
		// Obtenemos el total de sensores que usaremos
		int i = 0;
		for (int sensor : sensoresInt) {
			if (promedio)
				total += Tabla.promedioSensor(sensor, fechaIni, fechaFin,
						estados[i]);
			else
				total += Tabla.contarSensor(sensor, fechaIni, fechaFin,
						estados[i]);
			i++;
		}
		fill(255, 255, 255);
		text("Total: " + total, y - 50, 30);
		float[] angles = new float[sensoresInt.length];
		// int[] angles = { 30, 10, 45, 35, 60, 38, 75, 67 };

		// Obtenemos los grados de cada tipo de sensor
		i = 0;
		for (int sensor : sensoresInt) {
			if (promedio)
				angles[i] = (float) (Tabla.promedioSensor(sensor, fechaIni,
						fechaFin, estados[i]) * 360) / (float) total;
			else
				angles[i] = (float) (Tabla.contarSensor(sensor, fechaIni,
						fechaFin, estados[i]) * 360) / (float) total;
			i++;
		}

		float lastAngle = 0;
		smooth();
		noStroke();
		float diameter = (float) (min(width, height) * 0.75);
		noLoop(); // Run once and stop
		for (i = 0; i < angles.length; i++) {
			fill(colores[i][0], colores[i][1], colores[i][2]);
			arc(width / 2, height / 2, diameter, diameter, lastAngle, lastAngle
					+ radians(angles[i]));
			lastAngle += radians(angles[i]);
			text(Tabla.find(sensoresInt[i]).sensor_Name, y - 10,
					290 - (i * 12));
			if (promedio)
				text(Tabla.promedioSensor(sensoresInt[i], fechaIni, fechaFin,
						estados[i]), y - 50, 290 - (i * 12));
			else {
				int numero = (int) Tabla.contarSensor(sensoresInt[i],
						fechaIni, fechaFin, estados[i]);
				text(numero, y - 50, 290 - (i * 12));
			}
		}
	}

	public void dibujarPuntos(Integer sensoresInt[], String fechaIni,
			String fechaFin, String estados[]) {
		x = 0;
		float[] cantidad = new float[sensoresInt.length];
		int i = 0;
		Tabla[] sensores = new Tabla[sensoresInt.length];
		for (int sen : sensoresInt) {
			sensores[i] = Tabla.find(sen);
			i++;
		}

		int max = Tabla.obtenerMayor(sensores, fechaIni, fechaFin, estados);
		plano(max);
		i = 0;
		int[][] puntos = new int[sensoresInt.length][2];
		for (Tabla sensor : sensores) {
			if (promedio)
				cantidad[i] = Tabla.promedioSensor(sensor.sensor_Id, fechaIni,
						fechaFin, estados[i]);
			else
				cantidad[i] = Tabla.contarSensor(sensor.sensor_Id, fechaIni,
						fechaFin, estados[i]);
			x += 50;
			stroke(colores[i][0], colores[i][1], colores[i][2]);
			puntos[i][0] = x;
			puntos[i][1] = y - (int) (cantidad[i] / d) * (y / 10);
			point(puntos[i][0], puntos[i][1]);
			stroke(255, 255, 255);
			if (i > 0) {
				line(puntos[i - 1][0], puntos[i - 1][1], puntos[i][0],
						puntos[i][1]);
			}
			fill(colores[i][0], colores[i][1], colores[i][2]);
			stroke(colores[i][0], colores[i][1], colores[i][2]);
			line(puntos[i][0], puntos[i][1], puntos[i][0], y);
			if (promedio)
				text(cantidad[i], x - 10, y + 10);
			else{
				int numero =(int)cantidad[i];
				text(numero, x - 10, y + 10);
			}
			
			text(sensor.sensor_Name, x - 10, y + 20);
			i++;

		}
	}

	public void snake() {
		smooth();
		a = loadImage("img/dirt.jpg");
		background(226);
		image(a, 0, 0);
		dragSegment(0, mouseX - 8, mouseY - 8);
		for (int i = 0; i < z.length - 1; i++) {
			dragSegment(i + 1, z[i], w[i]);
		}
	}

	void dragSegment(int i, float xin, float yin) {
		float dx = xin - z[i];
		float dy = yin - w[i];
		float angle = atan2(dy, dx);
		z[i] = xin - cos(angle) * segLength;
		w[i] = yin - sin(angle) * segLength;
		// stroke(23, 79, 4, 220);

		pushMatrix();
		translate(z[i], w[i]);
		rotate(angle);

		int c;

		if (i % 3 == 1)
			c = color(0, 0, 0, 255);
		else if (i % 3 == 2)
			c = color(255, 255, 0, 255);
		else
			c = color(255, 0, 0, 255);

		stroke(c);
		strokeWeight(10);
		line(0, 0, segLength, 0);

		if (i == z.length - 1) {
			fill(c);
			noStroke();
			beginShape(TRIANGLES);
			vertex(0, 5);
			vertex(-2 * segLength, 0);
			vertex(0, -5);
			endShape();
		}

		if (i == 0) {
			// stroke(0, 255);
			noStroke();
			fill(0, 255);
			ellipse(segLength, -2, 3, 3);
			ellipse(segLength, 2, 3, 3);
			// point(segLength, -2);
			// point(segLength, 2);
		}

		popMatrix();
	}

	public Integer[] getSensores() {
		return sensores;
	}

	public void setSensores(Integer sensores[]) {
		this.sensores = sensores;
	}

}
