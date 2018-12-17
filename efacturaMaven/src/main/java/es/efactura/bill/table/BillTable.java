package es.efactura.bill.table;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.efactura.bill.model.BillDataProvider;
import es.efactura.bill.model.BillDto;
import es.efactura.bill.panels.BillPanel;
import es.efactura.utils.table.ObjectTableModel;

@Component
public class BillTable extends ObjectTableModel<BillDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	BillPanel parent;

	@Autowired
	private BillDataProvider billDataProvider;

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(BillDto t, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return t.getNumber();
		case 1:
			return t.getClient().getName();
		case 2:
			return t.getBroadcastDate();
		case 3:
			return t.getExpirationDate();
		case 4:
			final JButton button = new JButton("Eliminar");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int dialogResult = JOptionPane.showConfirmDialog(null, "¿Desea eliminar el registro?",
							"Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (dialogResult == JOptionPane.YES_OPTION) {
						billDataProvider.delete(t.getId());
						parent.refresh(parent.getFindText().getText());
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
			return "Número";
		case 1:
			return "Cliente";
		case 2:
			return "Fecha emisión";
		case 3:
			return "Fecha vencimiento";
		case 4:
			return "Acciones";
		}
		return null;
	}

}
