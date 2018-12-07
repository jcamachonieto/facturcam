package es.facturcam.table;

import javax.swing.table.AbstractTableModel;

import es.facturcam.model.ClientDto;

public class ClientsTable extends ObjectTableModel<ClientDto> {

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(ClientDto t, int columnIndex) {
		switch (columnIndex) {
        case 0:
            return t.getId();
        case 1:
            return t.getName();
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
    }
    return null;
	}

	

}
