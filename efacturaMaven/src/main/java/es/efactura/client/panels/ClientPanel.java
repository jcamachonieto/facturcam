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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.efactura.client.dialog.ClientDialog;
import es.efactura.client.model.ClientDataProvider;
import es.efactura.client.model.ClientDto;
import es.efactura.client.table.ClientRowAction;
import es.efactura.client.table.ClientTable;
import es.efactura.utils.table.JTableButtonMouseListener;
import es.efactura.utils.table.ObjectTableModel;
import es.efactura.utils.table.PaginatedTableDecorator;
import es.efactura.utils.table.PaginationDataProvider;

@Component
public class ClientPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;
	private List<ClientDto> listData;
	private PaginatedTableDecorator<ClientDto> paginatedTableDecorator;
	private PaginationDataProvider<ClientDto> paginationDataProvider;

	@Autowired
	private ClientDataProvider clientDataProvider;

	@Autowired
	ClientDialog clientDialog;

	@Autowired
	ClientTable clientTable;

	@Autowired
	ClientRowAction clientRowAction;

	public ClientPanel() {
		super(new GridLayout(3, 0));
	}

	public void initialize() {
		clientDialog.initialize(600, 600);
		clientDialog.setVisible(false);

		JButton showDialogButton = new JButton("Añadir");
		showDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clientDialog.clearData();
				clientDialog.setVisible(true);
			}
		});
		add(showDialogButton);

		table = new JTable(clientTable);
		TableColumn column = table.getColumnModel().getColumn(9);
		column.setCellRenderer(clientRowAction);
		table.addMouseListener(new JTableButtonMouseListener(table));

		table.setAutoCreateRowSorter(true);

		// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
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
		paginatedTableDecorator = PaginatedTableDecorator.decorate(table, paginationDataProvider,
				new int[] { 10, 20, 50 }, 10);

		add(paginatedTableDecorator.getContentPanel());
	}

	@SuppressWarnings("unchecked")
	public void refresh() {
		listData = clientDataProvider.getList();

		TableModel model = table.getModel();
		int startIndex = (paginatedTableDecorator.getCurrentPage() - 1) * paginatedTableDecorator.getCurrentPageSize();
		int endIndex = startIndex + paginatedTableDecorator.getCurrentPageSize();
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
