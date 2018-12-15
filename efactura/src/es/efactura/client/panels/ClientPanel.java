package es.efactura.client.panels;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import es.efactura.client.dialog.ClientDialog;
import es.efactura.client.model.ClientDataProvider;
import es.efactura.client.model.ClientDto;
import es.efactura.client.table.ClientRowAction;
import es.efactura.client.table.ClientTable;
import es.efactura.table.JTableButtonMouseListener;
import es.efactura.table.ObjectTableModel;
import es.efactura.table.PaginatedTableDecorator;
import es.efactura.table.PaginationDataProvider;

public class ClientPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;
	private PaginatedTableDecorator<ClientDto> paginatedDecorator;
	private List<ClientDto> listData;
	private PaginationDataProvider<ClientDto> paginationDataProvider;

	private ClientDataProvider clientDataProvider;

	public ClientPanel() {
		super(new GridLayout(3, 0));

		clientDataProvider = new ClientDataProvider();

		ClientDialog clientDialog = new ClientDialog(600, 600, this);
		clientDialog.setVisible(false);

		JButton showDialogButton = new JButton("Añadir");
		showDialogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clientDialog.clearData();
				clientDialog.setVisible(true);
			}
		});
		add(showDialogButton);

		table = new JTable(new ClientTable(this));
		ClientRowAction rowAction = new ClientRowAction();
		TableColumn column = table.getColumnModel().getColumn(3);
		column.setCellRenderer(rowAction);
		table.addMouseListener(new JTableButtonMouseListener(table));

		table.setAutoCreateRowSorter(true);

		// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				if (me.getClickCount() == 2) {
					clientDialog.setVisible(true);
					clientDialog.fillData(listData.get(row));
				}
			}
		});

		// Add the scroll pane to this panel.
		add(table);

		paginationDataProvider = createDataProvider();
		paginatedDecorator = PaginatedTableDecorator.decorate(table, paginationDataProvider, new int[] { 10, 20, 50 },
				10);

		add(paginatedDecorator.getContentPanel());
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		listData = clientDataProvider.getList();

		TableModel model = table.getModel();
		int startIndex = (paginatedDecorator.getCurrentPage() - 1) * paginatedDecorator.getCurrentPageSize();
		int endIndex = startIndex + paginatedDecorator.getCurrentPageSize();
		if (endIndex > paginationDataProvider.getTotalRowCount()) {
			endIndex = paginationDataProvider.getTotalRowCount();
		}
		List<ClientDto> rows = paginationDataProvider.getRows(startIndex, endIndex);

		ObjectTableModel<ClientDto> objectTableModel = (ObjectTableModel<ClientDto>) model;

		objectTableModel.setObjectRows(rows);
		objectTableModel.fireTableDataChanged();
	}

	private PaginationDataProvider<ClientDto> createDataProvider() {
		listData = clientDataProvider.getList();

		return new PaginationDataProvider<ClientDto>() {
			@Override
			public int getTotalRowCount() {
				return listData.size();
			}

			@Override
			public java.util.List<ClientDto> getRows(int startIndex, int endIndex) {
				return listData.subList(startIndex, endIndex);
			}
		};
	}

}
