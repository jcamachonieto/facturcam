/**
 * 
 */
package es.efactura;

import java.awt.EventQueue;

import es.efactura.principal.PrincipalFrame;

/**
 * @author jcamacho
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
