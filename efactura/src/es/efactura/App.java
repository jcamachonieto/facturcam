/**
 * 
 */
package es.efactura;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import es.efactura.principal.PrincipalFrame;

/**
 * @author jcamacho
 *
 */
public class App {
	
	public static Properties properties;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		properties = new Properties();
		FileInputStream input = new FileInputStream("C:\\efactura\\config.properties");
		
		// load a properties file
		properties.load(input);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrincipalFrame principalFrame = new PrincipalFrame();
					principalFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
