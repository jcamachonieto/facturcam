package es.efactura.client.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.efactura.client.model.ClientDataProvider;
import es.efactura.client.model.ClientDto;
import es.efactura.client.panels.ClientPanel;
import es.efactura.utils.table.ObjectTableModel;

@Component
public class ClientTable extends ObjectTableModel<ClientDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	ClientPanel parent;

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(ClientDto t, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return t.getName();
		case 1:
			return t.getCif();
		case 2:
			return t.getAddress();
		case 3:
			final JButton button = new JButton("Eliminar");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int dialogResult = JOptionPane.showConfirmDialog(null, "�Desea eliminar el registro?",
							"Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (dialogResult == JOptionPane.YES_OPTION) {
						new ClientDataProvider().delete(t.getId());
						parent.refresh();
					}
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
			return "Nombre fiscal";
		case 1:
			return "CIF";
		case 2:
			return "Direcci�n fiscal";
		case 3:
			return "Acciones";
		}
		return null;
	}

}
