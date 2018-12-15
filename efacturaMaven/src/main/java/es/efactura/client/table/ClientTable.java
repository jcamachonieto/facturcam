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

	@Autowired
	private ClientDataProvider clientDataProvider;

	@Override
	public int getColumnCount() {
		return 10;
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
			return t.getLocation();
		case 4:
			return t.getProvince();
		case 5:
			return t.getPostalCode();
		case 6:
			return t.getCountry();
		case 7:
			return t.getTelephone();
		case 8:
			return t.getEmail();
		case 9:
			final JButton button = new JButton("Eliminar");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int dialogResult = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el registro?",
							"Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (dialogResult == JOptionPane.YES_OPTION) {
						clientDataProvider.delete(t.getId());
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
			return "Dirección";
		case 3:
			return "Localidad";
		case 4:
			return "Provincia";
		case 5:
			return "Código postal";
		case 6:
			return "País";
		case 7:
			return "Teléfono";
		case 8:
			return "Email";
		case 9:
			return "Acciones";
		}
		return null;
	}

}
