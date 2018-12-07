/**
 * 
 */
package es.facturcam;

import java.awt.EventQueue;

import es.facturcam.principal.PrincipalFrame;

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
