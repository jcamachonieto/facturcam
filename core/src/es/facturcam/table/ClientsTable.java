package es.facturcam.table;

import javax.swing.table.AbstractTableModel;

public class ClientsTable extends AbstractTableModel {

	String[] columnNames = { "Name", "Degree", "Board/University", "Year of Passing", "CGPA", "Part-Time" };

	Object[][] data = { { "Saira", "B.Tech", "VTU", new Integer(2015), new Float(8.33), new Boolean(false) },
			{ "Smaira", "B.Sc", "CBSE", new Integer(2007), new Float(7.77), new Boolean(true) },
			{ "John", "M.tech", "IIT", new Integer(2009), new Float(8.77), new Boolean(false) },
			{ "Jia", "M.Sc", "Thapar", new Integer(2011), new Float(7.21), new Boolean(true) },
			{ "Kerry", "B.Com", "DU", new Integer(2014), new Float(8.92), new Boolean(false) }

	};

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
		if (col < 1) {
			return false;
		} else {
			return true;
		}
	}

	public void setValueAt(Object value, int row, int col) {

		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
