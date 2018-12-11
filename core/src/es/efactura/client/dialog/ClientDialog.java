package es.efactura.client.dialog;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import es.efactura.client.model.ClientDto;

public class ClientDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JTextField id;
	JTextField name;
	JTextField cif;

	public ClientDialog(int width, int height) {
		setSize(width, height);

		setLocationRelativeTo(null);

		id = new JTextField();
		id.setVisible(false);
		
		name = new JTextField();
		cif = new JTextField();

		JPanel panel = new JPanel(new GridLayout(10, 10));

		panel.add(id);

		panel.add(new JLabel("Nombre:"));
		panel.add(name);

		panel.add(new JLabel("CIF/NIF:"));
		panel.add(cif);

		add(panel);
	}

	public void fillData(ClientDto t) {
		id.setText("" + t.getId());
		name.setText(t.getName());
		cif.setText(t.getCif());
	}
	
	public void clearData() {
		id.setText("");
		name.setText("");
		cif.setText("");
	}

}
