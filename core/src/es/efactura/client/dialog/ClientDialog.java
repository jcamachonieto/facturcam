package es.efactura.client.dialog;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ClientDialog(int width, int height) {
	    setSize(width, height);

	    setLocationRelativeTo(null);
		
		JTextField field1 = new JTextField("");
        JTextField field2 = new JTextField("");
        
        JPanel panel = new JPanel(new GridLayout(10, 10));
        
        panel.add(new JLabel("Nombre:"));
        panel.add(field1);
        
        panel.add(new JLabel("CIF/NIF:"));
        panel.add(field2);
        
        add(panel);
	}
	
	

}
