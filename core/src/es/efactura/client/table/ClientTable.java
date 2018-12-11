package es.efactura.client.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import es.efactura.client.model.ClientDto;
import es.efactura.table.ObjectTableModel;

public class ClientTable extends ObjectTableModel<ClientDto> {

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(ClientDto t, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return t.getId();
		case 1:
			return t.getName();
		case 2:
			return t.getCif();
		case 3:
			final JButton button = new JButton("Editar");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(button),
							"Button clicked for row");
				}
			});
			return button;
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Id";
		case 1:
			return "Name";
		case 2:
			return "Cif";
		case 3:
			return "Acciones";
		}
		return null;
	}

}