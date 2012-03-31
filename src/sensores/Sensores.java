package sensores;

import javax.swing.UIManager;

public class Sensores {

	public static void main(String[] args) {		
		// Recorre todos los temas para ventanas instalados
		
		for (UIManager.LookAndFeelInfo laf : UIManager
				.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(laf.getName())) {
				try {
					// Selecciona el tema Nimbus por default
					UIManager.setLookAndFeel(laf.getClassName());
				} catch (Exception ex) {
					System.err
							.println("No se ha encontrado " + ex.getMessage());
				}
			}
		}
		
		/*
		String max =JOptionPane.showInputDialog(null, "Conexiones maximas", "50");
		if (max!=null){
			Conectar.setConexionesMax(Integer.parseInt(max));
			System.out.println("Conexiones maximas: "+max);
			
		}
		*/
		new Splash();
	}

}
