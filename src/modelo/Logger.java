package modelo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {

	private File f;
	private FileWriter fw;
	private BufferedWriter bw;
	private PrintWriter pw;

	private static final String archivo = "logger.log";

	public Logger() {
		abrir();
	}

	private boolean abrir() {
		boolean correcto = true;
		f = new File(archivo);
		try {
			fw = new FileWriter(f,true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
		} catch (IOException e) {
			e.printStackTrace();
			correcto = false;
		}
		return correcto;
	}

	private void cerrar() {
		try {
			pw.close();
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void add(String linea) {
		Logger log = new Logger();
		log.pw.append(getDate()+linea+"\n");
		log.cerrar();

	}
	
	public static String getDate(){
		return new Date()+" : ";
	}
	
	public static void delete(){
		File f = new File(archivo);
		f.delete();
	}
}
