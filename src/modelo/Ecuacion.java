package modelo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Ecuacion {

	private static double K;
	private static double N;
	private static double M;

	public static double[] resolverGauss(double[][] coeficientes,
			double[] soluciones) {
		@SuppressWarnings("unused")
		int i = 0, j = 0, s = 0, k = 0, h = 0, n = 0;
		double d;
		double m[][];
		double r[];
		double x[];

		for (@SuppressWarnings("unused")
		double sol : soluciones)
			n++;
		m = new double[n][n];
		r = new double[n];
		x = new double[n];
		for (i = 0; i <= n - 1; i++) {
			k = i + 1;
			r[i] = soluciones[i];
			x[i] = 0;
			for (j = 0; j <= n - 1; j++) {
				h = j + 1;
				m[i][j] = coeficientes[i][j];
			}
		}
		for (i = 0; i < n; i++) {
			for (j = i; j < n; j++) {
				if (i == j) {
					d = m[i][j];
					for (s = 0; s < n; s++) {
						m[i][s] = ((m[i][s]) / d);
					}
					r[i] = ((r[i]) / d);
				} else {
					d = m[j][i];
					for (s = 0; s < n; s++) {
						m[j][s] = m[j][s] - (d * m[i][s]);
					}
					r[j] = r[j] - (d * r[i]);
				}
			}
		}
		for (i = n - 1; i >= 0; i--) {
			double y = r[i];
			for (j = n - 1; j >= i; j--) {
				y = y - x[j] * m[i][j];
			}
			x[i] = y;
		}
		return x;
	}

	public static void generarDatos() {
		double sumX2, sumX1, sumY, sumX1Y, sumX2Y, sumX12, sumX22, sumX1X2;
		double[] resultadosEq = { 0, 0, 0 };

		CalculoParametros parametros = CalculoParametros.sum();
		sumX2 = parametros.getX2();
		sumX1 = parametros.getX2();
		sumY = parametros.getY();
		sumX1Y = parametros.getX1x2();
		sumX2Y = parametros.getX2y();
		sumX12 = parametros.getX12();
		sumX22 = parametros.getX22();
		sumX1X2 = parametros.getX1x2();
		int n = CalculoParametros.count("SELECT count(1) FROM "
				+ CalculoParametros.tabla);

		CalculoParametros.guardarSumatoria(n, sumX2, sumX1, sumY, sumX1Y,
				sumX2Y, sumX12, sumX22, sumX1X2);

		double[][] coeficientes = { { n, sumX1, sumX2 },
				{ sumX1, sumX2, sumX1X2 }, { sumX2, sumX1X2, sumX22 } };
		double[] soluciones = { sumY, sumX1Y, sumX2Y };
		resultadosEq = Ecuacion.resolverGauss(coeficientes, soluciones);
		K = Math.pow(10, resultadosEq[0]);
		M = resultadosEq[1];
		N = resultadosEq[2];
		FileOutputStream file;
		try {
			file = new FileOutputStream("resultadosEcuacion.txt");
			BufferedOutputStream bufer = new BufferedOutputStream(file);
			int i = 0;
			for (double resultado : resultadosEq) {
				bufer.write(("a" + i++ + ":" + resultado + '\n').getBytes());
			}
			bufer.write(("K=" + K + "\n").getBytes());
			bufer.write(("m=" + M + "\n").getBytes());
			bufer.write(("n=" + N + "\n").getBytes());
			bufer.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void leer() {
		try {
			FileInputStream file = new FileInputStream("resultadosEcuacion.txt");
			BufferedReader bufer = new BufferedReader(new InputStreamReader(
					file));
			String texto = "", textoTmp;

			while ((textoTmp = bufer.readLine())!=null){
				texto+=textoTmp;
			}
			file.close();

			StringTokenizer token = new StringTokenizer(texto, "\n");
			String[] datos = new String[token.countTokens()];
			int i = 0;
			while (token.hasMoreElements()) {
				datos[i] = token.nextToken();
				i++;
			}
			for (String dato : datos) {
				switch (dato.toCharArray()[0]) {
				case 'K':
					setK(Double.parseDouble(dato.substring(2, dato.length())));
					break;

				case 'M':
					setM(Double.parseDouble(dato.substring(2, dato.length())));
					break;

				case 'N':
					setN(Integer.parseInt(dato.substring(2, dato.length())));
					break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static double getK() {
		return K;
	}

	public static void setK(double k) {
		K = k;
	}

	public static double getN() {
		return N;
	}

	public static void setN(double n) {
		N = n;
	}

	public static double getM() {
		return M;
	}

	public static void setM(double m) {
		M = m;
	}
}
