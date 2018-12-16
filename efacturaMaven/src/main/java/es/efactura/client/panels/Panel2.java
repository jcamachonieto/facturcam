package es.efactura.client.panels;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class Panel2 extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public Panel2() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("left:max(71dlu;default)"),
				ColumnSpec.decode("left:max(54dlu;default):grow"),
				ColumnSpec.decode("left:max(61dlu;default)"),
				FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("right:max(50dlu;default)"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("New label");
		add(lblNewLabel, "1, 1, right, center");
		
		textField = new JTextField();
		add(textField, "2, 1, fill, default");
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		add(btnNewButton, "3, 1");
		
		JButton btnNewButton_1 = new JButton("New button");
		add(btnNewButton_1, "5, 1");

	}

}
